package io.nigro.retroroutepuzzle.feature.search.dfs;

import io.nigro.retroroutepuzzle.exception.RoomNotFoundException;
import io.nigro.retroroutepuzzle.feature.roommap.model.Item;
import io.nigro.retroroutepuzzle.feature.roommap.model.Room;
import io.nigro.retroroutepuzzle.feature.search.RoomTreeSearch;
import io.nigro.retroroutepuzzle.feature.search.model.RoomNode;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;
import java.util.stream.Collectors;

@Slf4j
public class RoomDfsTreeSearch extends RoomTreeSearch {

    private Stack<RoomNode> roomNodeRouteStack;

    public RoomDfsTreeSearch(List<Room> rooms) {
        super(rooms);
    }

    public void calculateRoomRoute(RoomNode roomNode, List<String> itemsToCollect) {
        calculateDFSRouteEvents(roomNode, itemsToCollect);
    }

    public RoomNode initializeRoomGraph(Long roomRootId) {
        roomNodeRouteStack = new Stack<>();

        Map<Long, RoomNode> roomNodeMap = getRoomNodeMap();
        RoomNode roomNodeRoot = roomNodeMap.get(roomRootId);
        visitedRooms = roomNodeMap.keySet().stream()
                .collect(Collectors.toMap(k -> k, v -> false));

        if (roomNodeRoot == null) {
            throw new RoomNotFoundException();
        }
        return roomNodeRoot;
    }

    private void calculateDFSRouteEvents(RoomNode visitedRoomNode, List<String> itemsToCollect) {
        // No objects to look for
        if (itemsToCollect.isEmpty() || allRoomsAreVisited()) {
            return;
        }
        // If stack is empty means it's first recursion, so push rootNode in stack
        // If last element of stack is not equals to current node means that DFS is going to the child nodes, so push new visited node in stack
        if (roomNodeRouteStack.isEmpty() || !roomNodeRouteStack.peek().equals(visitedRoomNode)) {
            roomNodeRouteStack.push(visitedRoomNode);
        }

        visitedRooms.put(visitedRoomNode.getId(), true);

        var roomNodeObjects = Optional.ofNullable(visitedRoomNode.getObjects())
                .orElse(List.of())
                .stream()
                .map(Item::getName)
                .collect(Collectors.toSet());

        var itemsFound = itemsToCollect.stream()
                .filter(item -> roomNodeObjects.stream().anyMatch(item::equalsIgnoreCase))
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
                .filter(roomNode -> !visitedRooms.get(roomNode.getId()))
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

}
