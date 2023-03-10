using CatalogService as service from '../../srv/cat-service';

// annotate service.CustRequests with @(
//   UI.SelectionFields : [
//     custRequestId,
//     openDateTime
//   ]
// );


// annotate service.CustRequests with @(UI.LineItem: [
//     {
//         $Type: 'UI.DataField',
//         Value: custRequestId,
//         Label: '服务ID',
//     },
//     {
//         $Type: 'UI.DataField',
//         Value: partyGroup.groupName,
//         Label: '客户名称',
//     },
//     {
//         $Type: 'UI.DataField',
//         Value: custRequestItem.product.productName,
//         Label: '机器名称',
//     },
//     {
//         $Type: 'UI.DataField',
//         Value: openDateTime,
//         Label: '报修时间',
//     },
// ],
// ![@UI.Criticality]:processingResultLevel,
// );

annotate service.CustRequests with @UI.LineItem : {
    $value : [
        {$Type : 'UI.DataField',Value: custRequestId,Label: '服务ID'},
        {$Type : 'UI.DataField',Value: partyGroup.groupName,Label: '客户名称',},
        {$Type : 'UI.DataField',Value: custRequestItem.product.productName,Label: '机器名称',},
        {$Type : 'UI.DataField',Value: openDateTime, Label: '报修时间'},
    ],
    ![@UI.Criticality]:processingResultLevel,
};

//Header 服务情报基础信息
annotate service.CustRequests with @(
    UI.FieldGroup #HeaderFields: {
        Label: '服务情报基础信息',
        $Type: 'UI.FieldGroupType',
        Data : [
            {
                $Type: 'UI.DataField',
                Value: openDateTime,
                Label: '报修时间'
            },
            {
                $Type: 'UI.DataField',
                Value: internalComment,
                Label: '所属维修站'
            },
            {
                $Type: 'UI.DataField',
                Value: custRequestItem.sequenceNum,
                Label: '保修卡号'
            },
            
        ],
    },
    UI.HeaderFacets                : [{
        $Type : 'UI.ReferenceFacet',
        ID    : 'HeaderFieldGroup',
        Label : '服务情报基本信息',
        Target: '@UI.FieldGroup#HeaderFields',
    }],
    UI.HeaderInfo                  : {
        TypeName      : 'CustRequest Manage',
        TypeNamePlural: '',
        Title         : {
            $Type: 'UI.DataField',
            Value: custRequestId
        }
    }
);

annotate service.CustRequests with @(
    // 客户信息
    UI.FieldGroup #CoustomInfo: {
        $Type: 'UI.FieldGroupType',
        Data : [
            {
                $Type: 'UI.DataField',
                Value: partyGroup.groupName,
                Label: '客户名称',
            }
        ],
    },
    //设备信息
    UI.FieldGroup #FixedAssetInfo: {
        $Type: 'UI.FieldGroupType',
        Data : [
            {
                $Type: 'UI.DataField',
                Value: custRequestItem.product.productId,
                Label: '设备ID',
            },
            {
                $Type: 'UI.DataField',
                Value: custRequestItem.product.internalName,
                Label: '设备序列号',
            },
            {
                $Type: 'UI.DataField',
                Value: custRequestItem.product.brandName,
                Label: '产品型号',
            },
            {
                $Type: 'UI.DataField',
                Value: custRequestItem.product.productName,
                Label: '产品名称',
            },
        ],
    },
    UI.Facets              : [{
        $Type : 'UI.ReferenceFacet',
        ID    : 'BASEINFO',
        Label : '客户信息',
        Target: '@UI.FieldGroup#CoustomInfo'
    },{
        $Type : 'UI.ReferenceFacet',
        ID    : 'FixedAssetInfo',
        Label : '设备信息',
        Target: '@UI.FieldGroup#FixedAssetInfo'
    }]
);



// annotate service.CustRequests with @(UI.LineItem: [
//     {
//         $Type: 'UI.DataField',
//         Value: ID,
//         Label: '服务ID',
//     },
//     {
//         $Type: 'UI.DataField',
//         Value: groupName,
//         Label: '客户名称',
//     },
//     {
//         $Type: 'UI.DataField',
//         Value: productName,
//         Label: '机器名称',
//     },
//     {
//         $Type: 'UI.DataField',
//         Value: openDateTime,
//         Label: '报修时间',
//     },
// ]);

// //Header 服务情报基础信息
// annotate service.CustRequests with @(
//     UI.FieldGroup #HeaderFieldGroup: {
//         Label: '服务情报基础信息',
//         $Type: 'UI.FieldGroupType',
//         Data : [
//             {
//                 $Type: 'UI.DataField',
//                 Value: openDateTime,
//                 Label: '报修时间'
//             },
//             {
//                 $Type: 'UI.DataField',
//                 Value: productId,
//                 Label: '报修卡号'
//             },
//             {
//                 $Type: 'UI.DataField',
//                 Value: productId,
//                 Label: '所属维修站'
//             },
//         ],
//     },
//     UI.HeaderFacets                : [{
//         $Type : 'UI.ReferenceFacet',
//         ID    : 'HeaderFieldGroup',
//         Label : '服务情报基本信息',
//         Target: '@UI.FieldGroup#HeaderFieldGroup',
//     }],
//     UI.HeaderInfo                  : {
//         TypeName      : 'CustRequest Manage',
//         TypeNamePlural: '',
//         Title         : {
//             $Type: 'UI.DataField',
//             Value: ID
//         }
//     }
// );



// annotate service.CustRequests with @(
//     // 客户信息
//     UI.FieldGroup #CoustomInfo: {
//         $Type: 'UI.FieldGroupType',
//         Data : [
//             {
//                 $Type: 'UI.DataField',
//                 Value: groupName,
//                 Label: '客户名称',
//             },
//             {
//                 $Type: 'UI.DataField',
//                 Value: groupName,
//                 Label: '电话',
//             },
//             {
//                 $Type: 'UI.DataField',
//                 Value: groupName,
//                 Label: '手机',
//             },
//             {
//                 $Type: 'UI.DataField',
//                 Value: groupName,
//                 Label: '地址',
//             },
//             {
//                 $Type: 'UI.DataField',
//                 Value: groupName,
//                 Label: '邮编',
//             },
//         ],
//     },
//     // 设备信息
//     UI.FieldGroup #FixedAssetInfo: {
//         $Type: 'UI.FieldGroupType',
//         Data : [
//             {
//                 $Type: 'UI.DataField',
//                 Value: groupName,
//                 Label: '设备ID',
//             },
//             {
//                 $Type: 'UI.DataField',
//                 Value: groupName,
//                 Label: '设备序列号',
//             },
//             {
//                 $Type: 'UI.DataField',
//                 Value: groupName,
//                 Label: '产品型号',
//             },
//             {
//                 $Type: 'UI.DataField',
//                 Value: groupName,
//                 Label: '保修卡号',
//             },
//         ],
//     },
//     UI.Facets              : [{
//         $Type : 'UI.ReferenceFacet',
//         ID    : 'BASEINFO',
//         Label : '客户信息',
//         Target: '@UI.FieldGroup#CoustomInfo'
//     },{
//         $Type : 'UI.ReferenceFacet',
//         ID    : 'FixedAssetInfo',
//         Label : '设备信息',
//         Target: '@UI.FieldGroup#FixedAssetInfo'
//     }]
// );

