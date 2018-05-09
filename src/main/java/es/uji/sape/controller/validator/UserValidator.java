package es.uji.sape.controller.validator;

import es.uji.sape.security.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class UserValidator implements Validator {

    @Override
    public boolean supports(@NotNull Class<?> cls) {
        return User.class.isAssignableFrom(cls);
    }

    @Override
    public void validate(@NotNull Object obj, @NotNull Errors errors) {
        User usr = (User) obj;
        if (usr.getUsername().isEmpty()) {
            errors.rejectValue("username", "obligatorio", "Nombre de usuario requerido");
        }
        if (usr.getPassword().isEmpty()) {
            errors.rejectValue("password", "obligatorio", "Contraseña requerida");
        }
    }
}
