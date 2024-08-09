package network.lightsail;

import org.stellar.sdk.Server;
import org.stellar.sdk.exception.BadRequestException;
import org.stellar.sdk.exception.BadResponseException;
import org.stellar.sdk.exception.NetworkException;
import org.stellar.sdk.requests.RequestBuilder;
import org.stellar.sdk.responses.Page;
import org.stellar.sdk.responses.operations.OperationResponse;

public class QueryHorizon {
  public static void main(String[] args) {
    // testnet horizon, for public network use https://horizon.stellar.org
    String horizonUrl = "https://horizon-testnet.stellar.org";
    Server server = new Server(horizonUrl);
    // Query the last 20 operations in the test network
    Page<OperationResponse> data;
    try {
      data = server.operations().order(RequestBuilder.Order.DESC).limit(20).execute();
    } catch (BadRequestException e) {
      System.out.println("Bad Request: " + e.getMessage());
      return;
    } catch (BadResponseException e) {
      System.out.println("Bad Response: " + e.getMessage());
      return;
    } catch (NetworkException e) {
      // This catch block will handle any NetworkException that was not caught by the previous catch
      // blocks.
      // NetworkException is a superclass of BadRequestException and BadResponseException.
      // It may have other subclasses that are not explicitly caught above.
      // By catching NetworkException here, we ensure that any uncaught subclasses of
      // NetworkException are handled.
      System.out.println("Network Issue: " + e.getMessage());
      return;
    }
    for (OperationResponse operation : data.getRecords()) {
      System.out.println(operation);
    }
  }
}
