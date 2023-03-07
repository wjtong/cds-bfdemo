using CatalogService as service from '../../srv/cat-service';

annotate service.CustRequests with @(
    UI.LineItem : [
        {
            $Type : 'UI.DataField',
            Value : ID,
            Label : 'ID',
        },
        {
            $Type : 'UI.DataField',
            Value : productId,
            Label : 'Product ID',
        },
    ]
);
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
                Value : productId,
            },
        ],
    },
    UI.Facets : [
        {
            $Type : 'UI.ReferenceFacet',
            ID : 'BASEINFO',
            Target : '@UI.FieldGroup#BaseInfo'
        }
    ]
);
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
);
