package es.uji.sape.security;

import lombok.Getter;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.List;

public final class UserInfo implements UserDetails {

    @NonNls
    private static final String CLASS_NAME = "es.uji.sape.security.UserInfo";
    private static final long serialVersionUID = 6080316272288741795L;

    @Getter
    private final @NotNull User user;

    public UserInfo(@NotNull User user) {
        this.user = user;
    }

    @Override
    public @NotNull Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(user.getRole());
    }

    @Override
    public @NotNull String getPassword() {
        return user.getPassword();
    }

    @Override
    public @NotNull String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    private void readObject(ObjectInputStream in) throws NotSerializableException {
        throw new NotSerializableException(CLASS_NAME);
    }

    private void writeObject(ObjectOutputStream out) throws NotSerializableException {
        throw new NotSerializableException(CLASS_NAME);
    }
}
