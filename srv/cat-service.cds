using my.bookshop as my from '../db/data-model';

service CatalogService {
    @readonly entity Books as projection on my.Books;

    @odata.draft.enabled
    entity CustRequests as projection on my.CustRequests;
}