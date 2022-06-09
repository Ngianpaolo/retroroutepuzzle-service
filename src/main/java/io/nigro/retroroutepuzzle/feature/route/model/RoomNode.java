package io.nigro.retroroutepuzzle.feature.route.model;

import io.nigro.retroroutepuzzle.feature.roommap.model.Item;
import io.nigro.retroroutepuzzle.feature.roommap.model.Room;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
public class RoomNode {

    private Long id;

    private String name;

    private List<Item> objects;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Long> adjacentRoomNodeIds;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<RoomNode> adjacentRoomNodes;

    private boolean visited;

    public void setAdjacentRoomNodes(Map<Long, RoomNode> adjacentRoomNodeMaps) {
        this.adjacentRoomNodes = adjacentRoomNodeIds.stream()
                .map(adjacentRoomNodeMaps::get)
                .collect(Collectors.toSet());
    }

    public static RoomNode from(Room room) {
        var adjacentRoomNodeIds = new HashSet<Long>();

        if (room.getNorth() != null)
            adjacentRoomNodeIds.add(room.getNorth());
        if (room.getSouth() != null)
            adjacentRoomNodeIds.add(room.getSouth());
        if (room.getEast() != null)
            adjacentRoomNodeIds.add(room.getEast());
        if (room.getWest() != null)
            adjacentRoomNodeIds.add(room.getWest());

        return RoomNode.builder()
                .id(room.getId())
                .name(room.getName())
                .objects(room.getObjects())
                .adjacentRoomNodeIds(adjacentRoomNodeIds)
                .build();
    }

}
