package io.nigro.retroroutepuzzle.feature.routeresult;

import io.nigro.retroroutepuzzle.feature.route.model.RouteEvent;
import io.nigro.retroroutepuzzle.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Repository;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Repository
@Slf4j
public class RouteResultRepository {

    private final static String path = "./storage/route_results/";

    public ByteArrayInputStream save(List<RouteEvent> routeEvents, String filename) {
        var routeEventBytes = FileUtil.routeEventToCSV(routeEvents).readAllBytes();
        try {
            IOUtils.copy(new ByteArrayInputStream(routeEventBytes), new FileOutputStream(path + "" + filename.toLowerCase() + ".txt"));
        } catch (IOException e) {
            log.error("Error: ", e);
        }
        return new ByteArrayInputStream(routeEventBytes);
    }
}
