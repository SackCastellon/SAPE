package es.uji.sape.controller


import es.uji.sape.dao.StudentDao
import es.uji.sape.exceptions.ResourceNotFoundException
import es.uji.sape.model.Student
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/student")
class StudentController {

    private val log = LoggerFactory.getLogger(javaClass)

    @Autowired
    private lateinit var dao: StudentDao

    @GetMapping
    fun list(model: Model): String {
        model.addAttribute("students", dao.findAll())
        return "/student/list"
    }

    @GetMapping("/add")
    fun add(model: Model): String {
        model.addAttribute("student", Student())
        return "/student/add"
    }

    @PostMapping("/add")
    fun processAddSubmit(@ModelAttribute student: Student, bindingResult: BindingResult): String {
        if (bindingResult.hasErrors()) return "/student/add"
        try {
            dao.add(student)
        } catch (e: Throwable) {
            log.error(e.message)
        }

        return "redirect:/student"
    }

    @GetMapping("/update/{code}")
    fun update(model: Model, @PathVariable code: String): String {
        model.addAttribute("student", dao.find(code) ?: throw ResourceNotFoundException("Student", mapOf("code" to code)))
        return "/student/update"
    }

    @PostMapping("/update/{code}")
    fun processUpdateSubmit(@ModelAttribute student: Student, @PathVariable code: String, bindingResult: BindingResult): String {
        if (bindingResult.hasErrors() || !student.code.contentEquals(code)) return "/student/update"
        try {
            dao.update(student)
        } catch (e: Throwable) {
            log.error(e.message)
        }

        return "redirect:/student"
    }

    @GetMapping("/delete/{code}")
    fun processDelete(@PathVariable code: String): String {
        dao.delete(code)
        return "redirect:/student"
    }
}
