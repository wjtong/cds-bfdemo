package com.banfftech.cdsbfdemo.handlers;

import static cds.gen.catalogservice.CatalogService_.WORK_EFFORTS;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sap.cds.Result;
import com.sap.cds.Row;
import com.sap.cds.ql.Insert;
import com.sap.cds.ql.Select;
import com.sap.cds.ql.Update;
import com.sap.cds.ql.cqn.CqnAnalyzer;
import com.sap.cds.ql.cqn.CqnSelect;
import com.sap.cds.reflect.CdsModel;
import com.sap.cds.services.cds.CdsCreateEventContext;
import com.sap.cds.services.cds.CdsUpdateEventContext;
import com.sap.cds.services.cds.CqnService;
import com.sap.cds.services.draft.DraftService;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.After;
import com.sap.cds.services.handler.annotations.On;
import com.sap.cds.services.handler.annotations.ServiceName;
import com.sap.cds.services.persistence.PersistenceService;

import cds.gen.catalogservice.AddNotesContext;
// import cds.gen.catalogservice.ActivateCustRequestsActionContext;
import cds.gen.catalogservice.Books;
import cds.gen.catalogservice.CatalogService_;
import cds.gen.catalogservice.CustRequestItems;
import cds.gen.catalogservice.CustRequestItems_;
import cds.gen.catalogservice.CustRequestNotes;
import cds.gen.catalogservice.CustRequestNotes_;
import cds.gen.catalogservice.CustRequestWorkEfforts;
import cds.gen.catalogservice.CustRequestWorkEfforts_;
import cds.gen.catalogservice.CustRequests;
import cds.gen.catalogservice.CustRequests_;
import cds.gen.catalogservice.FixedAssetFaults;
import cds.gen.catalogservice.FixedAssetFaults_;
import cds.gen.catalogservice.FixedAssets_;
import cds.gen.catalogservice.NewCustRequestsActionContext;
import cds.gen.catalogservice.NoteDatas;
import cds.gen.catalogservice.NoteDatas_;
import cds.gen.catalogservice.RequestNotes;
import cds.gen.catalogservice.WorkEfforts;

@Component
@ServiceName(CatalogService_.CDS_NAME)
public class CatalogServiceHandler implements EventHandler {
	@Autowired
	private PersistenceService db;

	@Autowired
	private DraftService draftService;

	@After(event = CqnService.EVENT_READ)
	public void discountBooks(Stream<Books> books) {
		books.filter(b -> b.getTitle() != null && b.getStock() != null)
		.filter(b -> b.getStock() > 200)
		.forEach(b -> b.setTitle(b.getTitle() + " (discounted)"));
	}

	@After(event = CqnService.EVENT_CREATE)
	public void createWorkEffort(CdsCreateEventContext context, CustRequests custRequest) {
		System.out.println("------------------------------- in after create event handler");
		// CqnService service = context.getService();
		WorkEfforts workEfforts = WorkEfforts.create();
		workEfforts.setWorkEffortId(UUID.randomUUID().toString());
		CustRequestWorkEfforts custRequestWorkEfforts = CustRequestWorkEfforts.create();
		custRequestWorkEfforts.setCustRequestId(custRequest.getId());
		custRequestWorkEfforts.setWorkEffortId(workEfforts.getWorkEffortId());
		Insert workEffortInsert = Insert.into(WORK_EFFORTS).entry(workEfforts);
		Insert custRequestWorkEffortInsert = Insert.into(CustRequestWorkEfforts_.class).entry(custRequestWorkEfforts);
		// service.run(workEffortInsert);
		// service.run(custRequestWorkEffortInsert);
		db.run(workEffortInsert);
		db.run(custRequestWorkEffortInsert);
		updateNoteDatas(custRequest);
	}
	@After(event = CqnService.EVENT_UPDATE)
	public void afterUpdateCustRequest(CdsUpdateEventContext context, CustRequests custRequest) {
		System.out.println("------------------------------- afterUpdateCustRequest");
		updateNoteDatas(custRequest);
	}

