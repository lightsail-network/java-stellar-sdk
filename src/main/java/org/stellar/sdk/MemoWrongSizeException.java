package org.stellar.sdk;

/**
 * Indicates that value passed to Memo has not the right size
 * @see Memo
 */
public class MemoWrongSizeException extends RuntimeException {
    private int size;
    
    public MemoWrongSizeException(int length) {
        super();
        this.size = length;
    }

    public MemoWrongSizeException(String message, int length) {
        super(message);
        this.size = length;
    }
    
    public int getActualSize() {
        return size;
    }
}
