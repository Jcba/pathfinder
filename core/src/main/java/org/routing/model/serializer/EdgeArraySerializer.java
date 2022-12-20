package org.routing.model.serializer;

import org.jetbrains.annotations.NotNull;
import org.mapdb.DataInput2;
import org.mapdb.DataOutput2;
import org.mapdb.Serializer;
import org.routing.model.Edge;
import org.routing.model.Node;

import java.io.IOException;

public class EdgeArraySerializer implements Serializer<Edge[]> {

    private final NodeSerializer nodeSerializer;

    public EdgeArraySerializer(NodeSerializer nodeSerializer) {
        this.nodeSerializer = nodeSerializer;
    }

    @Override
    public void serialize(@NotNull DataOutput2 out, @NotNull Edge[] edges) throws IOException {
        out.writeShort(edges.length);
        for (Edge edge : edges) {
            long id = edge.getId();
            double cost = edge.getCost();
            Node nodeFrom = edge.getFrom();
            Node nodeTo = edge.getTo();
            out.writeLong(id);
            out.writeDouble(cost);
            nodeSerializer.serialize(out, nodeFrom);
            nodeSerializer.serialize(out, nodeTo);
        }
    }

    @Override
    public Edge[] deserialize(@NotNull DataInput2 input, int available) throws IOException {
        short nrEdges = input.readShort();
        Edge[] result = new Edge[nrEdges];
        for (int i = 0; i < nrEdges; i++) {
            long id = input.readLong();
            double cost = input.readDouble();
            Node from = nodeSerializer.deserialize(input, available);
            Node to = nodeSerializer.deserialize(input, available);
            result[i] = new Edge(id, from, to, cost);
        }
        return result;
    }
}
