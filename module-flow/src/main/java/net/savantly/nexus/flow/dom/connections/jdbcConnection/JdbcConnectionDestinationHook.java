package net.savantly.nexus.flow.dom.connections.jdbcConnection;

import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import net.savantly.nexus.common.types.KeyValueDto;
import net.savantly.nexus.flow.dom.destinations.Destination;
import net.savantly.nexus.flow.dom.destinations.DestinationHook;
import net.savantly.nexus.flow.dom.destinations.DestinationHookResponse;
import net.savantly.nexus.flow.dom.formData.FormDataRecord;
import net.savantly.nexus.flow.dom.formMapping.Mapping;

@RequiredArgsConstructor
public class JdbcConnectionDestinationHook implements DestinationHook {

    final private DataSource dataSource;
    final private ObjectMapper objectMapper;

    @Override
    public DestinationHookResponse execute(Destination destination, Map<String, Object> payload, Collection<? extends Mapping> formMappings) {

        // Convert the payload to a FormDataRecord
        var formDataRecord = new FormDataRecord();
        for (Mapping formMapping : formMappings) {
            var kv = extractKeyValue(formMapping, payload);
            formDataRecord.put(kv.getKey(), kv.getValue());
        }
        if (formDataRecord.isEmpty()) {
            // create default 
            for (var entry : payload.entrySet()) {
                var kv = extractKeyValue(new Mapping(){
                    @Override
                    public String getSourcePath() {
                        return defaultColumnNameFormat(entry.getKey());
                    }
                    @Override
                    public String getTargetPath() {
                        return defaultColumnNameFormat(entry.getKey());
                    }
                }, payload);
                formDataRecord.put(kv.getKey(), kv.getValue());
            }
        }

        try (var connection = dataSource.getConnection()) {
            var columnNames = formDataRecord.keySet().stream().toList();
            var columnValues = formDataRecord.values().stream().toList();
            var sql = prepareStatement(
                    destination.getCollectionName(),
                    columnNames);

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                for (int i = 0; i < columnValues.size(); i++) {
                    preparedStatement.setString(i + 1, columnValues.get(i));
                }
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            return new DestinationHookResponse().setSuccess(false).setMessage(e.getMessage());
        }
        return new DestinationHookResponse().setSuccess(true).setMessage("inserted record into " + destination.getCollectionName());
    }

    private KeyValueDto extractKeyValue(Mapping formMapping, Map<String, Object> payload) {
        // Extract the value from the payload
        try {
            var value = payload.get(formMapping.getSourcePath());
            return new KeyValueDto(formMapping.getTargetPath(), objectMapper.writeValueAsString(value));
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract value from payload", e);
        }
    }

    private String prepareStatement(String tableName, List<String> columns) {

        // Build the SQL statement
        StringBuilder sql = new StringBuilder("INSERT INTO ");
        sql.append(tableName).append(" (");

        for (int i = 0; i < columns.size(); i++) {
            sql.append(formatAsColumnName(columns.get(i)));
            if (i < columns.size() - 1) {
                sql.append(", ");
            }
        }

        sql.append(") VALUES (");

        for (int i = 0; i < columns.size(); i++) {
            sql.append("?");
            if (i < columns.size() - 1) {
                sql.append(", ");
            }
        }

        sql.append(")");
        return sql.toString();

    }

    private String formatAsColumnName(String column) {
        return column.replaceAll("[^a-zA-Z0-9]", "_");
    }

    private String defaultColumnNameFormat(String column) {
        return formatAsColumnName(column.toLowerCase());
    }

}
