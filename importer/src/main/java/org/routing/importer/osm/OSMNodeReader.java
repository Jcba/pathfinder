package org.routing.importer.osm;

import crosby.binary.Osmformat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class OSMNodeReader extends AbstractOSMParser {

    private final static Logger log = LoggerFactory.getLogger(OSMNodeReader.class);

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

                    nodeStore.saveId(nodeId);

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
        log.info("""
                ------------------------------------------------
                Completed reading nodes from OSM PBF
                ------------------------------------------------
                %n""");
        nodeStore.close();
    }

}

