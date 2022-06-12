package io.nigro.retroroutepuzzle.feature.search.bdf;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nigro.retroroutepuzzle.exception.RoomNotFoundException;
import io.nigro.retroroutepuzzle.feature.roommap.model.Item;
import io.nigro.retroroutepuzzle.feature.roommap.model.Room;
import io.nigro.retroroutepuzzle.feature.route.model.RouteEvent;
import io.nigro.retroroutepuzzle.feature.search.RoomTreeSearch;
import io.nigro.retroroutepuzzle.feature.search.bfs.RoomBfsTreeSearch;
import io.nigro.retroroutepuzzle.utils.JsonFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
public class RoomBfsTreeSearchTest {

    private final static ObjectMapper objectMapper = new ObjectMapper();
    private final static String path = "./src/test/resources/";

    @Test
    void getRouteByRoomRootAndFindItems_should_be_work_withRoomMap1() {
        var roomMapContract = JsonFileUtil.loadMapFromJsonFile("RoomMap1");
        var roomBFS = new RoomBfsTreeSearch(roomMapContract.getRooms());

        var rootRoomId = 2L;
        var itemsToCollect = new ArrayList<String>();
        itemsToCollect.add("Knife");
        itemsToCollect.add("Potted Plant");

        var result = roomBFS.getRouteByRoomRootAndItemsToFind(rootRoomId, itemsToCollect);

        assertEquals(RouteEvent.builder().id(rootRoomId).room("Dining Room").objectCollected("None").build(), result.get(0));
        assertEquals(RouteEvent.builder().id(1L).room("Hallway").objectCollected("None").build(), result.get(1));
        assertEquals(RouteEvent.builder().id(rootRoomId).room("Dining Room").objectCollected("None").build(), result.get(2));
        assertEquals(RouteEvent.builder().id(4L).room("Sun Room").objectCollected("Potted Plant").build(), result.get(3));
        assertEquals(RouteEvent.builder().id(rootRoomId).room("Dining Room").objectCollected("None").build(), result.get(4));
        assertEquals(RouteEvent.builder().id(3L).room("Kitchen").objectCollected("Knife").build(), result.get(5));
    }

