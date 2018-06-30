package es.uji.sape.controller;

import es.uji.sape.controller.validator.ModifyRequestValidator;
import es.uji.sape.dao.ModifyRequestDao;
import es.uji.sape.model.ModifyRequest;
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

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequestMapping("/modifyRequest")
public class ModifyRequestController {

    private final @NotNull ModifyRequestDao dao;

    @Autowired
    public ModifyRequestController(@NotNull ModifyRequestDao dao) {
        this.dao = dao;
    }

    @GetMapping("/requestList/{projectOfferId:[\\d]+}")
    public final @NotNull String list(@NotNull Model model, @PathVariable("projectOfferId") int projectOfferId) {
        model.addAttribute("internshipOffers", dao.findPerBusiness(projectOfferId));
        return "/requestList/{projectOfferId:[\\d]+}";
    }

    @GetMapping("/add")
    public final @NotNull String add(@NotNull Model model) {
        model.addAttribute("projectOffer", new ModifyRequest());
        return "/projectOffers/list";
    }

    @PostMapping("/add/{projectOfferId:[\\d]+}")
    public final @NotNull String processAddSubmit(HttpSession session, @ModelAttribute("modifyRequest") @NotNull ModifyRequest modifyRequest, @NotNull BindingResult bindingResult, @PathVariable("projectOfferId") String projectOfferId) {
        ModifyRequestValidator validator = new ModifyRequestValidator();
        validator.validate(modifyRequest, bindingResult);
        if (bindingResult.hasErrors()) return "/modifyRequest/add";
        try {
            modifyRequest.setProjectOfferId(Integer.parseInt(projectOfferId));
            dao.add(modifyRequest);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "redirect:/modifyRequest/list";
    }

    @DeleteMapping("/delete/{id:[\\d]+}")
    public final @NotNull String processDelete(@PathVariable("id") int id) {
        dao.delete(id);
        return "redirect:/projectOffers/list";
    }
}
