package com.xkcoding.common.exception;

import org.apache.commons.lang3.StringUtils;

class BaseException extends RuntimeException {
    private static final long serialVersionUID = 2943470682335539492L;
    private String code;
    private String subCode;
    private String subMessage;
    private String tipMessage;
    private Object responseDTO;

    BaseException(String code, String tipMessage, String message, Throwable cause) {
        this(code, tipMessage, message, cause, (Object)null);
    }

    BaseException(String code, String tipMessage, String message, Throwable cause, Object responseDTO) {
        super(StringUtils.isBlank(message) ? tipMessage : message, cause);
        this.code = code;
        this.tipMessage = tipMessage;
        this.responseDTO = responseDTO;
    }

    BaseException(String code, String tipMessage, String message, String subCode, String subMessage, Throwable cause,
                  Object responseDTO) {
        super(StringUtils.isBlank(message) ? tipMessage : message, cause);
        this.code = code;
        this.tipMessage = tipMessage;
        this.subCode = subCode;
        this.subMessage = subMessage;
        this.responseDTO = responseDTO;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public void setSubCode(final String subCode) {
        this.subCode = subCode;
    }

    public void setSubMessage(final String subMessage) {
        this.subMessage = subMessage;
    }

    public void setTipMessage(final String tipMessage) {
        this.tipMessage = tipMessage;
    }

    public void setResponseDTO(final Object responseDTO) {
        this.responseDTO = responseDTO;
    }

    public String getCode() {
        return this.code;
    }

    public String getSubCode() {
        return this.subCode;
    }

    public String getSubMessage() {
        return this.subMessage;
    }

    public String getTipMessage() {
        return this.tipMessage;
    }

    public Object getResponseDTO() {
        return this.responseDTO;
    }
}
