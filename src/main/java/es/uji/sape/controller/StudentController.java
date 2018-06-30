package es.uji.sape.controller;

import es.uji.sape.dao.StudentDao;
import es.uji.sape.model.Student;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/student")
public class StudentController {

    private final @NotNull StudentDao dao;

    @Autowired
    public StudentController(@NotNull StudentDao dao) {
        this.dao = dao;
    }

    @GetMapping({"/", "/list"})
    public final @NotNull String list(@NotNull Model model) {
        List<Student> studentList = dao.findAll().stream()
                .sorted(Comparator
                        .comparing(Student::getAverageScore).reversed()
                        .thenComparing(Student::getCode))
                .collect(Collectors.toList());
        model.addAttribute("students", studentList);
        return "/student/list";
    }
}
