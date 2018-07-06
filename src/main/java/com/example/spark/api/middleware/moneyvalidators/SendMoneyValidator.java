package com.example.spark.api.middleware.moneyvalidators;

import com.example.spark.api.dto.request.SendMoneyDTO;
import com.example.spark.api.errorhandler.UserException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class SendMoneyValidator implements Validator {

    public boolean supports(Class clazz) {
        return SendMoneyDTO.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone", "phone.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "amount", "amount.required");

        List<FieldError> fieldErrors = errors.getFieldErrors();
        int size = fieldErrors.size();
        //System.out.println(size);
        for (int i = 0; i < size; i++) {
            FieldError fieldError = fieldErrors.get(i);
            if (fieldError.getField().compareTo("phone") == 0) {
                throw new UserException("please enter a valid phone no", 400);
            } else if (fieldError.getField().compareTo("amount") == 0) {
                throw new UserException("please enter a valid amount", 400);
            }
        }
    }
}
