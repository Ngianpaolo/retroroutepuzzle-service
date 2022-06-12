package io.nigro.retroroutepuzzle.fixtures;

import io.nigro.retroroutepuzzle.feature.route.model.RouteEvent;

import java.util.List;

public class RouteEventFixtures {

    public static List<RouteEvent> getRouteEvents() {
        return List.of(
                RouteEvent.builder().id(4L).room("Sun Room").objectCollected("None").build(),
                RouteEvent.builder().id(2L).room("Dining Room").objectCollected("None").build(),
                RouteEvent.builder().id(4L).room("Sun Room").objectCollected("None").build(),
                RouteEvent.builder().id(6L).room("Bathroom").objectCollected("None").build(),
                RouteEvent.builder().id(4L).room("Sun Room").objectCollected("None").build(),
                RouteEvent.builder().id(7L).room("Living room").objectCollected("Potted Plant").build(),
                RouteEvent.builder().id(1L).room("Hallway").objectCollected("None").build(),
                RouteEvent.builder().id(7L).room("Living room").objectCollected("None").build(),
                RouteEvent.builder().id(4L).room("Sun Room").objectCollected("None").build(),
                RouteEvent.builder().id(2L).room("Dining Room").objectCollected("None").build(),
                RouteEvent.builder().id(3L).room("Kitchen").objectCollected("Knife").build(),
                RouteEvent.builder().id(2L).room("Dining Room").objectCollected("None").build(),
                RouteEvent.builder().id(5L).room("Bedroom").objectCollected("Pillow").build()
        );
    }
}
