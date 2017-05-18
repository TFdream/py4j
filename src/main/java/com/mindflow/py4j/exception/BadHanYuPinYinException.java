package com.mindflow.py4j.exception;

/**
 *
 * @author Ricky Fung
 */
public class BadHanYuPinYinException extends RuntimeException {

    private static final long serialVersionUID = 4447260855879734366L;

    public BadHanYuPinYinException() {
    }

    public BadHanYuPinYinException(String message) {
        super(message);
    }

    public BadHanYuPinYinException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadHanYuPinYinException(Throwable cause) {
        super(cause);
    }
}
