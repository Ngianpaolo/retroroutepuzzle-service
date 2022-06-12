package io.nigro.retroroutepuzzle.fixtures;

import io.nigro.retroroutepuzzle.feature.roommap.contract.RoomMapContract;

public class RoomMapContractFixtures {

    public static RoomMapContract getRoomMapContract(String id) {
        return RoomMapContract.builder()
                .id(id)
                .rooms(RoomFixtures.getRooms())
                .build();
    }
}
