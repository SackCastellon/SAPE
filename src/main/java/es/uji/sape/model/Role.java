package es.uji.sape.model;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    STUDENT("Student"),
    CEiTFG("CEiTFG"),
    CCG("CCG"),
    CONTACT_PERSON("Contact Person");

    @Getter
    private final @NotNull String label;

    Role(@NotNull String label) {
        this.label = label;
    }

    @Override
    public @NotNull String getAuthority() {
        return name();
    }
}
