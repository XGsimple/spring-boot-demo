package com.xkcoding.common.utils;

import cn.hutool.core.util.StrUtil;
import com.xkcoding.common.enums.ResponseCodeType;
import com.xkcoding.common.exception.MyRuntimeException;

import java.util.Collection;
import java.util.function.Supplier;

public class Conditions {
    public Conditions() {
    }

    public static <T> T checkNotNull(T reference) {
        return checkNotNull(reference, ResponseCodeType.PARA_ERROR);
    }

    public static <T> T checkNotNull(T reference, String responseMessage) {
        return checkNotNull(reference, ResponseCodeType.PARA_ERROR, responseMessage);
    }

    public static <T> T checkNotNull(T reference, ResponseCodeType responseCodeType) {
        return checkNotNull(reference, responseCodeType, responseCodeType.getMsg());
    }

    public static <T> T checkNotNull(T reference, ResponseCodeType responseCodeType, String responseMessage) {
        return checkNotNull(reference, (ResponseCodeType)responseCodeType, responseMessage, (String)null, (String)null);
    }

    public static <T> T checkNotNull(T reference, ResponseCodeType responseCodeType, String responseMessage,
                                     String subCode, String subMessage) {
        if (reference == null) {
            throw new MyRuntimeException(responseCodeType.getCode(), responseMessage);
        } else {
            return reference;
        }
    }

    public static <T> void checkNotEmpty(T reference) {
        checkNotEmpty(reference, ResponseCodeType.PARA_ERROR);
    }

    public static <T> void checkNotEmpty(T reference, String responseMessage) {
        checkNotEmpty(reference, ResponseCodeType.PARA_ERROR, responseMessage);
    }

    public static <T> void checkNotEmpty(T reference, ResponseCodeType responseCodeType) {
        checkNotEmpty(reference, responseCodeType, responseCodeType.getMsg());
    }

    public static <T> void checkNotEmpty(T reference, ResponseCodeType responseCodeType, String responseMessage) {
        checkNotEmpty(reference, (ResponseCodeType)responseCodeType, responseMessage, (String)null, (String)null);
    }

    public static <T> void checkNotEmpty(T reference, ResponseCodeType responseCodeType, String responseMessage,
                                         String subCode, String subMessage) {
        checkNotEmpty(reference, responseCodeType.getCode(), responseMessage, subCode, subMessage);
    }

    public static <T> void checkNotEmpty(T reference, String responseCode, String responseMessage, String subCode,
                                         String subMessage) {
        if (reference == null) {
            throw new MyRuntimeException(responseCode,
                                         (String)null,
                                         responseMessage,
                                         subCode,
                                         subMessage,
                                         (Throwable)null,
                                         (Object)null);
        } else if (reference instanceof String && StrUtil.isEmpty(String.valueOf(reference))) {
            throw new MyRuntimeException(responseCode,
                                         (String)null,
                                         responseMessage,
                                         subCode,
                                         subMessage,
                                         (Throwable)null,
                                         (Object)null);
        } else if (reference instanceof Collection && (reference == null || ((Collection)reference).isEmpty())) {
            throw new MyRuntimeException(responseCode,
                                         (String)null,
                                         responseMessage,
                                         subCode,
                                         subMessage,
                                         (Throwable)null,
                                         (Object)null);
        }
    }

    public static <T> void checkNotBlank(T reference) {
        checkNotBlank(reference, ResponseCodeType.PARA_ERROR);
    }

    public static <T> void checkNotBlank(T reference, String responseMessage) {
        checkNotBlank(reference,
                      (String)ResponseCodeType.PARA_ERROR.getCode(),
                      responseMessage,
                      (String)null,
                      (String)null);
    }

    public static <T> void checkNotBlank(T reference, ResponseCodeType errorType) {
        checkNotBlank(reference, (String)errorType.getCode(), errorType.getMsg(), (String)null, (String)null);
    }

