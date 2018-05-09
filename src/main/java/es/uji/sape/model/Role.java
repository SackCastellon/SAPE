package es.uji.sape.model;

import lombok.Getter;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    STUDENT("role.student"),
    CEiTFG("role.ceitfg"),
    CCG("role.ccg"),
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
}
