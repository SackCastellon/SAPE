package es.uji.sape.model;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.NotSerializableException;
import java.io.ObjectOutputStream;

public enum Itinerary {
    SOFTWARE_ENGINEERING("Software Engineering"),
    INFORMATION_SYSTEMS("Information Systems"),
    INFORMATION_TECHNOLOGIES("Information Technologies"),
    COMPUTER_ENGINEERING("Computer Engineering");

    @Getter
    private final @NotNull String label;

    Itinerary(@NotNull String label) {
        this.label = label;
    }

    private void writeObject(ObjectOutputStream out) throws NotSerializableException {
        throw new NotSerializableException("es.uji.sape.model.Itinerary");
    }
}
