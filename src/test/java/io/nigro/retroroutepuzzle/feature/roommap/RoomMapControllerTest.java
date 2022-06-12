package io.nigro.retroroutepuzzle.feature.roommap;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import static io.nigro.retroroutepuzzle.fixtures.RoomMapContractFixtures.getRoomMapContract;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RoomMapController.class)
@TestPropertySource("classpath:application-junit.yml")
class RoomMapControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RoomMapService roomMapService;

    @Test
    void createRoomMap_shouldWork() throws Exception {
        var id = "roomMap1";

        var roomMapContract = getRoomMapContract(id);

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

        var roomMapContract = getRoomMapContract(id);

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

        var roomMapContract = getRoomMapContract(id);
        var roomMapContract2 = getRoomMapContract(id2);
        roomMapContract.getRooms().get(0).setName("Room1");
        roomMapContract.getRooms().get(1).setName("Room2");
        roomMapContract2.getRooms().get(0).setName("Room3");
        roomMapContract2.getRooms().get(1).setName("Room4");


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
