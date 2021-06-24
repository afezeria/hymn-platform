package com.github.afezeria.hymn.common.util;

/**
 * @author afezeria
 * date 2021/6/10 上午10:18
 */
public class UidGenerateException extends RuntimeException {
    public UidGenerateException(Throwable cause) {
        super(cause);
    }

    public UidGenerateException(String message) {
        super(message);
    }
}
