package com.fancyfrog.security.common.exception.response;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by Ujjwal Gupta on Jan,2020
 */

public enum ErrorCode {

    GLOBAL(2),AUTHENTICATION(10),JWT_TOKEN_EXPIRED(11);

    private int errorCode;

    ErrorCode(int errorCode){
        this.errorCode = errorCode;
    }

    @JsonValue
    public int getErrorCode(){
        return errorCode;
    }
}
