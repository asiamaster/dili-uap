package com.dili.uap.validator;

import org.apache.poi.ss.formula.functions.T;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * <B>Description</B>
 * <B>Copyright</B>
 * 本软件源代码版权归农丰时代所有,未经许可不得任意复制与传播.<br />
 * <B>农丰时代科技有限公司</B>
 *
 * @author yuehongbo
 * @createTime 2018/6/15 10:38
 */
public class BeanValidator {

    private static Validator validator;

    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public static String validator(Object bean, Class<?>... groups) {
        StringBuffer buf = new StringBuffer();
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(bean,groups);
        for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
            buf.append(constraintViolation.getMessage()).append("; ");
        }
        return buf.toString();
    }
}
