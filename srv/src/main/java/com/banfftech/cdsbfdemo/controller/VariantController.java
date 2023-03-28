package com.banfftech.cdsbfdemo.controller;

import cds.gen.banfftech.user.VariantFile;
import cds.gen.banfftech.user.VariantFile_;
import com.banfftech.cdsbfdemo.Utils.ControllerRes;
import com.sap.cds.Result;
import com.sap.cds.Row;
import com.sap.cds.ql.Insert;
import com.sap.cds.ql.Select;
import com.sap.cds.ql.Update;
import com.sap.cds.services.persistence.PersistenceService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import static com.banfftech.cdsbfdemo.controller.LoginController.CSRF_TOKEN;

/**
 * FE保存变体接口
 *
 * @author scy
 * @date 2023/3/27
 */
@Controller
@RequestMapping("sap/bc/lrep")
public class VariantController {

    /**
     * csrfToken列表
     */
    private static final Map<String, String> CSRF_TOKEN_LIST = new ConcurrentHashMap<>();


    @Resource
    PersistenceService dbService;


    /**
     * 获取csrfToken
     */
    @ResponseBody
    @RequestMapping("/actions/getcsrftoken")
    public ControllerRes getcsrftoken(HttpServletRequest request, HttpServletResponse response) {
        String csrfToken = (String) request.getAttribute(CSRF_TOKEN);
        if (csrfToken != null) {
            return new ControllerRes(200, csrfToken);
        }
        HttpSession session = request.getSession();
        synchronized (session) {
            // if the session has a previous key in place, remove it from the master list
            String sesExtKey = (String) session.getAttribute(CSRF_TOKEN);
            if (sesExtKey != null) {
                CSRF_TOKEN_LIST.remove(sesExtKey);
            }
            //check the userLogin here, after the old session setting is set so that it will always be cleared
            String userLoginId = (String) request.getAttribute("userLoginId");
            //no key made yet for this request, create one
            while (csrfToken == null || CSRF_TOKEN_LIST.containsKey(csrfToken)) {
                csrfToken = "EL" + UUID.randomUUID();
            }
            request.setAttribute(CSRF_TOKEN, csrfToken);
            session.setAttribute(CSRF_TOKEN, csrfToken);
            response.addHeader(CSRF_TOKEN, csrfToken);
            CSRF_TOKEN_LIST.put(csrfToken, userLoginId);
        }
        return new ControllerRes(200, "success");
    }

    @ResponseBody
    @RequestMapping("/flex/data/*")
    public String flex(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userLoginId = (String) request.getAttribute("userLoginId");
        String pathInfo = request.getRequestURI();
        String reference = pathInfo.substring(pathInfo.lastIndexOf("/") + 1);
        JSONObject jsonObject = new JSONObject();
        JSONObject loadModules = JSONObject.fromObject("{\"loadModules\":false}");
        JSONObject changes = JSONObject.fromObject("{\"changes\":[]}");
        JSONObject contexts = JSONObject.fromObject("{\"contexts\":[]}");
        JSONObject ui2personalization = JSONObject.fromObject("{\"ui2personalization\":[]}");
        JSONObject settings = JSONObject.fromObject("{\"settings\":[]}");
        jsonObject.putAll(loadModules);
        jsonObject.putAll(changes);
        jsonObject.putAll(contexts);
        addVariantSection(jsonObject, reference, userLoginId);
        jsonObject.putAll(ui2personalization);
        jsonObject.putAll(settings);
        JSONArray jsonArray = jsonObject.getJSONArray("changes");
        addChanges(jsonArray, reference, userLoginId);
        response.setContentType("application/json");
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            outputStream.write(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
        }
        return "success";
    }

    @ResponseBody
    @RequestMapping("/changes")
    public String changes(@RequestBody String jsonData, HttpServletRequest request, HttpServletResponse response) {
        JSONArray jsonArray = JSONArray.fromObject(jsonData);
        String userLoginId =  (String) request.getAttribute("userLoginId");
        System.out.println("jsonArray.size() = " + jsonArray.size());
        for (Object variantFileObj : jsonArray) {
            Map<String, Object> variantFileMap = (Map<String, Object>) variantFileObj;
            Result result = dbService.run(Select.from(VariantFile_.class)
                    .where(vf -> vf.fileName().eq(variantFileMap.get("fileName").toString())
                            .and(vf.userLoginId().eq(userLoginId))));
            VariantFile variantFile;
            if (result.rowCount() == 0) {
                variantFile = VariantFile.create();
                variantFile.setFileName(variantFileMap.get("fileName").toString());
                variantFile.setUserLoginId(userLoginId);
                dbService.run(Insert.into(VariantFile_.CDS_NAME).entry(variantFile));
            } else {
                variantFile = result.single().as(VariantFile.class);
            }
            variantFile.setReference((String) variantFileMap.get("reference"));
            variantFile.setFileType((String) variantFileMap.get("fileType"));
            variantFile.setFileName((String) variantFileMap.get("fileName"));
            variantFile.setVariantReference((String) variantFileMap.get("variantReference"));
            variantFile.setVariantManagementReference((String) variantFileMap.get("variantManagementReference"));
            variantFile.setVariantData(variantFileObj.toString());
            dbService.run(Update.entity(VariantFile_.CDS_NAME).entry(variantFile));
        }
        return "success";
    }

    private void addVariantSection(JSONObject jsonObject, String reference, String userLoginId) {
        Result result = dbService.run(Select.from(VariantFile_.class)
                .where(vf -> vf.userLoginId().eq(userLoginId)
                        .and(vf.fileType().eq("ctrl_variant"))
                        .and(vf.reference().eq(reference))));
        final String variantSectionKey = "variantSection";
        if (result.rowCount() == 0) {
            jsonObject.put(variantSectionKey, "{}");
            return;
        }
        String variantReference = result.first(VariantFile.class).orElseThrow().getVariantReference();
        JSONObject variantSection = new JSONObject();
        JSONObject variants = JSONObject.fromObject("{\"variants\":[]}");
        variantSection.put(variantReference, variants);
        jsonObject.put(variantSectionKey, variantSection);
    }

    private void addChanges(JSONArray jsonArray, String reference, String userLoginId) {
        Result result = dbService.run(Select.from(VariantFile_.class)
                .where(vf -> vf.userLoginId().eq(userLoginId).and(vf.reference().eq(reference))));
        for (Row row : result) {
            jsonArray.add(row.as(VariantFile.class).getVariantData());
        }
    }

}
