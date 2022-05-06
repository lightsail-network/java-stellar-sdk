package org.stellar.sdk;

/**
 * Indicates that value passed to Memo has not the right size
 * @see Memo
 */
public class IncorrectMemoSizeException extends RuntimeException {
    private int actualSize;
    
    public IncorrectMemoSizeException(int actualSize) {
        super();
        this.actualSize = actualSize;
    }

    public IncorrectMemoSizeException(String message, int actualSize) {
        super(message);
        this.actualSize = actualSize;
    }
    
    public int getActualSize() {
        return actualSize;
    }
}
