package es.uji.sape.model;

import lombok.Data;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Data
public final class Business {

    @Getter
    private final @NotNull String cif;
    @Getter
    private final @NotNull String name;
    @Getter
    private final @NotNull String address;
    @Getter
    private final @NotNull String telephone;
}
