package com.banfftech.cdsbfdemo.handlers;

import static cds.gen.catalogservice.CatalogService_.WORK_EFFORTS;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.sap.cds.Row;
import com.sap.cds.ql.Insert;
import com.sap.cds.ql.Select;
import com.sap.cds.ql.cqn.CqnSelect;
import com.sap.cds.services.EventContext;
import com.sap.cds.services.cds.CdsCreateEventContext;
import com.sap.cds.services.cds.CqnService;
import com.sap.cds.services.draft.DraftCreateEventContext;
import com.sap.cds.services.draft.DraftNewEventContext;
import com.sap.cds.services.draft.DraftService;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.After;
import com.sap.cds.services.handler.annotations.Before;
import com.sap.cds.services.handler.annotations.On;
import com.sap.cds.services.handler.annotations.ServiceName;

import cds.gen.catalogservice.ActivateCustRequestsActionContext;
import cds.gen.catalogservice.Books;
import cds.gen.catalogservice.CatalogService_;
import cds.gen.catalogservice.CustRequestItems;
import cds.gen.catalogservice.CustRequestItems_;
import cds.gen.catalogservice.CustRequestNotes;
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
import cds.gen.catalogservice.WorkEfforts;
import cds.gen.my.bookshop.NoteData;

@Component
@ServiceName(CatalogService_.CDS_NAME)
public class CatalogServiceHandler implements EventHandler {
	@After(event = CqnService.EVENT_READ)
	public void discountBooks(Stream<Books> books) {
		books.filter(b -> b.getTitle() != null && b.getStock() != null)
		.filter(b -> b.getStock() > 200)
		.forEach(b -> b.setTitle(b.getTitle() + " (discounted)"));
	}

	@After(entity = CustRequests_.CDS_NAME,event = CqnService.EVENT_CREATE)
	public void afterCreateCustRequests(EventContext context) {
		System.out.println("------------------------------- in after event handler");
	}

	@Before(event = DraftService.EVENT_DRAFT_NEW)
	public void beforeNewDraft(DraftNewEventContext context) {
		System.out.println("------------------------------- in before draft new event handler");
	}
	@After(event = DraftService.EVENT_DRAFT_NEW)
	public void AfterNewDraft(DraftNewEventContext context, CustRequests custRequest) {
		System.out.println("------------------------------- in after draft new event handler");
	}
	@After(event = DraftService.EVENT_DRAFT_CREATE)
	public void AfterCreateDraft(DraftCreateEventContext context, CustRequests custRequest) {
		System.out.println("------------------------------- in after draft create event handler");
		// DraftService catalogService = context.getService();
		// CustRequestItems custRequestItem = custRequest.getCustRequestItem();
		// if (custRequestItem == null) {
		// 	custRequestItem = CustRequestItems.create();
		// 	custRequestItem.put("custRequestItemSeqId", "10");
		// 	custRequestItem.put("custRequestId", custRequest.getCustRequestId());
		// 	Iterable<? extends Map<String, ?>> result =  catalogService.newDraft(Insert.into(CustRequestItems_.class).entry(custRequestItem));
		// 	context.setResult(result);
		// 	custRequest.setCustRequestItem(custRequestItem);
		// }
	}
	@Before(event = DraftService.EVENT_DRAFT_CREATE)
	public void BeforeCreateDraft(DraftCreateEventContext context) {
		System.out.println("------------------------------- in before draft create event handler");
	}
	@On(event = DraftService.EVENT_DRAFT_CREATE)
	public void OnCreateCustRequestNotesDraft(DraftCreateEventContext context, CustRequestNotes custRequestNote) {
		System.out.println("------------------------------- on CustRequestNotes draft create event handler");
		if (custRequestNote.getNoteId() == null) {
			System.out.println("noteId is null");
			String noteId = UUID.randomUUID().toString();
			custRequestNote.setNoteId(noteId);
		}
	}
	@After(event = DraftService.EVENT_DRAFT_CREATE)
	public void AfterCreateCustRequestNotesDraft(DraftCreateEventContext context, CustRequestNotes custRequestNote) {
		System.out.println("------------------------------- after CustRequestNotes draft create event handler");
		NoteDatas noteDatas = NoteDatas.create();
		String noteId = custRequestNote.getNoteId();
		noteDatas.setNoteId(noteId);
		DraftService service = (DraftService) context.getService();
		NoteDatas result = service.newDraft(Insert.into(NoteDatas_.class).entry(noteDatas)).single(NoteDatas.class);
	}

	@After(event = CqnService.EVENT_CREATE)
	public void createWorkEffort(CdsCreateEventContext context, CustRequests custRequest) {
		System.out.println("------------------------------- in after create event handler");
		CqnService service = context.getService();
		WorkEfforts workEfforts = WorkEfforts.create();
		workEfforts.setWorkEffortId(UUID.randomUUID().toString());
		CustRequestWorkEfforts custRequestWorkEfforts = CustRequestWorkEfforts.create();
		custRequestWorkEfforts.setCustRequestId(custRequest.getCustRequestId());
		custRequestWorkEfforts.setWorkEffortId(workEfforts.getWorkEffortId());
		Insert workEffortInsert = Insert.into(WORK_EFFORTS).entry(workEfforts);
		Insert custRequestWorkEffortInsert = Insert.into(CustRequestWorkEfforts_.class).entry(custRequestWorkEfforts);
		service.run(workEffortInsert);
		service.run(custRequestWorkEffortInsert);
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
		System.out.println(service);
		// insert CustRequests
		CustRequests custRequests = CustRequests.create();
		String custRequestId = UUID.randomUUID().toString();
		custRequests.setCustRequestId(custRequestId);
		CustRequests result = service.newDraft(Insert.into(CustRequests_.class).entry(custRequests)).single(CustRequests.class);
		// insert CustRequestItems
		CustRequestItems custRequestItem = CustRequestItems.create();
		custRequestItem.put("custRequestItemSeqId", "10");
		custRequestItem.put("custRequestId", custRequests.getCustRequestId());
		service.newDraft(Insert.into(CustRequestItems_.class).entry(custRequestItem));
		// insert FixedAssetFault
		FixedAssetFaults fixedAssetFault = FixedAssetFaults.create();
		fixedAssetFault.setFixedAssetFaultId(UUID.randomUUID().toString());
		fixedAssetFault.setFixedAssetId(fixedAssetId);
		fixedAssetFault.setCustRequestId(custRequestItem.getCustRequestId());
		fixedAssetFault.setCustRequestItemSeqId(custRequestItem.getCustRequestItemSeqId());
		service.newDraft(Insert.into(FixedAssetFaults_.class).entry(fixedAssetFault));
		custRequestItem.setFixedAssetFault(fixedAssetFault);
		custRequests.setCustRequestItem(custRequestItem);
		context.setResult(result);
		// custRequests.setCustRequestName(co);
	}

	@On(entity = CustRequests_.CDS_NAME)
	public void activateCustRequestsAction(ActivateCustRequestsActionContext context) {
		System.out.println("------------------------------- in activateCustRequestsAction");
		CustRequests custRequests = CustRequests.create();
		// custRequests.setCustRequestName(co);
	}
}