package es.uji.sape.controller;

import es.uji.sape.dao.StudentDao;
import es.uji.sape.exceptions.ResourceNotFoundException;
import es.uji.sape.model.Student;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/students")
@SuppressWarnings("FieldHasSetterButNoGetter")
public class StudentController {

    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_COUNT = 5;

    private StudentDao dao;

    @Autowired
    public final void setDao(@NotNull StudentDao dao) {
        this.dao = dao;
    }

    @GetMapping
    public final @NotNull String list(@NotNull Model model, @RequestParam(value = "page", required = false) @NotNull Optional<String> page, @RequestParam(value = "count", required = false) @NotNull Optional<String> count) {
        int pageInt = page.filter(NumberUtils::isParsable).map(Integer::parseInt).orElse(DEFAULT_PAGE);
        int countInt = count.filter(NumberUtils::isParsable).map(Integer::parseInt).orElse(DEFAULT_COUNT);

        model.addAttribute("students", dao.findAll(pageInt, countInt));
        model.addAttribute("page", pageInt);
        model.addAttribute("count", countInt);
        model.addAttribute("totalPages", (int) Math.ceil(dao.getCount() / (float) countInt));

        return "/students/list";
    }

    @GetMapping("/add")
    public final @NotNull String add(@NotNull Model model) {
        model.addAttribute("student", new Student());
        return "/students/add";
    }

    @PostMapping("/add")
    public final @NotNull String processAddSubmit(@ModelAttribute("student") @NotNull Student student, @NotNull BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "/students/add";
        try {
            dao.add(student);
        } catch (Throwable e) {
            log.error(e.getMessage());
        }
        return "redirect:/students";
    }

    @GetMapping("/update/{code}")
    public final @NotNull String update(@NotNull Model model, @PathVariable("code") @NotNull String code) {
        model.addAttribute("student", dao.find(code).orElseThrow(() -> new ResourceNotFoundException("Student", Map.of("code", code))));
        return "/students/update";
    }

    @PostMapping("/update/{code}")
    public final @NotNull String processUpdateSubmit(@ModelAttribute("student") @NotNull Student student, @PathVariable("code") @NotNull String code, @NotNull BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "/students/update";
        try {
            dao.update(student);
        } catch (Throwable e) {
            log.error(e.getMessage());
        }
        return "redirect:/students";
    }

    @GetMapping("/delete/{code}")
    public final @NotNull String processDelete(@PathVariable("code") @NotNull String code) {
        dao.delete(code);
        return "redirect:/students";
    }
}
