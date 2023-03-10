package com.banfftech.cdsbfdemo.handlers;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.sap.cds.services.cds.CqnService;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.After;
import com.sap.cds.services.handler.annotations.ServiceName;

import cds.gen.catalogservice.CatalogService_;
import cds.gen.catalogservice.CustRequests;
// import cds.gen.catalogservice.CustRequests;
import cds.gen.catalogservice.Books;

@Component
@ServiceName(CatalogService_.CDS_NAME)
public class CatalogServiceHandler implements EventHandler {

	@After(event = CqnService.EVENT_READ)
	public void discountBooks(Stream<Books> books) {
		books.filter(b -> b.getTitle() != null && b.getStock() != null)
		.filter(b -> b.getStock() > 200)
		.forEach(b -> b.setTitle(b.getTitle() + " (discounted)"));
	}

	@After(event = CqnService.EVENT_READ)
	public void readCustrequests(Stream<CustRequests> cusrequests) {
		//添加语义化字段
		cusrequests.forEach(cr -> cr.setProcessingResultLevel(new Random().nextInt(5)));
	}

	// @After(event = CqnService.EVENT_CREATE)
	// public void insertCustrequests(CustRequests cusrequests) {
	// 	System.out.println(">>>>>> draft Save");
	// }

}