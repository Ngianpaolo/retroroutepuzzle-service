package io.nigro.retroroutepuzzle.feature.search.bfs;

import io.nigro.retroroutepuzzle.exception.RoomNotFoundException;
import io.nigro.retroroutepuzzle.feature.roommap.model.Item;
import io.nigro.retroroutepuzzle.feature.roommap.model.Room;
import io.nigro.retroroutepuzzle.feature.search.RoomTreeSearch;
import io.nigro.retroroutepuzzle.feature.search.model.RoomNode;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * Class that implements the Breadth-first search on a room graph
 */
@Slf4j
public class RoomBfsTreeSearch extends RoomTreeSearch {

    private Stack<RoomNode> roomNodeRouteStack;

    private Map<Integer, Set<RoomNode>> levelMap;

    private RoomNode lastVisitedForwardRoomNode;

    private Map<Long, Boolean> roomsNodeHaveBranchVisited;

    public RoomBfsTreeSearch(List<Room> rooms) {
        super(rooms);
    }

    public RoomNode initializeRoomGraph(Long roomRootId) {
        roomNodeRouteStack = new Stack<>();
        levelMap = new HashMap<>();

        Map<Long, RoomNode> roomNodeMap = getRoomNodeMap();
        RoomNode roomNodeRoot = roomNodeMap.get(roomRootId);
        visitedRooms = roomNodeMap.keySet().stream()
                .collect(Collectors.toMap(k -> k, v -> false));
        roomsNodeHaveBranchVisited = roomNodeMap.keySet().stream()
                .collect(Collectors.toMap(k -> k, v -> false));

        if (roomNodeRoot == null) {
            throw new RoomNotFoundException();
        }

        return roomNodeRoot;
    }


    public void calculateRoomRoute(RoomNode roomNode, List<String> itemsToCollect) {
        calculateBFSRouteEvents(roomNode, itemsToCollect);
    }

    /**
     * This method implements the logic of Breadth-first search recursively
     * @param visitedRoomNode actual room visited (The first room node is the root)
     * @param itemsToCollect items to search for
     */
    private void calculateBFSRouteEvents(RoomNode visitedRoomNode, List<String> itemsToCollect) {
        // No objects to look for
        if (itemsToCollect.isEmpty() || allRoomsAreVisited()) {
            return;
        }
        // If stack is empty means it's first recursion, so push rootNode in stack
        // If last element of stack is not equals to current node means that BFS is going to the child nodes, so push new visited node in stack
        if (roomNodeRouteStack.isEmpty() || !roomNodeRouteStack.peek().equals(visitedRoomNode)) {
            roomNodeRouteStack.push(visitedRoomNode);
        }

        //roomToVisitDeque.remove(visitedRoomNode);
        var actualLevelSet = levelMap.getOrDefault(roomNodeRouteStack.size(), new HashSet<>());
        actualLevelSet.add(visitedRoomNode);
        levelMap.put(roomNodeRouteStack.size(), actualLevelSet);

        // Find adjacent room not yet visited
        Set<RoomNode> prevLevel = levelMap.getOrDefault(roomNodeRouteStack.size() - 1, new HashSet<>());
        Set<RoomNode> nextRoomNodeToVisit = visitedRoomNode.getAdjacentRoomNodes()
                .stream()
                .filter(roomNode -> !roomsNodeHaveBranchVisited.get(roomNode.getId()) && !prevLevel.contains(roomNode))
                .collect(Collectors.toSet());

        var nextLevelSet = levelMap.getOrDefault(roomNodeRouteStack.size() + 1, new HashSet<>());
        nextLevelSet.addAll(nextRoomNodeToVisit);
        levelMap.put(roomNodeRouteStack.size() + 1, nextLevelSet);

        visitedRooms.put(visitedRoomNode.getId(), true);
        // Set the flag only if it has no other adjacent rooms to visit
        if (nextRoomNodeToVisit.isEmpty())
            roomsNodeHaveBranchVisited.put(visitedRoomNode.getId(), true);

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

        var siblingsNotVisited = levelMap.get(roomNodeRouteStack.size()).stream()
                .filter(roomNode -> !visitedRooms.get(roomNode.getId()))
                .collect(Collectors.toSet());

        var siblingsLinkedToVisit = siblingsNotVisited.stream()
                .filter(nextRoomNodeToVisit::contains)
                .collect(Collectors.toSet());

        if (!siblingsNotVisited.isEmpty()) {
            var siblingLinkedToVisit = siblingsLinkedToVisit.stream().findAny();
            if (siblingLinkedToVisit.isPresent()) {
                calculateBFSRouteEvents(siblingLinkedToVisit.get(), itemsToCollect);
            } else {
                lastVisitedForwardRoomNode = roomNodeRouteStack.pop();
                calculateBFSRouteEvents(roomNodeRouteStack.peek(), itemsToCollect);
            }
            return;
        }

        var childrenNotVisited = levelMap.keySet().stream()
                .filter(level -> level > roomNodeRouteStack.size())
                .map(level -> levelMap.get(level))
                .flatMap(Collection::stream)
                .filter(roomNode -> !visitedRooms.get(roomNode.getId()))
                .collect(Collectors.toSet());

        var childrenLinkedToVisit = childrenNotVisited.stream()
                .filter(nextRoomNodeToVisit::contains)
                .collect(Collectors.toSet());

        if (!childrenNotVisited.isEmpty()) {
            var childLinkedToVisit = childrenLinkedToVisit.stream().findAny();
            if (childLinkedToVisit.isPresent()) {
                calculateBFSRouteEvents(childLinkedToVisit.get(), itemsToCollect);
            } else if (roomsNodeHaveBranchVisited.get(visitedRoomNode.getId())) {
                lastVisitedForwardRoomNode = roomNodeRouteStack.pop();
                calculateBFSRouteEvents(roomNodeRouteStack.peek(), itemsToCollect);
            } else {
                nextRoomNodeToVisit.stream()
                        // Avoid loop in cyclic graph
                        .filter(roomNode -> !roomNode.equals(lastVisitedForwardRoomNode))
                        .findAny()
                        .ifPresentOrElse(roomNode -> calculateBFSRouteEvents(roomNode, itemsToCollect), () ->
                                {
                                    lastVisitedForwardRoomNode = roomNodeRouteStack.pop();
                                    calculateBFSRouteEvents(roomNodeRouteStack.peek(), itemsToCollect);
                                }
                        );
            }
            return;
        }
        // If the roomNodeRouteStack is empty and all adjacent rooms are yet visited
        // then it means that there are no other rooms to look for
        // then the search is finished
        log.info("No other nodes to visit, these objects could not be found {}", itemsToCollect);
    }

}
