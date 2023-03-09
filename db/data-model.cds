namespace my.bookshop;

entity Books {
  key ID : Integer;
  title  : String;
  stock  : Integer;
}

entity CustRequest {
  key ID : UUID;
  custRequestName: String;
  description: String;
  statusId : String;
  fromPartyId : String;
  custRequestItem : Composition of one CustRequestItem on custRequestItem.custRequestId = ID;
}

entity CustRequestItem {
  key ID : UUID;
  custRequestItemSeqId : String;
  custRequestId : String; 
  productId : String;
  quantity : Double;
}