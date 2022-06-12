package io.nigro.retroroutepuzzle.feature.search;

import io.nigro.retroroutepuzzle.feature.roommap.model.Room;
import io.nigro.retroroutepuzzle.feature.route.model.RouteEvent;
import io.nigro.retroroutepuzzle.feature.search.bfs.RoomBfsTreeSearch;
import io.nigro.retroroutepuzzle.feature.search.dfs.RoomDfsTreeSearch;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteSearchService {

    public List<RouteEvent> getRouteByRoomRootAndItemsToFind(List<Room> rooms,
                                                             Long roomId,
                                                             List<String> itemToCollect,
                                                             RoomTreeSearchType searchType) {
        RoomTreeSearch roomTreeSearch;
        switch (searchType) {
            case DFS:
                roomTreeSearch = new RoomDfsTreeSearch(rooms);
                break;
            case BFS:
                roomTreeSearch = new RoomBfsTreeSearch(rooms);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + searchType);
        }

        return roomTreeSearch.getRouteByRoomRootAndItemsToFind(roomId, itemToCollect);
    }

}
