package io.nigro.retroroutepuzzle.feature.roommap.contract;

import io.nigro.retroroutepuzzle.feature.roommap.model.Room;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@Builder
public class RoomMapContract {

    private String id;

    @NotEmpty
    private List<Room> rooms;

}
