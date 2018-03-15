package es.uji.sape.model;

import lombok.Data;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;

@Data
public final class Assignment {

    @Getter
    private final int projectOfferId;
    @Getter
    private final @NotNull String studentDni;
    @Getter
    private final @NotNull String tutorDni;
    @Getter
    private final @NotNull LocalDate proposalDate;
    @Getter
    private final @Nullable LocalDate acceptanceDate;
    @Getter
    private final @Nullable LocalDate rejectionDate;
    @Getter
    private final @NotNull LocalDate igluTransferDate;
    @Getter
    private final @NotNull AssignmentState state;

}
