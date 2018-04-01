package es.uji.sape.controller;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class CommonController {

    @GetMapping("/about")
    public final @NotNull String about(@NotNull Model model) {
        return "/about";
    }

    @GetMapping("/login")
    public final @NotNull String login(@NotNull Model model) {
        return "/login";
    }
}