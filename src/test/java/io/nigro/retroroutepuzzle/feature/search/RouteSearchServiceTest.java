package io.nigro.retroroutepuzzle.feature.search;

import io.nigro.retroroutepuzzle.feature.route.model.RouteEvent;
import io.nigro.retroroutepuzzle.utils.JsonFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class RouteSearchServiceTest {

    private final static RouteSearchService routeSearchService = new RouteSearchService();

    @Test
    void getRouteByRoomRootAndFindItems_BFS_should_be_work_withRoomMap2() {
        var roomMapContract = JsonFileUtil.loadMapFromJsonFile("RoomMap2");

        var rootRoomId = 4L;
        var itemsToCollect = new ArrayList<String>();
        itemsToCollect.add("Knife");
        itemsToCollect.add("Potted Plant");
        itemsToCollect.add("Pillow");

        var result = routeSearchService.getRouteByRoomRootAndItemsToFind(roomMapContract.getRooms(), rootRoomId, itemsToCollect, RoomTreeSearchType.BFS);

        assertEquals(RouteEvent.builder().id(rootRoomId).room("Sun Room").objectCollected("None").build(), result.get(0));
        assertEquals(RouteEvent.builder().id(2L).room("Dining Room").objectCollected("None").build(), result.get(1));
        assertEquals(RouteEvent.builder().id(rootRoomId).room("Sun Room").objectCollected("None").build(), result.get(2));
        assertEquals(RouteEvent.builder().id(6L).room("Bathroom").objectCollected("None").build(), result.get(3));
        assertEquals(RouteEvent.builder().id(rootRoomId).room("Sun Room").objectCollected("None").build(), result.get(4));
        assertEquals(RouteEvent.builder().id(7L).room("Living room").objectCollected("Potted Plant").build(), result.get(5));
        assertEquals(RouteEvent.builder().id(1L).room("Hallway").objectCollected("None").build(), result.get(6));
        assertEquals(RouteEvent.builder().id(7L).room("Living room").objectCollected("None").build(), result.get(7));
        assertEquals(RouteEvent.builder().id(rootRoomId).room("Sun Room").objectCollected("None").build(), result.get(8));
        assertEquals(RouteEvent.builder().id(2L).room("Dining Room").objectCollected("None").build(), result.get(9));
        assertEquals(RouteEvent.builder().id(3L).room("Kitchen").objectCollected("Knife").build(), result.get(10));
        assertEquals(RouteEvent.builder().id(2L).room("Dining Room").objectCollected("None").build(), result.get(11));
        assertEquals(RouteEvent.builder().id(5L).room("Bedroom").objectCollected("Pillow").build(), result.get(12));
    }

    @Test
    void getRouteByRoomRootAndFindItems_DFS_should_be_work_withRoomMap2() {
        var roomMapContract = JsonFileUtil.loadMapFromJsonFile("RoomMap2");

        var rootRoomId = 4L;
        var itemsToCollect = new ArrayList<String>();
        itemsToCollect.add("Knife");
        itemsToCollect.add("Potted Plant");
        itemsToCollect.add("Pillow");

        var result = routeSearchService.getRouteByRoomRootAndItemsToFind(roomMapContract.getRooms(), rootRoomId, itemsToCollect, RoomTreeSearchType.DFS);

        assertEquals(RouteEvent.builder().id(rootRoomId).room("Sun Room").objectCollected("None").build(), result.get(0));
        assertEquals(RouteEvent.builder().id(2L).room("Dining Room").objectCollected("None").build(), result.get(1));
        assertEquals(RouteEvent.builder().id(1L).room("Hallway").objectCollected("None").build(), result.get(2));
        assertEquals(RouteEvent.builder().id(7L).room("Living room").objectCollected("Potted Plant").build(), result.get(3));
        assertEquals(RouteEvent.builder().id(1L).room("Hallway").objectCollected("None").build(), result.get(4));
        assertEquals(RouteEvent.builder().id(2L).room("Dining Room").objectCollected("None").build(), result.get(5));
        assertEquals(RouteEvent.builder().id(3L).room("Kitchen").objectCollected("Knife").build(), result.get(6));
        assertEquals(RouteEvent.builder().id(2L).room("Dining Room").objectCollected("None").build(), result.get(7));
        assertEquals(RouteEvent.builder().id(5L).room("Bedroom").objectCollected("Pillow").build(), result.get(8));
    }

    @Test
    void getRouteByRoomRootAndFindItems_BFS_should_be_ignore_case_tolerant() {
        var roomMapContract = JsonFileUtil.loadMapFromJsonFile("RoomMap2");

        var rootRoomId = 4L;
        var itemsToCollect = new ArrayList<String>();
        itemsToCollect.add("knife");
        itemsToCollect.add("potted Plant");
        itemsToCollect.add("PIllow");

        var result = routeSearchService.getRouteByRoomRootAndItemsToFind(roomMapContract.getRooms(), rootRoomId, itemsToCollect, RoomTreeSearchType.BFS);

        assertEquals(RouteEvent.builder().id(rootRoomId).room("Sun Room").objectCollected("None").build(), result.get(0));
        assertEquals(RouteEvent.builder().id(2L).room("Dining Room").objectCollected("None").build(), result.get(1));
        assertEquals(RouteEvent.builder().id(rootRoomId).room("Sun Room").objectCollected("None").build(), result.get(2));
        assertEquals(RouteEvent.builder().id(6L).room("Bathroom").objectCollected("None").build(), result.get(3));
        assertEquals(RouteEvent.builder().id(rootRoomId).room("Sun Room").objectCollected("None").build(), result.get(4));
        assertEquals(RouteEvent.builder().id(7L).room("Living room").objectCollected("Potted Plant").build(), result.get(5));
        assertEquals(RouteEvent.builder().id(1L).room("Hallway").objectCollected("None").build(), result.get(6));
        assertEquals(RouteEvent.builder().id(7L).room("Living room").objectCollected("None").build(), result.get(7));
        assertEquals(RouteEvent.builder().id(rootRoomId).room("Sun Room").objectCollected("None").build(), result.get(8));
        assertEquals(RouteEvent.builder().id(2L).room("Dining Room").objectCollected("None").build(), result.get(9));
        assertEquals(RouteEvent.builder().id(3L).room("Kitchen").objectCollected("Knife").build(), result.get(10));
        assertEquals(RouteEvent.builder().id(2L).room("Dining Room").objectCollected("None").build(), result.get(11));
        assertEquals(RouteEvent.builder().id(5L).room("Bedroom").objectCollected("Pillow").build(), result.get(12));
    }


    @Test
    void getRouteByRoomRootAndFindItems_DFS_should_be_ignore_case_tolerant() {
        var roomMapContract = JsonFileUtil.loadMapFromJsonFile("RoomMap2");

        var rootRoomId = 4L;
        var itemsToCollect = new ArrayList<String>();
        itemsToCollect.add("knifE");
        itemsToCollect.add("potteD Plant");
        itemsToCollect.add("pillow");

        var result = routeSearchService.getRouteByRoomRootAndItemsToFind(roomMapContract.getRooms(), rootRoomId, itemsToCollect, RoomTreeSearchType.DFS);

        assertEquals(RouteEvent.builder().id(rootRoomId).room("Sun Room").objectCollected("None").build(), result.get(0));
        assertEquals(RouteEvent.builder().id(2L).room("Dining Room").objectCollected("None").build(), result.get(1));
        assertEquals(RouteEvent.builder().id(1L).room("Hallway").objectCollected("None").build(), result.get(2));
        assertEquals(RouteEvent.builder().id(7L).room("Living room").objectCollected("Potted Plant").build(), result.get(3));
        assertEquals(RouteEvent.builder().id(1L).room("Hallway").objectCollected("None").build(), result.get(4));
        assertEquals(RouteEvent.builder().id(2L).room("Dining Room").objectCollected("None").build(), result.get(5));
        assertEquals(RouteEvent.builder().id(3L).room("Kitchen").objectCollected("Knife").build(), result.get(6));
        assertEquals(RouteEvent.builder().id(2L).room("Dining Room").objectCollected("None").build(), result.get(7));
        assertEquals(RouteEvent.builder().id(5L).room("Bedroom").objectCollected("Pillow").build(), result.get(8));
    }


}
