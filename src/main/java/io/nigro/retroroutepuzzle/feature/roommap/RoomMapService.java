package io.nigro.retroroutepuzzle.feature.roommap;

import io.nigro.retroroutepuzzle.feature.roommap.contract.RoomMapContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomMapService {

    private final RoomMapRepository roomMapRepository;

    @Autowired
    public RoomMapService(RoomMapRepository roomMapRepository) {
        this.roomMapRepository = roomMapRepository;
    }

    public RoomMapContract createRoomMap(RoomMapContract request) {
        return roomMapRepository.save(request);
    }

    public List<RoomMapContract> getRoomMaps() {
        return roomMapRepository.findAllRoomMaps();
    }

    public RoomMapContract getRoomMap(String roomMapId) {
        return roomMapRepository.findRoomMapById(roomMapId).orElseThrow();
    }
}
