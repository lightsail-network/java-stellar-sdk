package org.stellar.sdk.scval;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.stellar.sdk.xdr.SCVal;
import org.stellar.sdk.xdr.SCValType;
import org.stellar.sdk.xdr.SCVec;

/** Represents an {@link SCVal} with the type of {@link SCValType#SCV_VEC}. */
@Value
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ScvVec extends Scv {
  private static final SCValType TYPE = SCValType.SCV_VEC;

  @NonNull List<Scv> value;

  @Override
  public SCVal toSCVal() {
    return new SCVal.Builder()
        .discriminant(TYPE)
        .vec(new SCVec(value.stream().map(Scv::toSCVal).toArray(SCVal[]::new)))
        .build();
  }

  @Override
  public SCValType getSCValType() {
    return TYPE;
  }

  public static ScvVec fromSCVal(SCVal scVal) {
    if (scVal.getDiscriminant() != TYPE) {
      throw new IllegalArgumentException(
          String.format(
              "invalid scVal type, expected %s, but got %s", TYPE, scVal.getDiscriminant()));
    }

    List<SCVal> scValList = Arrays.asList(scVal.getVec().getSCVec());
    List<Scv> value = scValList.stream().map(Scv::fromSCVal).collect(Collectors.toList());
    return new ScvVec(value);
  }
}
