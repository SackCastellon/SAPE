package es.uji.sape.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.io.NotSerializableException;
import java.io.ObjectOutputStream;


@Getter
@RequiredArgsConstructor
public enum Itinerary {
    SOFTWARE_ENGINEERING("Software Engineering"),
    INFORMATION_SYSTEMS("Information Systems"),
    INFORMATION_TECHNOLOGIES("Information Technologies"),
    COMPUTER_ENGINEERING("Computer Engineering");

    private final @NotNull String label;

    private void writeObject(ObjectOutputStream out) throws NotSerializableException {
        throw new NotSerializableException("es.uji.sape.model.Itinerary");
    }

    @Override
    public String toString() {
        return label;
    }
}
