package es.uji.sape;

import es.uji.sape.services.UserService;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@Profile("!https")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final int STRENGTH = 10;

    private final @NotNull UserService service;

    @Autowired
    public WebSecurityConfig(@NotNull UserService service) {
        this.service = service;
    }

    @Override
    protected final void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/assets/**", "/webjars/**", "/", "/about", "/login").permitAll().anyRequest().authenticated();
        http.formLogin().loginPage("/login").defaultSuccessUrl("/").permitAll();
        http.logout().invalidateHttpSession(true).permitAll();
    }

    @Override
    public final void configure(WebSecurity web) {
        web.ignoring().antMatchers("/assets/**");
    }

    @Override
    protected final void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public @NotNull DaoAuthenticationProvider authenticationProvider() {
        val authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(service);
        authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }

    @Bean
    public @NotNull PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(STRENGTH);
    }
}
