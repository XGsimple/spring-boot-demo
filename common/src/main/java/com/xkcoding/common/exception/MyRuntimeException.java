package com.xkcoding.common.exception;

import cn.hutool.core.util.StrUtil;
import com.xkcoding.common.enums.ResponseCodeType;

public class MyRuntimeException extends BaseException {
    private static final long serialVersionUID = -3547094953316779244L;

    public MyRuntimeException(ResponseCodeType responseCodeType) {
        this(responseCodeType, (String)null, responseCodeType.getMsg());
    }

    public MyRuntimeException(ResponseCodeType responseCodeType, String message) {
        this(responseCodeType, (String)null, message);
    }

    public MyRuntimeException(String code, String message) {
        this(code, (String)null, message, (String)null, (String)null, (Throwable)null, (Object)null);
    }

    public MyRuntimeException(ResponseCodeType responseCodeType, String tipMessage, String message) {
        this(responseCodeType.getCode(),
             StrUtil.isBlank(tipMessage) ? responseCodeType.getMsg() : tipMessage,
             StrUtil.isBlank(message) ? responseCodeType.getMsg() : message,
             (String)null,
             (String)null,
             new Throwable(),
             (Object)null);
    }

    public MyRuntimeException(ResponseCodeType responseCodeType, Object responseDTO) {
        this(responseCodeType, responseDTO, responseCodeType.getMsg());
    }

    public MyRuntimeException(ResponseCodeType responseCodeType, Object responseDTO, String message) {
        this(responseCodeType.getCode(),
             StrUtil.isBlank(message) ? responseCodeType.getMsg() : message,
             StrUtil.isBlank(message) ? responseCodeType.getMsg() : message,
             (String)null,
             (String)null,
             new Throwable(),
             responseDTO);
    }

    public MyRuntimeException(ResponseCodeType responseCodeType, Object responseDTO, String tipMessage,
                              String message) {
        this(responseCodeType.getCode(),
             StrUtil.isBlank(tipMessage) ? responseCodeType.getMsg() : tipMessage,
             StrUtil.isBlank(message) ? responseCodeType.getMsg() : message,
             (String)null,
             (String)null,
             new Throwable(),
             responseDTO);
    }

    public MyRuntimeException(String code, String tipMessage, String message, Throwable cause, Object responseDTO) {
        this(code, tipMessage, message, (String)null, (String)null, cause, responseDTO);
    }

    public MyRuntimeException(String code, String tipMessage, String message, String subCode, String subMessage) {
        this(code, tipMessage, message, subCode, subMessage, (Throwable)null, (Object)null);
    }

    public MyRuntimeException(String code, String tipMessage, String message, String subCode, String subMessage,
                              Throwable cause, Object responseDTO) {
        super(code, tipMessage, message, subCode, subMessage, cause, responseDTO);
    }

    public static MyRuntimeExceptionBuilder builder() {
        return new MyRuntimeExceptionBuilder();
    }

    public static class MyRuntimeExceptionBuilder {
        private String code;
        private String tipMessage;
        private String message;
        private String subCode;
        private String subMessage;
        private Throwable cause;
        private Object responseDTO;

        MyRuntimeExceptionBuilder() {
        }

        public MyRuntimeExceptionBuilder code(final String code) {
            this.code = code;
            return this;
        }

        public MyRuntimeExceptionBuilder tipMessage(final String tipMessage) {
            this.tipMessage = tipMessage;
            return this;
        }

        public MyRuntimeExceptionBuilder message(final String message) {
            this.message = message;
            return this;
        }

        public MyRuntimeExceptionBuilder subCode(final String subCode) {
            this.subCode = subCode;
            return this;
        }

        public MyRuntimeExceptionBuilder subMessage(final String subMessage) {
            this.subMessage = subMessage;
            return this;
        }

        public MyRuntimeExceptionBuilder cause(final Throwable cause) {
            this.cause = cause;
            return this;
        }

        public MyRuntimeExceptionBuilder responseDTO(final Object responseDTO) {
            this.responseDTO = responseDTO;
            return this;
        }

        public MyRuntimeException build() {
            return new MyRuntimeException(this.code,
                                          this.tipMessage,
                                          this.message,
                                          this.subCode,
                                          this.subMessage,
                                          this.cause,
                                          this.responseDTO);
        }

        public String toString() {
            return "MyRuntimeException.MyRuntimeExceptionBuilder(code=" + this.code + ", tipMessage=" +
                   this.tipMessage + ", message=" + this.message + ", subCode=" + this.subCode + ", subMessage=" +
                   this.subMessage + ", cause=" + this.cause + ", responseDTO=" + this.responseDTO + ")";
        }
    }
}
