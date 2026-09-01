package org.stellar.sdk.contract.exception;

import lombok.Getter;
import org.stellar.sdk.Util;

/**
 * Raised when the <a href="https://stellar.org/protocol/cap-85" target="_blank">CAP-85</a>
 * executable tag entry that an external executable reference points at cannot be found on the
 * network. The entry may have been archived; restoring the tag entry footprint may be required.
 */
public class ExternalRefNotFoundException extends ContractIntrospectionException {
  @Getter private final String owner;
  private final byte[] tag;

  public ExternalRefNotFoundException(String owner, byte[] tag) {
    super(
        "External executable tag entry not found or archived. The tag entry footprint may need to be restored. owner: "
            + owner
            + ", tag: "
            + describeTag(tag));
    this.owner = owner;
    this.tag = tag == null ? null : tag.clone();
  }

  /** Returns a defensive copy of the tag that was looked up. */
  public byte[] getTag() {
    return tag == null ? null : tag.clone();
  }

  /**
   * Renders the tag for the message. A tag is an unbounded {@code SCString} that need not be valid
   * UTF-8, so a binary tag is shown as hex rather than lenient-decoded into text that no longer
   * identifies it.
   */
  private static String describeTag(byte[] tag) {
    if (tag == null) {
      return "null";
    }
    return Util.decodeUtf8(tag).orElseGet(() -> "0x" + Util.bytesToHex(tag));
  }
}
