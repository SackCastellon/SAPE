package es.uji.sape.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;

import static es.uji.sape.model.AssignmentState.UNDEFINED;

@Data
@NoArgsConstructor
public final class Assignment {

    private int projectOfferId;
    private @NotNull String studentCode = "";
    private @NotNull String tutorCode = "";
    private @NotNull LocalDate proposalDate = LocalDate.now();
    private @Nullable LocalDate acceptanceDate;
    private @Nullable LocalDate rejectionDate;
    private @Nullable LocalDate igluTransferDate;
    private @NotNull AssignmentState state = UNDEFINED;

}
