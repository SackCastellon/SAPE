package es.uji.sape.controller;

import es.uji.sape.dao.ProjectOfferDao;
import es.uji.sape.exceptions.ResourceNotFoundException;
import es.uji.sape.model.ProjectOffer;
import es.uji.sape.security.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@Slf4j
@Controller
public class ProjectOfferController {

    private final @NotNull ProjectOfferDao dao;

    @Autowired
    public ProjectOfferController(@NotNull ProjectOfferDao dao) {
        this.dao = dao;
    }

    @GetMapping("/projectOffers/list")
    public final @NotNull String list(@NotNull Model model) {
        model.addAttribute("projectOffers", dao.findAll());
        return "/projectOffers/list";
    }

    @GetMapping("/projectOffers/businessList")
    public final @NotNull String businessList(@NotNull Model model, Authentication auth) {
        model.addAttribute("projectOffers", dao.findPerBussiness(((UserInfo) auth.getPrincipal()).getUsername()));
        return "/projectOffers/businessList";
    }

    @GetMapping("/projectOffers/details/{id:[\\d]+}")
    public final @NotNull ProjectOffer get(@PathVariable("id") int id) {
        return dao.find(id).orElseThrow(() -> new ResourceNotFoundException("ProjectOffer", Map.of("id", id)));
    }

    @GetMapping("/projectOffers/details/{id:[\\d]+}")
    public final @NotNull String getDetails(@PathVariable("id") int id, @NotNull Model model) {
        model.addAttribute("offerDetails",dao.find(id).orElseThrow(() -> new ResourceNotFoundException("ProjectOffer", Map.of("id", id))));
        return "/projectOffer/details/";
    }

    @GetMapping("/projectOffers/add")
    public final @NotNull String add(@NotNull Model model) {
        model.addAttribute("projectOffer", new ProjectOffer());
        return "/projectOffers/add";
    }

    @PostMapping("/projectOffers/add")
    public final @NotNull String processAddSubmit(@ModelAttribute("projectOffer") @NotNull ProjectOffer projectOffer, @NotNull BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "/projectOffers/add";
        try {
            dao.add(projectOffer);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "redirect:/projectOffers/list";
    }

    @GetMapping("/projectOffers/update/{id:[\\d]+}")
    public final @NotNull String update(@NotNull Model model, @PathVariable("id") int id) {
        model.addAttribute("projectOffer", dao.find(id));
        return "/projectOffers/update";
    }

    @PostMapping("/projectOffers/update}")
    public final @NotNull String processUpdateSubmit(@ModelAttribute("projectOffer") @NotNull ProjectOffer projectOffer, @NotNull BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "/projectOffers/update";
        try {
            dao.update(projectOffer);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "redirect:/projectOffers/list";
    }

    @DeleteMapping("/projectOffers/delete/{id:[\\d]+}")
    public final @NotNull String processDelete(@PathVariable("id") int id) {
        dao.delete(id);
        return "redirect:/projectOffers/list";
    }

    @GetMapping("/preference/add")
    public final @NotNull String preferenceOffer(@NotNull Model model) {
        model.addAttribute("projectOffersNotPreferences", dao.findForPreferences());
        return "/preference/add";
    }

}
