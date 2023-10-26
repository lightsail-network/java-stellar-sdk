package org.stellar.sdk.responses.sorobanrpc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import org.stellar.sdk.responses.GsonSingleton;

public class SimulateTransactionDeserializerTest {
  @Test
  public void testDeserializeSuccess() {
    SorobanRpcResponse<SimulateTransactionResponse> simulateTransactionResponse =
        GsonSingleton.getInstance()
            .fromJson(
                jsonSuccess,
                new TypeToken<SorobanRpcResponse<SimulateTransactionResponse>>() {}.getType());
    SimulateTransactionResponse data = simulateTransactionResponse.getResult();
    assertEquals(
        data.getTransactionData(),
        "AAAAAAAAAAMAAAAAAAAAAFi3xKLI8peqjz0kcSgf38zsr+SOVmMxPsGOEqc+ypihAAAABgAAAAHFiyv7xPBU5zJPa/IMrT4CbkG7rRptIMPX1JGN7RZUEQAAABQAAAABAAAAAAAAAAd9NB8oNB2RvkInt877MS77oKzbRandwCJ7VsvW4NmL2QAAAAAAAAACAAAABgAAAAAAAAAAWLfEosjyl6qPPSRxKB/fzOyv5I5WYzE+wY4Spz7KmKEAAAAVWqB4d0kcwg0AAAAAAAAAAAAAAAYAAAABxYsr+8TwVOcyT2vyDK0+Am5Bu60abSDD19SRje0WVBEAAAAQAAAAAQAAAAIAAAAPAAAAB0NvdW50ZXIAAAAAEgAAAAAAAAAAWLfEosjyl6qPPSRxKB/fzOyv5I5WYzE+wY4Spz7KmKEAAAABAAAAAAAWW9sAAAYkAAABnAAAA2AAAAAAAAAAqQ==");
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
    assertEquals(
        data.getResults().get(0).getAuth().get(0),
        "AAAAAQAAAAAAAAAAWLfEosjyl6qPPSRxKB/fzOyv5I5WYzE+wY4Spz7KmKFaoHh3SRzCDQAAAAAAAAAAAAAAAAAAAAHFiyv7xPBU5zJPa/IMrT4CbkG7rRptIMPX1JGN7RZUEQAAAAlpbmNyZW1lbnQAAAAAAAACAAAAEgAAAAAAAAAAWLfEosjyl6qPPSRxKB/fzOyv5I5WYzE+wY4Spz7KmKEAAAADAAAACgAAAAA=");
    assertEquals(data.getResults().get(0).getXdr(), "AAAAAwAAAAo=");
    assertEquals(data.getCost().getCpuInstructions().longValue(), 1274180L);
    assertEquals(data.getCost().getMemoryBytes().longValue(), 162857L);
    assertEquals(data.getLatestLedger().longValue(), 694L);
    assertNull(data.getError());
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
  }

  String jsonSuccess =
      "{\n"
          + "    \"jsonrpc\": \"2.0\",\n"
          + "    \"id\": \"4a69486e5a83404bbf9772f3f02c21e5\",\n"
          + "    \"result\": {\n"
          + "        \"transactionData\": \"AAAAAAAAAAMAAAAAAAAAAFi3xKLI8peqjz0kcSgf38zsr+SOVmMxPsGOEqc+ypihAAAABgAAAAHFiyv7xPBU5zJPa/IMrT4CbkG7rRptIMPX1JGN7RZUEQAAABQAAAABAAAAAAAAAAd9NB8oNB2RvkInt877MS77oKzbRandwCJ7VsvW4NmL2QAAAAAAAAACAAAABgAAAAAAAAAAWLfEosjyl6qPPSRxKB/fzOyv5I5WYzE+wY4Spz7KmKEAAAAVWqB4d0kcwg0AAAAAAAAAAAAAAAYAAAABxYsr+8TwVOcyT2vyDK0+Am5Bu60abSDD19SRje0WVBEAAAAQAAAAAQAAAAIAAAAPAAAAB0NvdW50ZXIAAAAAEgAAAAAAAAAAWLfEosjyl6qPPSRxKB/fzOyv5I5WYzE+wY4Spz7KmKEAAAABAAAAAAAWW9sAAAYkAAABnAAAA2AAAAAAAAAAqQ==\",\n"
          + "        \"events\": [\n"
          + "            \"AAAAAQAAAAAAAAAAAAAAAgAAAAAAAAADAAAADwAAAAdmbl9jYWxsAAAAAA0AAAAgxYsr+8TwVOcyT2vyDK0+Am5Bu60abSDD19SRje0WVBEAAAAPAAAACWluY3JlbWVudAAAAAAAABAAAAABAAAAAgAAABIAAAAAAAAAAFi3xKLI8peqjz0kcSgf38zsr+SOVmMxPsGOEqc+ypihAAAAAwAAAAo=\",\n"
          + "            \"AAAAAQAAAAAAAAABxYsr+8TwVOcyT2vyDK0+Am5Bu60abSDD19SRje0WVBEAAAACAAAAAAAAAAIAAAAPAAAACWZuX3JldHVybgAAAAAAAA8AAAAJaW5jcmVtZW50AAAAAAAAAwAAAAo=\"\n"
          + "        ],\n"
          + "        \"minResourceFee\": \"94876\",\n"
          + "        \"results\": [\n"
          + "            {\n"
          + "                \"auth\": [\n"
          + "                    \"AAAAAQAAAAAAAAAAWLfEosjyl6qPPSRxKB/fzOyv5I5WYzE+wY4Spz7KmKFaoHh3SRzCDQAAAAAAAAAAAAAAAAAAAAHFiyv7xPBU5zJPa/IMrT4CbkG7rRptIMPX1JGN7RZUEQAAAAlpbmNyZW1lbnQAAAAAAAACAAAAEgAAAAAAAAAAWLfEosjyl6qPPSRxKB/fzOyv5I5WYzE+wY4Spz7KmKEAAAADAAAACgAAAAA=\"\n"
          + "                ],\n"
          + "                \"xdr\": \"AAAAAwAAAAo=\"\n"
          + "            }\n"
          + "        ],\n"
          + "        \"cost\": {\n"
          + "            \"cpuInsns\": \"1274180\",\n"
          + "            \"memBytes\": \"162857\"\n"
          + "        },\n"
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
