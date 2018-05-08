package es.uji.sape.security;

import es.uji.sape.model.Role;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Data
@NoArgsConstructor
public final class User {
    private int id;
    private @NotNull String username = "";
    private @NotNull String password = "";
    private @NotNull Role role = Role.STUDENT;
}
