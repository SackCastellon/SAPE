package es.uji.sape.controller;


import es.uji.sape.dao.InternshipOfferDao;
import es.uji.sape.exceptions.ResourceNotFoundException;
import es.uji.sape.model.InternshipOffer;
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
@RequestMapping("/internshipOffers")
public class InternshipOfferController {

    private InternshipOfferDao dao;

    @Autowired
    public final void setDao(@NotNull InternshipOfferDao dao) {
        this.dao = dao;
    }

    @GetMapping("/list")
    public final @NotNull String list(@NotNull Model model) {
        model.addAttribute("internshipOffers", dao.findAll());
        return "/internshipOffers/list";
    }

    @GetMapping("/{id:[\\d]+}")
    public final @NotNull InternshipOffer get(@PathVariable("id") int id) {
        return dao.find(id).orElseThrow(() -> new ResourceNotFoundException("ProjectOffer", Map.of("id", id)));
    }

    @GetMapping("/add")
    public final @NotNull String add(@NotNull Model model) {
        model.addAttribute("internshipOffer", new InternshipOffer());
        return "/internshipOffers/add";
    }

    @PostMapping("/add")
    public final @NotNull String processAddSubmit(@ModelAttribute("internshipOffer") @NotNull InternshipOffer internshipOffer, @NotNull BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "/internshipOffers/add";
        try {
            dao.add(internshipOffer);
        } catch (Throwable e) {
            log.error(e.getMessage());
        }
        return "redirect:/internshipOffers";
    }

    @GetMapping("/update/{id:[\\d]+}")
    public final @NotNull String update(@NotNull Model model, @PathVariable("id") @NotNull int id) {
        model.addAttribute("internshipOffer", dao.find(id).orElseThrow(() -> new ResourceNotFoundException("InternshipOffer", Map.of("id", id))));
        return "/internshipOffers/update";
    }

    @PostMapping("/update/{id:[\\d]+}")
    public final @NotNull String processUpdateSubmit(@ModelAttribute("student") @NotNull InternshipOffer internshipOffer, @PathVariable("id") @NotNull String id, @NotNull BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "/internshipOffers/update";
        try {
            dao.update(internshipOffer);
        } catch (Throwable e) {
            log.error(e.getMessage());
        }
        return "redirect:/internshipOffers";
    }

    @GetMapping("/delete/{id:[\\d]+}")
    public final @NotNull String processDelete(@PathVariable("id") @NotNull int id) {
        dao.delete(id);
        return "redirect:/internshipOffers";
    }
}
