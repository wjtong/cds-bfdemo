namespace my.bookshop;

entity Books {
  key ID : Integer;
  title  : String;
  stock  : Integer;
}

entity CustRequest {
  key custRequestId : String;
  custRequestName: String;
  description: String;
  statusId : String;
  fromPartyId : String;
  custRequestItem : Composition of many CustRequestItem on custRequestItem.custRequestId = custRequestId @assert.integrity;
}

entity CustRequestItem {
  key custRequestId : String;
  key custRequestItemSeqId : String default 00001;
  custRequest : Association to CustRequest on custRequest.custRequestId = custRequestId; 
  productId : String;
  quantity : Double;
}