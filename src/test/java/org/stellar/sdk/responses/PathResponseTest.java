package org.stellar.sdk.responses;

import org.junit.Test;
import org.stellar.sdk.AssetTypeCreditAlphaNum4;
import org.stellar.sdk.AssetTypeNative;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class PathResponseTest {
    @Test
    public void testDeserialize() throws IOException {
        String filePath = "src/test/resources/responses/path.json";
        String json = new String(Files.readAllBytes(Paths.get(filePath)));
        PathResponse pathResponse =
                GsonSingleton.getInstance().fromJson(json, PathResponse.class);

        assertEquals("credit_alphanum4", pathResponse.getSourceAssetType());
        assertEquals("CNY", pathResponse.getSourceAssetCode());
        assertEquals("GAREELUB43IRHWEASCFBLKHURCGMHE5IF6XSE7EXDLACYHGRHM43RFOX", pathResponse.getSourceAssetIssuer());
        assertEquals("39.9854243", pathResponse.getSourceAmount());
        assertEquals(new AssetTypeCreditAlphaNum4("CNY", "GAREELUB43IRHWEASCFBLKHURCGMHE5IF6XSE7EXDLACYHGRHM43RFOX"), pathResponse.getSourceAsset());

        assertEquals("credit_alphanum4", pathResponse.getDestinationAssetType());
        assertEquals("BB1", pathResponse.getDestinationAssetCode());
        assertEquals("GD5J6HLF5666X4AZLTFTXLY46J5SW7EXRKBLEYPJP33S33MXZGV6CWFN", pathResponse.getDestinationAssetIssuer());
        assertEquals("5.0000000", pathResponse.getDestinationAmount());
        assertEquals(new AssetTypeCreditAlphaNum4("BB1", "GD5J6HLF5666X4AZLTFTXLY46J5SW7EXRKBLEYPJP33S33MXZGV6CWFN"), pathResponse.getDestinationAsset());

        assertEquals(2, pathResponse.getPath().size());
        assertEquals(new AssetTypeNative(), pathResponse.getPath().get(0));
        assertEquals(new AssetTypeCreditAlphaNum4("USD","GDUKMGUGDZQK6YHYA5Z6AY2G4XDSZPSZ3SW5UN3ARVMO6QSRDWP5YLEX"), pathResponse.getPath().get(1));
    }
}