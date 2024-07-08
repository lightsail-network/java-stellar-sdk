package org.stellar.sdk.responses.sorobanrpc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import org.junit.Test;
import org.stellar.sdk.responses.gson.GsonSingleton;

public class SimulateTransactionDeserializerTest {
  @Test
  public void testDeserializeSuccess() throws IOException {
    SorobanRpcResponse<SimulateTransactionResponse> simulateTransactionResponse =
        GsonSingleton.getInstance()
            .fromJson(
                jsonSuccess,
                new TypeToken<SorobanRpcResponse<SimulateTransactionResponse>>() {}.getType());
    SimulateTransactionResponse data = simulateTransactionResponse.getResult();
    assertEquals(
        data.getTransactionData(),
        "AAAAAAAAAAIAAAAGAAAAAcwD/nT9D7Dc2LxRdab+2vEUF8B+XoN7mQW21oxPT8ALAAAAFAAAAAEAAAAHy8vNUZ8vyZ2ybPHW0XbSrRtP7gEWsJ6zDzcfY9P8z88AAAABAAAABgAAAAHMA/50/Q+w3Ni8UXWm/trxFBfAfl6De5kFttaMT0/ACwAAABAAAAABAAAAAgAAAA8AAAAHQ291bnRlcgAAAAASAAAAAAAAAAAg4dbAxsGAGICfBG3iT2cKGYQ6hK4sJWzZ6or1C5v6GAAAAAEAHfKyAAAFiAAAAIgAAAAAAAAAAw==");
    assertEquals(data.getEvents().size(), 2);
    assertEquals(
        data.getEvents().get(0),
        "AAAAAQAAAAAAAAAAAAAAAgAAAAAAAAADAAAADwAAAAdmbl9jYWxsAAAAAA0AAAAgxYsr+8TwVOcyT2vyDK0+Am5Bu60abSDD19SRje0WVBEAAAAPAAAACWluY3JlbWVudAAAAAAAABAAAAABAAAAAgAAABIAAAAAAAAAAFi3xKLI8peqjz0kcSgf38zsr+SOVmMxPsGOEqc+ypihAAAAAwAAAAo=");
    assertEquals(
        data.getEvents().get(1),
        "AAAAAQAAAAAAAAABxYsr+8TwVOcyT2vyDK0+Am5Bu60abSDD19SRje0WVBEAAAACAAAAAAAAAAIAAAAPAAAACWZuX3JldHVybgAAAAAAAA8AAAAJaW5jcmVtZW50AAAAAAAAAwAAAAo=");
    assertEquals(data.getMinResourceFee().longValue(), 94876L);
    assertEquals(data.getResults().size(), 1);
    assertEquals(data.getResults().get(0).getAuth().size(), 1);
    assertNotNull(data.getResults().get(0).parseAuth());
    assertEquals(
        data.getResults().get(0).parseXdr().toXdrBase64(), data.getResults().get(0).getXdr());
    assertEquals(
        data.getResults().get(0).getAuth().get(0),
        "AAAAAAAAAAAAAAAB6bfni71JNBarlvcR3WP2056a8vvFXQ0/CGfiBeDQA/wAAAAJaW5jcmVtZW50AAAAAAAAAgAAABIAAAAAAAAAAFi3xKLI8peqjz0kcSgf38zsr+SOVmMxPsGOEqc+ypihAAAAAwAAAAoAAAAA");
    assertEquals(data.getResults().get(0).getXdr(), "AAAAAwAAAAo=");
    assertEquals(data.getCost().getCpuInstructions().longValue(), 1274180L);
    assertEquals(data.getCost().getMemoryBytes().longValue(), 162857L);
    assertEquals(data.getStateChanges().size(), 1);
    assertEquals(data.getStateChanges().get(0).getType(), "created");
    assertEquals(
        data.getStateChanges().get(0).getKey(),
        "AAAAAAAAAABuaCbVXZ2DlXWarV6UxwbW3GNJgpn3ASChIFp5bxSIWg==");
    assertEquals(
        data.getStateChanges().get(0).parseKey().toXdrBase64(),
        data.getStateChanges().get(0).getKey());
    assertNull(data.getStateChanges().get(0).getBefore());
    assertNull(data.getStateChanges().get(0).parseBefore());
    assertEquals(
        data.getStateChanges().get(0).getAfter(),
        "AAAAZAAAAAAAAAAAbmgm1V2dg5V1mq1elMcG1txjSYKZ9wEgoSBaeW8UiFoAAAAAAAAAZAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=");
    assertEquals(
        data.getStateChanges().get(0).parseAfter().toXdrBase64(),
        data.getStateChanges().get(0).getAfter());
    assertNull(data.getStateChanges().get(0).parseBefore());
    assertNotNull(data.getStateChanges().get(0).parseKey());
    assertEquals(data.getLatestLedger().longValue(), 694L);
    assertNull(data.getError());
    assertNotNull(data.parseTransactionData());
    assertNotNull(data.parseEvents());
    assertEquals(data.parseEvents().size(), 2);
  }

