package es.uji.sape.controller;

import es.uji.sape.dao.StudentDao;
import es.uji.sape.exceptions.ResourceNotFoundException;
import es.uji.sape.model.Student;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/{dni}")
    public @NotNull Student getStudentByDni(@PathVariable("dni") @NotNull String dni) {
        return dao.find(dni).orElseThrow(() -> new ResourceNotFoundException("Student", "dni", dni));
    }
}
