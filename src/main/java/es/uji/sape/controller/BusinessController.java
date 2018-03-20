package es.uji.sape.controller;

import es.uji.sape.dao.BusinessDao;
import es.uji.sape.exceptions.ResourceNotFoundException;
import es.uji.sape.model.Business;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/businesses")
@SuppressWarnings({"DesignForExtension", "FieldHasSetterButNoGetter"})
public class BusinessController {

    private BusinessDao dao;

    @Autowired
    public void setDao(@NotNull BusinessDao dao) {
        this.dao = dao;
    }

    @GetMapping
    public @NotNull List<Business> getAllBusinesses() {
        return dao.findAll();
    }

    @GetMapping("/{cif}")
    public @NotNull Business getProjectOfferById(@PathVariable("cif") @NotNull String cif) {
        return dao.find(cif).orElseThrow(() -> new ResourceNotFoundException("Business", "cif", cif));
    }

    @RequestMapping(value="/add")
    public @NotNull String addProjectOffer(Model model){
        model.addAttribute("business", new Business());
        return "businesss/add";
    }

    @PostMapping(value="/add")
    public String processAddSubmit(@ModelAttribute("business") Business business, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return "business/add";
        dao.add(business);
        return "redirect:list.html";
    }

    @RequestMapping(value="/update/{cif}")
    public @NotNull String editPostOffer(Model model, @PathVariable String cif) {
        model.addAttribute("business", dao.find(cif));
        return "business/update";
    }

    @PostMapping(value="/update/{cif}")
    public String processUpdateSubmit(@PathVariable String cif, @ModelAttribute("business") Business business, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return "business/update";
        dao.update(business);
        return "redirect:../list";
    }

    @DeleteMapping(value="/delete/{cif}")
    public String processDelete(@PathVariable String cif) {
        dao.delete(cif);
        return "redirect:../list";
    }

}
