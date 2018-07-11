package com.example.spark.api.middleware.uservalidators;

import com.example.spark.api.dto.request.LogInDTO;
import com.example.spark.api.errorhandler.UserException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class UserLoginValidator implements Validator {

    public boolean supports(Class clazz) {
        return LogInDTO.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors){

        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"username","username.required");
        List<FieldError> fieldErrors= errors.getFieldErrors();
        int size = fieldErrors.size();
        //System.out.println(size);
        for(int i=0; i<size; i++)
        {
            FieldError fieldError = fieldErrors.get(i);
            if(fieldError.getField().compareTo("username")==0)
            {
                throw new UserException("username can not be empty",400);
            }
            else if(fieldError.getField().compareTo("password")==0)
            {
                throw new UserException("password should be at least 8 characters long",400);
            }
        }

    }
}
