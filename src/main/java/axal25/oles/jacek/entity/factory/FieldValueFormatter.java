package axal25.oles.jacek.entity.factory;

import axal25.oles.jacek.entity.ApplicationEntity;
import axal25.oles.jacek.entity.ReleaseEntity;
import axal25.oles.jacek.entity.TicketEntity;

public class FieldValueFormatter {

    private final String entityObjectName;

    public FieldValueFormatter(Class<?> entityClass) {
        entityObjectName = getEntityObjectName(entityClass);
    }

    public String getStringValue(String fieldName, String methodName, Integer id) {
        return getStringValue(
                entityObjectName,
                fieldName,
                methodName,
                id);
    }

    public static String getStringValue(Class<?> entityClass, String fieldName, String methodName, Integer id) {
        return getStringValue(
                getEntityObjectName(entityClass),
                fieldName,
                methodName,
                id);
    }


    public static String getStringValue(String entityObjectName, String fieldName, String methodName, Integer id) {
        return String.format("%s %s for %s %d",
                entityObjectName,
                fieldName,
                methodName,
                id);
    }

    private static String getEntityObjectName(Class<?> entityClass) {
        if (entityClass == null) {
            throw new IllegalArgumentException("entityClass cannot be null");
        }

        if (ApplicationEntity.class.equals(entityClass)) {
            return "application";
        }
        if (ReleaseEntity.class.equals(entityClass)) {
            return "release";
        }
        if (TicketEntity.class.equals(entityClass)) {
            return "ticket";
        }

        throw new UnsupportedOperationException("unsupported value for entityClass: " + entityClass);
    }
}
