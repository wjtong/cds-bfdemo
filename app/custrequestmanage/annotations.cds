using CatalogService as service from '../../srv/cat-service';

annotate service.CustRequests with @(
    UI.LineItem : [
        {
            $Type : 'UI.DataField',
            Value : custRequestId,
            Label : 'ID',
        },
        {
            $Type : 'UI.DataField',
            Value : custRequestItem.productId,
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
                Value : custRequestId,
            },
            {
                $Type : 'UI.DataField',
                Value : custRequestItem.productId,
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
        Value : custRequestId,
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
