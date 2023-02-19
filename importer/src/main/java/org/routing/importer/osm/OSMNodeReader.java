package org.routing.importer.osm;

import crosby.binary.Osmformat;

import java.util.List;
import java.util.Map;

public class OSMNodeReader extends AbstractOSMParser {

    private final NodeStore nodeStore;

    public OSMNodeReader(NodeStore nodeStore) {
        this.nodeStore = nodeStore;
    }

    @Override
    protected void parseRelations(List<Osmformat.Relation> list) {
        //do nothing
    }

    @Override
    protected void parseDense(Osmformat.DenseNodes denseNodes) {
    }

    @Override
    protected void parseNodes(List<Osmformat.Node> list) {
        // do nothing
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

                    nodeStore.save(nodeId);

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
        System.out.printf("""
                ------------------------------------------------
                Completed reading nodes from OSM PBF
                ------------------------------------------------
                %n""");
        nodeStore.close();
    }
    
}

