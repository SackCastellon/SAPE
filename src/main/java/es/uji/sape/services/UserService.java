package es.uji.sape.services;

import es.uji.sape.dao.UserDao;
import es.uji.sape.security.UserInfo;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final @NotNull UserDao dao;

    @Autowired
    public @NotNull UserService(@NotNull UserDao dao) {
        this.dao = dao;
    }

    @Override
    public final UserDetails loadUserByUsername(String username) {
        val user = dao.find(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return new UserInfo(user);
    }
}
