package io.nigro.retroroutepuzzle.feature.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nigro.retroroutepuzzle.feature.route.contract.RouteMapSavedRequest;
import io.nigro.retroroutepuzzle.feature.route.contract.RouteRequest;
import io.nigro.retroroutepuzzle.feature.routeresult.RouteResultService;
import io.nigro.retroroutepuzzle.feature.search.RoomTreeSearchType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static io.nigro.retroroutepuzzle.fixtures.ByteArrayInputStreamFixtures.getByteArrayInputStreamResult;
import static io.nigro.retroroutepuzzle.fixtures.RoomFixtures.getRooms;
import static io.nigro.retroroutepuzzle.fixtures.RouteEventFixtures.getRouteEvents;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RouteController.class)
@TestPropertySource("classpath:application-junit.yml")
class RouteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RouteService routeService;

    @MockBean
    private RouteResultService routeResultService;

    @Test
    void searchRoute_BFS_shouldWork() throws Exception {
        var searchType = RoomTreeSearchType.BFS;
        var itemToCollect = List.of("Item1", "Item2");
        var startRoomId = 1L;
        var rooms = getRooms();

        var routeRequest = RouteRequest.builder()
                .startRoomId(startRoomId)
                .itemToCollect(itemToCollect)
                .rooms(rooms)
                .build();


        when(routeService.searchRoute(routeRequest, searchType)).thenReturn(getRouteEvents());
        when(routeResultService.saveRouteEvents(eq(getRouteEvents()), any()))
                .thenReturn(getByteArrayInputStreamResult("route_result_example"));

        mockMvc.perform(post("/api/route/search/" + searchType)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(routeRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain"))
                .andExpect(content().string(
                        " -------------------------------------------------------------------- \n" +
                                " ID     \t Room               \t Object  collected         \n" +
                                " -------------------------------------------------------------------- \n" +
                                " 2       \t Dining  Room       \t None                                 \n" +
                                " 1       \t Hallway               \t None                                 \n" +
                                " 2       \t Dining  Room       \t None                                 \n" +
                                " 4       \t Sun  Room             \t None                                 \n" +
                                " 2       \t Dining  Room       \t None                                 \n" +
                                " 3       \t Kitchen               \t Knife                               \n" +
                                " 2       \t Dining  Room       \t None                                 \n" +
                                " 5       \t Bedroom               \t None                                 \n" +
                                " 6       \t Bathroom             \t None                                 \n" +
                                " 5       \t Bedroom               \t None                                 \n" +
                                " 2       \t Dining  Room       \t None                                 \n" +
                                " 1       \t Hallway               \t None                                 \n" +
                                " 7       \t Living  room       \t Potted  Plant                 \n"));
    }

    @Test
    void searchRoute_DFS_shouldWork() throws Exception {
        var searchType = RoomTreeSearchType.DFS;
        var itemToCollect = List.of("Item1", "Item2");
        var startRoomId = 1L;
        var rooms = getRooms();

        var routeRequest = RouteRequest.builder()
                .startRoomId(startRoomId)
                .itemToCollect(itemToCollect)
                .rooms(rooms)
                .build();


        when(routeService.searchRoute(routeRequest, searchType)).thenReturn(getRouteEvents());
        when(routeResultService.saveRouteEvents(eq(getRouteEvents()), any()))
                .thenReturn(getByteArrayInputStreamResult("route_result_example"));

        mockMvc.perform(post("/api/route/search/" + searchType)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(routeRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain"))
                .andExpect(content().string(
                        " -------------------------------------------------------------------- \n" +
                                " ID     \t Room               \t Object  collected         \n" +
                                " -------------------------------------------------------------------- \n" +
                                " 2       \t Dining  Room       \t None                                 \n" +
                                " 1       \t Hallway               \t None                                 \n" +
                                " 2       \t Dining  Room       \t None                                 \n" +
                                " 4       \t Sun  Room             \t None                                 \n" +
                                " 2       \t Dining  Room       \t None                                 \n" +
                                " 3       \t Kitchen               \t Knife                               \n" +
                                " 2       \t Dining  Room       \t None                                 \n" +
                                " 5       \t Bedroom               \t None                                 \n" +
                                " 6       \t Bathroom             \t None                                 \n" +
                                " 5       \t Bedroom               \t None                                 \n" +
                                " 2       \t Dining  Room       \t None                                 \n" +
                                " 1       \t Hallway               \t None                                 \n" +
                                " 7       \t Living  room       \t Potted  Plant                 \n"));
    }

    @Test
    void searchRoute_shouldReturn400_invalidSearchType() throws Exception {
        var searchType = "InvalidSearchType";
        var itemToCollect = List.of("Item1", "Item2");
        var startRoomId = 1L;
        var rooms = getRooms();

        var routeRequest = RouteRequest.builder()
                .startRoomId(startRoomId)
                .itemToCollect(itemToCollect)
                .rooms(rooms)
                .build();


        mockMvc.perform(post("/api/route/search/" + searchType)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(routeRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void searchRouteByMapId_BFS_shouldWork() throws Exception {
        var searchType = RoomTreeSearchType.BFS;
        var itemToCollect = List.of("Item1", "Item2");
        var startRoomId = 1L;
        var roomMapId = "RoomMapId";

        var routeRequest = RouteMapSavedRequest.builder()
                .startRoomId(startRoomId)
                .itemToCollect(itemToCollect)
                .build();

        when(routeService.searchRoute(routeRequest, searchType, roomMapId)).thenReturn(getRouteEvents());
        when(routeResultService.saveRouteEvents(eq(getRouteEvents()), any()))
                .thenReturn(getByteArrayInputStreamResult("route_result_example"));

        mockMvc.perform(post("/api/route/search/" + searchType + "/rooms/" + roomMapId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(routeRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain"))
                .andExpect(content().string(
                        " -------------------------------------------------------------------- \n" +
                                " ID     \t Room               \t Object  collected         \n" +
                                " -------------------------------------------------------------------- \n" +
                                " 2       \t Dining  Room       \t None                                 \n" +
                                " 1       \t Hallway               \t None                                 \n" +
                                " 2       \t Dining  Room       \t None                                 \n" +
                                " 4       \t Sun  Room             \t None                                 \n" +
                                " 2       \t Dining  Room       \t None                                 \n" +
                                " 3       \t Kitchen               \t Knife                               \n" +
                                " 2       \t Dining  Room       \t None                                 \n" +
                                " 5       \t Bedroom               \t None                                 \n" +
                                " 6       \t Bathroom             \t None                                 \n" +
                                " 5       \t Bedroom               \t None                                 \n" +
                                " 2       \t Dining  Room       \t None                                 \n" +
                                " 1       \t Hallway               \t None                                 \n" +
                                " 7       \t Living  room       \t Potted  Plant                 \n"));
    }

    @Test
    void searchRouteByMapId_DFS_shouldWork() throws Exception {
        var searchType = RoomTreeSearchType.DFS;
        var itemToCollect = List.of("Item1", "Item2");
        var startRoomId = 1L;
        var roomMapId = "RoomMapId";

        var routeRequest = RouteMapSavedRequest.builder()
                .startRoomId(startRoomId)
                .itemToCollect(itemToCollect)
                .build();

        when(routeService.searchRoute(routeRequest, searchType, roomMapId)).thenReturn(getRouteEvents());
        when(routeResultService.saveRouteEvents(eq(getRouteEvents()), any()))
                .thenReturn(getByteArrayInputStreamResult("route_result_example"));

        mockMvc.perform(post("/api/route/search/" + searchType + "/rooms/" + roomMapId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(routeRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain"))
                .andExpect(content().string(
                        " -------------------------------------------------------------------- \n" +
                                " ID     \t Room               \t Object  collected         \n" +
                                " -------------------------------------------------------------------- \n" +
                                " 2       \t Dining  Room       \t None                                 \n" +
                                " 1       \t Hallway               \t None                                 \n" +
                                " 2       \t Dining  Room       \t None                                 \n" +
                                " 4       \t Sun  Room             \t None                                 \n" +
                                " 2       \t Dining  Room       \t None                                 \n" +
                                " 3       \t Kitchen               \t Knife                               \n" +
                                " 2       \t Dining  Room       \t None                                 \n" +
                                " 5       \t Bedroom               \t None                                 \n" +
                                " 6       \t Bathroom             \t None                                 \n" +
                                " 5       \t Bedroom               \t None                                 \n" +
                                " 2       \t Dining  Room       \t None                                 \n" +
                                " 1       \t Hallway               \t None                                 \n" +
                                " 7       \t Living  room       \t Potted  Plant                 \n"));
    }

    @Test
    void searchRouteByMapId_shouldReturn400_invalidSearchType() throws Exception {
        var searchType = "InvalidSearchType";
        var itemToCollect = List.of("Item1", "Item2");
        var startRoomId = 1L;
        var rooms = getRooms();
        var roomMapId = "RoomMapId";

        var routeRequest = RouteRequest.builder()
                .startRoomId(startRoomId)
                .itemToCollect(itemToCollect)
                .rooms(rooms)
                .build();


        mockMvc.perform(post("/api/route/search/" + searchType + "/rooms/" + roomMapId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(routeRequest)))
                .andExpect(status().isBadRequest());
    }

}
