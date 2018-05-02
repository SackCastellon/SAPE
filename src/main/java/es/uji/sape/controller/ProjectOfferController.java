package es.uji.sape.controller;

import es.uji.sape.dao.ProjectOfferDao;
import es.uji.sape.exceptions.ResourceNotFoundException;
import es.uji.sape.model.ProjectOffer;
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
@RequestMapping("/projectOffers")
@SuppressWarnings("FieldHasSetterButNoGetter")
public class ProjectOfferController {

    private final @NotNull ProjectOfferDao dao;

    @Autowired
    public ProjectOfferController(@NotNull ProjectOfferDao dao) {
        this.dao = dao;
    }

    @GetMapping("/list")
    public final @NotNull String list(@NotNull Model model) {
        model.addAttribute("offers", dao.findAll());
        return "/projectOffers/list";
    }

    @GetMapping("/{id:[\\d]+}")
    public final @NotNull ProjectOffer get(@PathVariable("id") int id) {
        return dao.find(id).orElseThrow(() -> new ResourceNotFoundException("ProjectOffer", Map.of("id", id)));
    }

    @GetMapping("/add")
    public final @NotNull String add(@NotNull Model model) {
        model.addAttribute("projectOffer", new ProjectOffer());
        return "/projectOffers/add";
    }

    @PostMapping("/add")
    public final @NotNull String processAddSubmit(@ModelAttribute("projectOffer") @NotNull ProjectOffer projectOffer, @NotNull BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "/projectOffers/add";
        try {
            dao.add(projectOffer);
        } catch (Throwable e) {
            log.error(e.getMessage());
        }
        return "redirect:/projectOffers/list";
    }

    @GetMapping("/update/{id:[\\d]+}")
    public final @NotNull String update(@NotNull Model model, @PathVariable("id") int id) {
        model.addAttribute("projectOffer", dao.find(id));
        return "/projectOffers/update";
    }

    @PostMapping("/update}")
    public final @NotNull String processUpdateSubmit(@ModelAttribute("projectOffer") @NotNull ProjectOffer projectOffer, @NotNull BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "/projectOffers/update";
        try {
            dao.update(projectOffer);
        } catch (Throwable e) {
            log.error(e.getMessage());
        }
        return "redirect:/projectOffers/list";
    }

    @DeleteMapping("/delete/{id:[\\d]+}")
    public final @NotNull String processDelete(@PathVariable("id") int id) {
        dao.delete(id);
        return "redirect:/projectOffers/list";
    }


}
