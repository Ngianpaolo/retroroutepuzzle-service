package io.nigro.retroroutepuzzle.feature.route;

import io.nigro.retroroutepuzzle.exception.RoomMapNotFoundException;
import io.nigro.retroroutepuzzle.feature.roommap.RoomMapRepository;
import io.nigro.retroroutepuzzle.feature.route.model.RouteEvent;
import io.nigro.retroroutepuzzle.feature.route.contract.RouteMapSavedRequest;
import io.nigro.retroroutepuzzle.feature.route.contract.RouteRequest;
import io.nigro.retroroutepuzzle.feature.search.RoomTreeSearchType;
import io.nigro.retroroutepuzzle.feature.search.RouteSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteService {

    private final RoomMapRepository roomMapRepository;

    private final RouteSearchService routeSearchService;

    @Autowired
    public RouteService(RoomMapRepository roomMapRepository, RouteSearchService routeSearchService) {
        this.roomMapRepository = roomMapRepository;
        this.routeSearchService = routeSearchService;
    }

    public List<RouteEvent> calculateRoute(RouteRequest request, RoomTreeSearchType searchType) {
        return routeSearchService.getRouteByRoomRootAndItemsToFind(
                request.getRooms(),
                request.getStartRoomId(),
                request.getItemToCollect(),
                searchType);
    }

    public List<RouteEvent> calculateRoute(RouteMapSavedRequest request, RoomTreeSearchType searchType, String roomMapId) {
        return routeSearchService.getRouteByRoomRootAndItemsToFind(
                roomMapRepository.findRoomMapById(roomMapId).orElseThrow(RoomMapNotFoundException::new).getRooms(),
                request.getStartRoomId(),
                request.getItemToCollect(),
                searchType);
    }
}
