package org.stellar.sdk.exception;

import lombok.Getter;

@Getter
public class BadResponseException extends SdkException {
    private final int code;
    private final String body;
    private final String title;
    private final String detail;

    public BadResponseException(int code, String body, String title, String detail) {
        super("Bad response.");
        this.code = code;
        this.body = body;
        this.title = title;
        this.detail = detail;
    }
}