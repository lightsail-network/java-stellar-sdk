package org.stellar.sdk.responses.operations;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Represents SetOptions operation response.
 *
 * @see <a
 *     href="https://developers.stellar.org/docs/data/horizon/api-reference/resources/operations/object/set-options"
 *     target="_blank">Operation documentation</a>
 * @see org.stellar.sdk.requests.OperationsRequestBuilder
 * @see org.stellar.sdk.Server#operations()
 */
@Value
@EqualsAndHashCode(callSuper = true)
public class SetOptionsOperationResponse extends OperationResponse {
  @SerializedName("low_threshold")
  Integer lowThreshold;

  @SerializedName("med_threshold")
  Integer medThreshold;

  @SerializedName("high_threshold")
  Integer highThreshold;

  @SerializedName("inflation_dest")
  String inflationDestination;

  @SerializedName("home_domain")
  String homeDomain;

  @SerializedName("signer_key")
  String signerKey;

  @SerializedName("signer_weight")
  Integer signerWeight;

  @SerializedName("master_key_weight")
  Integer masterKeyWeight;

  @SerializedName("clear_flags")
  List<Integer> clearFlags;

  @SerializedName("clear_flags_s")
  List<String> clearFlagStrings;

  @SerializedName("set_flags")
  List<Integer> setFlags;

  @SerializedName("set_flags_s")
  List<String> setFlagStrings;
}
