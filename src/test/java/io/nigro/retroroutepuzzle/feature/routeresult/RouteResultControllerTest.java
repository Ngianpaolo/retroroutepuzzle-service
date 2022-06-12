package io.nigro.retroroutepuzzle.feature.routeresult;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nigro.retroroutepuzzle.feature.routeresult.contract.RouteResultContract;
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
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
    private RouteResultService routeResultService;

    @Test
    void getAllRouteResults_shouldWork() throws Exception {
        var roomMapContract = RouteResultContract.builder()
                .routeResultIds(List.of(
                        "id1",
                        "id2",
                        "id3"
                ))
                .build();

        when(routeResultService.getAllRouteResults()).thenReturn(roomMapContract);
        mockMvc.perform(get("/api/route/results")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(roomMapContract)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.routeResultIds[0]", is("id1")))
                .andExpect(jsonPath("$.routeResultIds[1]", is("id2")))
                .andExpect(jsonPath("$.routeResultIds[2]", is("id3")));
    }

    @Test
    void getRouteResultById_shouldWork() throws Exception {
        var id = "route_result_example";

        when(routeResultService.getRouteResultById(id)).thenReturn(getByteArrayInputStreamResult(id));
        mockMvc.perform(get("/api/route/results/" + id)
                .contentType(MediaType.APPLICATION_JSON))
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
    void getLatestRouteResults_shouldWork() throws Exception {
        var size = 5;

        when(routeResultService.getLatestRouteResults(size)).thenReturn(getByteArrayInputStreamResult("route_result_example"));
        mockMvc.perform(get("/api/route/results/historical?size=" + size)
                .contentType(MediaType.APPLICATION_JSON))
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

}
