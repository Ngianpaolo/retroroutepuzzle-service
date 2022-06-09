package io.nigro.retroroutepuzzle.feature.roommap;

import io.nigro.retroroutepuzzle.feature.roommap.contract.RoomMapContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class RoomMapController {

    private final RoomMapService roomMapService;

    @Autowired
    public RoomMapController(RoomMapService roomMapService) {
        this.roomMapService = roomMapService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/retro-route-puzzle-service/room-map")
    public RoomMapContract createRoomMap(@Valid RoomMapContract request) {
        return roomMapService.createRoomMap(request);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/retro-route-puzzle-service/room-map")
    public List<RoomMapContract> getAllRoomMaps() {
        return roomMapService.getRoomMaps();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/retro-route-puzzle-service/room-map/{roomMapId}")
    public RoomMapContract getRoomMap(@PathVariable String roomMapId) {
        return roomMapService.getRoomMap(roomMapId);
    }
}
