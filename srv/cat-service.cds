using my.bookshop as my from '../db/data-model';

service CatalogService {
    @readonly entity Books as projection on my.Books;

    // @odata.draft.enabled
    // entity CustRequests as projection on my.CustRequests;

    @odata.draft.enabled
    entity CustRequests as projection on my.CustRequest;

    entity CustRequestItems as projection on my.CustRequestItem;

    // @cds.persistence.exists
    entity PartyGroups as projection on my.PartyGroup;

    // @odata.draft.enabled
    entity Products as projection on my.Product;
    
}