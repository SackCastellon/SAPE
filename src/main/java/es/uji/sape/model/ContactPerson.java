package es.uji.sape.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Data
@NoArgsConstructor
public final class ContactPerson {
    private @NotNull String username = "";
    private @NotNull String name = "";
    private @NotNull String email = "";
    private @NotNull String businessCif = "";
}
