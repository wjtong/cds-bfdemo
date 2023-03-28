namespace banfftech.user;

entity UserLogin {
  key userLoginId: String;
  currentPassword: String;
  passwordHint: String;
  isSystem: Boolean;
  hasLoggedOut: Boolean;
  requirePasswordChange: Boolean;
  lastCurrencyUom: String;
  lastLocale: String;
  lastTimeZone: String;
  disabledDateTime: DateTime;
  successiveFailedLogins: Integer;
  externalAuthId: String;
  userLdapDn: String;
  disabledBy: String;
}

entity VariantFile {
   key userLoginId: String;
   key fileName: String;
   fileType: String;
   variantManagementReference: String;
   variantReference: String;
   reference: String;
   packageName: String;
   variantData: String;
   userLogin : Association to one UserLogin on userLoginId = userLogin.userLoginId;
}

