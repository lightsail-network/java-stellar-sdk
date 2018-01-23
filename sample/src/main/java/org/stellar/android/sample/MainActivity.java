package org.stellar.android.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.stellar.sdk.KeyPair;
import org.stellar.sdk.Server;
import org.stellar.sdk.responses.AccountResponse;

import java.io.IOException;
import java.net.URI;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        KeyPair pair = KeyPair.random();

        Server server = new Server("https://horizon-testnet.stellar.org");
        AccountResponse account = null;
        try {
            account = server.accounts().account(URI.create(pair.getAccountId()));
            System.out.println("Balances for account " + pair.getAccountId());
            for (AccountResponse.Balance balance : account.getBalances()) {
                System.out.println(String.format(
                        "Type: %s, Code: %s, Balance: %s",
                        balance.getAssetType(),
                        balance.getAssetCode(),
                        balance.getBalance()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
