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
                Value : Items.productId,
            },
            {
                $Type : 'UI.DataField',
                Value : Items.quantity,
            },
            {
                $Type : 'UI.DataField',
                Value : fromPartyId,
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
    fromPartyId @Common : { 
        ValueList : {
            $Type : 'Common.ValueListType',
            CollectionPath : 'Parties',
            Parameters : [
                {
                    $Type : 'Common.ValueListParameterInOut',
                    LocalDataProperty : fromPartyId,
                    ValueListProperty : 'partyId'
                },
            ]
        },
    }
    // party
    // @ValueList.entity : 'Parties';
};
annotate service.CustRequestItems with {
    productId @Common : {
        Text : product.productName,
        TextArrangement : #TextOnly,
        ValueList : {
            $Type : 'Common.ValueListType',
            CollectionPath : 'Products',
            Parameters : [
                {
                    $Type : 'Common.ValueListParameterInOut',
                    LocalDataProperty : productId,
                    ValueListProperty : 'productId'
                },
            ]
        },
    };
};

annotate service.CustRequests with @(
    UI.Facets : [
        {
            $Type : 'UI.ReferenceFacet',
            ID : 'BASEINFO',
            Target : '@UI.FieldGroup#BaseInfo',
        },
        {
            $Type : 'UI.ReferenceFacet',
            Target : 'CustRequestNote/@UI.LineItem',
            Label : 'Notes',
            ID : 'CUSTREQUESTNOTE',
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
            Value : Items.productId,
            Label : 'Product ID',
        },
        {
            $Type : 'UI.DataField',
            Value : custRequestName,
        },
    ]
);
