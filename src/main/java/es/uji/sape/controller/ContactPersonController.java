package es.uji.sape.controller;

import es.uji.sape.dao.ContactPersonDao;
import es.uji.sape.exceptions.ResourceNotFoundException;
import es.uji.sape.model.ContactPerson;
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
@RequestMapping("/contactPersons")
@SuppressWarnings("FieldHasSetterButNoGetter")
public class ContactPersonController {

    private final @NotNull ContactPersonDao dao;

    @Autowired
    public ContactPersonController(@NotNull ContactPersonDao dao) {
        this.dao = dao;
    }

    @GetMapping
    public final @NotNull String list(@NotNull Model model) {
        model.addAttribute("contactPersons", dao.findAll());
        return "/contactPersons/list";
    }

    @GetMapping("/{username}")
    public final @NotNull ContactPerson get(@PathVariable("username") String username) {
        return dao.find(username).orElseThrow(() -> new ResourceNotFoundException("ContactPerson", Map.of("username", username)));
    }

    @GetMapping("/add")
    public final @NotNull String add(@NotNull Model model) {
        model.addAttribute("contactPerson", new ContactPerson());
        return "/contactPersons/add";
    }

    @PostMapping("/add")
    public final @NotNull String processAddSubmit(@ModelAttribute("contactPerson") @NotNull ContactPerson contactPerson, @NotNull BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "/contactPersons/add";
        try {
            dao.add(contactPerson);
        } catch (Throwable e) {
            log.error(e.getMessage());
        }
        return "redirect:/contactPersons";
    }

    @GetMapping("/update/{username}")
    public final @NotNull String update(@NotNull Model model, @PathVariable("username") @NotNull String username) {
        model.addAttribute("contactPerson", dao.find(username).orElseThrow(() -> new ResourceNotFoundException("ContactPerson", Map.of("username", username))));
        return "/contactPersons/update";
    }

    @PostMapping("/update/{username}")
    public final @NotNull String processUpdateSubmit(@ModelAttribute("student") @NotNull ContactPerson contactPerson, @PathVariable("username") @NotNull String username, @NotNull BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "/contactPersons/update";
        try {
            dao.update(contactPerson);
        } catch (Throwable e) {
            log.error(e.getMessage());
        }
        return "redirect:/contactPersons";
    }

    @GetMapping("/delete/{username}")
    public final @NotNull String processDelete(@PathVariable("username") @NotNull String username) {
        dao.delete(username);
        return "redirect:/contactPersons";
    }
}
