namespace my.bookshop;

entity Books {
  key ID : Integer;
  title  : String;
  stock  : Integer;
}

entity CustRequest {
  // key ID : String;
  key custRequestId: String;
  custRequestTypeId: String;
  custRequestCategoryId: String;
  priority: String;
  custRequestDate: DateTime;
  responseRequiredDate: DateTime;
  custRequestName: String;
  maximumAmountUomId: String;
  productStoreId: String;
  salesChannelEnumId: String;
  fulfillContactMechId: String;
  currencyUomId: String;
  closedDateTime: DateTime;
  internalComment: String;
  reason: String;
  createdDate: DateTime;
  createdByUserLogin: String;
  lastModifiedDate: DateTime;
  lastModifiedByUserLogin: String;
  description: String;
  statusId : String;
  fromPartyId : String;
  openDateTime : DateTime;
  processingResultLevel : Integer;
  custRequestItem : Composition of one CustRequestItem on custRequestItem.custRequestId = custRequestId;
  partyGroup : Association to one PartyGroup on partyGroup.partyId = fromPartyId;
  party : Association to one Party on party.partyId = fromPartyId;
}

entity CustRequestItem {
  // key ID : String;
  key custRequestId : String;
  key custRequestItemSeqId : String;
  productId : String;
  quantity : Double;
  custRequestResolutionId : String;
  priority : Integer;
  sequenceNum : Integer;
  requiredByDate : DateTime;
  selectedAmount : Double;
  maximumAmount : Double;
  reservStart : DateTime;
  reservLength : Double;
  reservPersons : Double;
  configId : String;
  description : String;
  story : String;
  custRequest : Association to one CustRequest on custRequestId = custRequest.custRequestId;
  product : Association to one Product on productId = product.productId;
}

entity Party {
  // key ID : String;
  partyId : String;
  partyTypeId : String;
}

entity PartyGroup {
  // key ID : String;
  partyId : String;
  groupName : String;
  party : Association to one Party on partyId = party.partyId;
}

entity Product {
  // key ID : String;
  productId: String;
  productTypeId: String;
  primaryProductCategoryId : String;
  facilityId : String;
  internalName : String;
  brandName : String;
  comments : String;
  productName : String;
  description : String;
}

// entity FixedAsset {
//   // key ID : String;
//   fixedAssetId: String;
//   productModel: String;
//   fixedAssetTypeId : String;
//   instanceOfProductId : String;
//   serialNumber : String;
// }

// entity FixedAssetFault {
//   // key ID : String;
//   fixedAssetFaultId: String;
//   fixedAssetId: String;
//   productPartsId: String;
//   productFaultId : String;
//   fixedAsset : Association to one FixedAsset on fixedAssetId = fixedAsset.fixedAssetId;
// }

// entity CustRequests as select key 
//   custRequest.custRequestId,
//   custRequest.openDateTime,
//   custRequest.fromPartyId
// from CustRequest as custRequest;

// entity CustRequests as select key custRequest.custRequestId, custRequestItem.productId,custRequest.fromPartyId,partyGroup.groupName,product.productName,openDateTime
//         from CustRequest as custRequest
//         inner join CustRequestItem as custRequestItem on custRequest.custRequestId = custRequestItem.custRequestId
//         left join PartyGroup as partyGroup on custRequest.fromPartyId = partyGroup.partyId
//         left join Product as product on custRequestItem.productId = product.productId
//         group by custRequest.custRequestId;