package com.banfftech.cdsbfdemo.handlers;

import java.util.Random;
import java.util.stream.Stream;

import cds.gen.catalogservice.*;
import com.sap.cds.Result;
import com.sap.cds.ql.Insert;
import com.sap.cds.ql.Select;
import com.sap.cds.services.draft.DraftService;
import com.sap.cds.services.persistence.PersistenceService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.sap.cds.services.cds.CqnService;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.After;
import com.sap.cds.services.handler.annotations.ServiceName;

@Component
@ServiceName(CatalogService_.CDS_NAME)
public class CatalogServiceHandler implements EventHandler {
    private final DraftService catalogService;
    private final PersistenceService db;
//    private final CqnAnalyzer analyzer;

    CatalogServiceHandler(PersistenceService db, @Qualifier(CatalogService_.CDS_NAME) DraftService catalogService) {
        this.db = db;
        this.catalogService = catalogService;
    }

    @After(event = CqnService.EVENT_READ)
    public void discountBooks(Stream<Books> books) {
        books.filter(b -> b.getTitle() != null && b.getStock() != null)
                .filter(b -> b.getStock() > 200)
                .forEach(b -> b.setTitle(b.getTitle() + " (discounted)"));
    }

    @After(event = CqnService.EVENT_READ)
    public void readCustrequests(Stream<CustRequests> cusrequests) {
        //Highlight 添加语义化字段
         cusrequests.forEach(cr -> cr.setProcessingResultLevel(new Random().nextInt(5) + 1));
    }

    @After(event = DraftService.EVENT_DRAFT_NEW)
    public void custRequestNew(Stream<CustRequests> cusRequests) {
        System.out.println(">>>>>>>>>>> cusRequest new123");
        cusRequests.forEach(cr -> {
            //查询是否存在CusRequestItem
            Result result = db.run(Select.from(CatalogService_.CUST_REQUEST_ITEMS)
                    .where(custReqItem -> custReqItem.custRequestId().eq(cr.getCustRequestId())));
            //不存在 创建一个
            if (result.rowCount() == 0) {
                CustRequestItems custRequestItems = CustRequestItems.create();
                custRequestItems.setCustRequestId(cr.getCustRequestId());
                custRequestItems.setCustRequestItemSeqId("00001");
                custRequestItems.setProductId("p_001");
                Insert insert = Insert.into(CustRequestItems_.CDS_NAME).entry(custRequestItems);
                Result runResult = catalogService.newDraft(insert);
                CustRequestItems createdItem = runResult.single().as(CustRequestItems.class);
                System.out.println(">>>> created Item proId:" + createdItem.getProductId());
				cr.setCustRequestItem(createdItem);
            }
        });
    }

    @After(event = DraftService.EVENT_DRAFT_SAVE)
    public void custRequestSave(Stream<CustRequests> cusRequests) {
        cusRequests.forEach(cr -> {
            
        });
    }
	
    @After(event = DraftService.EVENT_DRAFT_PATCH,entity = CustRequestItems_.CDS_NAME)
    public void itemPatch(CustRequestItems custItem) {
        System.out.println(">>>>>>>>>>> item patch");
    }

	@After(event = DraftService.EVENT_DRAFT_PATCH,entity = CustRequests_.CDS_NAME)
    public void custReqPatch(CustRequests custReq) {
        System.out.println(">>>>>>>>>>> custReq patch");
    }

    @After(event = DraftService.EVENT_DRAFT_EDIT)
    public void itemEdit(Stream<CustRequestItems> cusRequestItems) {
        System.out.println(">>>>>>>>>>> item edit");
    }

    @After(event = DraftService.EVENT_DRAFT_SAVE, entity = CustRequestItems_.CDS_NAME)
    public void itemSave(Stream<CustRequestItems> cusRequestItems) {
        System.out.println(">>>>>>>>>>> item save");
    }

}