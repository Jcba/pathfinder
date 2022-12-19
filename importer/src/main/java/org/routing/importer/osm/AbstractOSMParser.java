package org.routing.importer.osm;

import crosby.binary.BinaryParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractOSMParser extends BinaryParser {

    protected Map<String, List<String>> getExpandedKeyVals(List<Integer> keysList, List<Integer> valsList) {
        Map<String, List<String>> result = new HashMap<>(keysList.size());
        for (int i = 0; i < keysList.size(); i++) {
            String key = getStringById(keysList.get(i));
            String value = getStringById(valsList.get(i));
            if (result.containsKey(key)) {
                result.get(key).add(value);
            } else {
                result.put(key, List.of(value));
            }
        }

        return result;
    }

    protected boolean shouldParseWayType(Map<String, List<String>> expandedWayKeyVals) {
        return expandedWayKeyVals.containsKey("highway") && shouldParseHighWayType(expandedWayKeyVals.get("highway"));
    }

    private boolean shouldParseHighWayType(List<String> highwayTypes) {
        return highwayTypes.contains("motorway") ||
                highwayTypes.contains("trunk") ||
                highwayTypes.contains("primary") ||
                highwayTypes.contains("secondary") ||
                highwayTypes.contains("tertiary") ||
                highwayTypes.contains("unclassified") ||
                highwayTypes.contains("residential") ||
                highwayTypes.contains("motorway_link") ||
                highwayTypes.contains("trunk_link") ||
                highwayTypes.contains("primary_link") ||
                highwayTypes.contains("secondary_link") ||
                highwayTypes.contains("tertiary_link");
    }
}
