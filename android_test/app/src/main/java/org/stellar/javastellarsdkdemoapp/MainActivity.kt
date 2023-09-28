package org.stellar.javastellarsdkdemoapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.stellar.javastellarsdkdemoapp.ui.theme.JavaStellarSDKDemoAppTheme
import org.stellar.sdk.Account
import org.stellar.sdk.AccountConverter
import org.stellar.sdk.Address
import org.stellar.sdk.Auth
import org.stellar.sdk.InvokeHostFunctionOperation
import org.stellar.sdk.KeyPair
import org.stellar.sdk.Network
import org.stellar.sdk.Server
import org.stellar.sdk.SorobanServer
import org.stellar.sdk.TimeBounds
import org.stellar.sdk.Transaction
import org.stellar.sdk.TransactionBuilder
import org.stellar.sdk.TransactionPreconditions
import org.stellar.sdk.federation.FederationServer
import org.stellar.sdk.scval.Scv
import org.stellar.sdk.xdr.ContractExecutable
import org.stellar.sdk.xdr.ContractExecutableType
import org.stellar.sdk.xdr.ContractIDPreimage
import org.stellar.sdk.xdr.ContractIDPreimage.ContractIDPreimageFromAddress
import org.stellar.sdk.xdr.ContractIDPreimageType
import org.stellar.sdk.xdr.CreateContractArgs
import org.stellar.sdk.xdr.ExtensionPoint
import org.stellar.sdk.xdr.HostFunction
import org.stellar.sdk.xdr.HostFunctionType
import org.stellar.sdk.xdr.Int64
import org.stellar.sdk.xdr.InvokeContractArgs
import org.stellar.sdk.xdr.LedgerEntryType
import org.stellar.sdk.xdr.LedgerFootprint
import org.stellar.sdk.xdr.LedgerKey
import org.stellar.sdk.xdr.LedgerKey.LedgerKeyAccount
import org.stellar.sdk.xdr.SorobanAddressCredentials
import org.stellar.sdk.xdr.SorobanAuthorizationEntry
import org.stellar.sdk.xdr.SorobanAuthorizedFunction
import org.stellar.sdk.xdr.SorobanAuthorizedFunctionType
import org.stellar.sdk.xdr.SorobanAuthorizedInvocation
import org.stellar.sdk.xdr.SorobanCredentials
import org.stellar.sdk.xdr.SorobanCredentialsType
import org.stellar.sdk.xdr.SorobanResources
import org.stellar.sdk.xdr.SorobanTransactionData
import org.stellar.sdk.xdr.Uint256
import org.stellar.sdk.xdr.Uint32
import org.stellar.sdk.xdr.XdrUnsignedInteger


class MainActivity : ComponentActivity() {
    private lateinit var sdkTestViewModel: SdkTestViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sdkTestViewModel = ViewModelProvider(this).get(SdkTestViewModel::class.java)

        setContent {
            JavaStellarSDKDemoAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Main(sdkTestViewModel)
                }
            }
        }
    }
}

class SdkTestViewModel : ViewModel() {
    private val _result = mutableStateOf<String?>(null)
    val result: String? get() = _result.value

    fun runSDKTest() {
        viewModelScope.launch {
            _result.value = withContext(Dispatchers.IO) {
                testSDK()
            }
        }
    }
}

@Composable
fun Main(sdkTestViewModel: SdkTestViewModel, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { sdkTestViewModel.runSDKTest() }
        ) {
            Text(text = "Run Test")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = sdkTestViewModel.result ?: "Not Run",
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    JavaStellarSDKDemoAppTheme {
        val sdkTestViewModel = SdkTestViewModel()
        Main(sdkTestViewModel)
    }
}

