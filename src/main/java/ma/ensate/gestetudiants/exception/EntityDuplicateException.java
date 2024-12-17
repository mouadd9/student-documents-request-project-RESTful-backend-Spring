package ma.ensate.gestetudiants.exception;

public class EntityDuplicateException extends RuntimeException {
    public EntityDuplicateException(final String message) {
        super(message);
    }
}