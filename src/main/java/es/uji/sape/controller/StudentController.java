package es.uji.sape.controller;

import es.uji.sape.dao.StudentDao;
import es.uji.sape.exceptions.ResourceNotFoundException;
import es.uji.sape.model.Student;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/student")
@SuppressWarnings("FieldHasSetterButNoGetter")
public class StudentController {

    private final @NotNull StudentDao dao;

    @Autowired
    public StudentController(@NotNull StudentDao dao) {
        this.dao = dao;
    }

    @GetMapping
    public final @NotNull String list(@NotNull Model model) {
        model.addAttribute("students", dao.findAll());
        return "/student/list";
    }

    @GetMapping("/add")
    public final @NotNull String add(@NotNull Model model) {
        model.addAttribute("student", new Student());
        return "/student/add";
    }

    @PostMapping("/add")
    public final @NotNull String processAddSubmit(@ModelAttribute("student") @NotNull Student student, @NotNull BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "/student/add";
        try {
            dao.add(student);
        } catch (Throwable e) {
            log.error(e.getMessage());
        }
        return "redirect:/student";
    }

    @GetMapping("/update/{code}")
    public final @NotNull String update(@NotNull Model model, @PathVariable("code") @NotNull String code) {
        model.addAttribute("student", dao.find(code).orElseThrow(() -> new ResourceNotFoundException("Student", Map.of("code", code))));
        return "/student/update";
    }

    @PostMapping("/update/{code}")
    public final @NotNull String processUpdateSubmit(@ModelAttribute("student") @NotNull Student student, @PathVariable("code") @NotNull String code, @NotNull BindingResult bindingResult) {
        if (bindingResult.hasErrors() || !student.getCode().contentEquals(code)) return "/student/update";

        val optional = dao.find(code);
        if (optional.isPresent() && optional.get() != student) {
            try {
                dao.update(student);
            } catch (Throwable e) {
                log.error(e.getMessage());
            }
        }
        return "redirect:/student";
    }

    @GetMapping("/delete/{code}")
    public final @NotNull String processDelete(@PathVariable("code") @NotNull String code) {
        dao.delete(code);
        return "redirect:/student";
    }

    static class StudentValidator implements Validator {

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

            if(std.getCode().length()>std.getCodeMaxLength()){
                errors.rejectValue("code","grande","Codigo mas grande de lo permitido");
            }
            if(std.getName().length()>std.getNameSurMaxLength()) {
                errors.rejectValue("name", "grande", "Nombre mas grande de lo permitido");
            }
            if(std.getSurname().length()>std.getNameSurMaxLength()) {
                errors.rejectValue("surname", "grande", "Apellido mas grande de lo permitido");
            }

        }
    }
}
