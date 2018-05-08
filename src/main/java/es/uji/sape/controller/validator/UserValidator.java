package es.uji.sape.controller.validator;

import es.uji.sape.security.User;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class UserValidator implements Validator {
    @Override
    public boolean supports(Class<?> cls) {
        return User.class.isAssignableFrom(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        User usr = (User) obj;
        if (usr.getUsername().equals("")) {
            errors.rejectValue("username", "obligatorio", "Nombre de usuario requerido");
        }
        if (usr.getPassword().equals("")) {
            errors.rejectValue("password", "obligatorio", "Contraseña requerida");
        }
    }
}
