package io.nigro.retroroutepuzzle.feature.roommap;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nigro.retroroutepuzzle.feature.roommap.contract.RoomMapContract;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;

import java.io.File;
import java.io.IOException;

import static io.nigro.retroroutepuzzle.fixtures.RoomMapContractFixtures.getRoomMapContract;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestPropertySource("classpath:application-junit.yml")
class RoomMapRepositoryTest {

    @Mock
    private ObjectMapper objectMapper;

    private RoomMapRepository roomMapRepository;

    @BeforeEach
    void setUp() {
        roomMapRepository = new RoomMapRepository(objectMapper, "./src/test/resources/room_maps/");
    }

    @Test
    void save_shouldWork() throws IOException {
        var id = "roomMap1";
        var roomMapContract = getRoomMapContract(id);

        var actualResult = roomMapRepository.save(roomMapContract);
        verify(objectMapper).writeValue(any(File.class), eq(roomMapContract));

        assertEquals(roomMapContract, actualResult);
    }

    @Test
    void findAllRoomMaps_shouldWork() throws IOException {
        var contract = getRoomMapContract("example");
        when(objectMapper.readValue(any(File.class), eq(RoomMapContract.class)))
                .thenReturn(contract);
        var actualResults = roomMapRepository.findAllRoomMaps();
        assertTrue(actualResults.size() > 0);
        assertEquals(contract, actualResults.get(0));
    }

    @Test
    void findRoomMapById_shouldWork() throws IOException {
        var id = "defaultRoomMap";
        var contract = getRoomMapContract(id);
        when(objectMapper.readValue(any(File.class), eq(RoomMapContract.class)))
                .thenReturn(contract);
        var actualResults = roomMapRepository.findRoomMapById(id);
        assertTrue(actualResults.isPresent());
        assertEquals(contract, actualResults.get());
    }
}
