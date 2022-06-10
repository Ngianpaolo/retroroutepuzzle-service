package io.nigro.retroroutepuzzle.feature.roommap.contract;

import io.nigro.retroroutepuzzle.feature.roommap.model.Room;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomMapContract {

    private String id;

    @NotEmpty
    private List<Room> rooms;

}
