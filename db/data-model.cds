namespace my.bookshop;

entity Books {
  key ID : Integer;
  title  : String;
  stock  : Integer;
}

entity CustRequest {
  key ID : String;
  custRequestName: String;
  description: String;
  statusId : String;
  fromPartyId : String;
  custRequestItem : Association to many CustRequestItem on custRequestItem.custRequestId = ID;
}

entity CustRequestItem {
  key ID : String;
  custRequestId : String;
  custRequestItemSeqId : String;
  productId : String;
  quantity : Double;
  custRequest : Association to CustRequest on custRequestId = custRequest.ID;
}