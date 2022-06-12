package io.nigro.retroroutepuzzle.feature.routeresult;

import io.nigro.retroroutepuzzle.feature.routeresult.contract.RouteResultContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
public class RouteResultController {

    private final RouteResultService routeResultService;

    @Autowired
    public RouteResultController(RouteResultService routeResultService) {
        this.routeResultService = routeResultService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/route/results")
    public RouteResultContract getAllRouteResults() {
        return routeResultService.getAllRouteResults();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/route/results/{routeResultId}")
    public ResponseEntity<Resource> getRouteResultById(@PathVariable(name = "routeResultId") String routeResultId) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + routeResultId + ".txt")
                .contentType(MediaType.parseMediaType("text/plain"))
                .body(new InputStreamResource(routeResultService.getRouteResultById(routeResultId)));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/route/results/historical")
    public ResponseEntity<Resource> getLatestRouteResults(@RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=historical_" + Instant.now().getEpochSecond() + ".txt")
                .contentType(MediaType.parseMediaType("text/plain"))
                .body(new InputStreamResource(routeResultService.getLatestRouteResults(size)));
    }
}
