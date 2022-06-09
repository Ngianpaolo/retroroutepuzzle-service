package io.nigro.retroroutepuzzle.feature.roommap.model;

import lombok.Data;

import java.util.List;

@Data
public class Room {

    private Long id;

    private String name;

    private Long south;

    private Long north;

    private Long west;

    private Long east;

    private List<String> objects;

}
