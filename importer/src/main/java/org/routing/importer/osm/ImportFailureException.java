package org.routing.importer.osm;

public class ImportFailureException extends RuntimeException {
    public ImportFailureException(Exception e) {
        super(e);
    }
}
