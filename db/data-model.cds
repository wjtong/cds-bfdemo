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

entity CustRequest : cuid,managed {
  custRequestName: String @title : '{i18n>CustRequestName}';
  description: String;
  statusId : String;
  fromParty : Association to Party;
  CustRequestNote : Composition of many CustRequestNote on CustRequestNote.custRequest = $self;
  Items : Composition of one CustRequestItem on Items.custRequest = $self;
}

entity CustRequestItem : managed {
  key custRequest : Association to CustRequest;
  key itemSeqId : String default 10;
  quantity : Double;
  product : Association to Product;
  // fixedAssetFault : Composition of one FixedAssetFault on custRequestId = fixedAssetFault.custRequestId and itemSeqId = fixedAssetFault.itemSeqId;
  fixedAssetFault : Composition of one FixedAssetFault on fixedAssetFault.custRequestItems = $self;
}

entity Product {
  key ID : String;
  productTypeId : String;
  internalName : String;
  productName : String;
  description : String;
}

entity Party {
  key ID : String;
  partyName : String;
}

entity WorkEffort {
  key workEffortId : String;
  workEffortName : String;
}

entity CustRequestWorkEffort {
  key workEffortId : String;
  key custRequest : Association to one CustRequest;
  workEffort : Association to one WorkEffort on workEffort.workEffortId = workEffortId;
}

entity FixedAsset {
  key fixedAssetId : String;
  instanceOfProduct : Association to Product;
  serialNumber : String;
}

entity FixedAssetFault : cuid,managed {
  // fixedAssetId : String;
  fixedAsset : Association to one FixedAsset;
  custRequestItems : Association to one CustRequestItem;
  // workEffortId : String;
  workEffort : Association to one WorkEffort;
  statusId : String;
  description : String;
}

entity NoteData : cuid, managed {
  noteName : String @title : '{i18n>NoteName}';
  noteInfo : String @title : '{i18n>NoteInfo}';
}

entity CustRequestNote {
  key custRequest : Association to CustRequest;
  // key noteData : Association to NoteData @title : '{i18n>NoteId}';
  key noteData : Composition of NoteData @title : '{i18n>NoteId}';
}