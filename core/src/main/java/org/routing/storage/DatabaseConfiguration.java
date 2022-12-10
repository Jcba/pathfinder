package org.routing.storage;

public record DatabaseConfiguration(
        String jdbcUrl,
        int queryTimeoutSeconds,
        boolean createSchemaIfNotExists,

        boolean dropTableIfExists
) {
}
