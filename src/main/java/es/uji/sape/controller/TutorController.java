package es.uji.sape.controller;

import es.uji.sape.dao.TutorDao;
import es.uji.sape.exceptions.ResourceNotFoundException;
import es.uji.sape.model.Tutor;
import lombok.extern.slf4j.Slf4j;
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

import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/tutors")
@SuppressWarnings("FieldHasSetterButNoGetter")
public class TutorController {

    private final @NotNull TutorDao dao;

    @Autowired
    public TutorController(@NotNull TutorDao dao) {
        this.dao = dao;
    }

    @GetMapping
    public final @NotNull String list(@NotNull Model model) {
        model.addAttribute("tutors", dao.findAll());
        return "/tutors/list";
    }

    @GetMapping("/{code}")
    public final @NotNull Tutor get(@PathVariable("code") String code) {
        return dao.find(code).orElseThrow(() -> new ResourceNotFoundException("Tutor", Map.of("code", code)));
    }

    @GetMapping("/add")
    public final @NotNull String add(@NotNull Model model) {
        model.addAttribute("tutor", new Tutor());
        return "/tutors/add";
    }

    @PostMapping("/add")
    public final @NotNull String processAddSubmit(@ModelAttribute("tutor") @NotNull Tutor tutor, @NotNull BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "/tutors/add";
        try {
            dao.add(tutor);
        } catch (Throwable e) {
            log.error(e.getMessage());
        }
        return "redirect:/tutors";
    }

    @GetMapping("/update/{code}")
    public final @NotNull String update(@NotNull Model model, @PathVariable("code") @NotNull String code) {
        model.addAttribute("tutor", dao.find(code).orElseThrow(() -> new ResourceNotFoundException("Tutor", Map.of("code", code))));
        return "/tutors/update";
    }

    @PostMapping("/update/{code}")
    public final @NotNull String processUpdateSubmit(@ModelAttribute("student") @NotNull Tutor tutor, @PathVariable("code") @NotNull String code, @NotNull BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "/tutors/update";
        try {
            dao.update(tutor);
        } catch (Throwable e) {
            log.error(e.getMessage());
        }
        return "redirect:/tutors";
    }

    @GetMapping("/delete/{code}")
    public final @NotNull String processDelete(@PathVariable("code") @NotNull String code) {
        dao.delete(code);
        return "redirect:/tutors";
    }
}
