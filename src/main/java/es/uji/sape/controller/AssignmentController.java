package es.uji.sape.controller;

import es.uji.sape.dao.AssignmentDao;
import es.uji.sape.exceptions.ResourceNotFoundException;
import es.uji.sape.model.Assignment;
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
    public final @NotNull String list(@NotNull Model model) {
        model.addAttribute("assignments", dao.findAll());
        return "/assignment/list";
    }

    @GetMapping("/{id:[\\d]+}/{studentCode}")
    public final @NotNull Assignment get(@PathVariable("id") int id, @PathVariable("studentCode") @NotNull String studentCode) {
        return dao.find(id, studentCode).orElseThrow(() -> new ResourceNotFoundException("Assignment", Map.of("id", id, "studentCode", studentCode)));
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
        } catch (Throwable e) {
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
        } catch (Throwable e) {
            log.error(e.getMessage());
        }
        return "redirect:/assignment/list";
    }

    @DeleteMapping("/delete/{id:[\\d]+}/{studentCode}")
    public final @NotNull String processDelete(@PathVariable("id") int id, @PathVariable("studentCode") @NotNull String studentCode) {
        dao.delete(id, studentCode);
        return "redirect:/assignment/list";
    }
}
