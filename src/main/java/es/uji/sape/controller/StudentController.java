package es.uji.sape.controller;

import es.uji.sape.dao.StudentDao;
import es.uji.sape.exceptions.ResourceNotFoundException;
import es.uji.sape.model.Student;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/student")
@SuppressWarnings("FieldHasSetterButNoGetter")
public class StudentController {

    @Setter(onMethod = @__(@Autowired), onParam = @__(@NotNull))
    private StudentDao dao;

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
        if (bindingResult.hasErrors()) return "/student/update";
        if (!student.getCode().contentEquals(code)) return "/student/update";
        try {
            dao.update(student);
        } catch (Throwable e) {
            log.error(e.getMessage());
        }
        return "redirect:/student";
    }

    @GetMapping("/delete/{code}")
    public final @NotNull String processDelete(@PathVariable("code") @NotNull String code) {
        dao.delete(code);
        return "redirect:/student";
    }
}
