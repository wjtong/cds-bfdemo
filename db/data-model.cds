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
  custRequestItem : Composition of one CustRequestItem on custRequestItem.custRequestId = ID;
}

entity CustRequestItem {
  key ID : String;
  custRequestItemSeqId : String;
  custRequestId : String; 
  productId : String;
  quantity : Double;
}