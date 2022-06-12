package io.nigro.retroroutepuzzle.fixtures;

import io.nigro.retroroutepuzzle.feature.route.contract.RouteRequest;

import java.util.List;

import static io.nigro.retroroutepuzzle.fixtures.RoomFixtures.getRooms;

public class RouteRequestFixtures {

    public static RouteRequest getRouteRequest() {
        return RouteRequest.builder()
                .startRoomId(1L)
                .itemToCollect(List.of("Item1", "Item2"))
                .rooms(getRooms())
                .build();
    }
}
