package com.fancyfrog.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Ujjwal Gupta on Dec,2019
 */

@Setter
@Getter
@NoArgsConstructor
public class ApiResponse {

    private boolean success;
    private String message;

    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
