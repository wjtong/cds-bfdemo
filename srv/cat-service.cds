using my.bookshop as my from '../db/data-model';

service CatalogService @(requires: 'any') {
    @readonly entity Books as projection on my.Books;

    @odata.draft.enabled
    @Common.DraftRoot.NewAction: 'CatalogService.newCustRequestsAction'
    // @Common.DraftRoot.ActivationAction : 'CatalogService.activateCustRequestsAction'
    entity CustRequests as projection on my.CustRequest actions {
        @cds.odata.bindingparameter.collection
        action newCustRequestsAction(serialNumber: String) returns CustRequests;
        // action activateCustRequestsAction() returns CustRequests;
    };

    extend my.CustRequestItem with {
        fixedAssetFault : Composition of one FixedAssetFaults on custRequestId = fixedAssetFault.custRequestId and custRequestItemSeqId = fixedAssetFault.custRequestItemSeqId;
    };

    entity CustRequestItems as select from my.CustRequestItem {
        *,
        null as computedField : String,
        product.productName as productName
    };

    entity Products as projection on my.Product;

    entity Parties as projection on my.Party;

    entity WorkEfforts as projection on my.WorkEffort;

    entity CustRequestWorkEfforts as projection on my.CustRequestWorkEffort;

    entity FixedAssets as projection on my.FixedAsset;

    entity FixedAssetFaults as projection on my.FixedAssetFault;
 
    // access control restrictions
    // annotate CustRequests with @restrict : [
    //     {
    //         grant : '*',
    //         to : 'authenticated-user',
    //         where : 'createdBy=$user'
    //     },
    //     {
    //         grant : '*',
    //         to : 'admin',
    //     }
    // ];
}