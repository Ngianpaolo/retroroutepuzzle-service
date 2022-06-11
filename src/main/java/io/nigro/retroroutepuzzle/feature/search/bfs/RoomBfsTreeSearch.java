package io.nigro.retroroutepuzzle.feature.search.bfs;

import io.nigro.retroroutepuzzle.exception.RoomNotFoundException;
import io.nigro.retroroutepuzzle.feature.roommap.model.Item;
import io.nigro.retroroutepuzzle.feature.roommap.model.Room;
import io.nigro.retroroutepuzzle.feature.route.contract.RouteEvent;
import io.nigro.retroroutepuzzle.feature.search.RoomTreeSearch;
import io.nigro.retroroutepuzzle.feature.search.model.RoomNode;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

@Slf4j
public class RoomBfsTreeSearch extends RoomTreeSearch {

    private Stack<RoomNode> roomNodeRouteStack;

    private Deque<RoomNode> roomToVisitDeque;

    private Map<Integer, Set<RoomNode>> levelMap;

    public RoomBfsTreeSearch(List<Room> rooms) {
        super(rooms);
    }

    public List<RouteEvent> calculateRoomRoute(Long roomRootId, List<String> itemsToCollect) {
        roomNodeRouteStack = new Stack<>();
        routeEvent = new ArrayList<>();
        roomToVisitDeque = new ArrayDeque<>();
        levelMap = new HashMap<>();

        Map<Long, RoomNode> roomNodeMap = getRoomNodeMap();
        RoomNode roomNodeRoot = roomNodeMap.get(roomRootId);

        if (roomNodeRoot == null) {
            throw new RoomNotFoundException();
        }

        roomToVisitDeque.addFirst(roomNodeRoot);

        calculateBFSRouteEvents(roomNodeRoot, itemsToCollect);

        return routeEvent;
    }


    private void calculateBFSRouteEvents(RoomNode visitedRoomNode, List<String> itemsToCollect) {
        // No objects to look for
        if (itemsToCollect.isEmpty()) {
            return;
        }
        // If stack is empty means it's first recursion, so push rootNode in stack
        // If last element of stack is not equals to current node means that DFS is going to the child nodes, so push new visited node in stack
        if (roomNodeRouteStack.isEmpty() || !roomNodeRouteStack.peek().equals(visitedRoomNode)) {
            roomNodeRouteStack.push(visitedRoomNode);
        }

        roomToVisitDeque.remove(visitedRoomNode);

        var actualLevelSet = levelMap.getOrDefault(roomNodeRouteStack.size(), new HashSet<>());
        actualLevelSet.add(visitedRoomNode);
        levelMap.put(roomNodeRouteStack.size(), actualLevelSet);

        // Find adjacent room not yet visited
        Set<RoomNode> prevLevel = levelMap.getOrDefault(roomNodeRouteStack.size() - 1, new HashSet<>());
        List<RoomNode> nextRoomNodeToVisit = visitedRoomNode.getAdjacentRoomNodes()
                .stream()
                .filter(roomNode -> !roomNode.isVisited() && !prevLevel.contains(roomNode))
                .collect(Collectors.toList());

        var nextLevelSet = levelMap.getOrDefault(roomNodeRouteStack.size() + 1, new HashSet<>());
        nextLevelSet.addAll(nextRoomNodeToVisit);
        levelMap.put(roomNodeRouteStack.size() + 1, nextLevelSet);

        if (nextRoomNodeToVisit.isEmpty())
            visitedRoomNode.setVisited(true);

        var newRoomNodeToPutInQueue = new ArrayList<>(nextRoomNodeToVisit);
        newRoomNodeToPutInQueue.removeAll(roomToVisitDeque);
        roomToVisitDeque.addAll(newRoomNodeToPutInQueue);

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

        if (!roomToVisitDeque.isEmpty() && nextRoomNodeToVisit.contains(roomToVisitDeque.peekFirst())) {
            calculateBFSRouteEvents(roomToVisitDeque.peekFirst(), itemsToCollect);
        } else {
            // No new room to visit, so go back

            if (!roomToVisitDeque.isEmpty()) {
                if (roomNodeRouteStack.size() > 1) {
                    roomNodeRouteStack.pop();
                    calculateBFSRouteEvents(roomNodeRouteStack.peek(), itemsToCollect);
                } else {
                    if (!nextRoomNodeToVisit.isEmpty())
                        calculateBFSRouteEvents(nextRoomNodeToVisit.get(0), itemsToCollect);
                }
            } else {
                // If the roomNodeRouteStack is empty and all adjacent rooms are yet visited
                // then it means that there are no other rooms to look for
                // then the search is finished
                log.info("No other nodes to visit, these objects could not be found {}", itemsToCollect);
            }
        }
    }

}
