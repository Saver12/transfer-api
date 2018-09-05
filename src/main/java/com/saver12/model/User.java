package com.saver12.model;

import lombok.Data;

@Data
public final class User {
    private final long id;
    private final String name;
    private final String email;
}
