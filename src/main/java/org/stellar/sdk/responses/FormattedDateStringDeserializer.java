package org.stellar.sdk.responses;

/**
 * Deserializes iso 8601 formatted strings into epochs
 */
public interface FormattedDateStringDeserializer {
    /**
     * deserialization of datetime string formats
     *
     * @param formmattedDateString formatted datetime string
     * @param defaultEpoch         default return value if formmattedDateString is invalid
     * @return                     unix epoch seconds represented by formmattedDateString
     * @throws IllegalArgumentException
     */
    long unixEpochSeconds(String formmattedDateString, long defaultEpoch);
}
