package es.uji.sape.model

import es.uji.sape.model.Itinerary.SOFTWARE_ENGINEERING
import es.uji.sape.model.OfferState.PENDING

data class ProjectOffer(
    val id: Int = 0,
    val itinerary: Itinerary = SOFTWARE_ENGINEERING,
    val technologies: String = "",
    val objectives: String = "",
    val state: OfferState = PENDING,
    val internshipOfferId: Int = 0
)
