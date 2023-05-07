package axal25.oles.jacek.entity.factory;

public interface EntityFactory<T extends Object> {
    default T getId(T id, IdGenerateMode idGenerateMode) {
        if (idGenerateMode == null) {
            throw new RuntimeException("Argument \"" +
                    IdGenerateMode.class.getSimpleName() +
                    "\" cannot be null.");
        }
        switch (idGenerateMode) {
            case IMPOSED:
                if (id == null) {
                    throw new RuntimeException("If argument \"" +
                            IdGenerateMode.class.getSimpleName() +
                            "\" is " +
                            IdGenerateMode.IMPOSED.name() +
                            " then argument \"id\" should NOT be <null>.");
                }
                return id;
            case NULL:
                if (id != null) {
                    throw new RuntimeException("If argument \"" +
                            IdGenerateMode.class.getSimpleName() +
                            "\" is " +
                            IdGenerateMode.NULL.name() +
                            " then argument \"id\" should be <null>.");
                }
                return null;
            case RANDOM:
                if (id != null) {
                    throw new RuntimeException("If argument \"" +
                            IdGenerateMode.class.getSimpleName() +
                            "\" is " +
                            IdGenerateMode.RANDOM.name() +
                            " then argument \"id\" should be <null>.");
                }
                return generateRandomId();
            case FROM_JDBC:
                if (id != null) {
                    throw new RuntimeException("If argument \"" +
                            IdGenerateMode.class.getSimpleName() +
                            "\" is " +
                            IdGenerateMode.FROM_JDBC.name() +
                            " then argument \"id\" should be <null>.");
                }
                return getIdFromJdbc();
            default:
                throw new UnsupportedOperationException("Argument \"" +
                        IdGenerateMode.class.getSimpleName() +
                        "\" cannot be <" +
                        idGenerateMode.name() +
                        "> (Unsupported).");
        }
    }

    T getIdFromJdbc();

    T generateRandomId();

    enum IdGenerateMode {
        NULL, RANDOM, FROM_JDBC, IMPOSED
    }
}
