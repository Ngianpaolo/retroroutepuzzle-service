package io.nigro.retroroutepuzzle.feature.dfs;

import io.nigro.retroroutepuzzle.exception.RoomNotFoundException;
import io.nigro.retroroutepuzzle.feature.dfs.model.RoomNode;
import io.nigro.retroroutepuzzle.feature.roommap.model.Item;
import io.nigro.retroroutepuzzle.feature.roommap.model.Room;
import io.nigro.retroroutepuzzle.feature.route.contract.RouteEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

@Slf4j
public class RoomDFS {

    private final List<Room> rooms;

    private Stack<RoomNode> roomNodeRouteStack;

    private List<RouteEvent> routeEvent;

    public RoomDFS(List<Room> rooms) {
        this.rooms = rooms;
    }

    public List<RouteEvent> calculateRoomRoute(Long roomRootId, List<String> itemsToCollect) {
        roomNodeRouteStack = new Stack<>();
        routeEvent = new ArrayList<>();

        Map<Long, RoomNode> roomNodeMap = getRoomNodeMap();
        RoomNode roomNodeRoot = roomNodeMap.get(roomRootId);

        if(roomNodeRoot == null) {
            throw new RoomNotFoundException();
        }
        calculateDFSRouteEvents(roomNodeRoot, itemsToCollect);

        return routeEvent;
    }

    private void calculateDFSRouteEvents(RoomNode visitedRoomNode, List<String> itemsToCollect) {
        // No objects to look for
        if (itemsToCollect.isEmpty()) {
            return;
        }
        // If stack is empty means it's first recursion, so push rootNode in stack
        // If last element of stack is not equals to current node means that DFS is going to the child nodes, so push new visited node in stack
        if (roomNodeRouteStack.isEmpty() || !roomNodeRouteStack.peek().equals(visitedRoomNode)) {
            roomNodeRouteStack.push(visitedRoomNode);
        }

        visitedRoomNode.setVisited(true);

        var roomNodeObjects = Optional.ofNullable(visitedRoomNode.getObjects())
                .orElse(List.of())
                .stream()
                .map(Item::getName)
                .collect(Collectors.toSet());

        var itemsFound = itemsToCollect.stream()
                .filter(roomNodeObjects::contains)
                .collect(Collectors.toSet());

        itemsToCollect.removeAll(itemsFound);
        addRouteEvent(visitedRoomNode, itemsFound);

        // No other objects to look for
        if (itemsToCollect.isEmpty()) {
            log.info("All objects have been found!");
            return;
        }

        // Find adjacent room not yet visited
        Optional<RoomNode> nextRoomNodeToVisit = visitedRoomNode.getAdjacentRoomNodes()
                .stream()
                .filter(roomNode -> !roomNode.isVisited())
                .findAny();

        if (nextRoomNodeToVisit.isPresent()) {
            calculateDFSRouteEvents(nextRoomNodeToVisit.get(), itemsToCollect);
        } else {
            // No new room to visit, so go back
            roomNodeRouteStack.pop();
            if (!roomNodeRouteStack.isEmpty()) {
                calculateDFSRouteEvents(roomNodeRouteStack.peek(), itemsToCollect);
            } else {
                // If the roomNodeRouteStack is empty and all adjacent rooms are yet visited
                // then it means that there are no other rooms to look for
                // then the search is finished
                log.info("No other nodes to visit, these objects could not be found {}", itemsToCollect);
            }
        }
    }

    private void addRouteEvent(RoomNode visitedRoomNode, Set<String> itemsFound) {
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

    private Map<Long, RoomNode> getRoomNodeMap() {
        Map<Long, RoomNode> roomNodeMap = rooms.stream()
                .map(RoomNode::from)
                .collect(Collectors.toMap(RoomNode::getId, roomNode -> roomNode));

        for (var roomId : roomNodeMap.keySet()) {
            roomNodeMap.get(roomId).setAdjacentRoomNodes(roomNodeMap);
        }
        return roomNodeMap;
    }

}
