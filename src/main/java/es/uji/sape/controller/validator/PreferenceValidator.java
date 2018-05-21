package es.uji.sape.controller.validator;

import es.uji.sape.model.Preference;
import org.jetbrains.annotations.NotNull;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public final class PreferenceValidator implements Validator {

    private final int maxPref = 5;

    @Override
    public boolean supports(@NotNull Class<?> cls) {
        return Preference.class.isAssignableFrom(cls);
    }

    @Override
    public void validate(@NotNull Object obj, @NotNull Errors errors) {

        Preference pref = (Preference) obj;
        if (pref.getPriority() > maxPref) {
            errors.rejectValue("priority", "maximo", "Solo puedes tener 5 como maximo de valor de preferencia");
        }

    }
}
