package es.uji.sape.model

enum class Itinerary(val label: String) {
    SOFTWARE_ENGINEERING("Software Engineering"),
    INFORMATION_SYSTEMS("Information Systems"),
    INFORMATION_TECHNOLOGIES("Information Technologies"),
    COMPUTER_ENGINEERING("Computer Engineering");

    override fun toString() = label
}
