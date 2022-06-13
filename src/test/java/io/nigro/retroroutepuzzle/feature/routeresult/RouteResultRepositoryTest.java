package io.nigro.retroroutepuzzle.feature.routeresult;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static io.nigro.retroroutepuzzle.fixtures.RouteEventFixtures.getRouteEvents;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@TestPropertySource("classpath:application-junit.yml")
class RouteResultRepositoryTest {

    private final String routeEventsFileId = "test";

    private RouteResultRepository routeResultRepository;

    @BeforeEach
    void setUp() {
        routeResultRepository = new RouteResultRepository("./src/test/resources/route_results/");
    }

    @Test
    void save_and_findRouteResultById_shouldWork() {
        var actualResult = saveARouteResults();
        var inputStreamRead = routeResultRepository.findRouteResultById(routeEventsFileId);
        assertTrue(inputStreamRead.isPresent());
        assertEquals(new String(inputStreamRead.get().readAllBytes()), new String(actualResult.readAllBytes()));
    }

    @Test
    void getLatestRouteResults_shouldWork() throws IOException {
        saveARouteResults();
        var inputStreamRead = routeResultRepository.getLatestRouteResults(5);
        assertNotNull(inputStreamRead);
        assertTrue(inputStreamRead.readAllBytes().length > 0);
    }

    private ByteArrayInputStream saveARouteResults() {
        return routeResultRepository.save(getRouteEvents(), routeEventsFileId);
    }

}