  @Test
  public void testDeserializeFailed() {
    SorobanRpcResponse<SimulateTransactionResponse> simulateTransactionResponse =
        GsonSingleton.getInstance()
            .fromJson(
                jsonFailed,
                new TypeToken<SorobanRpcResponse<SimulateTransactionResponse>>() {}.getType());
    SimulateTransactionResponse data = simulateTransactionResponse.getResult();
    assertEquals(
        data.getError(),
        "HostError: Error(WasmVm, MissingValue)\n\nEvent log (newest first):\n   0: [Diagnostic Event] contract:CBQHNAXSI55GX2GN6D67GK7BHVPSLJUGZQEU7WJ5LKR5PNUCGLIMAO4K, topics:[error, Error(WasmVm, MissingValue)], data:[\"invoking unknown export\", increment]\n   1: [Diagnostic Event] topics:[fn_call, Bytes(CBQHNAXSI55GX2GN6D67GK7BHVPSLJUGZQEU7WJ5LKR5PNUCGLIMAO4K), increment], data:[Address(Account(58b7c4a2c8f297aa8f3d2471281fdfccecafe48e5663313ec18e12a73eca98a1)), 10]\n\nBacktrace (newest first):\n   0: soroban_env_host::vm::Vm::invoke_function_raw\n   1: soroban_env_host::host::frame::<impl soroban_env_host::host::Host>::call_n_internal\n   2: soroban_env_host::host::frame::<impl soroban_env_host::host::Host>::invoke_function\n   3: preflight::preflight_invoke_hf_op::{{closure}}\n   4: preflight::catch_preflight_panic\n   5: _cgo_a3255893d7fd_Cfunc_preflight_invoke_hf_op\n             at /tmp/go-build/cgo-gcc-prolog:99:11\n   6: runtime.asmcgocall\n             at ./runtime/asm_amd64.s:848\n\n");
    assertEquals(data.getTransactionData(), "");
    assertNull(data.getEvents());
    assertEquals(data.getMinResourceFee().longValue(), 0L);
    assertEquals(data.getCost().getCpuInstructions().longValue(), 0L);
    assertEquals(data.getCost().getMemoryBytes().longValue(), 0L);
    assertEquals(data.getLatestLedger().longValue(), 898L);
    assertNull(data.parseTransactionData());
    assertNull(data.parseEvents());
  }

