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
  custRequestItem : Composition of one CustRequestItem on custRequestItem.custRequestId = custRequestId;
  // custRequestItem : Composition of one CustRequestItem not null;
}

entity CustRequestItem : managed {
  key custRequestId : String;
  key custRequestItemSeqId : String default 00001;
  custRequest : Association to CustRequest on custRequest.custRequestId = custRequestId; 
  productId : String; 
  quantity : Double;
  product : Association to one Product on product.productId = productId;
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