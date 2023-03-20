using my.bookshop as my from '../db/data-model';

service CatalogService @(requires: 'authenticated-user') {
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
        // fixedAssetFault : Composition of one FixedAssetFaults on fixedAssetFault.custRequestItems = $self;
    };

    // extend my.CustRequestNote with {
    //     noteData : Composition of my.NoteData on noteData.noteId = noteId;
    // };
    

    entity CustRequestItems as select from my.CustRequestItem {
        *,
        null as computedField : String,
        product.productName as productName
    };

    // entity CustRequestItems as projection on my.CustRequestItem;

    entity Products as projection on my.Product;

    entity Parties as projection on my.Party;

    entity WorkEfforts as projection on my.WorkEffort;

    entity CustRequestWorkEfforts as projection on my.CustRequestWorkEffort;

    entity FixedAssets as projection on my.FixedAsset;

    entity FixedAssetFaults as projection on my.FixedAssetFault;

    // @odata.draft.enabled
    entity NoteDatas as projection on my.NoteData;

    entity CustRequestNotes as projection on my.CustRequestNote;
 
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