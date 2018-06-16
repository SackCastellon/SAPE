package es.uji.sape.controller;

import es.uji.sape.controller.validator.StudentValidator;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/student")
public class StudentController {

    private final @NotNull StudentDao dao;

    @Autowired
    public StudentController(@NotNull StudentDao dao) {
        this.dao = dao;
    }

    @GetMapping
    public final @NotNull String list(@NotNull Model model) {
        List<Student> studentList = dao.findAll().stream()
                .sorted(Comparator.comparing(Student::getAverageScore).reversed()
                        .thenComparing(Student::getCode))
                .collect(Collectors.toList());
        model.addAttribute("students", studentList);
        model.addAttribute("canAssign", dao.findAllWithFivePrefsOrMore());
        return "/student/list";
    }

    @GetMapping("/add")
    public final @NotNull String add(@NotNull Model model) {
        model.addAttribute("student", new Student());
        return "/student/add";
    }

    @PostMapping("/add")
    public final @NotNull String processAddSubmit(@ModelAttribute("student") @NotNull Student student, @NotNull BindingResult bindingResult) {
        StudentValidator validator = new StudentValidator();
        validator.validate(student, bindingResult);
        if (bindingResult.hasErrors()) return "/student/add";
        try {
            dao.add(student);
        } catch (Exception e) {
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
        if (optional.isPresent() && (optional.get() != student)) {
            try {
                dao.update(student);
            } catch (Exception e) {
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

}
