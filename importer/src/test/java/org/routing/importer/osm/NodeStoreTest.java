package org.routing.importer.osm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class NodeStoreTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void saveNodeId_shouldBeInNodeStore() {
        NodeStore fixture = new NodeStore();

        fixture.saveId(1L);

        List<NodeStore.Node> all = fixture.getAll(List.of(1L));

        assertThat(all).extracting(NodeStore.Node::id).contains(1L);
    }

    @Test
    void updateNode_shouldBeInNodeStore() {
        NodeStore fixture = new NodeStore();

        fixture.saveId(1L);

        List<NodeStore.Node> all = fixture.getAll(List.of(1L));

        assertThat(all).extracting(NodeStore.Node::id).contains(1L);
    }
}