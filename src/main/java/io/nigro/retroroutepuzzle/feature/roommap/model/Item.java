package io.nigro.retroroutepuzzle.feature.roommap.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class Item {

    @NotBlank
    private String name;

}
