package es.uji.sape.controller.validator;

import es.uji.sape.model.ModifyRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ModifyRequestValidator implements Validator {

    private static final int minLength = 200;

    @Override
    public boolean supports(@NotNull Class<?> cls) {
        return ModifyRequest.class.isAssignableFrom(cls);
    }

    @Override
    public void validate(@NotNull Object obj, @NotNull Errors errors) {

        ModifyRequest req = (ModifyRequest) obj;
        if (req.getMessage().length()<minLength) {
            errors.rejectValue("modifyRequest", "longitud", "EL mensaje tiene que ser de al menos");
        }

    }
}
