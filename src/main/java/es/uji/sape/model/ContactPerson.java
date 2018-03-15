package es.uji.sape.model;

import lombok.Data;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Data
public final class ContactPerson {

    @Getter
    private final @NotNull String username;
    @Getter
    private final @NotNull String name;
    @Getter
    private final @NotNull String email;
    @Getter
    private final @NotNull String businessCif;
}
