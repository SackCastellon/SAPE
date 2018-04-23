package es.uji.sape.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import static es.uji.sape.model.Itinerary.SOFTWARE_ENGINEERING;
import static es.uji.sape.model.OfferState.PENDING;

@Data
@NoArgsConstructor
public final class ProjectOffer {
    private int id;
    private @NotNull Itinerary itinerary = SOFTWARE_ENGINEERING;
    private @NotNull String technologies = "";
    private @NotNull String objectives = "";
    private @NotNull OfferState state = PENDING;
    private int internshipOfferId;
}
