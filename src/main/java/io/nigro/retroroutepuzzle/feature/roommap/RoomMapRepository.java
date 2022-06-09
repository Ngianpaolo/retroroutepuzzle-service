package io.nigro.retroroutepuzzle.feature.roommap;

import io.nigro.retroroutepuzzle.feature.roommap.contract.RoomMapContract;
import org.springframework.stereotype.Repository;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Repository
public class RoomMapRepository {

    public RoomMapContract save(@Valid RoomMapContract request) {
        return request;
    }

    public List<RoomMapContract> findAllRoomMaps() {
        return List.of();
    }

    public Optional<RoomMapContract> findRoomMapById(String id) {
        return Optional.of(RoomMapContract
                .builder()
                .id(id)
                .build());
    }
}
