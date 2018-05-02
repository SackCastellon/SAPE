package es.uji.sape.controller;


import es.uji.sape.dao.UserDao;
import es.uji.sape.model.UserDetails;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequestMapping("/login")
public class LoginController {


    private final @NotNull UserDao dao;

    @Autowired
    public LoginController(@NotNull UserDao dao) {
        this.dao = dao;
    }


    public String login(Model model) {
        model.addAttribute("user", new UserDetails());
        return "login";
    }

    @PostMapping("/login")
    public String checkLogin(@ModelAttribute("user") UserDetails user,
                             BindingResult bindingResult, HttpSession session) {
        UserValidator userValidator = new UserValidator();
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "login";
        }

        user = dao.loadUserByUsername(user.getUsername(),user.getPassword());
        if (user == null) {
            bindingResult.rejectValue("password", "badpw", "La contraseña introducida es incorrecta");
            return "login";
        }

        session.setAttribute("user", user);
        String nextUrl = (String) session.getAttribute("nextUrl");
        if(nextUrl!=null){
            session.removeAttribute("nextUrl");
            return "redirect:"+nextUrl;
        }

        return "redirect:/";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }


}
class UserValidator implements Validator {
    @Override
    public boolean supports(Class<?> cls) {
        return UserDetails.class.isAssignableFrom(cls);
    }
    @Override
    public void validate(Object obj, Errors errors) {
        UserDetails usr = (UserDetails) obj;
        if (usr.getUsername().equals("")) {
            errors.rejectValue("username", "obligatorio", "Nombre de usuario requerido");
        }
        if (usr.getPassword().equals("")) {
            errors.rejectValue("password", "obligatorio", "Contraseña requerida");
        }
    }
}

