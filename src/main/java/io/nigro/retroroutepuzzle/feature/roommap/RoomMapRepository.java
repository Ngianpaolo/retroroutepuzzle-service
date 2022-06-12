package io.nigro.retroroutepuzzle.feature.roommap;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nigro.retroroutepuzzle.feature.roommap.contract.RoomMapContract;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final static String path = "./storage/room_maps/";
    private final static String defaultRoomMapId = "defaultRoomMap";

    private final ObjectMapper objectMapper;

    @Autowired
    public RoomMapRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public RoomMapContract save(RoomMapContract request) {
        request.setId(Strings.isNotBlank(request.getId()) ? request.getId() : defaultRoomMapId);
        try {
            objectMapper.writeValue(new File(path + "" + defaultRoomMapId + ".json"), request);
            return request;
        } catch (IOException e) {
            log.error("", e);
            //TODO fare eccezione dedicata
            throw new RuntimeException();
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
