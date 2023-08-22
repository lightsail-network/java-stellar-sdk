package org.stellar.javastellarsdkdemoapp

import android.os.Bundle
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
import org.stellar.sdk.Server

private const val HORIZON_SERVER = "https://horizon.stellar.lobstr.co/"
private const val PUBLIC = "Public Global Stellar Network ; September 2015"
private const val TESTNET = "Test SDF Network ; September 2015"

class MainActivity : ComponentActivity() {
    private lateinit var networkViewModel: NetworkViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        networkViewModel = ViewModelProvider(this).get(NetworkViewModel::class.java)

        setContent {
            JavaStellarSDKDemoAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Main(networkViewModel)
                }
            }
        }
    }
}

class NetworkViewModel : ViewModel() {
    private val _network = mutableStateOf<String?>(null)
    val network: String? get() = _network.value

    fun fetchNetworkPassphrase() {
        viewModelScope.launch {
            _network.value = withContext(Dispatchers.IO) {
                getNetwork()
            }
        }
    }
}

@Composable
fun Main(networkViewModel: NetworkViewModel, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { networkViewModel.fetchNetworkPassphrase() }
        ) {
            Text(text = "Get Network")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = networkViewModel.network ?: "No network info",
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    JavaStellarSDKDemoAppTheme {
        val networkViewModel = NetworkViewModel()
        Main(networkViewModel)
    }
}

private fun getNetwork(): String? {
    val server = Server(HORIZON_SERVER)
    return try {
        when (server.root().networkPassphrase) {
            PUBLIC -> {
                "public"
            }

            TESTNET -> {
                "testnet"
            }

            else -> {
                "others"
            }
        }
    } catch (e: Exception) {
        null
    }
}