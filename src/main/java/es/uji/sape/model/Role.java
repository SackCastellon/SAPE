package es.uji.sape.model;

import lombok.Getter;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.GrantedAuthority;

import java.io.NotSerializableException;
import java.io.ObjectOutputStream;

public enum Role implements GrantedAuthority {
    STUDENT("role.student"),
    CEYTFG("role.ceytfg"),
    DCC("role.dcc"),
    TUTOR("role.tutor"),
    CONTACT("role.contact");

    @Getter
    private final @NotNull String key;

    Role(@NonNls @NotNull String key) {
        this.key = key;
    }

    @Override
    public @NotNull String getAuthority() {
        return name();
    }

    private void writeObject(ObjectOutputStream out) throws NotSerializableException {
        throw new NotSerializableException("es.uji.sape.model.Role");
    }
}
