package es.uji.sape.controller.validator;

import es.uji.sape.model.Student;
import org.jetbrains.annotations.NotNull;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class StudentValidator implements Validator {

    @Override
    public boolean supports(@NotNull Class<?> cls) {
        return Student.class.isAssignableFrom(cls);
    }

    @Override
    public void validate(@NotNull Object obj, @NotNull Errors errors) {
        Student std = (Student) obj;
        if (std.getCode().isEmpty()) {
            errors.rejectValue("code", "obligatorio", "Codigo de estudiante requerido");
        }
        if (std.getName().isEmpty()) {
            errors.rejectValue("name", "obligatorio", "Nombre de estudiante requerido");
        }
        if (std.getSurname().isEmpty()) {
            errors.rejectValue("name", "obligatorio", "Apellido de estudiante requerido");
        }

        if (std.getCode().length() > Student.codeMaxLength) {
            errors.rejectValue("code", "grande", "Codigo mas grande de lo permitido");
        }
        if (std.getName().length() > Student.nameSurMaxLength) {
            errors.rejectValue("name", "grande", "Nombre mas grande de lo permitido");
        }
        if (std.getSurname().length() > Student.nameSurMaxLength) {
            errors.rejectValue("surname", "grande", "Apellido mas grande de lo permitido");
        }

    }
}
