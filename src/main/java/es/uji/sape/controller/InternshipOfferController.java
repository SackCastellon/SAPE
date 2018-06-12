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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/offer")
public class InternshipOfferController {

    private final @NotNull InternshipOfferDao dao;

    @Autowired
    public InternshipOfferController(@NotNull InternshipOfferDao dao) {
        this.dao = dao;
    }

    @GetMapping
    public final @NotNull String list(@NotNull Model model) {
        model.addAttribute("internshipOffers", dao.findAll());
        return "/projectOffers/list";
    }

    @GetMapping("/{id:[\\d]+}")
    public final @NotNull InternshipOffer get(@PathVariable("id") int id) {
        return dao.find(id).orElseThrow(() -> new ResourceNotFoundException("InternshipOffer", Map.of("id", id)));
    }

    @GetMapping("/add")
    public final @NotNull String add(@NotNull Model model) {
        model.addAttribute("internshipOffer", new InternshipOffer());
        return "/projectOffers/add";
    }

    @PostMapping("/add")
    public final @NotNull String processAddSubmit(@ModelAttribute("internshipOffer") @NotNull InternshipOffer internshipOffer, @NotNull BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "/projectOffers/add";
        try {
            dao.add(internshipOffer);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "redirect:/projectOffers";
    }

    @GetMapping("/update/{id:[\\d]+}")
    public final @NotNull String update(@NotNull Model model, @PathVariable("id") int id) {
        model.addAttribute("internshipOffer", dao.find(id).orElseThrow(() -> new ResourceNotFoundException("InternshipOffer", Map.of("id", id))));
        return "/projectOffers/update";
    }

    @PostMapping("/update/{id:[\\d]+}")
    public final @NotNull String processUpdateSubmit(@ModelAttribute("student") @NotNull InternshipOffer internshipOffer, @PathVariable("id") @NotNull String id, @NotNull BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "/projectOffers/update";
        try {
            dao.update(internshipOffer);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "redirect:/projectOffers";
    }

    @GetMapping("/delete/{id:[\\d]+}")
    public final @NotNull String processDelete(@PathVariable("id") int id) {
        dao.delete(id);
        return "redirect:/projectOffers";
    }


}
