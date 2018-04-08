package es.uji.sape.services;

import es.uji.sape.dao.UserDao;
import es.uji.sape.security.User;
import es.uji.sape.security.UserInfo;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("FieldHasSetterButNoGetter")
public class SapeUserDetailsService implements UserDetailsService {

    private UserDao dao;

    @Autowired
    public final void setDao(@NotNull UserDao dao) {
        this.dao = dao;
    }

    @Override
    public final UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final @NotNull User user = dao.find(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return new UserInfo(user);
    }
}
