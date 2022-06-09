package io.nigro.retroroutepuzzle.feature.roommap.contract;

import io.nigro.retroroutepuzzle.feature.roommap.model.Room;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RoomMapContract {

    private String id;

    private List<Room> rooms;

}