private fun testSDK(): String {
    return try {
        // send request to horizon server
        val server = Server("https://horizon.stellar.org")
        val horizonResp = server.root()
        if (horizonResp == null || horizonResp.networkPassphrase != Network.PUBLIC.networkPassphrase) {
            throw Exception("Query Horizon failed")
        }

        // send request to Soroban RPC server
        val sorobanServer = SorobanServer("https://soroban-testnet.stellar.org:443")
        if (sorobanServer.network.passphrase != Network.TESTNET.networkPassphrase) {
            throw Exception("Query Soroban Server failed")
        }

        // Test Federation
        val fedResp =
            FederationServer.createForDomain("lobstr.co").resolveAddress("example*lobstr.co")
        if (fedResp == null || fedResp.accountId == null) {
            throw Exception("Query Federation failed")
        }

        // build and parse transaction
        val source: KeyPair =
            KeyPair.fromSecretSeed("SCH27VUZZ6UAKB67BDNF6FA42YMBMQCBKXWGMFD5TZ6S5ZZCZFLRXKHS")

        val ledgerKey = LedgerKey.Builder()
            .discriminant(LedgerEntryType.ACCOUNT)
            .account(
                LedgerKeyAccount.Builder()
                    .accountID(
                        KeyPair.fromAccountId(
                            "GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO"
                        )
                            .xdrAccountId
                    )
                    .build()
            )
            .build()
        val sorobanData = SorobanTransactionData.Builder()
            .resources(
                SorobanResources.Builder()
                    .footprint(
                        LedgerFootprint.Builder()
                            .readOnly(arrayOf(ledgerKey))
                            .readWrite(arrayOf())
                            .build()
                    )
                    .readBytes(Uint32(XdrUnsignedInteger(699)))
                    .writeBytes(Uint32(XdrUnsignedInteger(0)))
                    .instructions(Uint32(XdrUnsignedInteger(34567)))
                    .build()
            )
            .refundableFee(Int64(100L))
            .ext(ExtensionPoint.Builder().discriminant(0).build())
            .build()
        val sorobanDataString = sorobanData.toXdrBase64()

        val createContractArgs = CreateContractArgs.Builder()
            .contractIDPreimage(
                ContractIDPreimage.Builder()
                    .discriminant(ContractIDPreimageType.CONTRACT_ID_PREIMAGE_FROM_ADDRESS)
                    .fromAddress(
                        ContractIDPreimageFromAddress.Builder()
                            .address(
                                Address(
                                    "GB7TAYRUZGE6TVT7NHP5SMIZRNQA6PLM423EYISAOAP3MKYIQMVYP2JO"
                                )
                                    .toSCAddress()
                            )
                            .salt(Uint256(ByteArray(32)))
                            .build()
                    )
                    .build()
            )
            .executable(
                ContractExecutable.Builder()
                    .discriminant(ContractExecutableType.CONTRACT_EXECUTABLE_TOKEN)
                    .build()
            )
            .build()
        val hostFunction = HostFunction.Builder()
            .discriminant(HostFunctionType.HOST_FUNCTION_TYPE_CREATE_CONTRACT)
            .createContract(createContractArgs)
            .build()
        val invokeHostFunctionOperation =
            InvokeHostFunctionOperation.builder().hostFunction(hostFunction).build()

        val sequenceNumber = 2908908335136768L
        val account = Account(source.accountId, sequenceNumber)
        val transaction: Transaction =
            TransactionBuilder(AccountConverter.enableMuxed(), account, Network.TESTNET)
                .addOperation(invokeHostFunctionOperation)
                .addPreconditions(
                    TransactionPreconditions.builder().timeBounds(TimeBounds(0, 0)).build()
                )
                .setBaseFee(100)
                .setSorobanData(sorobanDataString)
                .build()
        transaction.sign(source)
        Transaction.fromEnvelopeXdr(transaction.toEnvelopeXdrBase64(), Network.TESTNET)

        // sign entry
        val contractId = "CDCYWK73YTYFJZZSJ5V7EDFNHYBG4QN3VUNG2IGD27KJDDPNCZKBCBXK"
        val signer =
            KeyPair.fromSecretSeed("SAEZSI6DY7AXJFIYA4PM6SIBNEYYXIEM2MSOTHFGKHDW32MBQ7KVO6EN")
        val validUntilLedgerSeq = 654656L
        val network = Network.TESTNET

        val credentials = SorobanCredentials.Builder()
            .discriminant(SorobanCredentialsType.SOROBAN_CREDENTIALS_ADDRESS)
            .address(
                SorobanAddressCredentials.Builder()
                    .address(Address(signer.accountId).toSCAddress())
                    .nonce(Int64(123456789L))
                    .signatureExpirationLedger(Uint32(XdrUnsignedInteger(0L)))
                    .signature(Scv.toVoid())
                    .build()
            )
            .build()
        val invocation = SorobanAuthorizedInvocation.Builder()
            .function(
                SorobanAuthorizedFunction.Builder()
                    .discriminant(
                        SorobanAuthorizedFunctionType.SOROBAN_AUTHORIZED_FUNCTION_TYPE_CONTRACT_FN
                    )
                    .contractFn(
                        InvokeContractArgs.Builder()
                            .contractAddress(Address(contractId).toSCAddress())
                            .functionName(Scv.toSymbol("increment").sym)
                            .args(arrayOfNulls(0))
                            .build()
                    )
                    .build()
            )
            .subInvocations(arrayOfNulls(0))
            .build()
        val entry = SorobanAuthorizationEntry.Builder()
            .credentials(credentials)
            .rootInvocation(invocation)
            .build()
        Auth.authorizeEntry(entry.toXdrBase64(), signer, validUntilLedgerSeq, network)

        // send real transaction
        // https://horizon.stellar.org/transactions/fe833c504ca8b329c1e00adec7da79f61a55e28dc705e22a6515494427cc456a
        val xdr =
            "AAAAAgAAAAAcBaQEwcGuB3ErX1lnwKcP/pe84KcAabwB0GNk6SNvnwAAnQgCwi0TAAgJjwAAAAEAAAAAAAAAAAAAAABlD7HrAAAAAAAAAAIAAAAAAAAADAAAAAAAAAACZUJvbmQAAAAAAAAAAAAAAN80PsIQ9ohJQb1yLu4XaURrKURt5rbLoC7FfOM4vSZQAAAH0oBPrQsAAABFAB6EgAAAAABRAwlNAAAAAAAAAAwAAAAAAAAAAmVCb25kAAAAAAAAAAAAAADfND7CEPaISUG9ci7uF2lEaylEbea2y6AuxXzjOL0mUAAAByOt/5PHAAAAvQBMS0AAAAAAUQMJTgAAAAAAAAAB6SNvnwAAAEDR4cJo3zzZfCFusnM0sECT4xmhLW/bgwukIBxWWXvsDGLdQtE87lkrGijAPiyBEy3n1lDxDu4uwNpGnMXEaTAN"
        val tx: Transaction = Transaction.fromEnvelopeXdr(xdr, Network.PUBLIC) as Transaction
        val resp = server.submitTransaction(tx)
        if (!resp.isSuccess) {
            throw Exception("Submit transaction failed")
        }

        "SUCCESS"
    } catch (e: Exception) {
        Log.e("MainActivity", "testSDK ERROR", e)
        "FAILED"
    }
}