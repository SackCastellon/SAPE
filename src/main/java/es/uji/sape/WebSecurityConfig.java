package es.uji.sape;

import es.uji.sape.services.SapeUserDetailsService;
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
@SuppressWarnings("DesignForExtension")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private SapeUserDetailsService userDetailsService;

    @Autowired
    public final void setUserDetailsService(@NotNull SapeUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected final void configure(@NotNull HttpSecurity http) throws Exception {
        http
                .authorizeRequests().antMatchers("/", "/about").permitAll().anyRequest().authenticated().and()
                .formLogin().loginPage("/login").defaultSuccessUrl("/").permitAll().and()
                .logout().permitAll();
    }

    @Override
    public final void configure(@NotNull WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/images/**", "/js/**");
    }

    @Override
    protected final void configure(@NotNull AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public @NotNull DaoAuthenticationProvider authenticationProvider() {
        final @NotNull DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }

    @Bean
    public @NotNull PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(11);
    }

//    @Bean
//    @Override
//    @SuppressWarnings("DesignForExtension")
//    public @NotNull UserDetailsService userDetailsService() {
//        PasswordEncoder encoder = encoder();
//        // outputs {bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG
//        // remember the password that is printed out and use in the next step
//        String password = encoder.encode("password");
//        System.out.println(password);
//
//
//        UserDetails user = User.withUsername("user")
//                .password(password)
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(user);
//    }
}
