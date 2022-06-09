# retroroutepuzzle-service
# How to run
    docker build -t mytest .
    docker run -v $(pwd):/mnt -p 9090:9090 -w /mnt mytest ./scripts/build.sh
    docker run -v $(pwd):/mmt -p 9090:9090 -w /mnt mytest ./scripts/tests.sh
    docker run -v $(pwd):/mnt -p 9090:9090 -w /mnt mytest ./scripts/run.sh

# Windows command
    MSYS_NO_PATHCONV=1 docker run -v $(pwd):/mnt -p 9090:9090 -w /mnt mytest ./scripts/build.sh
    MSYS_NO_PATHCONV=1 docker run -v $(pwd):/mmt -p 9090:9090 -w /mnt mytest ./scripts/tests.sh
    MSYS_NO_PATHCONV=1 docker run -v $(pwd):/mnt -p 9090:9090 -w /mnt mytest ./scripts/run.sh

# How it works
    # Create a new roomMap
    curl --location --request POST 'localhost:8080/retro-route-puzzle-service/room-map' \
    --header 'Content-Type: application/json' \
    --data-raw '{"rooms":[{"id":1,"name":"Hallway","north":2,"objects":[]},{"id":2,"name":"Dining Room","south":1,"west":3,"east":4,"objects":[]},{"id":3,"name":"Kitchen","east":2,"objects":[{"name":"Knife"}]},{"id":4,"name":"Sun Room","west":2,"objects":[{"name":"Potted Plant"}]}]}'

    # Get all roomMaps
    curl --location --request GET 'localhost:8080/retro-route-puzzle-service/room-map