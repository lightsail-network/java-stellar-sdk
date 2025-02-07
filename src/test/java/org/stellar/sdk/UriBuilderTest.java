package org.stellar.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.net.URI;
import org.junit.Test;

public class UriBuilderTest {
  @Test
  public void testFromUriString() throws Exception {
    final var builder = new UriBuilder("https://google.com");
    assertEquals(new URI("https://google.com"), builder.build());
  }

  @Test
  public void testFromUriStringHandlesQueryParams() throws Exception {
    final var builder = new UriBuilder("https://google.com?param1=value1&param2=value2");
    assertEquals("value1", builder.getQueryParameter("param1").get());
    assertEquals("value2", builder.getQueryParameter("param2").get());
  }

  @Test
  public void testFromUri() throws Exception {
    final var builder = new UriBuilder(new URI("https://google.com"));
    assertEquals(new URI("https://google.com"), builder.build());
  }

  @Test
  public void testSetSchemeThrowsBlank() throws Exception {
    final var builder = new UriBuilder(new URI("https://google.com"));
    assertThrows(IllegalArgumentException.class, () -> builder.setScheme(""));
  }

  @Test
  public void testSetScheme() throws Exception {
    final var builder = new UriBuilder(new URI("https://google.com"));
    builder.setScheme("ftp");
    assertEquals("ftp", builder.build().getScheme());
  }

  @Test
  public void testSetHostThrowsBlank() throws Exception {
    final var builder = new UriBuilder(new URI("https://google.com"));
    assertThrows(IllegalArgumentException.class, () -> builder.setHost(""));
  }

  @Test
  public void testSetHost() throws Exception {
    final var builder = new UriBuilder(new URI("https://google.com"));
    builder.setHost("goooooogle");
    assertEquals("goooooogle", builder.build().getHost());
  }

  @Test
  public void testSetPortAllowsNull() throws Exception {
    final var builder = new UriBuilder(new URI("https://google.com:8080"));
    builder.setPort(null);
    assertEquals(new URI("https://google.com"), builder.build());
  }

  @Test
  public void testSetPortInvalid() throws Exception {
    final var builder = new UriBuilder(new URI("https://google.com:8080"));
    assertThrows(IllegalArgumentException.class, () -> builder.setPort(-1));
    assertThrows(IllegalArgumentException.class, () -> builder.setPort(65536));
  }

  @Test
  public void testSetQueryParam() throws Exception {
    final var builder = new UriBuilder("https://google.com");
    builder.setQueryParameter("param", "value");
    assertEquals(new URI("https://google.com?param=value"), builder.build());
  }

  @Test
  public void testSetMultipleQueryParams() throws Exception {
    final var builder = new UriBuilder("https://google.com");
    builder.setQueryParameter("param1", "value1");
    builder.setQueryParameter("param2", "value2");
    assertEquals(new URI("https://google.com?param1=value1&param2=value2"), builder.build());
  }

  @Test
  public void testAddPathSegmentBlankThrows() throws Exception {
    final var builder = new UriBuilder(new URI("https://google.com:8080"));
    assertThrows(IllegalArgumentException.class, () -> builder.addPathSegment(""));
  }

  @Test
  public void testAddPathSegment() throws Exception {
    final var builder = new UriBuilder("https://google.com");
    builder.addPathSegment("path");
    assertEquals(new URI("https://google.com/path"), builder.build());
  }
}
