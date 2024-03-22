package org.stellar.sdk.responses.operations;

import lombok.EqualsAndHashCode;

/**
 * Represents RestoreFootprint operation response.
 *
 * @see <a
 *     href="https://github.com/stellar/go/blob/7ff6ffae29d278f979fcd6c6bed8cd0d4b4d2e08/protocols/horizon/operations/main.go#L383-L386">Horizon
 *     Protocol</a>
 * @see <a href="https://developers.stellar.org/api/horizon/resources/operations"
 *     target="_blank">Operation documentation</a>
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
@EqualsAndHashCode(callSuper = true)
public class RestoreFootprintOperationResponse extends OperationResponse {}
