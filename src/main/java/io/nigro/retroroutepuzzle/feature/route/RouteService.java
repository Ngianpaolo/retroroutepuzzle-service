package io.nigro.retroroutepuzzle.feature.route;

import io.nigro.retroroutepuzzle.feature.roommap.RoomMapRepository;
import io.nigro.retroroutepuzzle.feature.route.contract.RouteEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteService {

    private final RoomMapRepository roomMapRepository;

    @Autowired
    public RouteService(RoomMapRepository roomMapRepository) {
        this.roomMapRepository = roomMapRepository;
    }

    public List<RouteEvent> calculateRoute(String roomMapId,
                                           String roomId,
                                           List<String> itemToCollect) {
        return List.of(RouteEvent.builder().build());
    }

}
