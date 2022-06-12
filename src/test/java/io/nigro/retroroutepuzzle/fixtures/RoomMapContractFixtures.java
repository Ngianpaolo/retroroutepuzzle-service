package io.nigro.retroroutepuzzle.fixtures;

import io.nigro.retroroutepuzzle.feature.roommap.contract.RoomMapContract;
import io.nigro.retroroutepuzzle.feature.roommap.model.Room;

import java.util.List;

public class RoomMapContractFixtures {

    public static RoomMapContract getRoomMapContract(String id) {
        return RoomMapContract.builder()
                .id(id)
                .rooms(RoomFixtures.getRooms())
                .build();
    }
}
