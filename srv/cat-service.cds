using my.bookshop as my from '../db/data-model';

service CatalogService {
    @readonly entity Books as projection on my.Books;

    @odata.draft.enabled
    entity CustRequests as projection on my.CustRequest;

    entity CustRequestItems as projection on my.CustRequestItem;

    entity Products as projection on my.Product;

    entity Parties as projection on my.Party;
}