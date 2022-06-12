package io.nigro.retroroutepuzzle.fixtures;

import io.nigro.retroroutepuzzle.feature.roommap.model.Room;

import java.util.List;

public class RoomFixtures {

    public static List<Room> getRooms() {
        return List.of(
                Room.builder().id(1L).name("Sun Room").north(2L).build(),
                Room.builder().id(2L).name("Living room").south(1L).build()
        );
    }
}
