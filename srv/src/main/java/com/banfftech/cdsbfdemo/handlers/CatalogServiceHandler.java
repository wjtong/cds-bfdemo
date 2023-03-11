package com.banfftech.cdsbfdemo.handlers;

import java.util.Map;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.sap.cds.ql.Insert;
import com.sap.cds.services.EventContext;
import com.sap.cds.services.cds.CqnService;
import com.sap.cds.services.draft.DraftCreateEventContext;
import com.sap.cds.services.draft.DraftNewEventContext;
import com.sap.cds.services.draft.DraftService;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.After;
import com.sap.cds.services.handler.annotations.Before;
import com.sap.cds.services.handler.annotations.ServiceName;

import cds.gen.catalogservice.Books;
import cds.gen.catalogservice.CatalogService_;
import cds.gen.catalogservice.CustRequestItems;
import cds.gen.catalogservice.CustRequestItems_;
import cds.gen.catalogservice.CustRequests;
import cds.gen.catalogservice.CustRequests_;

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
		DraftService catalogService = context.getService();
		CustRequestItems custRequestItem = custRequest.getCustRequestItem();
		if (custRequestItem == null) {
			custRequestItem = CustRequestItems.create();
			custRequestItem.put("custRequestItemSeqId", "00001");
			custRequestItem.put("custRequestId", custRequest.getCustRequestId());
			Iterable<? extends Map<String, ?>> result =  catalogService.newDraft(Insert.into(CustRequestItems_.class).entry(custRequestItem));
			context.setResult(result);
			custRequest.setCustRequestItem(custRequestItem);
		}
	}
	@Before(event = DraftService.EVENT_DRAFT_CREATE)
	public void BeforeCreateDraft(DraftCreateEventContext context) {
		System.out.println("------------------------------- in before draft create event handler");
	}
}