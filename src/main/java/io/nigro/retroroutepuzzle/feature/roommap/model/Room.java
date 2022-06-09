package io.nigro.retroroutepuzzle.feature.roommap.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class Room {

    @NotNull
    private Long id;

    @NotBlank
    private String name;

    private Long south;

    private Long north;

    private Long west;

    private Long east;

    private List<Item> objects;

}
