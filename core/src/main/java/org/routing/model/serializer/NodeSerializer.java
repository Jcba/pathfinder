package org.routing.model.serializer;

import org.jetbrains.annotations.NotNull;
import org.mapdb.DataInput2;
import org.mapdb.DataOutput2;
import org.mapdb.Serializer;
import org.routing.geometries.Point;
import org.routing.model.Node;

import java.io.IOException;

public class NodeSerializer implements Serializer<Node> {

    @Override
    public void serialize(@NotNull DataOutput2 dataOutput2, @NotNull Node node) throws IOException {
        long id = node.getId();
        Point coordinate = node.getCoordinate();

        dataOutput2.writeLong(id);
        dataOutput2.writeDouble(coordinate.getLat());
        dataOutput2.writeDouble(coordinate.getLon());
    }

    @Override
    public Node deserialize(@NotNull DataInput2 dataInput2, int availableBytes) throws IOException {
        long id = dataInput2.readLong();
        double lat = dataInput2.readDouble();
        double lon = dataInput2.readDouble();

        return new Node(id, new Point(lat, lon));
    }
}
