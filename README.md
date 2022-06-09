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
