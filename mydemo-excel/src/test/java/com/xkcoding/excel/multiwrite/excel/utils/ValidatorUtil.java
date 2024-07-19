package com.xkcoding.excel.multiwrite.excel.utils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 参数校验工具类
 **/
public class ValidatorUtil {

    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    public static void validate(Object obj) {
        Set<ConstraintViolation<Object>> validateSet = VALIDATOR.validate(obj);
        if (!validateSet.isEmpty()) {
            String errorMsg = validateSet.stream()
                                         .map(ConstraintViolation::getMessage)
                                         .collect(Collectors.joining("&"));
            throw new RuntimeException(errorMsg);
        }
    }

    /**
     * 校验返回异常信息
     *
     * @param obj 实体
     * @return java.lang.String
     * @author zhangbing
     **/
    public static Set<ConstraintViolation<Object>> validateAndReturnResults(Object obj) {
        Set<ConstraintViolation<Object>> validateSet = VALIDATOR.validate(obj);
        return validateSet;
    }
}
