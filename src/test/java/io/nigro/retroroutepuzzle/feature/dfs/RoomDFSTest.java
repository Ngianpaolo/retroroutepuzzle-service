package io.nigro.retroroutepuzzle.feature.dfs;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nigro.retroroutepuzzle.feature.roommap.contract.RoomMapContract;
import io.nigro.retroroutepuzzle.feature.route.contract.RouteEvent;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class RoomDFSTest {

    private final static ObjectMapper objectMapper = new ObjectMapper();
    private final static String path = "./src/test/resources/";

    @Test
    void calculateRoomRoute_should_be_work_withRoomMap1() {
        var roomMapContract = loadMapFromJsonFile("RoomMap1");
        var roomDFS = new RoomDFS(roomMapContract.getRooms());

        var rootRoomId = 2L;
        var itemsToCollect = new ArrayList<String>();
        itemsToCollect.add("Knife");
        itemsToCollect.add("Potted Plant");

        var result = roomDFS.calculateRoomRoute(rootRoomId, itemsToCollect);

        assertEquals(RouteEvent.builder().id(rootRoomId).room("Dining Room").objectCollected("None").build(), result.get(0));
        assertEquals(RouteEvent.builder().id(4L).room("Sun Room").objectCollected("Potted Plant").build(), result.get(1));
        assertEquals(RouteEvent.builder().id(rootRoomId).room("Dining Room").objectCollected("None").build(), result.get(2));
        assertEquals(RouteEvent.builder().id(1L).room("Hallway").objectCollected("None").build(), result.get(3));
        assertEquals(RouteEvent.builder().id(rootRoomId).room("Dining Room").objectCollected("None").build(), result.get(4));
        assertEquals(RouteEvent.builder().id(3L).room("Kitchen").objectCollected("Knife").build(), result.get(5));
    }

    @Test
    void calculateRoomRoute_should_be_work_withRoomMap2() {
        var roomMapContract = loadMapFromJsonFile("RoomMap2");
        var roomDFS = new RoomDFS(roomMapContract.getRooms());

        var rootRoomId = 4L;
        var itemsToCollect = new ArrayList<String>();
        itemsToCollect.add("Knife");
        itemsToCollect.add("Potted Plant");
        itemsToCollect.add("Pillow");

        var result = roomDFS.calculateRoomRoute(rootRoomId, itemsToCollect);

        assertEquals(RouteEvent.builder().id(rootRoomId).room("Sun Room").objectCollected("None").build(), result.get(0));
        assertEquals(RouteEvent.builder().id(7L).room("Living room").objectCollected("Potted Plant").build(), result.get(1));
        assertEquals(RouteEvent.builder().id(1L).room("Hallway").objectCollected("None").build(), result.get(2));
        assertEquals(RouteEvent.builder().id(2L).room("Dining Room").objectCollected("None").build(), result.get(3));
        assertEquals(RouteEvent.builder().id(3L).room("Kitchen").objectCollected("Knife").build(), result.get(4));
        assertEquals(RouteEvent.builder().id(2L).room("Dining Room").objectCollected("None").build(), result.get(5));
        assertEquals(RouteEvent.builder().id(5L).room("Bedroom").objectCollected("Pillow").build(), result.get(6));
    }

    private RoomMapContract loadMapFromJsonFile(String filename) {
        File file = new File(String.format("%s%s.json", path, filename));
        if (!file.exists()) {
            throw new IllegalStateException();
        }
        return convertJsonFileToRoomMap(file);
    }

    private RoomMapContract convertJsonFileToRoomMap(File jsonFile) {
        try {
            return objectMapper.readValue(jsonFile, RoomMapContract.class);
        } catch (IOException e) {
            log.error("", e);
            return null;
        }
    }
}
