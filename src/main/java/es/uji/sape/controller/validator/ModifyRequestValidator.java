package es.uji.sape.controller.validator;

import es.uji.sape.model.ModifyRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ModifyRequestValidator implements Validator {

    private static final int MIN_LENGTH = 200;

    @Override
    public boolean supports(@NotNull Class<?> cls) {
        return ModifyRequest.class.isAssignableFrom(cls);
    }

    @Override
    public void validate(@NotNull Object obj, @NotNull Errors errors) {

        ModifyRequest req = (ModifyRequest) obj;
        if (req.getMessage().length() < MIN_LENGTH) {
            errors.rejectValue("modifyRequest", "longitud", "El mensaje tiene que ser de al menos " + MIN_LENGTH + " caracteres.");
        }

    }
}
