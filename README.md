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

    # Create a new roomMap
    curl --location --request POST 'localhost:9090/api/room-map' \
    --header 'Content-Type: application/json' \
    --data-raw '{"rooms":[{"id":1,"name":"Hallway","north":2,"objects":[]},{"id":2,"name":"Dining Room","south":1,"west":3,"east":4,"objects":[]},{"id":3,"name":"Kitchen","east":2,"objects":[{"name":"Knife"}]},{"id":4,"name":"Sun Room","west":2,"objects":[{"name":"Potted Plant"}]}]}'

    # Get all roomMaps
    curl --location --request GET 'localhost:9090/api/room-map

    # Perform DFS search by passing list of rooms in parameters
    curl --location --request POST 'localhost:9090/api/route/search/DFS' \
    --header 'Content-Type: application/json' \
    --data-raw '{"startRoomId": 2,"itemToCollect": ["Knife"] ,"rooms":[{"id":1,"name":"Hallway","north":2,"objects":[]},{"id":2,"name":"Dining Room","south":1,"west":3,"east":4,"objects":[]},{"id":3,"name":"Kitchen","east":2,"objects":[{"name":"Knife"}]},{"id":4,"name":"Sun Room","west":2,"objects":[{"name":"Potted Plant"}]}]}'

    # Perform BFS search by passing list of rooms in parameters
    curl --location --request POST 'localhost:9090/api/route/search/BFS' \
    --header 'Content-Type: application/json' \
    --data-raw '{"startRoomId": 2,"itemToCollect": ["Knife"] ,"rooms":[{"id":1,"name":"Hallway","north":2,"objects":[]},{"id":2,"name":"Dining Room","south":1,"west":3,"east":4,"objects":[]},{"id":3,"name":"Kitchen","east":2,"objects":[{"name":"Knife"}]},{"id":4,"name":"Sun Room","west":2,"objects":[{"name":"Potted Plant"}]}]}'

    # Perform DFS search with saved roomMap
    curl --location --request POST 'localhost:9090/api/route/search/DFS/rooms/defaultRoomMap' \
    --header 'Content-Type: application/json' \
    --data-raw '{"startRoomId": 2,"itemToCollect": ["Knife"]}'

    # Perform BFS search with saved roomMap
    curl --location --request POST 'localhost:9090/api/route/search/BFS/rooms/defaultRoomMap' \
    --header 'Content-Type: application/json' \
    --data-raw '{"startRoomId": 2,"itemToCollect": ["Knife"]}'