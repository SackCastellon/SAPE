package es.uji.sape.controller;

import es.uji.sape.dao.PreferenceDao;
import es.uji.sape.exceptions.ResourceNotFoundException;
import es.uji.sape.model.Preference;
import es.uji.sape.security.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
@RequestMapping("/preferences")
@SuppressWarnings("FieldHasSetterButNoGetter")
public class PreferenceController {

    private final @NotNull PreferenceDao dao;

    @Autowired
    public PreferenceController(@NotNull PreferenceDao dao) {
        this.dao = dao;
    }

    @GetMapping
    public final @NotNull String list(@NotNull Model model) {
        model.addAttribute("preferences", dao.findAll());
        return "/preferences/list";
    }

    @GetMapping("/{projectOfferId:[\\d]+}/{studentCode}")
    public final @NotNull Preference get(@PathVariable("studentCode") String studentCode, @PathVariable("projectOfferId") int projectOfferId) {
        return dao.find(projectOfferId, studentCode).orElseThrow(() -> new ResourceNotFoundException("Preference", Map.of("projectOfferId", projectOfferId, "studentCode", studentCode)));
    }

    @GetMapping("/personalList")
    public final @NotNull String getStudentPreferences(@NotNull Model model, Authentication auth) {
        model.addAttribute("preferences", dao.findStudentPreferences(((UserInfo) auth.getPrincipal()).getUsername()));
        return "/preferences/personalList";
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

    @GetMapping("/update/{projectOfferId:[\\d]+}/{studentCode}")
    public final @NotNull String update(@NotNull Model model, @PathVariable("studentCode") @NotNull String studentCode, @PathVariable("projectOfferId") int projectOfferId) {
        model.addAttribute("preference", dao.find(projectOfferId, studentCode).orElseThrow(() -> new ResourceNotFoundException("Preference", Map.of("projectOfferId", projectOfferId, "studentCode", studentCode))));
        return "/preferences/update";
    }

    @PostMapping("/update/{projectOfferId:[\\d]+}/{studentCode}")
    public final @NotNull String processUpdateSubmit(@ModelAttribute("preference") @NotNull Preference preference, @PathVariable("studentCode") @NotNull String studentCode, @PathVariable("projectOfferId") int projectOfferId, @NotNull BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "/preferences/update";
        try {
            dao.update(preference);
        } catch (Throwable e) {
            log.error(e.getMessage());
        }
        return "redirect:/preferences";
    }

    @GetMapping("/delete/{projectOfferId:[\\d]+}/{studentCode}")
    public final @NotNull String processDelete(@PathVariable("studentCode") @NotNull String studentCode, @PathVariable("projectOfferId") int projectOfferId) {
        dao.delete(projectOfferId, studentCode);
        return "redirect:/preferences";
    }
}
