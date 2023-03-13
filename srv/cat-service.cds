using my.bookshop as my from '../db/data-model';

service CatalogService {
    @readonly entity Books as projection on my.Books;

    @odata.draft.enabled
    @Common.DraftRoot.NewAction: 'CatalogService.newCustRequestsAction'
    // @Common.DraftRoot.ActivationAction : 'CatalogService.activateCustRequestsAction'
    entity CustRequests as projection on my.CustRequest actions {
        @cds.odata.bindingparameter.collection
        action newCustRequestsAction(serialNumber: String) returns CustRequests;
        // action activateCustRequestsAction() returns CustRequests;
    };

    entity CustRequestItems as projection on my.CustRequestItem;

    entity Products as projection on my.Product;

    entity Parties as projection on my.Party;

    entity WorkEfforts as projection on my.WorkEffort;

    entity CustRequestWorkEfforts as projection on my.CustRequestWorkEffort;

    entity FixedAssets as projection on my.FixedAsset;

    entity FixedAssetFault as projection on my.FixedAssetFault;
}