package axal25.oles.jacek.entity.factory;

public interface EntityFactory<T extends Object> {

    /**
     * Generates fake id for other fields than entity's id field if previously generated id is null.
     **/
    default T getIdForNonIdFields(T entityIdFieldValue, IdGenerateMode idGenerateMode) {
        if (entityIdFieldValue != null) {
            return entityIdFieldValue;
        }
        if (IdGenerateMode.NULL.equals(idGenerateMode)) {
            idGenerateMode = IdGenerateMode.DEFAULT;
        }
        return getId(null, idGenerateMode);
    }

    /**
     * @param id             nullable. Input id.
     * @param idGenerateMode strategy type used to generate id.
     * @return generated id for entity's id field. Can return null.
     */
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
            case FROM_ENTITY_MANAGER:
                if (id != null) {
                    throw new RuntimeException("If argument \"" +
                            IdGenerateMode.class.getSimpleName() +
                            "\" is " +
                            IdGenerateMode.FROM_ENTITY_MANAGER.name() +
                            " then argument \"id\" should be <null>.");
                }
                return getIdFromEntityManager();
            default:
                throw new UnsupportedOperationException("Argument \"" +
                        IdGenerateMode.class.getSimpleName() +
                        "\" cannot be <" +
                        idGenerateMode.name() +
                        "> (Unsupported).");
        }
    }

    T getIdFromJdbc();

    T getIdFromEntityManager();

    T generateRandomId();

    enum IdGenerateMode {
        NULL, RANDOM, /* TODO: remove */ FROM_JDBC, IMPOSED, FROM_ENTITY_MANAGER;
        public static final IdGenerateMode DEFAULT = IdGenerateMode.FROM_ENTITY_MANAGER;
    }
}