    @Test
    void getRouteByRoomRootAndFindItems_should_be_work_withRoomMap2() {
        var roomMapContract = JsonFileUtil.loadMapFromJsonFile("RoomMap2");
        var roomBFS = new RoomBfsTreeSearch(roomMapContract.getRooms());

        var rootRoomId = 4L;
        var itemsToCollect = new ArrayList<String>();
        itemsToCollect.add("Knife");
        itemsToCollect.add("Potted Plant");
        itemsToCollect.add("Pillow");

        var result = roomBFS.getRouteByRoomRootAndItemsToFind(rootRoomId, itemsToCollect);

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
    void getRouteByRoomRootAndFindItems_should_throw_RoomNotFoundException() {
        RoomTreeSearch roomBDF = new RoomBfsTreeSearch(List.of(
                Room.builder().id(1L).name("Sun Room").north(2L).build(),
                Room.builder().id(2L).name("Living room").south(1L).build()
        ));

        var notExistingRootRoomId = 4L;
        var itemsToCollect = new ArrayList<String>();
        itemsToCollect.add("Knife");

        assertThrows(RoomNotFoundException.class, () -> roomBDF.getRouteByRoomRootAndItemsToFind(notExistingRootRoomId, itemsToCollect));

    }

    @Test
    void getRouteByRoomRootAndFindItems_should_be_tolerant_with_not_existing_item() {
        RoomTreeSearch roomBDF = new RoomBfsTreeSearch(List.of(
                Room.builder().id(1L).name("Room1").south(2L).east(4L).build(),
                Room.builder().id(2L).name("Room2").north(1L).east(3L).build(),
                Room.builder().id(3L).name("Room3").north(4L).west(2L).build(),
                Room.builder().id(4L).name("Room4").south(3L).west(1L).objects(List.of(Item.builder().name("Item1").build())).build()
        ));

        var rootRoomId = 1L;
        var itemsToCollect = new ArrayList<String>();
        itemsToCollect.add("NotExistingItem");

        var result = roomBDF.getRouteByRoomRootAndItemsToFind(rootRoomId, itemsToCollect);

        assertEquals(RouteEvent.builder().id(rootRoomId).room("Room1").objectCollected("None").build(), result.get(0));
        assertEquals(RouteEvent.builder().id(4L).room("Room4").objectCollected("None").build(), result.get(1));
        assertEquals(RouteEvent.builder().id(rootRoomId).room("Room1").objectCollected("None").build(), result.get(2));
        assertEquals(RouteEvent.builder().id(2L).room("Room2").objectCollected("None").build(), result.get(3));
        assertEquals(RouteEvent.builder().id(3L).room("Room3").objectCollected("None").build(), result.get(4));

    }

    @Test
    void getRouteByRoomRootAndFindItems_should_be_tolerant_with_cyclic_graphs_test1() {
        RoomTreeSearch roomBDF = new RoomBfsTreeSearch(List.of(
                Room.builder().id(1L).name("Room1").south(2L).east(4L).build(),
                Room.builder().id(2L).name("Room2").north(1L).east(3L).build(),
                Room.builder().id(3L).name("Room3").north(4L).west(2L).build(),
                Room.builder().id(4L).name("Room4").south(3L).west(1L).objects(List.of(Item.builder().name("Item1").build())).build()
        ));

        var rootRoomId = 1L;
        var itemsToCollect = new ArrayList<String>();
        itemsToCollect.add("Item1");

        var result = roomBDF.getRouteByRoomRootAndItemsToFind(rootRoomId, itemsToCollect);

        assertEquals(RouteEvent.builder().id(rootRoomId).room("Room1").objectCollected("None").build(), result.get(0));
        assertEquals(RouteEvent.builder().id(4L).room("Room4").objectCollected("Item1").build(), result.get(1));
    }

    @Test
    void getRouteByRoomRootAndFindItems_should_be_tolerant_with_cyclic_graphs_test2() {
        RoomTreeSearch roomBDF = new RoomBfsTreeSearch(List.of(
                Room.builder().id(1L).name("Room1").south(2L).east(4L).build(),
                Room.builder().id(2L).name("Room2").north(1L).east(3L).objects(List.of(Item.builder().name("Item1").build())).build(),
                Room.builder().id(3L).name("Room3").north(4L).west(2L).build(),
                Room.builder().id(4L).name("Room4").south(3L).west(1L).build()
        ));

        var rootRoomId = 1L;
        var itemsToCollect = new ArrayList<String>();
        itemsToCollect.add("Item1");

        var result = roomBDF.getRouteByRoomRootAndItemsToFind(rootRoomId, itemsToCollect);

        assertEquals(RouteEvent.builder().id(rootRoomId).room("Room1").objectCollected("None").build(), result.get(0));
        assertEquals(RouteEvent.builder().id(4L).room("Room4").objectCollected("None").build(), result.get(1));
        assertEquals(RouteEvent.builder().id(1L).room("Room1").objectCollected("None").build(), result.get(2));
        assertEquals(RouteEvent.builder().id(2L).room("Room2").objectCollected("Item1").build(), result.get(3));

    }

    @Test
    void getRouteByRoomRootAndFindItems_should_be_tolerant_with_cyclic_graphs_test3() {
        RoomTreeSearch roomBDF = new RoomBfsTreeSearch(List.of(
                Room.builder().id(1L).name("Room1").south(8L).east(7L).west(2L).build(),
                Room.builder().id(2L).name("Room2").south(3L).east(1L).build(),
                Room.builder().id(3L).name("Room3").south(4L).west(2L).build(),
                Room.builder().id(4L).name("Room4").north(3L).east(5L).objects(List.of(Item.builder().name("Item1").build())).build(),
                Room.builder().id(5L).name("Room5").west(4L).east(6L).build(),
                Room.builder().id(6L).name("Room6").north(7L).west(5L).build(),
                Room.builder().id(7L).name("Room7").west(1L).south(6L).objects(List.of(Item.builder().name("Item2").build())).build(),
                Room.builder().id(8L).name("Room8").north(1L).objects(List.of(Item.builder().name("Item3").build())).build()
        ));

        var rootRoomId = 1L;
        var itemsToCollect = new ArrayList<String>();
        itemsToCollect.add("Item1");
        itemsToCollect.add("Item2");
        itemsToCollect.add("Item3");

        var result = roomBDF.getRouteByRoomRootAndItemsToFind(rootRoomId, itemsToCollect);

        assertEquals(RouteEvent.builder().id(rootRoomId).room("Room1").objectCollected("None").build(), result.get(0));
        assertEquals(RouteEvent.builder().id(8L).room("Room8").objectCollected("Item3").build(), result.get(1));
        assertEquals(RouteEvent.builder().id(rootRoomId).room("Room1").objectCollected("None").build(), result.get(2));
        assertEquals(RouteEvent.builder().id(2L).room("Room2").objectCollected("None").build(), result.get(3));
        assertEquals(RouteEvent.builder().id(rootRoomId).room("Room1").objectCollected("None").build(), result.get(4));
        assertEquals(RouteEvent.builder().id(7L).room("Room7").objectCollected("Item2").build(), result.get(5));
        assertEquals(RouteEvent.builder().id(6L).room("Room6").objectCollected("None").build(), result.get(6));
        assertEquals(RouteEvent.builder().id(7L).room("Room7").objectCollected("None").build(), result.get(7));
        assertEquals(RouteEvent.builder().id(rootRoomId).room("Room1").objectCollected("None").build(), result.get(8));
        assertEquals(RouteEvent.builder().id(2L).room("Room2").objectCollected("None").build(), result.get(9));
        assertEquals(RouteEvent.builder().id(3L).room("Room3").objectCollected("None").build(), result.get(10));
        assertEquals(RouteEvent.builder().id(4L).room("Room4").objectCollected("Item1").build(), result.get(11));
    }
}
