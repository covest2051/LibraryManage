package ru.marat.springcourse.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.marat.springcourse.dao.PersonDAO;
import ru.marat.springcourse.models.Person;

@Component
public class PersonValidator implements Validator {
    private final PersonDAO personDAO;

    @Autowired
    public PersonValidator(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    // Нам нужно обратиться к базе данных, чтобы проверить, занят ли email, введённый пользователем
    @Override
    public boolean supports(Class<?> clazz) { // Тут даём понять к какому классу относится валидатор
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
    }
}
