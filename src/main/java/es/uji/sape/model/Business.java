package es.uji.sape.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Data
@NoArgsConstructor
public final class Business {
    private @NotNull String cif = "";
    private @NotNull String name = "";
    private @NotNull String address = "";
    private @NotNull String telephone = "";
}