	private void updateNoteDatas(CustRequests custRequests) {
		List<RequestNotes> requestNotes = custRequests.getRequestNotes();
		for (RequestNotes requestNote:requestNotes) {
			System.out.println("------------------------------- " + requestNote.getNoteId());
			Result result = db.run(Select.from(NoteDatas_.class).byId(requestNote.getNoteId()));
			if (result.rowCount() == 0) {
				System.out.println("------------------------------- " + requestNote.getNoteId() + " doesn't exist");
				NoteDatas noteDatas = NoteDatas.create();
				noteDatas.setId(requestNote.getNoteId());
				noteDatas.setNoteInfo(requestNote.getNoteInfo());
				noteDatas.setNoteName(requestNote.getNoteInfo());
				Insert insertNoteDatas = Insert.into(NoteDatas_.class).entry(noteDatas);
				db.run(insertNoteDatas).single(NoteDatas.class);
			} else {
				System.out.println("------------------------------- " + requestNote.getNoteId() + " exists");
				NoteDatas noteDatas = NoteDatas.create();
				noteDatas.setId(requestNote.getNoteId());
				noteDatas.setNoteInfo(requestNote.getNoteInfo());
				noteDatas.setNoteName(requestNote.getNoteInfo());
				db.run(Update.entity(NoteDatas_.class).data(noteDatas));
			}
		}
	}

	@On(entity = CustRequests_.CDS_NAME)
	public void newCustRequestsAction(NewCustRequestsActionContext context) {
		System.out.println("------------------------------- in newCustRequestsAction");
		String serialNumber = context.getSerialNumber();
		Map<String, Object>	fixedAssetValue = new HashMap<>();
		fixedAssetValue.put("serialNumber", serialNumber);
		CqnSelect select = Select.from(FixedAssets_.class).matching(fixedAssetValue);
		DraftService service = (DraftService) context.getService();
		Row row = service.run(select).single();
		String fixedAssetId = row.getPath("fixedAssetId");

		// insert CustRequests
		CustRequests custRequests = CustRequests.create();
		custRequests.setId(UUID.randomUUID().toString());
		// custRequestItem.setCustRequest(custRequests);
		// custRequests.setItems(custRequestItem);
		CustRequests result = service.newDraft(Insert.into(CustRequests_.class).entry(custRequests)).single(CustRequests.class);

		// insert CustRequestItems
		CustRequestItems custRequestItem = CustRequestItems.create();
		custRequestItem.setItemSeqId("10");
		custRequestItem.setCustRequestId(custRequests.getId());
		service.newDraft(Insert.into(CustRequestItems_.class).entry(custRequestItem));

		// insert FixedAssetFault
		FixedAssetFaults fixedAssetFault = FixedAssetFaults.create();
		fixedAssetFault.setId(UUID.randomUUID().toString());
		fixedAssetFault.setFixedAssetFixedAssetId(fixedAssetId);
		fixedAssetFault.setCustRequestItemsCustRequestId(custRequestItem.getCustRequestId());
		fixedAssetFault.setCustRequestItemsItemSeqId(custRequestItem.getItemSeqId());
		service.newDraft(Insert.into(FixedAssetFaults_.class).entry(fixedAssetFault));
		custRequestItem.setFixedAssetFault(fixedAssetFault);
		custRequests.setItems(custRequestItem);
		context.setResult(result);
		// custRequests.setCustRequestName(co);
	}

	@On(entity = "CatalogService.CustRequestNotes", event = "addNotes")
	public void onAddNotes(AddNotesContext context) {
		System.out.println("------------------------------- in AddNotesContext action");
		String noteInfo = context.getNoteInfo();
		String noteName = context.getNoteName();

		CdsModel cdsModel = context.getModel();
		CqnAnalyzer analyzer = CqnAnalyzer.create(cdsModel);
		String custRequestId = (String) analyzer.analyze(context.getCqn()).rootKeys().get(CustRequests.ID);
		
		NoteDatas noteDatas = NoteDatas.create();
		noteDatas.setNoteInfo(noteInfo);
		noteDatas.setNoteName(noteName);
		Insert insertNoteDatas = Insert.into(NoteDatas_.class).entry(noteDatas);
		NoteDatas createdNoteDatas = db.run(insertNoteDatas).single(NoteDatas.class);
		String noteId = createdNoteDatas.getId();
		CustRequestNotes custRequestNotes = CustRequestNotes.create();
		custRequestNotes.setCustRequestId(custRequestId);
		custRequestNotes.setNoteDataId(noteId);
		Insert insertCustRequestNotes = Insert.into(CustRequestNotes_.class).entry(custRequestNotes);
		CustRequestNotes result = draftService.run(insertCustRequestNotes).single(CustRequestNotes.class);
		context.setResult(result);
	}
}