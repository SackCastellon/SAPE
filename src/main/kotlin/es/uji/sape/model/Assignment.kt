package es.uji.sape.model

import es.uji.sape.model.AssignmentState.UNDEFINED
import java.time.LocalDate

data class Assignment(
    val projectOfferId: Int = 0,
    val studentCode: String = "",
    val tutorCode: String = "",
    val proposalDate: LocalDate = LocalDate.now(),
    val acceptanceDate: LocalDate? = null,
    val rejectionDate: LocalDate? = null,
    val igluTransferDate: LocalDate? = null,
    val state: AssignmentState = UNDEFINED
)
