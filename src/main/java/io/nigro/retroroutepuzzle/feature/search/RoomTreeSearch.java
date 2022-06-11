package io.nigro.retroroutepuzzle.feature.search;

import io.nigro.retroroutepuzzle.feature.roommap.model.Room;
import io.nigro.retroroutepuzzle.feature.route.contract.RouteEvent;
import io.nigro.retroroutepuzzle.feature.search.model.RoomNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class RoomTreeSearch {

    protected final List<Room> rooms;

    protected List<RouteEvent> routeEvent;

    protected Map<Long, Boolean> visitedRooms;

    protected RoomTreeSearch(List<Room> rooms) {
        this.rooms = rooms;
    }

    public List<RouteEvent> getRouteByRoomRootAndFindItems(Long roomRootId, List<String> itemsToCollect) {
        initializeDataStructures();
        var roomNodeRoot = initializeRoomGraph(roomRootId);
        calculateRoomRoute(roomNodeRoot, itemsToCollect);
        return routeEvent;
    }

    protected void initializeDataStructures() {
        routeEvent = new ArrayList<>();
    }

    abstract protected RoomNode initializeRoomGraph(Long roomRootId);

    abstract protected void calculateRoomRoute(RoomNode roomNode, List<String> itemsToCollect);

    protected void addRouteEvent(RoomNode visitedRoomNode, Set<String> itemsFound) {
        String objectCollected = "None";
        if (itemsFound != null && !itemsFound.isEmpty()) {
            objectCollected = itemsFound.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));
        }

        routeEvent.add(RouteEvent.builder()
                .id(visitedRoomNode.getId())
                .room(visitedRoomNode.getName())
                .objectCollected(objectCollected)
                .build());
    }

    protected Map<Long, RoomNode> getRoomNodeMap() {
        Map<Long, RoomNode> roomNodeMap = rooms.stream()
                .map(RoomNode::from)
                .collect(Collectors.toMap(RoomNode::getId, roomNode -> roomNode));

        for (var roomId : roomNodeMap.keySet()) {
            roomNodeMap.get(roomId).setAdjacentRoomNodes(roomNodeMap);
        }
        return roomNodeMap;
    }

    protected boolean allRoomsAreVisited() {
        return !visitedRooms.isEmpty() && visitedRooms.values().stream().allMatch(x -> x);
    }

}
