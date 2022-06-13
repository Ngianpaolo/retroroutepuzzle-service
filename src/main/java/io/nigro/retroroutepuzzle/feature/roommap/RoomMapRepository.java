package io.nigro.retroroutepuzzle.feature.roommap;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nigro.retroroutepuzzle.exception.RoomMapCreationException;
import io.nigro.retroroutepuzzle.feature.roommap.contract.RoomMapContract;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class RoomMapRepository {

    private final static String defaultRoomMapId = "defaultRoomMap";

    private final ObjectMapper objectMapper;
    private final String path;

    @Autowired
    public RoomMapRepository(ObjectMapper objectMapper,
                             @Value("${io.nigro.retroroutepuzzle.storage.roommaps:./storage/room_maps/}") String path) {
        this.objectMapper = objectMapper;
        this.path = path;
    }

    public RoomMapContract save(RoomMapContract request) {
        var id = Strings.isNotBlank(request.getId()) ? request.getId() : defaultRoomMapId;
        request.setId(id);
        try {
            objectMapper.writeValue(new File(path + "" + id + ".json"), request);
            return request;
        } catch (IOException e) {
            throw new RoomMapCreationException();
        }
    }

    public List<RoomMapContract> findAllRoomMaps() {
        return getAllSavedJson().stream()
                .map(this::convertJsonFileToRoomMap)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private List<File> getAllSavedJson() {
        File dir = new File(path);
        File[] jsonList = dir.listFiles((dir1, name) -> name.endsWith(".json"));
        return Arrays.asList(Objects.requireNonNull(jsonList));
    }

    public Optional<RoomMapContract> findRoomMapById(String id) {
        File file = new File(String.format("%s%s.json", path, id));
        if (!file.exists()) {
            return Optional.empty();
        }
        return Optional.ofNullable(convertJsonFileToRoomMap(file));
    }

    private RoomMapContract convertJsonFileToRoomMap(File jsonFile) {
        try {
            return objectMapper.readValue(jsonFile, RoomMapContract.class);
        } catch (IOException e) {
            log.warn("Cannot retrieve json file {} because it's bad formatted.", jsonFile.getName());
            return null;
        }
    }
}
