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
  custRequestItem : Composition of one CustRequestItem on custRequestItem.custRequest = $self @assert.integrity;
}

entity CustRequestItem {
  key ID : UUID;
  custRequestItemSeqId : String;
  custRequest : Association to CustRequest; 
  productId : String;
  quantity : Double;
}