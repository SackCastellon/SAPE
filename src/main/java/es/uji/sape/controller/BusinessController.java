package es.uji.sape.controller;

import es.uji.sape.dao.BusinessDao;
import es.uji.sape.exceptions.ResourceNotFoundException;
import es.uji.sape.model.Business;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/business")
public class BusinessController {

    private final @NotNull BusinessDao dao;

    @Autowired
    public BusinessController(@NotNull BusinessDao dao) {
        this.dao = dao;
    }

    @GetMapping
    public final @NotNull String list(@NotNull Model model) {
        model.addAttribute("businesses", dao.findAll());
        return "/business/list";
    }

    @GetMapping("/{cif}")
    public final @NotNull Business get(@PathVariable("cif") @NotNull String cif) {
        return dao.find(cif).orElseThrow(() -> new ResourceNotFoundException("Business", Map.of("cif", cif)));
    }

    @GetMapping("/add")
    public final @NotNull String add(Model model) {
        model.addAttribute("business", new Business());
        return "/business/add";
    }

    @PostMapping("/add")
    public final @NotNull String processAddSubmit(@ModelAttribute("business") @NotNull Business business, @NotNull BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "/business/add";
        try {
            dao.add(business);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "redirect:/business";
    }

    @GetMapping("/update/{cif}")
    public final @NotNull String update(@NotNull Model model, @PathVariable("cif") @NotNull String cif) {
        Optional<Business> business = dao.find(cif);
        if (business.isPresent()) {
            model.addAttribute("business", business.get());
            return "/business/update";
        } else {
            return "redirect:/business";
        }
    }

    @PostMapping("/update")
    public final @NotNull String processUpdateSubmit(@ModelAttribute("business") @NotNull Business business, @NotNull BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "/business/update";
        try {
            dao.add(business);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "redirect:/business";
    }

    @DeleteMapping("/delete/{cif}")
    public final @NotNull String processDelete(@PathVariable("cif") @NotNull String cif) {
        dao.delete(cif);
        return "redirect:/business";
    }
}
