package com.bytebeats.py4j.exception;

/**
 * ${DESCRIPTION}
 *
 * @author Ricky Fung
 * @date 2017-02-16 20:38
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
