package org.stellar.sdk;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.stellar.base.Transaction;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private URIBuilder uriBuilder;
    private HttpClient httpClient = HttpClients.createDefault();

    public Server(String horizonURI) throws URISyntaxException {
        uriBuilder = new URIBuilder(horizonURI);
    }

    public Server(URI horizonURI) throws URISyntaxException {
        uriBuilder = new URIBuilder(horizonURI);
    }

    /**
     * To support mocking a client
     * @param httpClient
     */
    void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public SubmitTransactionResponse submitTransaction(Transaction transaction) throws IOException {
        URI transactionsURI;
        try {
            transactionsURI = uriBuilder.setPath("/transactions").build();
        } catch (URISyntaxException e) {
            throw new AssertionError(e);
        }
        HttpPost submitTransactionRequest = new HttpPost(transactionsURI);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tx", transaction.toEnvelopeXdrBase64()));
        submitTransactionRequest.setEntity(new UrlEncodedFormEntity(params));

        HttpResponse response = httpClient.execute(submitTransactionRequest);
        HttpEntity entity = response.getEntity();

        if (entity != null) {
            InputStream responseStream = entity.getContent();
            try {
                StringWriter writer = new StringWriter();
                IOUtils.copy(responseStream, writer, StandardCharsets.UTF_8);
                String responseString = writer.toString();
                SubmitTransactionResponse submitTransactionResponse = GsonSingleton.getInstance().fromJson(responseString, SubmitTransactionResponse.class);
                return submitTransactionResponse;
            } finally {
                responseStream.close();
            }
        }
        return null;
    }
}
