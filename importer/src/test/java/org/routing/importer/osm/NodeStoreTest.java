package org.routing.importer.osm;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class NodeStoreTest {

    @Test
    void saveNodeId_shouldBeInNodeStore() throws IOException {
        NodeStore fixture = new NodeStore();

        fixture.saveId(2L);

        List<NodeStore.Node> all = fixture.getAll(List.of(2L));

        assertThat(all).extracting(NodeStore.Node::id).contains(2L);

        fixture.destroy();
    }

    @Test
    void updateNode_shouldBeInNodeStore() throws IOException {
        NodeStore fixture = new NodeStore();

        fixture.saveId(1L);
        fixture.update(1L, 1.0, 2.0);

        List<NodeStore.Node> all = fixture.getAll(List.of(1L));

        assertThat(all).extracting(NodeStore.Node::id).contains(1L);
        assertThat(all).extracting(NodeStore.Node::lat).contains(1.0F);
        assertThat(all).extracting(NodeStore.Node::lon).contains(2.0F);

        fixture.destroy();
    }
}