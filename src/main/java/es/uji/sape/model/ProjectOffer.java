package es.uji.sape.model;

import lombok.Data;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Data
public final class ProjectOffer {

    @Getter
    private final int id;
    @Getter
    private final @NotNull Itinerary itinerary;
    @Getter
    private final @NotNull String technologies;
    @Getter
    private final @NotNull String objectives;
    @Getter
    private final @NotNull OfferState state;
    @Getter
    private final int internshipOfferId;
}
