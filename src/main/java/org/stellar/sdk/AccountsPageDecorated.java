package org.stellar.sdk;

class AccountsPageDecorated {
  // Deserialized by {@link AccountsPageDecoratedDeserializer}
  private AccountsPage page;

  public AccountsPageDecorated(AccountsPage page) {
    this.page = page;
  }

  public AccountsPage getPage() {
    return page;
  }
}
