package es.uji.sape.dao;

import es.uji.sape.model.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface UserDao {
    UserDetails loadUserByUsername(String username, String password);
    Collection<UserDetails> listAllUsers();

}
