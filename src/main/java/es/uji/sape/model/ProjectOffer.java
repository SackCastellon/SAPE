package es.uji.sape.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Data
@NoArgsConstructor
public final class ProjectOffer {

    private int id;
    private @Nullable Itinerary itinerary;
    private @NotNull String technologies = "";
    private @NotNull String objectives = "";
    private @Nullable OfferState state;
    private int internshipOfferId;
}
