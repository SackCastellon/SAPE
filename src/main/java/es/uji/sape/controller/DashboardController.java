package es.uji.sape.controller;

import es.uji.sape.exceptions.HttpUnauthorizedException;
import es.uji.sape.security.UserInfo;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @GetMapping
    public final @NotNull String dashboard(@NotNull Model model, @NotNull Authentication auth) {
        if (auth.getPrincipal() instanceof UserInfo) {
            val userInfo = (UserInfo) auth.getPrincipal();
            val role = userInfo.getUser().getRole();
            System.out.println(role);
            return "/dashboard/" + role.name().toLowerCase();
        } else {
            throw new HttpUnauthorizedException("/dashboard");
        }
    }
}
