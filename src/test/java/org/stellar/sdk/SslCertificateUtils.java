package org.stellar.sdk;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

public class SslCertificateUtils {
  public static SSLContext createSslContext() {
    try {
      KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
      keyPairGenerator.initialize(2048);
      KeyPair keyPair = keyPairGenerator.generateKeyPair();
      X509Certificate certificate = generateCertificate(keyPair);

      KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
      keyStore.load(null, null);
      keyStore.setKeyEntry(
          "private", keyPair.getPrivate(), new char[0], new Certificate[] {certificate});

      KeyManagerFactory keyManagerFactory =
          KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
      keyManagerFactory.init(keyStore, new char[0]);
      KeyManager[] keyManagers = keyManagerFactory.getKeyManagers();

      TrustManager trustManager = createTrustAllCertsManager();

      SSLContext sslContext = SSLContext.getInstance("TLS");
      sslContext.init(keyManagers, new TrustManager[] {trustManager}, null);
      return sslContext;
    } catch (Exception e) {
      throw new RuntimeException("Failed to create SSL socket factory", e);
    }
  }

  private static X509Certificate generateCertificate(KeyPair keyPair) throws Exception {
    long now = System.currentTimeMillis();
    Date startDate = new Date(now);
    Date endDate = new Date(now + 365L * 24L * 60L * 60L * 1000L); // 1 year validity

    X500Name issuerName = new X500Name("CN=localhost");
    BigInteger serialNumber = new BigInteger(64, new SecureRandom());
    SubjectPublicKeyInfo subjectPublicKeyInfo =
        SubjectPublicKeyInfo.getInstance(keyPair.getPublic().getEncoded());

    X509v3CertificateBuilder certificateBuilder =
        new X509v3CertificateBuilder(
            issuerName, serialNumber, startDate, endDate, issuerName, subjectPublicKeyInfo);

    ContentSigner contentSigner =
        new JcaContentSignerBuilder("SHA256WithRSA").build(keyPair.getPrivate());
    X509Certificate certificate =
        new JcaX509CertificateConverter().getCertificate(certificateBuilder.build(contentSigner));

    certificate.checkValidity(new Date());
    certificate.verify(keyPair.getPublic());

    return certificate;
  }

  public static X509TrustManager createTrustAllCertsManager() {
    return new X509TrustManager() {
      @Override
      public void checkClientTrusted(X509Certificate[] chain, String authType) {}

      @Override
      public void checkServerTrusted(X509Certificate[] chain, String authType) {}

      @Override
      public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
      }
    };
  }
}
