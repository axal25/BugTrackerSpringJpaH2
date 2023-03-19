package axal25.oles.jacek.jdbc;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DatabaseMetaDataUtils {

    public static ResultSet getTables(
            Connection connection,
            String filterByCatalogName,
            String filterBySchemaName,
            String filterByTableName,
            String[] filterByTableTypes) throws SQLException {
        return connection
                .getMetaData()
                .getTables(
                        filterByCatalogName,
                        filterBySchemaName,
                        filterByTableName,
                        filterByTableTypes);
    }

    public static GetTablesRow read(ResultSet resultSet) throws SQLException {
        return GetTablesRow.builder()
                .tableCatalog(resultSet.getString("TABLE_CAT"))
                .tableSchema(resultSet.getString("TABLE_SCHEM"))
                .tableName(resultSet.getString("TABLE_NAME"))
                .tableType(resultSet.getString("TABLE_TYPE"))
                .remarks(resultSet.getString("REMARKS"))
                .catalogTypes(resultSet.getString("TYPE_CAT"))
                .schemaTypes(resultSet.getString("TYPE_SCHEM"))
                .typeName(resultSet.getString("TYPE_NAME"))
                .selfReferencingColumnName(resultSet.getString("SELF_REFERENCING_COL_NAME"))
                .referenceGeneration(resultSet.getString("REF_GENERATION"))
                .build();
    }

    @Getter
    @ToString
    @Builder
    public static class GetTablesRow {
        private String tableCatalog;
        private String tableSchema;
        private String tableName;
        private String tableType;
        private String remarks;
        private String catalogTypes;
        private String schemaTypes;
        private String typeName;
        private String selfReferencingColumnName;
        private String referenceGeneration;
    }
}
