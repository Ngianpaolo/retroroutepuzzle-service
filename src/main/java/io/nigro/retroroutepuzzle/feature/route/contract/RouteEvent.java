package io.nigro.retroroutepuzzle.feature.route.contract;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RouteEvent {

    private Long id;
    private String room;
    private String objectCollected;
}
