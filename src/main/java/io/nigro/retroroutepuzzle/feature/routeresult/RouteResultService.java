package io.nigro.retroroutepuzzle.feature.routeresult;

import io.nigro.retroroutepuzzle.exception.RouteResultNotFoundException;
import io.nigro.retroroutepuzzle.feature.route.model.RouteEvent;
import io.nigro.retroroutepuzzle.feature.routeresult.contract.RouteResultContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

@Service
public class RouteResultService {

    private final RouteResultRepository routeResultRepository;

    @Autowired
    public RouteResultService(RouteResultRepository routeResultRepository) {
        this.routeResultRepository = routeResultRepository;
    }

    public ByteArrayInputStream saveRouteEvents(List<RouteEvent> routeEvents, String filename) {
        return routeResultRepository.save(routeEvents, filename);
    }

    public RouteResultContract getAllRouteResults() {
        return RouteResultContract.builder()
                .routeResultIds(routeResultRepository.findAllRouteResultIds())
                .build();
    }

    public ByteArrayInputStream getRouteResultById(String routeResultId) {
        return routeResultRepository.findRouteResultById(routeResultId)
                .orElseThrow(RouteResultNotFoundException::new);
    }

    public InputStream getLatestRouteResults(Integer size) {
        return routeResultRepository.getLatestRouteResults(size);
    }
}
