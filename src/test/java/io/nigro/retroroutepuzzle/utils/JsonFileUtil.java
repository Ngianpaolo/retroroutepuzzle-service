package io.nigro.retroroutepuzzle.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.nigro.retroroutepuzzle.feature.roommap.contract.RoomMapContract;

import java.io.File;
import java.io.IOException;

public class JsonFileUtil {

    private final static ObjectMapper objectMapper = new ObjectMapper();
    private final static String path = "./src/test/resources/json/";

    public static RoomMapContract loadMapFromJsonFile(String filename) {
        File file = new File(String.format("%s%s.json", path, filename));
        if (!file.exists()) {
            throw new IllegalStateException();
        }
        return convertJsonFileToRoomMap(file);
    }

    private static RoomMapContract convertJsonFileToRoomMap(File jsonFile) {
        try {
            return objectMapper.readValue(jsonFile, RoomMapContract.class);
        } catch (IOException e) {
            return null;
        }
    }
}
