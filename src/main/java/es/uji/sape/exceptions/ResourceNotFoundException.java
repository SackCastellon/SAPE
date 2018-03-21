package es.uji.sape.exceptions;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.NotSerializableException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.NOT_FOUND)
public final class ResourceNotFoundException extends RuntimeException {

    @Getter
    private final @NotNull String resourceName;
    @Getter
    private final @NotNull Map<String, Object> fields = new HashMap<>();

    public ResourceNotFoundException(@NotNull String resourceName, @NotNull Map<String, Object> fields) {
        super("Resource '" + resourceName + "' not found with fields: " + fields.entrySet().stream().map(it -> String.format("%s : '%s'", it.getKey(), it.getValue())).collect(Collectors.joining(", ", "[", "]")));
        this.resourceName = resourceName;
        this.fields.putAll(fields);
    }

    private void readObject(java.io.ObjectInputStream in) throws NotSerializableException {
        throw new NotSerializableException("es.uji.sape.exceptions.ResourceNotFoundException");
    }

    private void writeObject(java.io.ObjectOutputStream out) throws NotSerializableException {
        throw new NotSerializableException("es.uji.sape.exceptions.ResourceNotFoundException");
    }
}
