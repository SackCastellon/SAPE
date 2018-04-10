package es.uji.sape.controller;

import es.uji.sape.dao.PreferenceDao;
import es.uji.sape.exceptions.ResourceNotFoundException;
import es.uji.sape.model.Preference;
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
@RequestMapping("/preferences")
@SuppressWarnings("FieldHasSetterButNoGetter")
public class PreferenceController {

    private PreferenceDao dao;

    @Autowired
    public final void setDao(@NotNull PreferenceDao dao) {
        this.dao = dao;
    }

    @GetMapping
    public final @NotNull String list(@NotNull Model model) {
        model.addAttribute("preferences", dao.findAll());
        return "/preferences/list";
    }

    @GetMapping("/{project_offer_id:[\\d]+}/{student_code}")
    public final @NotNull Preference get(@PathVariable("student_code") String student_code, @PathVariable("project_offer_id") @NotNull int project_offer_id) {
        return dao.find(project_offer_id, student_code).orElseThrow(() -> new ResourceNotFoundException("Assignment", Map.of("project_offer_id", project_offer_id, "student_code", student_code)));
    }

    @GetMapping("/add")
    public final @NotNull String add(@NotNull Model model) {
        model.addAttribute("preference", new Preference());
        return "/preferences/add";
    }

    @PostMapping("/add")
    public final @NotNull String processAddSubmit(@ModelAttribute("preference") @NotNull Preference preference, @NotNull BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "/preferences/add";
        try {
            dao.add(preference);
        } catch (Throwable e) {
            log.error(e.getMessage());
        }
        return "redirect:/preferences";
    }

    @GetMapping("/update/{project_offer_id:[\\d]+}/{student_code}")
    public final @NotNull String update(@NotNull Model model, @PathVariable("student_code") @NotNull String student_code, @PathVariable("project_offer_id") int project_offer_id) {
        model.addAttribute("preference", dao.find(project_offer_id, student_code).orElseThrow(() -> new ResourceNotFoundException("Preference", Map.of("project_offer_id", project_offer_id, "student_code", student_code))));
        return "/preferences/update";
    }

    @PostMapping("/update/{project_offer_id:[\\d]+}/{student_code}")
    public final @NotNull String processUpdateSubmit(@ModelAttribute("preference") @NotNull Preference preference, @PathVariable("student_code") @NotNull String student_code, @PathVariable("project_offer_id") @NotNull int project_offer_id, @NotNull BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "/preferences/update";
        try {
            dao.update(preference);
        } catch (Throwable e) {
            log.error(e.getMessage());
        }
        return "redirect:/preferences";
    }

    @GetMapping("/delete/{project_offer_id:[\\d]+}/{student_code}")
    public final @NotNull String processDelete(@PathVariable("student_code") @NotNull String student_code, @PathVariable("project_offer_id") int project_offer_id) {
        dao.delete(project_offer_id, student_code);
        return "redirect:/preferences";
    }
}
