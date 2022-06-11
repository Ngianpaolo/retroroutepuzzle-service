package io.nigro.retroroutepuzzle.feature.route;

import io.nigro.retroroutepuzzle.feature.route.contract.RouteMapSavedRequest;
import io.nigro.retroroutepuzzle.feature.route.contract.RouteRequest;
import io.nigro.retroroutepuzzle.feature.search.RoomTreeSearchType;
import io.nigro.retroroutepuzzle.util.FileUtil;
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

@RestController
public class RouteController {

    private final RouteService routeService;

    @Autowired
    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @PostMapping("/api/route/search/{searchType}")
    public ResponseEntity<Resource> calculateRoute(@PathVariable(name = "searchType") RoomTreeSearchType searchType,
                                                   @Valid @RequestBody RouteRequest request) {
        String filename = request.getStartRoomId() + "_" + searchType + "_" + Instant.now() + ".csv";
        InputStreamResource file = new InputStreamResource(FileUtil.routeEventToCSV(routeService.calculateRoute(request, searchType)));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);
    }

    @PostMapping("/api/route/search/{searchType}/rooms/{roomMapId}")
    public ResponseEntity<Resource> calculateRoute(@PathVariable(name = "searchType") RoomTreeSearchType searchType,
                                                   @PathVariable(name = "roomMapId") String roomMapId,
                                                   @Valid @RequestBody RouteMapSavedRequest request) {
        String filename = request.getStartRoomId() + "_" + searchType + "_" + Instant.now() + ".csv";
        InputStreamResource file = new InputStreamResource(FileUtil.routeEventToCSV(routeService.calculateRoute(request, searchType, roomMapId)));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);
    }
}
