package io.nigro.retroroutepuzzle.feature.route;

import io.nigro.retroroutepuzzle.feature.route.contract.IRouteRequest;
import io.nigro.retroroutepuzzle.feature.route.contract.RouteMapSavedRequest;
import io.nigro.retroroutepuzzle.feature.route.contract.RouteRequest;
import io.nigro.retroroutepuzzle.feature.route.model.RouteEvent;
import io.nigro.retroroutepuzzle.feature.routeresult.RouteResultService;
import io.nigro.retroroutepuzzle.feature.search.RoomTreeSearchType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.Instant;
import java.util.List;

@RestController
public class RouteController {

    private final RouteService routeService;

    private final RouteResultService routeResultService;

    @Autowired
    public RouteController(RouteService routeService, RouteResultService routeResultService) {
        this.routeService = routeService;
        this.routeResultService = routeResultService;
    }

    @PostMapping("/api/route/search/{searchType}")
    public ResponseEntity<Resource> searchRoute(@PathVariable(name = "searchType") RoomTreeSearchType searchType,
                                                   @Valid @RequestBody RouteRequest request) {
        String filename = getFilename(request, searchType);

        var routeEvents = routeService.searchRoute(request, searchType);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename + ".txt")
                .contentType(MediaType.parseMediaType("text/plain"))
                .body(getFile(routeEvents, filename));
    }

    @PostMapping("/api/route/search/{searchType}/rooms/{roomMapId}")
    public ResponseEntity<Resource> searchRoute(@PathVariable(name = "searchType") RoomTreeSearchType searchType,
                                                   @PathVariable(name = "roomMapId") String roomMapId,
                                                   @Valid @RequestBody RouteMapSavedRequest request) {
        String filename = getFilename(request, searchType);

        var routeEvents = routeService.searchRoute(request, searchType, roomMapId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename + ".txt")
                .contentType(MediaType.parseMediaType("text/plain"))
                .body(getFile(routeEvents, filename));
    }

    private String getFilename(IRouteRequest routeRequest, RoomTreeSearchType searchType) {
        return Instant.now().getEpochSecond() + "_start_room_id" + routeRequest.getStartRoomId() + "_" + searchType;
    }

    private InputStreamResource getFile(List<RouteEvent> routeEvents, String filename) {
        return new InputStreamResource(routeResultService.saveRouteEvents(routeEvents, filename));
    }
}
