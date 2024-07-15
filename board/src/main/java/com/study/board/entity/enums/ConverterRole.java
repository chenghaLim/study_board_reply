package com.study.board.entity.enums;

import com.study.board.entity.Role;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ConverterRole extends AbstractCodedEnumConverter<Role,String> {
    public ConverterRole() {
        super(Role.class);
    }
}
