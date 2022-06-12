package io.nigro.retroroutepuzzle.fixtures;

import io.nigro.retroroutepuzzle.feature.route.contract.RouteSavedMapRequest;

import java.util.List;

public class RouteSavedMapRequestFixtures {

    public static RouteSavedMapRequest getRouteSavedMapRequest() {
        return RouteSavedMapRequest.builder()
                .startRoomId(1L)
                .itemToCollect(List.of("Item1", "Item2"))
                .build();
    }
}
