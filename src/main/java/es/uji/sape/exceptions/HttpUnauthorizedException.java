package es.uji.sape.exceptions;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public final class HttpUnauthorizedException extends RuntimeException {

    @NonNls
    private static final String CLASS_NAME = "es.uji.sape.exceptions.HttpUnauthorizedException";

    public HttpUnauthorizedException(@NotNull String path) {
        super(String.format("You do not have the necessary authorization to access this resource '%s'", path));
    }

    private void readObject(ObjectInputStream in) throws NotSerializableException {
        throw new NotSerializableException(CLASS_NAME);
    }

    private void writeObject(ObjectOutputStream out) throws NotSerializableException {
        throw new NotSerializableException(CLASS_NAME);
    }
}
