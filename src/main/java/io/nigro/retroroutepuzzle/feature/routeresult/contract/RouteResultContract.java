package io.nigro.retroroutepuzzle.feature.routeresult.contract;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RouteResultContract {

    private List<String> routeResultIds;

}
