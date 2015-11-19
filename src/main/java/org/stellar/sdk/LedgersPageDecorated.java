package org.stellar.sdk;

class LedgersPageDecorated {
  // Deserialized by {@link LedgersPageDecoratedDeserializer}
  private LedgersPage page;

  public LedgersPageDecorated(LedgersPage page) {
    this.page = page;
  }

  public LedgersPage getPage() {
    return page;
  }
}
