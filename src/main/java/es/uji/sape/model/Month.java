package es.uji.sape.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.io.NotSerializableException;
import java.io.ObjectOutputStream;

@Getter
@RequiredArgsConstructor
public enum Month {
    JANUARY("January"),
    FEBRUARY("February"),
    MARCH("March"),
    APRIL("April"),
    MAY("May"),
    JUNE("June"),
    JULY("July"),
    AUGUST("August"),
    SEPTEMBER("September"),
    OCTOBER("October"),
    NOVEMBER("November"),
    DECEMBER("December");

    private final @NotNull String label;

    private void writeObject(ObjectOutputStream out) throws NotSerializableException {
        throw new NotSerializableException("es.uji.sape.model.Month");
    }

    @Override
    public String toString() {
        return String.format("%02d - %s", ordinal() + 1, label);
    }
}
