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
        fixture.update(1L, 1.0, 2.0);

        List<NodeStore.Node> all = fixture.getAll(List.of(1L));

        assertThat(all).extracting(NodeStore.Node::id).contains(1L);
        assertThat(all).extracting(NodeStore.Node::lat).contains(1.0F);
        assertThat(all).extracting(NodeStore.Node::lon).contains(2.0F);
    }
}