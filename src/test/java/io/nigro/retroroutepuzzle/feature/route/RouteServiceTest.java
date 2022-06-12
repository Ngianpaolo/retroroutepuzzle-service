package io.nigro.retroroutepuzzle.feature.route;

import io.nigro.retroroutepuzzle.exception.RoomMapNotFoundException;
import io.nigro.retroroutepuzzle.feature.roommap.RoomMapRepository;
import io.nigro.retroroutepuzzle.feature.search.RoomTreeSearchType;
import io.nigro.retroroutepuzzle.feature.search.RouteSearchService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static io.nigro.retroroutepuzzle.fixtures.RoomFixtures.getRooms;
import static io.nigro.retroroutepuzzle.fixtures.RoomMapContractFixtures.getRoomMapContract;
import static io.nigro.retroroutepuzzle.fixtures.RouteEventFixtures.getRouteEvents;
import static io.nigro.retroroutepuzzle.fixtures.RouteRequestFixtures.getRouteRequest;
import static io.nigro.retroroutepuzzle.fixtures.RouteSavedMapRequestFixtures.getRouteSavedMapRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestPropertySource("classpath:application-junit.yml")
class RouteServiceTest {

    @Mock
    private RoomMapRepository roomMapRepository;

    @Mock
    private RouteSearchService routeSearchService;

    @InjectMocks
    private RouteService routeService;

    @Test
    void searchRoute_shouldWork() {
        var routeRequest = getRouteRequest();
        var searchType = RoomTreeSearchType.DFS;
        var routeEvents = getRouteEvents();
        when(routeSearchService.getRouteByRoomRootAndItemsToFind(
                routeRequest.getRooms(),
                routeRequest.getStartRoomId(),
                routeRequest.getItemToCollect(),
                searchType))
                .thenReturn(routeEvents);
        var actualResult = routeService.searchRoute(routeRequest, searchType);
        assertEquals(routeEvents, actualResult);
    }

    @Test
    void searchRoute_on_savedRoomMap_shouldWork() {
        var roomMapId = "roomMapId1";
        var routeRequest = getRouteSavedMapRequest();
        var searchType = RoomTreeSearchType.DFS;
        var routeEvents = getRouteEvents();
        var foundRooms = getRooms();

        when(roomMapRepository.findRoomMapById(roomMapId))
                .thenReturn(Optional.of(getRoomMapContract(roomMapId)));
        when(routeSearchService.getRouteByRoomRootAndItemsToFind(
                foundRooms,
                routeRequest.getStartRoomId(),
                routeRequest.getItemToCollect(),
                searchType))
                .thenReturn(routeEvents);

        var actualResult = routeService.searchRoute(routeRequest, searchType, roomMapId);
        assertEquals(routeEvents, actualResult);
    }

    @Test
    void searchRoute_on_savedRoomMap_shouldThrowRoomMapNotFoundException() {
        var roomMapId = "roomMapId1";
        var routeRequest = getRouteSavedMapRequest();
        var searchType = RoomTreeSearchType.DFS;

        when(roomMapRepository.findRoomMapById(roomMapId))
                .thenReturn(Optional.empty());

        assertThrows(RoomMapNotFoundException.class, () -> routeService.searchRoute(routeRequest, searchType, roomMapId));
    }
}
