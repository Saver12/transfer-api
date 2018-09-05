package com.saver12.model;

import lombok.Data;

@Data
public final class AppResponse {
    private final int code;
    private final String type;
    private final String message;
}
