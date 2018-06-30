package es.uji.sape.controller;

import es.uji.sape.dao.AssignmentDao;
import es.uji.sape.exceptions.ResourceNotFoundException;
import es.uji.sape.model.Assignment;
import es.uji.sape.model.AssignmentState;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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

@Slf4j
@Controller
@RequestMapping("/assignment")
public class AssignmentController {

    private final @NotNull AssignmentDao dao;

    @Autowired
    public AssignmentController(@NotNull AssignmentDao dao) {
        this.dao = dao;
    }

    @GetMapping("/list")
    public final @NotNull String list(@NotNull Model model, Authentication auth) {
        model.addAttribute("assignments", dao.findPerStudent(((UserDetails) auth.getPrincipal()).getUsername()));
        return "/assignment/list";
    }

    @GetMapping("/listAll")
    public final @NotNull String listAll(@NotNull Model model) {
        model.addAttribute("assignments", dao.findAll());
        return "/assignment/list";
    }

    @GetMapping("/{id:[\\d]+}/{studentCode}")
    public final @NotNull Assignment get(@PathVariable("id") int id, @PathVariable("studentCode") @NotNull String studentCode) {
        return dao.find(id, studentCode).orElseThrow(() -> new ResourceNotFoundException("Assignment", Map.of("id", id, "studentCode", studentCode)));
    }

    @GetMapping("/accept/{id:[\\d]+}/{studentCode}")
    public final @NotNull String getToChange(@NotNull Model model, @PathVariable("id") int id, @PathVariable("studentCode") @NotNull String studentCode) {
        model.addAttribute("assignment", dao.find(id, studentCode).orElseThrow(() -> new ResourceNotFoundException("Assignment", Map.of("id", id, "studentCode", studentCode))));
        return "/assignment/accept";
    }

    @GetMapping("/add")
    public final @NotNull String add(@NotNull Model model) {
        model.addAttribute("assignment", new Assignment());
        return "/assignment/add";
    }

    @PostMapping("/add")
    public final @NotNull String processAddSubmit(@ModelAttribute("assignment") @NotNull Assignment assignment, @NotNull BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "/assignment/add";
        try {
            dao.add(assignment);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "redirect:/assignment/list";
    }

    @GetMapping("/update/{id:[\\d]+}/{studentCode}")
    public final @NotNull String update(@NotNull Model model, @PathVariable("id") int id, @PathVariable("studentCode") @NotNull String studentCode) {
        model.addAttribute("assignment", dao.find(id, studentCode));
        return "/assignment/update";
    }

    @PostMapping("/update")
    public final @NotNull String processUpdateSubmit(@ModelAttribute("assignment") @NotNull Assignment assignment, @NotNull BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "/assignment/update";
        try {
            dao.update(assignment);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "redirect:/assignment/list";
    }

    @PostMapping("/reject")
    public final @NotNull String processReject(@ModelAttribute("assignment") @NotNull Assignment assignment, @NotNull BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            assignment.setState(AssignmentState.REJECTED);
            dao.update(assignment);
        }
        return "redirect:/assignment/list"; //FIXME A donde tiene que redirigir
    }

    @PostMapping("/accept")
    public final @NotNull String processAccept(@ModelAttribute("assignment") @NotNull Assignment assignment, @NotNull BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            assignment.setState(AssignmentState.ACCEPTED);
            dao.update(assignment);
        }
        return "redirect:/assignment/list"; //FIXME A donde tiene que redirigir
    }

    @DeleteMapping("/delete/{id:[\\d]+}/{studentCode}")
    public final @NotNull String processDelete(@PathVariable("id") int id, @PathVariable("studentCode") @NotNull String studentCode) {
        dao.delete(id, studentCode);
        return "redirect:/assignment/list";
    }
}
