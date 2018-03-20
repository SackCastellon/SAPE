package es.uji.sape.controller;

import es.uji.sape.dao.ProjectOfferDao;
import es.uji.sape.exceptions.ResourceNotFoundException;
import es.uji.sape.model.ProjectOffer;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/projectOffers")
@SuppressWarnings({"DesignForExtension", "FieldHasSetterButNoGetter"})
public class ProjectOfferController {

    private ProjectOfferDao dao;

    @Autowired
    public void setDao(@NotNull ProjectOfferDao dao) {
        this.dao = dao;
    }

    @GetMapping
    public @NotNull List<ProjectOffer> getAllProjectOffers() {
        return dao.findAll();
    }

    @GetMapping("/{id}")
    public @NotNull ProjectOffer getProjectOfferById(@PathVariable("id") @NotNull int id) {
        return dao.find(id).orElseThrow(() -> new ResourceNotFoundException("ProjectOffer", "id", id));
    }

    @RequestMapping(value="/add")
    public @NotNull String addProjectOffer(Model model){
        model.addAttribute("projectOffer", new ProjectOffer());
        return "projectOffers/add";
    }

    @PostMapping(value="/add")
    public String processAddSubmit(@ModelAttribute("projectOffer") ProjectOffer projectOffer, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return "projectOffer/add";
        dao.add(projectOffer);
        return "redirect:list.html";
    }

    @RequestMapping(value="/update/{id}")
    public @NotNull String editPostOffer(Model model, @PathVariable int id) {
        model.addAttribute("projectOffer", dao.find(id));
        return "projectOffer/update";
    }

    @PostMapping(value="/update/{id}")
    public String processUpdateSubmit(@PathVariable int id, @ModelAttribute("projectOffer") ProjectOffer projectOffer, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return "projectOffer/update";
        dao.update(projectOffer);
        return "redirect:../list";
    }

    @DeleteMapping(value="/delete/{id}")
    public String processDelete(@PathVariable int id) {
        dao.delete(id);
        return "redirect:../list";
    }




}