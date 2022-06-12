package io.nigro.retroroutepuzzle.feature.route.contract;

import io.nigro.retroroutepuzzle.feature.roommap.model.Room;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
public class RouteRequest implements IRouteRequest{

    @NotEmpty
    private List<Room> rooms;

    @NotNull
    private Long startRoomId;

    @NotEmpty
    private List<String> itemToCollect;

}
