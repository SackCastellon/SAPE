package es.uji.sape.controller;

import es.uji.sape.dao.AssignmentDao;
import es.uji.sape.exceptions.ResourceNotFoundException;
import es.uji.sape.model.Assignment;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/assignments")
@SuppressWarnings({"DesignForExtension", "FieldHasSetterButNoGetter"})
public class AssignmentController {

    private AssignmentDao dao;

    @Autowired
    public void setDao(@NotNull AssignmentDao dao) {
        this.dao = dao;
    }

    @GetMapping
    public @NotNull List<Assignment> getAllAssignments() {
        return dao.findAll();
    }

    @GetMapping("/{cif}")
    public @NotNull Assignment getProjectOfferById(@PathVariable("cif") @NotNull String cif) {
        return dao.find(cif).orElseThrow(() -> new ResourceNotFoundException("Assignment", "cif", cif));
    }

    @RequestMapping(value="/add")
    public @NotNull String addProjectOffer(Model model){
        model.addAttribute("assignment", new Assignment());
        return "assignment/add";
    }

    @PostMapping(value="/add")
    public String processAddSubmit(@ModelAttribute("assignment") Assignment assignment, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return "assignment/add";
        dao.add(assignment);
        return "redirect:list.html";
    }

    @RequestMapping(value="/update/{cif}")
    public @NotNull String editPostOffer(Model model, @PathVariable String cif) {
        model.addAttribute("assignment", dao.find(cif));
        return "assignment/update";
    }

    @PostMapping(value="/update/{cif}")
    public String processUpdateSubmit(@PathVariable String cif, @ModelAttribute("assignment") Assignment assignment, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return "assignment/update";
        dao.update(assignment);
        return "redirect:../list";
    }

    @DeleteMapping(value="/delete/{cif}")
    public String processDelete(@PathVariable String cif) {
        dao.delete(cif);
        return "redirect:../list";
    }
}
