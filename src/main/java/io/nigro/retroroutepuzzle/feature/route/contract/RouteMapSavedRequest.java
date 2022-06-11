package io.nigro.retroroutepuzzle.feature.route.contract;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
public class RouteMapSavedRequest {

    @NotNull
    private Long startRoomId;

    @NotEmpty
    private List<String> itemToCollect;

}
