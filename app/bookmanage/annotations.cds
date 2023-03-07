using CatalogService as service from '../../srv/cat-service';

annotate CatalogService.Books with @(
    UI.LineItem : [
        {
            $Type : 'UI.DataField',
            Value : ID,
            Label : 'ID',
        },
        {
            $Type : 'UI.DataField',
            Value : stock,
            Label : 'Stock',
        },
        {
            $Type : 'UI.DataField',
            Value : title,
            Label : 'Title',
        },
    ]
);
