package org.routing.storage.serializer;

import org.jetbrains.annotations.NotNull;
import org.mapdb.DataInput2;
import org.mapdb.DataOutput2;
import org.mapdb.Serializer;
import org.routing.model.Edge;
import org.routing.model.Node;

import java.io.IOException;

public class EdgeSerializer implements Serializer<Edge> {

    private final NodeSerializer nodeSerializer;

    public EdgeSerializer(NodeSerializer nodeSerializer) {
        this.nodeSerializer = nodeSerializer;
    }

    @Override
    public void serialize(@NotNull DataOutput2 out, @NotNull Edge edge) throws IOException {
        long id = edge.getId();
        double cost = edge.getCost();
        Node nodeFrom = edge.getFrom();
        Node nodeTo = edge.getTo();
        out.writeLong(id);
        out.writeDouble(cost);
        nodeSerializer.serialize(out, nodeFrom);
        nodeSerializer.serialize(out, nodeTo);
    }

    @Override
    public Edge deserialize(@NotNull DataInput2 input, int available) throws IOException {
        long id = input.readLong();
        double cost = input.readDouble();
        Node from = nodeSerializer.deserialize(input, available);
        Node to = nodeSerializer.deserialize(input, available);
        return new Edge(id, from, to, cost);
    }
}
