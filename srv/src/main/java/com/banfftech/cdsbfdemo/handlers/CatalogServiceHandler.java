package com.banfftech.cdsbfdemo.handlers;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.sap.cds.services.EventContext;
import com.sap.cds.services.cds.CqnService;
import com.sap.cds.services.draft.DraftCreateEventContext;
import com.sap.cds.services.draft.DraftNewEventContext;
import com.sap.cds.services.draft.DraftService;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.After;
import com.sap.cds.services.handler.annotations.Before;
import com.sap.cds.services.handler.annotations.ServiceName;

import cds.gen.catalogservice.CatalogService_;
import cds.gen.catalogservice.CustRequestItems;
import cds.gen.catalogservice.Books;
import cds.gen.catalogservice.CustRequests_;
import cds.gen.catalogservice.CustRequests;

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
		CustRequestItems custRequestItem = custRequest.getCustRequestItem();
		if (custRequestItem == null) {
			System.out.println("------------------------------- custRequestItem is null");
			custRequestItem = CustRequestItems.create();
			custRequest.setCustRequestItem(custRequestItem);
			System.out.println("-------------------------------" + custRequestItem);
			System.out.println("-------------------------------" + custRequest);
		}
		custRequestItem.put("custRequestItemSeqId", "00001");
		custRequestItem.put("custRequestId", custRequest.getCustRequestId());
	}
	@After(event = DraftService.EVENT_DRAFT_CREATE)
	public void AfterCreateDraft(DraftCreateEventContext context) {
		System.out.println("------------------------------- in after draft create event handler");
	}
	@Before(event = DraftService.EVENT_DRAFT_CREATE)
	public void BeforeCreateDraft(DraftCreateEventContext context) {
		System.out.println("------------------------------- in before draft create event handler");
	}
}