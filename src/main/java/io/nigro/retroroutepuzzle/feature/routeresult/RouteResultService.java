package io.nigro.retroroutepuzzle.feature.routeresult;

import io.nigro.retroroutepuzzle.feature.route.model.RouteEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
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

}
