package io.nigro.retroroutepuzzle.feature.roommap.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
