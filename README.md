# retroroutepuzzle-service

# How to run

    docker build -t mytest .
    docker run -v $(pwd):/mnt -p 9090:9090 -w /mnt mytest ./scripts/build.sh
    docker run -v $(pwd):/mmt -p 9090:9090 -w /mnt mytest ./scripts/tests.sh
    docker run -v $(pwd):/mnt -p 9090:9090 -w /mnt mytest ./scripts/run.sh

# Windows command

    MSYS_NO_PATHCONV=1  docker run -v $(pwd):/mnt -p 9090:9090 -w /mnt mytest ./scripts/build.sh
    MSYS_NO_PATHCONV=1  docker run -v $(pwd):/mmt -p 9090:9090 -w /mnt mytest ./scripts/tests.sh
    MSYS_NO_PATHCONV=1  docker run -v $(pwd):/mnt -p 9090:9090 -w /mnt mytest ./scripts/run.sh

# How it works

    APIs LIST
        For convenience you could import the postman collection, it is located under the postman_export folder in the project root

    ROOM-MAP
    POST - localhost:9090/api/room-map
        Create or replace a roomMap (RoomMap will be saved under the folder storage/room_maps as JSON)
        Call Example:
            curl --location --request POST 'localhost:9090/api/room-map' \
            --header 'Content-Type: application/json' \
            --data-raw '{"rooms":[{"id":1,"name":"Hallway","north":2,"objects":[]},{"id":2,"name":"Dining Room","south":1,"west":3,"east":4,"objects":[]},{"id":3,"name":"Kitchen","east":2,"objects":[{"name":"Knife"}]},{"id":4,"name":"Sun Room","west":2,"objects":[{"name":"Potted Plant"}]}]}'
    
    GET - localhost:9090/api/room-map
        Get all stored roomMaps
        Call Example:
            curl --location --request GET 'localhost:9090/api/room-map'

    GET - localhost:9090/api/room-map/{roomMapId}
        Get a specific saved roomMap
        Call Example:
            curl --location --request GET 'localhost:9090/api/room-map/defaultRoomMap'

    ROUTE SEARCH
    POST - localhost:9090/api/route/search/{searchType}
        Perform DFS search by passing list of rooms in parameters
        Call Example:
            curl --location --request POST 'localhost:9090/api/route/search/DFS' \
            --header 'Content-Type: application/json' \
            --data-raw '{ 
                "startRoomId": 2, 
                "itemToCollect": ["Knife"] , 
                "rooms":[{"id":1,"name":"Hallway","north":2,"objects":[]},{"id":2,"name":"Dining Room","south":1,"west":3,"east":4,"objects":[]},{"id":3,"name":"Kitchen","east":2,"objects":[{"name":"Knife"}]},{"id":4,"name":"Sun Room","west":2,"objects":[{"name":"Potted Plant"}]}] 
                }'
    
        Perform BFS search by passing list of rooms in parameters
        Call Example:
            curl --location --request POST 'localhost:9090/api/route/search/BFS' \
            --header 'Content-Type: application/json' \
            --data-raw '{
                "startRoomId": 2,
                "itemToCollect": ["Knife"] ,
                "rooms":[{"id":1,"name":"Hallway","north":2,"objects":[]},{"id":2,"name":"Dining Room","south":1,"west":3,"east":4,"objects":[]},{"id":3,"name":"Kitchen","east":2,"objects":[{"name":"Knife"}]},{"id":4,"name":"Sun Room","west":2,"objects":[{"name":"Potted Plant"}]}]
                }'

    POST - localhost:9090/api/route/search/{searchType}/rooms/{roomMapId}
        Perform DFS search on a saved roomMap
        Call Example:
            curl --location --request POST 'localhost:9090/api/route/search/DFS/rooms/defaultRoomMap' \
            --header 'Content-Type: application/json' \
            --data-raw '{
                "startRoomId": 2,
                "itemToCollect": ["Knife"]
                }'

        Perform BFS search on a saved roomMap
        Call Example:
            curl --location --request POST 'localhost:9090/api/route/search/BFS/rooms/defaultRoomMap' \
            --header 'Content-Type: application/json' \
            --data-raw '{
                "startRoomId": 2,
                "itemToCollect": ["Knife"]
                }'

    ROUTE RESULT
        curl --location --request GET 'localhost:9090/api/route/results'
        curl --location --request GET 'localhost:9090/api/route/results/1655022906_start_room_id2_bfs'
        curl --location --request GET 'localhost:9090/api/route/results/historical?size=5'