    public static <T> void checkNotBlank(T reference, ResponseCodeType responseCodeType, String responseMessage) {
        checkNotBlank(reference, (String)responseCodeType.getCode(), responseMessage, (String)null, (String)null);
    }

    public static <T> void checkNotBlank(T reference, ResponseCodeType responseCodeType, String responseMessage,
                                         String subCode, String subMessage) {
        checkNotBlank(reference, responseCodeType.getCode(), responseMessage, subCode, subMessage);
    }

    public static <T> void checkNotBlank(T reference, String responseCode, String responseMessage, String subCode,
                                         String subMessage) {
        if (reference == null) {
            throw new MyRuntimeException(responseCode,
                                         (String)null,
                                         responseMessage,
                                         subCode,
                                         subMessage,
                                         (Throwable)null,
                                         (Object)null);
        } else if (reference instanceof String && StrUtil.isBlank(String.valueOf(reference))) {
            throw new MyRuntimeException(responseCode,
                                         (String)null,
                                         responseMessage,
                                         subCode,
                                         subMessage,
                                         (Throwable)null,
                                         (Object)null);
        }
    }

    public static void assertTrue(boolean reference, String responseMessage) {
        if (!reference) {
            throw new MyRuntimeException(ResponseCodeType.BIZ_EXCEPTION.getCode(), responseMessage);
        }
    }

    public static void assertTrue(boolean reference, String responseCode, String responseMessage) {
        if (!reference) {
            throw new MyRuntimeException(responseCode, responseMessage);
        }
    }

    public static void assertTrue(boolean reference, ResponseCodeType responseCodeType) {
        if (!reference) {
            throw new MyRuntimeException(responseCodeType.getCode(), responseCodeType.getMsg());
        }
    }

    public static void assertTrue(boolean reference, ResponseCodeType responseCodeType, String responseMessage) {
        if (!reference) {
            throw new MyRuntimeException(responseCodeType.getCode(), responseMessage);
        }
    }

    public static void assertTrue(boolean reference, ResponseCodeType responseCodeType, String responseMessage,
                                  String subCode, String subMessage) {
        if (!reference) {
            throw new MyRuntimeException(responseCodeType.getCode(),
                                         (String)null,
                                         responseMessage,
                                         subCode,
                                         subMessage,
                                         (Throwable)null,
                                         (Object)null);
        }
    }

    public static void assertFalse(boolean reference, String responseMessage) {
        if (reference) {
            throw new MyRuntimeException(ResponseCodeType.BIZ_EXCEPTION.getCode(), responseMessage);
        }
    }

    public static void assertFalse(boolean reference, String responseCode, String responseMessage) {
        if (reference) {
            throw new MyRuntimeException(responseCode, responseMessage);
        }
    }

    public static void assertFalse(boolean reference, ResponseCodeType responseCodeType) {
        if (reference) {
            throw new MyRuntimeException(responseCodeType.getCode(), responseCodeType.getMsg());
        }
    }

    public static void assertFalse(boolean reference, ResponseCodeType responseCodeType, String responseMessage) {
        if (reference) {
            throw new MyRuntimeException(responseCodeType.getCode(), responseMessage);
        }
    }

    public static void assertFalse(boolean reference, ResponseCodeType responseCodeType, String responseMessage,
                                   String subCode, String subMessage) {
        if (reference) {
            throw new MyRuntimeException(responseCodeType.getCode(),
                                         (String)null,
                                         responseMessage,
                                         subCode,
                                         subMessage,
                                         (Throwable)null,
                                         (Object)null);
        }
    }

    public static void runIfTrue(boolean condition, Runnable action) {
        if (condition) {
            action.run();
        }

    }

    public static <T> T get(boolean condition, Supplier<? extends T> trueSupplier,
                            Supplier<? extends T> falseSupplier) {
        return condition ? trueSupplier.get() : falseSupplier.get();
    }
}
