package org.stellar.sdk.federation;

/**
 * Helper class for resolving Stellar addresses.
 *
 * @see <a href="https://developers.stellar.org/docs/glossary/federation/"
 *     target="_blank">Federation docs</a>
 */
public class Federation {
  private Federation() {}

  /**
   * This method is a helper method for handling user inputs that contain `destination` value. It
   * accepts two types of values:
   *
   * <ul>
   *   <li>For Stellar address (ex. <code>bob*stellar.org`</code>) it splits Stellar address and
   *       then tries to find information about federation server in <code>stellar.toml</code> file
   *       for a given domain.
   *   <li>For account ID (ex. <code>GB5XVAABEQMY63WTHDQ5RXADGYF345VWMNPTN2GFUDZT57D57ZQTJ7PS</code>
   *       ) it simply returns the given Account ID.
   * </ul>
   *
   * @param value Stellar address or account id
   * @throws MalformedAddressException Address is malformed
   * @throws ConnectionErrorException Connection problems
   * @throws NoFederationServerException Stellar.toml does not contain federation server info
   * @throws FederationServerInvalidException Federation server is invalid (malformed URL, not
   *     HTTPS, etc.)
   * @throws StellarTomlNotFoundInvalidException Stellar.toml file was not found or was malformed.
   * @throws NotFoundException Stellar address not found by federation server
   * @throws ServerErrorException Federation server responded with error
   * @return FederationResponse
   */
  public static FederationResponse resolve(String value) {
    String[] tokens = value.split("\\*");
    if (tokens.length == 1) {
      // accountId
      return new FederationResponse(null, value, null, null);
    } else if (tokens.length == 2) {
      String domain = tokens[1];
      FederationServer server = FederationServer.createForDomain(domain);
      return server.resolveAddress(value);
    } else {
      throw new MalformedAddressException();
    }
  }
}
