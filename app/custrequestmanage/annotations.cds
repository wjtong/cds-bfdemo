using CatalogService as service from '../../srv/cat-service';

annotate service.CustRequests with @(
    UI.FieldGroup #BaseInfo : {
        $Type : 'UI.FieldGroupType',
        Data : [
            {
                $Type : 'UI.DataField',
                Value : ID,
            },
            {
                $Type : 'UI.DataField',
                Value : Items.product_ID,
            },
            {
                $Type : 'UI.DataField',
                Value : Items.quantity,
            },
            {
                $Type : 'UI.DataField',
                Value : fromParty_ID,
            },
        ],
    });
annotate service.CustRequests with @(
    UI.DataPoint #ID : {
        $Type : 'UI.DataPointType',
        Value : ID,
        Title : 'ID',
    },
    UI.HeaderFacets : [
        {
            $Type : 'UI.ReferenceFacet',
            ID : 'ID',
            Target : '@UI.DataPoint#ID',
        },
    ]
) {
    // fromPartyId @Common : { 
    //     ValueList : {
    //         $Type : 'Common.ValueListType',
    //         CollectionPath : 'Parties',
    //         Parameters : [
    //             {
    //                 $Type : 'Common.ValueListParameterInOut',
    //                 LocalDataProperty : fromPartyId,
    //                 ValueListProperty : 'partyId'
    //             },
    //         ]
    //     },
    // }

    // party
    // @ValueList.entity : 'Parties';
};
// annotate service.CustRequestItems with {
//     product_ID @Common : {
//         Text : product.productName,
//         TextArrangement : #TextOnly,
//         ValueList : {
//             $Type : 'Common.ValueListType',
//             CollectionPath : 'Products',
//             Parameters : [
//                 {
//                     $Type : 'Common.ValueListParameterInOut',
//                     LocalDataProperty : product_ID,
//                     ValueListProperty : 'ID'
//                 },
//             ]
//         },
//     };
// };

annotate service.CustRequests with @(
    UI.Facets : [
        {
            $Type : 'UI.ReferenceFacet',
            ID : 'BASEINFO',
            Target : '@UI.FieldGroup#BaseInfo',
        },
        // {
        //     $Type : 'UI.ReferenceFacet',
        //     Target : 'custRequestNote/@UI.LineItem',
        //     Label : 'Notes',
        //     ID : 'CUSTREQUESTNOTE',
        // },
        {
            $Type : 'UI.ReferenceFacet',
            Target : 'requestNotes/@UI.LineItem',
            Label : 'Notes',
            ID : 'CUSTREQUESTNOTE',
        },
        {
            $Type : 'UI.ReferenceFacet',
            Target : 'custRequestParty/@UI.LineItem',
            Label : 'Party',
            ID : 'CUSTREQUESPARTY',
        },
    ]
);
annotate service.RequestNotes with @(
    UI.LineItem : [
        {
            $Type : 'UI.DataField',
            Value : noteId,
            Label : 'Note ID',
        },
        {
            $Type : 'UI.DataField',
            Value : noteName,
        },
        {
            $Type : 'UI.DataField',
            Value : noteInfo,
        },
   ]
);
annotate service.CustRequestParties with @(
    UI.LineItem : [
        {
            $Type : 'UI.DataField',
            Value : party_ID,
            Label : 'Party ID',
        },
        {
            $Type : 'UI.DataField',
            Value : roleTypeId,
            Label : 'Role ID'
        },
   ]
);
annotate service.CustRequestNotes with @(
    UI.LineItem : [
        {
            $Type : 'UI.DataField',
            Value : noteData_ID,
            Label : 'Note ID',
        },
        {
            $Type : 'UI.DataField',
            Value : noteData.noteName,
        },
        {
            $Type : 'UI.DataField',
            Value : noteData.noteInfo,
        },
        {
            $Type : 'UI.DataFieldForAction',
            Label : '{i18n>AddNotes}',
            Action : 'CatalogService.addNotes'
        },
   ]
);
annotate service.CustRequests with @(
    UI.LineItem : [
        {
            $Type : 'UI.DataField',
            Value : ID,
            Label : 'ID',
        },
        {
            $Type : 'UI.DataField',
            Value : Items.product_ID,
            Label : 'Product ID',
        },
        {
            $Type : 'UI.DataField',
            Value : custRequestName,
        },
    ]
);
