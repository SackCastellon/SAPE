package es.uji.sape.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@Data
@NoArgsConstructor
public final class Preference implements Serializable {
    private static final long serialVersionUID = -6558543423517749165L;

    private int priority;
    private @NotNull String studentCode = "";
    private int projectOfferId;
    private String name;
}
