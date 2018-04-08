package es.uji.sape.security;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public final class User {

    private final int id;
    private final @NotNull String username;
    private final @NotNull String password;
}
