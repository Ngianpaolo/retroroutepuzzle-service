package io.nigro.retroroutepuzzle.feature.routeresult;

import io.nigro.retroroutepuzzle.feature.routeresult.contract.RouteResultContract;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static io.nigro.retroroutepuzzle.fixtures.ByteArrayInputStreamFixtures.getByteArrayInputStreamResult;
import static io.nigro.retroroutepuzzle.fixtures.RouteEventFixtures.getRouteEvents;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestPropertySource("classpath:application-junit.yml")
class RouteResultServiceTest {

    @Mock
    private RouteResultRepository routeResultRepository;

    @InjectMocks
    private RouteResultService routeResultService;

    @Test
    void getAllRouteResults_shouldWork() {
        var ids = List.of("id1", "id2", "id3");
        var expectedResult = RouteResultContract.builder()
                .routeResultIds(ids)
                .build();
        when(routeResultRepository.findAllRouteResultIds())
                .thenReturn(ids);
        var actualResult = routeResultService.getAllRouteResults();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void getLatestRouteResults_shouldWork() throws IOException {
        var size = 5;
        when(routeResultRepository.getLatestRouteResults(size))
                .thenReturn(getByteArrayInputStreamResult());
        var actualResult = routeResultService.getLatestRouteResults(size);
        assertEquals(new String(getByteArrayInputStreamResult().readAllBytes()), new String(actualResult.readAllBytes()));
    }

    @Test
    void getRouteResultById_shouldWork() {
        var routeResultId = "routeResultId";
        when(routeResultRepository.findRouteResultById(routeResultId))
                .thenReturn(Optional.of(getByteArrayInputStreamResult()));
        var actualResult = routeResultService.getRouteResultById(routeResultId);
        assertEquals(new String(getByteArrayInputStreamResult().readAllBytes()), new String(actualResult.readAllBytes()));
    }

    @Test
    void saveRouteEvents_shouldWork() {
        var routeEvents = getRouteEvents();
        var filename = "filename";
        when(routeResultRepository.save(routeEvents, filename))
                .thenReturn(getByteArrayInputStreamResult());
        var actualResult = routeResultService.saveRouteEvents(routeEvents, filename);
        assertEquals(new String(getByteArrayInputStreamResult().readAllBytes()), new String(actualResult.readAllBytes()));
    }
}
