package io.nigro.retroroutepuzzle.feature.routeresult;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nigro.retroroutepuzzle.feature.roommap.RoomMapService;
import io.nigro.retroroutepuzzle.feature.roommap.contract.RoomMapContract;
import io.nigro.retroroutepuzzle.feature.roommap.model.Room;
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

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RouteResultController.class)
@TestPropertySource("classpath:application-junit.yml")
class RouteResultControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RoomMapService roomMapService;

    @Test
    void createRoomMap_shouldWork() throws Exception {
        var id = "roomMap1";

        var roomMapContract = RoomMapContract.builder()
                .id(id)
                .rooms(List.of(
                        Room.builder().id(1L).name("Sun Room").north(2L).build(),
                        Room.builder().id(2L).name("Living room").south(1L).build()
                ))
                .build();

        when(roomMapService.createRoomMap(roomMapContract)).thenReturn(roomMapContract);
        mockMvc.perform(post("/api/room-map")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(roomMapContract)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.rooms[0].name", is("Sun Room")))
                .andExpect(jsonPath("$.rooms[1].name", is("Living room")));
    }

    @Test
    void getRoomMap_shouldWork() throws Exception {
        var id = "roomMap1";

        var roomMapContract = RoomMapContract.builder()
                .id(id)
                .rooms(List.of(
                        Room.builder().id(1L).name("Sun Room").north(2L).build(),
                        Room.builder().id(2L).name("Living room").south(1L).build()
                ))
                .build();

        when(roomMapService.getRoomMap(id)).thenReturn(roomMapContract);
        mockMvc.perform(get("/api/room-map/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(roomMapContract)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.rooms[0].name", is("Sun Room")))
                .andExpect(jsonPath("$.rooms[1].name", is("Living room")));
    }

    @Test
    void getAllRoomMaps_shouldWork() throws Exception {
        var id = "roomMap1";
        var id2 = "roomMap2";

        var roomMapContract = RoomMapContract.builder()
                .id(id)
                .rooms(List.of(
                        Room.builder().id(1L).name("Room1").north(2L).build(),
                        Room.builder().id(2L).name("Room2").south(1L).build()
                ))
                .build();

        var roomMapContract2 = RoomMapContract.builder()
                .id(id2)
                .rooms(List.of(
                        Room.builder().id(1L).name("Room3").north(2L).build(),
                        Room.builder().id(2L).name("Room4").south(1L).build()
                ))
                .build();

        when(roomMapService.getAllRoomMaps()).thenReturn(List.of(roomMapContract, roomMapContract2));
        mockMvc.perform(get("/api/room-map")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(roomMapContract)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(id)))
                .andExpect(jsonPath("$[0].rooms[0].name", is("Room1")))
                .andExpect(jsonPath("$[0].rooms[1].name", is("Room2")))
                .andExpect(jsonPath("$[1].id", is(id2)))
                .andExpect(jsonPath("$[1].rooms[0].name", is("Room3")))
                .andExpect(jsonPath("$[1].rooms[1].name", is("Room4")));
    }


}
