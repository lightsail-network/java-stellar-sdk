package org.stellar.sdk.federation

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.net.URLEncoder
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.stellar.sdk.SslCertificateUtils
import org.stellar.sdk.federation.exception.FederationServerInvalidException
import org.stellar.sdk.federation.exception.NoFederationServerException
import org.stellar.sdk.federation.exception.NotFoundException
import org.stellar.sdk.federation.exception.StellarTomlNotFoundInvalidException

class FederationTest :
  FunSpec({
    lateinit var mockWebServer: MockWebServer
    lateinit var client: OkHttpClient
    lateinit var domain: String

    // Runs before each test block
    beforeTest {
      val sslSocketFactory = SslCertificateUtils.createSslSocketFactory()
      val trustAllCerts = SslCertificateUtils.createTrustAllCertsManager()
      mockWebServer = MockWebServer()
      mockWebServer.useHttps(sslSocketFactory, false)
      mockWebServer.start()
      val baseUrl = mockWebServer.url("")
      domain = "${baseUrl.host}:${baseUrl.port}"
      client =
        OkHttpClient.Builder()
          .sslSocketFactory(sslSocketFactory, trustAllCerts)
          .hostnameVerifier { _, _ -> true }
          .build()
    }

    // Runs after each test block
    afterTest { mockWebServer.shutdown() }

    // A helper function to create a Federation instance
    fun federation() = Federation(client)

    test("resolveAddress success") {
      val stellarToml = """FEDERATION_SERVER = "https://$domain/federation""""
      val successResponse =
        """
                {
                  "stellar_address": "bob*$domain",
                  "account_id": "GCW667JUHCOP5Y7KY6KGDHNPHFM4CS3FCBQ7QWDUALXTX3PGXLSOEALY"
                }
                """
          .trimIndent()

      mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(stellarToml))
      mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(successResponse))

      val response = federation().resolveAddress("bob*$domain")

      val stellarTomlRequest = mockWebServer.takeRequest()
      stellarTomlRequest.method shouldBe "GET"
      stellarTomlRequest.path shouldBe "/.well-known/stellar.toml"

      val federationRequest = mockWebServer.takeRequest()
      federationRequest.method shouldBe "GET"
      federationRequest.path shouldBe
        "/federation?type=name&q=bob*${URLEncoder.encode(domain, "UTF-8")}"

      response.stellarAddress shouldBe "bob*$domain"
      response.accountId shouldBe "GCW667JUHCOP5Y7KY6KGDHNPHFM4CS3FCBQ7QWDUALXTX3PGXLSOEALY"
      response.memoType shouldBe null
      response.memo shouldBe null
    }

    test("resolveAddress success with memo") {
      val stellarToml = """FEDERATION_SERVER = "https://$domain/federation""""
      val successResponse =
        """
                {
                  "stellar_address": "bob*$domain",
                  "account_id": "GCW667JUHCOP5Y7KY6KGDHNPHFM4CS3FCBQ7QWDUALXTX3PGXLSOEALY",
                  "memo_type": "text",
                  "memo": "test federation"
                }
                """
          .trimIndent()

      mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(stellarToml))
      mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(successResponse))

      val response = federation().resolveAddress("bob*$domain")

      response.stellarAddress shouldBe "bob*$domain"
      response.accountId shouldBe "GCW667JUHCOP5Y7KY6KGDHNPHFM4CS3FCBQ7QWDUALXTX3PGXLSOEALY"
      response.memoType shouldBe "text"
      response.memo shouldBe "test federation"
    }

    test("resolveAddress not found") {
      val stellarToml = """FEDERATION_SERVER = "https://$domain/federation""""
      val notFoundResponse = """{"code":"not_found","message":"Account not found"}"""

      mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(stellarToml))
      mockWebServer.enqueue(MockResponse().setResponseCode(404).setBody(notFoundResponse))

      shouldThrow<NotFoundException> { federation().resolveAddress("bob*$domain") }

      // Verify requests were made correctly
      mockWebServer.requestCount shouldBe 2
    }

    test("resolveAccountId success") {
      val accountId = "GCW667JUHCOP5Y7KY6KGDHNPHFM4CS3FCBQ7QWDUALXTX3PGXLSOEALY"
      val stellarToml = """FEDERATION_SERVER = "https://$domain/federation""""
      val successResponse =
        """
                {
                  "stellar_address": "bob*$domain",
                  "account_id": "$accountId"
                }
                """
          .trimIndent()

      mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(stellarToml))
      mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(successResponse))

      val response = federation().resolveAccountId(accountId, domain)

      val stellarTomlRequest = mockWebServer.takeRequest()
      stellarTomlRequest.method shouldBe "GET"
      stellarTomlRequest.path shouldBe "/.well-known/stellar.toml"

      val federationRequest = mockWebServer.takeRequest()
      federationRequest.method shouldBe "GET"
      federationRequest.path shouldBe "/federation?type=id&q=$accountId"

      response.accountId shouldBe accountId
      response.memo shouldBe null
    }

    test("malformed address throws exception") {
      shouldThrow<IllegalArgumentException> { Federation().resolveAddress("bob*stellar.org*test") }
      shouldThrow<IllegalArgumentException> { Federation().resolveAddress("bob") }
    }

    test("stellar.toml not found throws exception") {
      mockWebServer.enqueue(MockResponse().setResponseCode(404).setBody(""))

      shouldThrow<StellarTomlNotFoundInvalidException> {
        federation().resolveAddress("bob*$domain")
      }
    }

    test("no federation server in stellar.toml throws exception") {
      mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(""))

      shouldThrow<NoFederationServerException> { federation().resolveAddress("bob*$domain") }
    }

    test("federation server using http throws exception") {
      val stellarToml = """FEDERATION_SERVER = "http://$domain/federation""""
      mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(stellarToml))

      shouldThrow<FederationServerInvalidException> { federation().resolveAddress("bob*$domain") }
    }
  })
