package com.example.spark.api.middleware.uservalidators;

import com.example.spark.api.dto.request.UserRequestDTO;
import com.example.spark.api.errorhandler.UserException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class CreateUserValidator implements Validator {

    public boolean supports(Class clazz) {
        return UserRequestDTO.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"first_name","first_name.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"last_name","first_name.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors,"username","first_name.required");

        List<FieldError> fieldErrors= errors.getFieldErrors();
        int size = fieldErrors.size();
        //System.out.println(size);
        for(int i=0; i<size; i++)
        {
            FieldError fieldError = fieldErrors.get(i);
            if(fieldError.getField().compareTo("first_name")==0)
            {
                throw new UserException("first name can not be empty",400);
            }
            else if(fieldError.getField().compareTo("last_name")==0)
            {
                throw new UserException("last name can not be empty",400);
            }
            else if(fieldError.getField().compareTo("username")==0)
            {
                throw new UserException("username name can not be empty",400);
            }
            else if(fieldError.getField().compareTo("phone")==0)
            {
                throw new UserException("please enter a valid phone no.",400);
            }
            else if(fieldError.getField().compareTo("password")==0)
            {
                throw new UserException("password should be at least 8 characters long",400);
            }
        }
    }
}
