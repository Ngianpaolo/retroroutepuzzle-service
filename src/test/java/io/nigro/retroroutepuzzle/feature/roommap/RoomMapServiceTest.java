package io.nigro.retroroutepuzzle.feature.roommap;

import io.nigro.retroroutepuzzle.exception.RoomMapNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static io.nigro.retroroutepuzzle.fixtures.RoomMapContractFixtures.getRoomMapContract;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestPropertySource("classpath:application-junit.yml")
class RoomMapServiceTest {

    @Mock
    private RoomMapRepository roomMapRepository;

    @InjectMocks
    private RoomMapService roomMapService;


    @Test
    void createRoomMap_shouldWork() {
        var id = "roomMap1";
        var roomMapContract = getRoomMapContract(id);
        when(roomMapRepository.save(roomMapContract))
                .thenReturn(roomMapContract);
        var actualResult = roomMapService.createRoomMap(roomMapContract);
        assertEquals(roomMapContract, actualResult);
    }

    @Test
    void getAllRoomMaps_shouldWork() {
        var foundRoomMaps = List.of(
                getRoomMapContract("roomMap1"),
                getRoomMapContract("roomMap2"),
                getRoomMapContract("roomMap3")
        );
        when(roomMapRepository.findAllRoomMaps())
                .thenReturn(foundRoomMaps);
        var actualResult = roomMapService.getAllRoomMaps();
        assertEquals(foundRoomMaps, actualResult);
    }

    @Test
    void getRoomMap_shouldWork() {
        var id = "roomMap1";
        var foundRoomMap = getRoomMapContract(id);
        when(roomMapRepository.findRoomMapById(id))
                .thenReturn(Optional.of(getRoomMapContract(id)));
        var actualResult = roomMapService.getRoomMap(id);
        assertEquals(foundRoomMap, actualResult);
    }

    @Test
    void getRoomMap_shouldThrow_roomMapNotFoundException() {
        var id = "notExistingRoomMap";
        var foundRoomMap = getRoomMapContract(id);
        when(roomMapRepository.findRoomMapById(id))
                .thenReturn(Optional.empty());
        assertThrows(RoomMapNotFoundException.class, () -> roomMapService.getRoomMap(id));
    }

}
