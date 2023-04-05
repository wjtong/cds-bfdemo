using my.bookshop as my from '../db/data-model';

service CatalogService @(requires: 'authenticated-user') {
    @readonly entity Books as projection on my.Books;

    extend my.CustRequest with {
        requestNotes : Composition of many RequestNotes on requestNotes.custRequestId = ID;
    };

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

    @cds.odata.valuelist
    entity Products as projection on my.Product;

    @cds.odata.valuelist
    entity Parties as projection on my.Party;

    entity WorkEfforts as projection on my.WorkEffort;

    entity CustRequestWorkEfforts as projection on my.CustRequestWorkEffort;

    entity FixedAssets as projection on my.FixedAsset;

    entity FixedAssetFaults as projection on my.FixedAssetFault;

    // @odata.draft.enabled
    entity NoteDatas as projection on my.NoteData;

    // entity CustRequestNotes as projection on my.CustRequestNote;

    entity CustRequestParties as projection on my.CustRequestParty;

    // @Common.DraftNode : {
    //     $Type : 'Common.DraftNodeType',
    //     PreparationAction : 'CatalogService.prepareRequestNotes'
    // }
    entity RequestNotes as select from CustRequestNotes {
        key noteData.ID as noteId,
        key custRequest.ID as custRequestId,
        noteData.noteInfo as noteInfo,
        noteData.noteName as noteName,
    } actions {
        action prepareRequestNotes(SideEffectsQualifier: String) returns RequestNotes;
    };

    entity CustRequestNotes as projection on my.CustRequestNote actions {
        @(
            Common.SideEffects : {
                TargetProperties : ['_it'],
                TargetEntities : [_it]
            },
            cds.odata.bindingparameter.name : '_it'
        )
        @cds.odata.bindingparameter.collection
        action addNotes(noteName: String, noteInfo: String) returns CustRequestNotes;
    };
 
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