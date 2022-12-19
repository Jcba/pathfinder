package org.routing.importer.osm;

import crosby.binary.Osmformat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OSMNodeDegreeParser extends AbstractOSMParser {

    private final Map<Long, Short> nodeDegreeMap = new HashMap<>();

    @Override
    protected void parseRelations(List<Osmformat.Relation> list) {

    }

    @Override
    protected void parseDense(Osmformat.DenseNodes denseNodes) {

    }

    @Override
    protected void parseNodes(List<Osmformat.Node> list) {

    }

    @Override
    protected void parseWays(List<Osmformat.Way> list) {
        for (Osmformat.Way way : list) {
            Map<String, List<String>> expandedWayKeyValues = getExpandedKeyVals(way.getKeysList(), way.getValsList());

            if (shouldParseWayType(expandedWayKeyValues)) {
                List<Long> refsList = way.getRefsList();
                long prevNodeReference = 0;
                for (long ref : refsList) {
                    long nodeId = ref + prevNodeReference;
                    if (nodeDegreeMap.containsKey(nodeId)) {
                        nodeDegreeMap.put(nodeId, (short) (nodeDegreeMap.get(nodeId) + 1));
                    } else {
                        nodeDegreeMap.put(nodeId, (short) 1);
                    }
                    prevNodeReference = nodeId;
                }
            }
        }
    }

    @Override
    protected void parse(Osmformat.HeaderBlock headerBlock) {

    }

    @Override
    public void complete() {

    }

    public Map<Long, Short> getNodeDegreeMap() {
        return nodeDegreeMap;
    }
}
