package com.example.stockmsanewsfeed.common;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class EnumListValidator implements ConstraintValidator<EnumList, List<java.lang.Enum>> {
    private EnumList annotation;

    @Override
    public void initialize(EnumList constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(List<java.lang.Enum> values, ConstraintValidatorContext context) {
        boolean result = false;
        Object[] enumValues = this.annotation.enumClass().getEnumConstants();
        if (enumValues != null && values != null) {
            for (java.lang.Enum value : values) {
                for (Object enumValue : enumValues) {
                    if (value == enumValue) {
                        result = true;
                        break;
                    }
                }
                if (!result) {
                    break;
                }
            }
        }
        return result;
    }
}
