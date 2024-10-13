package org.stellar.sdk.responses.sorobanrpc;

import static org.junit.Assert.assertEquals;

import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;
import org.stellar.sdk.responses.gson.GsonSingleton;

public class GetVersionInfoDeserializerTest {
  @Test
  public void testDeserialize() throws IOException {
    String filePath = "src/test/resources/responses/sorobanrpc/get_version_info.json";
    String json = new String(Files.readAllBytes(Paths.get(filePath)));
    SorobanRpcResponse<GetVersionInfoResponse> getVersionInfoResponse =
        GsonSingleton.getInstance()
            .fromJson(
                json, new TypeToken<SorobanRpcResponse<GetVersionInfoResponse>>() {}.getType());
    assertEquals(getVersionInfoResponse.getResult().getVersion(), "21.1.0");
    assertEquals(
        getVersionInfoResponse.getResult().getCommitHash(),
        "fcd2f0523f04279bae4502f3e3fa00ca627e6f6a");
    assertEquals(getVersionInfoResponse.getResult().getBuildTimestamp(), "2024-05-10T11:18:38");
    assertEquals(
        getVersionInfoResponse.getResult().getCaptiveCoreVersion(),
        "stellar-core 21.0.0.rc2 (c6f474133738ae5f6d11b07963ca841909210273)");
    assertEquals(getVersionInfoResponse.getResult().getProtocolVersion().intValue(), 21);
  }
}
