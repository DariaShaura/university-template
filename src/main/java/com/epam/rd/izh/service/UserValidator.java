package com.epam.rd.izh.service;

import com.epam.rd.izh.dto.AuthorizedUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class UserValidator implements Validator {

    @Autowired
    DateTimeFormatter dateTimeFormatter;

    @Override
    public boolean supports(Class<?> aClass) {
        return AuthorizedUserDto.class.equals(aClass);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        AuthorizedUserDto user = (AuthorizedUserDto) obj;

        LocalDate birth = LocalDate.parse(user.getBirthDate(), dateTimeFormatter);

        if (LocalDate.now().getYear() - birth.getYear() < 15) {
            //errors.rejectValue("age", "value.tooSmall");
            errors.reject(null, "Слишком маленький возраст!");
        }
    }
}
