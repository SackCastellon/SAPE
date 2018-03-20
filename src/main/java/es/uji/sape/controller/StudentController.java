package es.uji.sape.controller;

import es.uji.sape.dao.StudentDao;
import es.uji.sape.exceptions.ResourceNotFoundException;
import es.uji.sape.model.Student;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@SuppressWarnings({"DesignForExtension", "FieldHasSetterButNoGetter"})
public class StudentController {

    private StudentDao dao;

    @Autowired
    public void setDao(@NotNull StudentDao dao) {
        this.dao = dao;
    }

    @GetMapping
    public @NotNull List<Student> getAllStudents() {
        return dao.findAll();
    }

    @GetMapping("/{code}")
    public @NotNull Student getStudentByDni(@PathVariable("code") @NotNull String code) {
        return dao.find(code).orElseThrow(() -> new ResourceNotFoundException("Student", "code", code));
    }

    @RequestMapping(value="/add")
    public @NotNull String addStudent(Model model){
        model.addAttribute("student", new Student());
        return "students/add";
    }

    @PostMapping(value="/add")
    public String processAddSubmit(@ModelAttribute("student") Student student, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return "student/add";
        dao.add(student);
        return "redirect:list.html";
    }

    @RequestMapping(value="/update/{code}")
    public @NotNull String editPostOffer(Model model, @PathVariable String code) {
        model.addAttribute("student", dao.find(code));
        return "student/update";
    }

    @PostMapping(value="/update/{code}")
    public String processUpdateSubmit(@PathVariable String code, @ModelAttribute("student") Student student, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return "student/update";
        dao.update(student);
        return "redirect:../list";
    }

    @DeleteMapping(value="/delete/{code}")
    public String processDelete(@PathVariable String code) {
        dao.delete(code);
        return "redirect:../list";
    }
}
