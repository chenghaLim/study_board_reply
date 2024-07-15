package com.study.board.entity;

import com.study.board.entity.enums.AbstractCodedEnumConverter;
import com.study.board.entity.enums.CodeEnum;

public enum Role implements CodeEnum<String> {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