  String jsonSuccess =
      "{\n"
          + "    \"jsonrpc\": \"2.0\",\n"
          + "    \"id\": \"4a69486e5a83404bbf9772f3f02c21e5\",\n"
          + "    \"result\": {\n"
          + "        \"transactionData\": \"AAAAAAAAAAIAAAAGAAAAAcwD/nT9D7Dc2LxRdab+2vEUF8B+XoN7mQW21oxPT8ALAAAAFAAAAAEAAAAHy8vNUZ8vyZ2ybPHW0XbSrRtP7gEWsJ6zDzcfY9P8z88AAAABAAAABgAAAAHMA/50/Q+w3Ni8UXWm/trxFBfAfl6De5kFttaMT0/ACwAAABAAAAABAAAAAgAAAA8AAAAHQ291bnRlcgAAAAASAAAAAAAAAAAg4dbAxsGAGICfBG3iT2cKGYQ6hK4sJWzZ6or1C5v6GAAAAAEAHfKyAAAFiAAAAIgAAAAAAAAAAw==\",\n"
          + "        \"events\": [\n"
          + "            \"AAAAAQAAAAAAAAAAAAAAAgAAAAAAAAADAAAADwAAAAdmbl9jYWxsAAAAAA0AAAAgxYsr+8TwVOcyT2vyDK0+Am5Bu60abSDD19SRje0WVBEAAAAPAAAACWluY3JlbWVudAAAAAAAABAAAAABAAAAAgAAABIAAAAAAAAAAFi3xKLI8peqjz0kcSgf38zsr+SOVmMxPsGOEqc+ypihAAAAAwAAAAo=\",\n"
          + "            \"AAAAAQAAAAAAAAABxYsr+8TwVOcyT2vyDK0+Am5Bu60abSDD19SRje0WVBEAAAACAAAAAAAAAAIAAAAPAAAACWZuX3JldHVybgAAAAAAAA8AAAAJaW5jcmVtZW50AAAAAAAAAwAAAAo=\"\n"
          + "        ],\n"
          + "        \"minResourceFee\": \"94876\",\n"
          + "        \"results\": [\n"
          + "            {\n"
          + "                \"auth\": [\n"
          + "                    \"AAAAAAAAAAAAAAAB6bfni71JNBarlvcR3WP2056a8vvFXQ0/CGfiBeDQA/wAAAAJaW5jcmVtZW50AAAAAAAAAgAAABIAAAAAAAAAAFi3xKLI8peqjz0kcSgf38zsr+SOVmMxPsGOEqc+ypihAAAAAwAAAAoAAAAA\"\n"
          + "                ],\n"
          + "                \"xdr\": \"AAAAAwAAAAo=\"\n"
          + "            }\n"
          + "        ],\n"
          + "        \"cost\": {\n"
          + "            \"cpuInsns\": \"1274180\",\n"
          + "            \"memBytes\": \"162857\"\n"
          + "        },\n"
          + "        \"stateChanges\": [\n"
          + "            {\n"
          + "                \"type\": \"created\",\n"
          + "                \"key\": \"AAAAAAAAAABuaCbVXZ2DlXWarV6UxwbW3GNJgpn3ASChIFp5bxSIWg==\",\n"
          + "                \"before\": null,\n"
          + "                \"after\": \"AAAAZAAAAAAAAAAAbmgm1V2dg5V1mq1elMcG1txjSYKZ9wEgoSBaeW8UiFoAAAAAAAAAZAAAAAAAAAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=\"\n"
          + "            }\n"
          + "        ],\n"
          + "        \"latestLedger\": \"694\"\n"
          + "    }\n"
          + "}";

  String jsonFailed =
      "{\n"
          + "    \"jsonrpc\": \"2.0\",\n"
          + "    \"id\": \"7b6ada2bdec04ee28147d1557aadc3cf\",\n"
          + "    \"result\": {\n"
          + "        \"error\": \"HostError: Error(WasmVm, MissingValue)\\n\\nEvent log (newest first):\\n   0: [Diagnostic Event] contract:CBQHNAXSI55GX2GN6D67GK7BHVPSLJUGZQEU7WJ5LKR5PNUCGLIMAO4K, topics:[error, Error(WasmVm, MissingValue)], data:[\\\"invoking unknown export\\\", increment]\\n   1: [Diagnostic Event] topics:[fn_call, Bytes(CBQHNAXSI55GX2GN6D67GK7BHVPSLJUGZQEU7WJ5LKR5PNUCGLIMAO4K), increment], data:[Address(Account(58b7c4a2c8f297aa8f3d2471281fdfccecafe48e5663313ec18e12a73eca98a1)), 10]\\n\\nBacktrace (newest first):\\n   0: soroban_env_host::vm::Vm::invoke_function_raw\\n   1: soroban_env_host::host::frame::<impl soroban_env_host::host::Host>::call_n_internal\\n   2: soroban_env_host::host::frame::<impl soroban_env_host::host::Host>::invoke_function\\n   3: preflight::preflight_invoke_hf_op::{{closure}}\\n   4: preflight::catch_preflight_panic\\n   5: _cgo_a3255893d7fd_Cfunc_preflight_invoke_hf_op\\n             at /tmp/go-build/cgo-gcc-prolog:99:11\\n   6: runtime.asmcgocall\\n             at ./runtime/asm_amd64.s:848\\n\\n\",\n"
          + "        \"transactionData\": \"\",\n"
          + "        \"events\": null,\n"
          + "        \"minResourceFee\": \"0\",\n"
          + "        \"cost\": {\n"
          + "            \"cpuInsns\": \"0\",\n"
          + "            \"memBytes\": \"0\"\n"
          + "        },\n"
          + "        \"latestLedger\": \"898\"\n"
          + "    }\n"
          + "}";
}
