package es.uji.sape.controller.validator;

import es.uji.sape.model.Student;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class StudentValidator implements Validator {

    @Override
    public boolean supports(Class<?> cls) {
        return Student.class.isAssignableFrom(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        Student std = (Student) obj;
        if (std.getCode().equals("")) {
            errors.rejectValue("code", "obligatorio", "Codigo de estudiante requerido");
        }
        if (std.getName().equals("")) {
            errors.rejectValue("name", "obligatorio", "Nombre de estudiante requerido");
        }
        if (std.getSurname().equals("")) {
            errors.rejectValue("name", "obligatorio", "Apellido de estudiante requerido");
        }

        if (std.getCode().length() > std.getCodeMaxLength()) {
            errors.rejectValue("code", "grande", "Codigo mas grande de lo permitido");
        }
        if (std.getName().length() > std.getNameSurMaxLength()) {
            errors.rejectValue("name", "grande", "Nombre mas grande de lo permitido");
        }
        if (std.getSurname().length() > std.getNameSurMaxLength()) {
            errors.rejectValue("surname", "grande", "Apellido mas grande de lo permitido");
        }

    }
}
