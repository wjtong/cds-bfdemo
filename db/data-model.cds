namespace my.bookshop;
using {
    Currency,
    sap,
    managed,
    cuid
} from '@sap/cds/common';

entity Books {
  key ID : Integer;
  title  : String;
  stock  : Integer;
}

entity CustRequest : managed {
  key custRequestId : String;
  custRequestName: String;
  description: String;
  statusId : String;
  fromPartyId : String;
  party : Association to Party on party.partyId = fromPartyId;
  custRequestItem : Composition of one CustRequestItem on custRequestItem.custRequestId = custRequestId and custRequestItem.custRequestItemSeqId = 00001;
}

entity CustRequestItem : managed {
  key custRequestId : String;
  key custRequestItemSeqId : String default 00001;
  custRequest : Association to CustRequest on custRequest.custRequestId = custRequestId; 
  productId : String; 
  quantity : Double;
  product : Association to one Product on product.productId = productId;
  fixedAssetFault : Composition of one FixedAssetFault on custRequestId = fixedAssetFault.custRequestId and custRequestItemSeqId = fixedAssetFault.custRequestItemSeqId;
}

entity Product {
  key productId : String;
  productTypeId : String;
  internalName : String;
  productName : String;
  description : String;
}

entity Party {
  key partyId : String;
  partyName : String;
}

entity WorkEffort {
  key workEffortId : String;
  workEffortName : String;
}

entity CustRequestWorkEffort {
  key custRequestId : String;
  key workEffortId : String;
  custRequest : Association to one CustRequest on custRequest.custRequestId = custRequestId;
  workEffort : Association to one WorkEffort on workEffort.workEffortId = workEffortId;
}

entity FixedAsset {
  key fixedAssetId : String;
  instanceOfProductId : String;
  instanceOfProduct : Association to one Product on instanceOfProductId = instanceOfProduct.productId;
  serialNumber : String;
}

entity FixedAssetFault {
  key fixedAssetFaultId : String;
  fixedAssetId : String;
  fixedAsset : Association to one FixedAsset on fixedAssetId = fixedAsset.fixedAssetId;
  custRequestId : String;
  custRequestItemSeqId : String;
  custRequest : Association to one CustRequest on custRequestId = custRequest.custRequestId;
  workEffortId : String;
  workEffort : Association to one WorkEffort on workEffortId = workEffort.workEffortId;
  statusId : String;
  description : String;
}