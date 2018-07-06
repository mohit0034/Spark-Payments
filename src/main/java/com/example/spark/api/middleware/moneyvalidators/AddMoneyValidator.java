package com.example.spark.api.middleware.moneyvalidators;

import com.example.spark.api.dto.request.AddMoneyDTO;
import com.example.spark.api.errorhandler.UserException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class AddMoneyValidator implements Validator {
    public boolean supports(Class clazz) {
        return AddMoneyDTO.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "amount", "amount.required");
        if (errors.hasErrors()) {
            throw new UserException("Please enter a valid amount", 400);
        }
    }
}
