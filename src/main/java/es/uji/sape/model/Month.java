package es.uji.sape.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.NotSerializableException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public enum Month {
    INDIFFERENT("Indifferent", -1),

    JANUARY("January", 1),
    FEBRUARY("February", 2),
    MARCH("March", 3),
    APRIL("April", 4),
    MAY("May", 5),
    JUNE("June", 6),
    JULY("July", 7),
    AUGUST("August", 8),
    SEPTEMBER("September", 9),
    OCTOBER("October", 10),
    NOVEMBER("November", 11),
    DECEMBER("December", 12);

    private final @NotNull String label;
    private final int code;

    private static final Map<Integer, Month> valueMap = new HashMap<>(Month.values().length);
    private static final List<Month> months = new ArrayList<>(Arrays.asList(values()));

    static {
        for (Month month : values()) valueMap.put(month.code, month);
    }

    @Contract(pure = true)
    public static Month valueOf(int code) {
        return valueMap.getOrDefault(code, INDIFFERENT);
    }

    public static List<Month> getMonths() {
        return months;
    }

    private void writeObject(ObjectOutputStream out) throws NotSerializableException {
        throw new NotSerializableException("es.uji.sape.model.Month");
    }

    @Override
    public String toString() {
        return String.format("%02d - %s", code, label);
    }
}
