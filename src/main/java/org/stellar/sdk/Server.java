package org.stellar.sdk;

import org.apache.commons.io.IOUtils;
import org.stellar.sdk.requests.*;
import org.stellar.sdk.responses.GsonSingleton;
import org.stellar.sdk.responses.SubmitTransactionResponse;
import org.stellar.sdk.utils.UrlUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Main class used to connect to Horizon server.
 */
public class Server {

    private URI serverURI;

    private URL submitTransactionUrl;

    public Server(String uri) {
        try {
            serverURI = new URI(uri);
            submitTransactionUrl = new URL(serverURI.toURL(), "/transactions");
        } catch (URISyntaxException | MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns {@link AccountsRequestBuilder} instance.
     */
    public AccountsRequestBuilder accounts() {
        return new AccountsRequestBuilder(serverURI);
    }

    /**
     * Returns {@link EffectsRequestBuilder} instance.
     */
    public EffectsRequestBuilder effects() {
        return new EffectsRequestBuilder(serverURI);
    }

    /**
     * Returns {@link LedgersRequestBuilder} instance.
     */
    public LedgersRequestBuilder ledgers() {
        return new LedgersRequestBuilder(serverURI);
    }

    /**
     * Returns {@link OffersRequestBuilder} instance.
     */
    public OffersRequestBuilder offers() {
        return new OffersRequestBuilder(serverURI);
    }

    /**
     * Returns {@link OperationsRequestBuilder} instance.
     */
    public OperationsRequestBuilder operations() {
        return new OperationsRequestBuilder(serverURI);
    }

    /**
     * Returns {@link OrderBookRequestBuilder} instance.
     */
    public OrderBookRequestBuilder orderBook() {
        return new OrderBookRequestBuilder(serverURI);
    }

    /**
     * Returns {@link TradesRequestBuilder} instance.
     */
    public TradesRequestBuilder trades() {
        return new TradesRequestBuilder(serverURI);
    }

    /**
     * Returns {@link PathsRequestBuilder} instance.
     */
    public PathsRequestBuilder paths() {
        return new PathsRequestBuilder(serverURI);
    }

    /**
     * Returns {@link PaymentsRequestBuilder} instance.
     */
    public PaymentsRequestBuilder payments() {
        return new PaymentsRequestBuilder(serverURI);
    }

    /**
     * Returns {@link TransactionsRequestBuilder} instance.
     */
    public TransactionsRequestBuilder transactions() {
        return new TransactionsRequestBuilder(serverURI);
    }

    /**
     * Submits transaction to the network.
     *
     * @param transaction transaction to submit to the network.
     * @return {@link SubmitTransactionResponse}
     * @throws IOException
     */
    public SubmitTransactionResponse submitTransaction(Transaction transaction) throws IOException {

        Map<String, String> parameters = new HashMap<>();
        parameters.put("tx", transaction.toEnvelopeXdrBase64());
        String postData = UrlUtils.getParametersAsString(parameters);

        HttpURLConnection conn = (HttpURLConnection) submitTransactionUrl.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Accept-Encoding", "identity");
        conn.setRequestProperty("Content-Length", String.valueOf(postData.length()));

        try (DataOutputStream out = new DataOutputStream(conn.getOutputStream())) {
            out.write(postData.getBytes());
        }

        try (InputStream responseStream = conn.getInputStream()) {
            StringWriter writer = new StringWriter();
            IOUtils.copy(responseStream, writer, StandardCharsets.UTF_8);
            String responseString = writer.toString();
            return GsonSingleton.getInstance().fromJson(responseString, SubmitTransactionResponse.class);
        }

    }

    /**
     * To support mocking a client
     * @param url submit transaction url
     */
    void setSubmitTransactionUrl(URL url) {
        submitTransactionUrl = url;
    }

}
