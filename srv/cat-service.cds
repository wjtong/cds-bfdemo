using my.bookshop as my from '../db/data-model';

service CatalogService {
    @readonly entity Books as projection on my.Books;
    entity CustRequests as select custRequest.ID, custRequestItem.productId from my.CustRequest as custRequest left join my.CustRequestItem as custRequestItem on custRequest.ID = custRequestItem.custRequestId;
}