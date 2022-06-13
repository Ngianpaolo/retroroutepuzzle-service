# RETRO ROUTE PUZZLE API application

## Docker Build

    docker build -t mytest .

## Build

    docker run -v $(pwd):/mnt -p 9090:9090 -w /mnt mytest ./scripts/build.sh

## Test

    docker run -v $(pwd):/mmt -p 9090:9090 -w /mnt mytest ./scripts/tests.sh

## Run

    docker run -v $(pwd):/mnt -p 9090:9090 -w /mnt mytest ./scripts/run.sh

# API

The API to the example app is described below.

## ROUTE SEARCH API

### Perform DFS search by passing list of rooms in parameters
#### Request

```http
POST localhost:9090/api/route/search/{searchType}
```

    curl --location --request POST 'localhost:9090/api/route/search/DFS' \
            --header 'Content-Type: application/json' \
            --data-raw '{ 
                "startRoomId": 2, 
                "itemToCollect": ["Knife", "Potted Plant"],
                "rooms":[{"id":1,"name":"Hallway","north":2,"objects":[]},{"id":2,"name":"Dining Room","south":1,"west":3,"east":4,"objects":[]},{"id":3,"name":"Kitchen","east":2,"objects":[{"name":"Knife"}]},{"id":4,"name":"Sun Room","west":2,"objects":[{"name":"Potted Plant"}]}] 
                }'


#### Response Body (Content-Type:"text/plain")

     -------------------------------------------------------------------- 
	 ID     	 Room               	 Object  collected         
	 -------------------------------------------------------------------- 
	 2       	 Dining  Room       	 None                                 
	 1       	 Hallway               	 None                                 
	 2       	 Dining  Room       	 None                                 
	 4       	 Sun  Room             	 Potted  Plant                 
	 2       	 Dining  Room       	 None                                 
	 3       	 Kitchen               	 Knife                               

### Perform BFS search by passing list of rooms in parameters
#### Request

```http
POST localhost:9090/api/route/search/{searchType}
```

    curl --location --request POST 'localhost:9090/api/route/search/BFS' \
            --header 'Content-Type: application/json' \
            --data-raw '{
                "startRoomId": 2,
                "itemToCollect": ["Knife", "Potted Plant"],
                "rooms":[{"id":1,"name":"Hallway","north":2,"objects":[]},{"id":2,"name":"Dining Room","south":1,"west":3,"east":4,"objects":[]},{"id":3,"name":"Kitchen","east":2,"objects":[{"name":"Knife"}]},{"id":4,"name":"Sun Room","west":2,"objects":[{"name":"Potted Plant"}]}]
                }'

#### Response Body (Content-Type:"text/plain")
	 -------------------------------------------------------------------- 
	 ID     	 Room               	 Object  collected         
	 -------------------------------------------------------------------- 
	 2       	 Dining  Room       	 None                                 
	 1       	 Hallway               	 None                                 
	 2       	 Dining  Room       	 None                                 
	 4       	 Sun  Room             	 Potted  Plant                 
	 2       	 Dining  Room       	 None                                 
	 3       	 Kitchen               	 Knife                               


### Perform DFS search on a saved roomMap
#### Request

```http
POST localhost:9090/api/route/search/{searchType}/rooms/{roomMapId}
```

    curl --location --request POST 'localhost:9090/api/route/search/DFS/rooms/RoomMap2' \
            --header 'Content-Type: application/json' \
            --data-raw '{
                "startRoomId": 7,
                "itemToCollect": ["Knife", "Potted Plant"]
                }'


#### Response Body (Content-Type:"text/plain")

     -------------------------------------------------------------------- 
	 ID     	 Room               	 Object  collected         
	 -------------------------------------------------------------------- 
	 7       	 Living  room       	 Potted  Plant                 
	 1       	 Hallway               	 None                                 
	 2       	 Dining  Room       	 None                                 
	 4       	 Sun  Room             	 None                                 
	 6       	 Bathroom             	 None                                 
	 5       	 Bedroom               	 None                                 
	 6       	 Bathroom             	 None                                 
	 4       	 Sun  Room             	 None                                 
	 2       	 Dining  Room       	 None                                 
	 3       	 Kitchen               	 Knife                               

### Perform BFS search on a saved roomMap
#### Request

```http
POST localhost:9090/api/route/search/{searchType}/rooms/{roomMapId}
```

    curl --location --request POST 'localhost:9090/api/route/search/BFS/rooms/RoomMap2' \
            --header 'Content-Type: application/json' \
            --data-raw '{
                "startRoomId": 7,
                "itemToCollect": ["Knife", "Potted Plant"]
                }'

#### Response Body (Content-Type:"text/plain")

     -------------------------------------------------------------------- 
	 ID     	 Room               	 Object  collected         
	 -------------------------------------------------------------------- 
	 7       	 Living  room       	 Potted  Plant                 
	 1       	 Hallway               	 None                                 
	 7       	 Living  room       	 None                                 
	 4       	 Sun  Room             	 None                                 
	 2       	 Dining  Room       	 None                                 
	 4       	 Sun  Room             	 None                                 
	 6       	 Bathroom             	 None                                 
	 5       	 Bedroom               	 None                                 
	 6       	 Bathroom             	 None                                 
	 4       	 Sun  Room             	 None                                 
	 2       	 Dining  Room       	 None                                 
	 3       	 Kitchen               	 Knife                               


## ROOM-MAP API

### Create or replace a roomMap (RoomMap will be saved under the folder ./storage/room_maps as JSON), if id in the request body is omitted will be assigned defaultRoomMap (default)
#### Request

```http
POST localhost:9090/api/room-map
```

    curl --location --request POST 'localhost:9090/api/room-map' \
            --header 'Content-Type: application/json' \
            --data-raw '{"id":"defaultRoomMap","rooms":[{"id":1,"name":"Hallway","north":2,"objects":[]},{"id":2,"name":"Dining Room","south":1,"west":3,"east":4,"objects":[]},{"id":3,"name":"Kitchen","east":2,"objects":[{"name":"Knife"}]},{"id":4,"name":"Sun Room","west":2,"objects":[{"name":"Potted Plant"}]}]}'


#### Response Body

    {
		"id":"defaultRoomMap",
		"rooms":[{"id":1,"name":"Hallway","south":null,"north":2,"west":null,"east":null,"objects":[]},{"id":2,"name":"Dining Room","south":1,"north":null,"west":3,"east":4,"objects":[]},{"id":3,"name":"Kitchen","south":null,"north":null,"west":null,"east":2,"objects":[{"name":"Knife"}]},{"id":4,"name":"Sun Room","south":null,"north":null,"west":2,"east":null,"objects":[{"name":"Potted Plant"}]}]
	}

### Get all stored roomMaps

#### Request

```http
GET localhost:9090/api/room-map
```

    curl --location --request GET 'localhost:9090/api/room-map'


#### Response Body

    [
		{
			"id":"defaultRoomMap",
			"rooms":[{"id":1,"name":"Hallway","south":null,"north":2,"west":null,"east":null,"objects":[]},{"id":2,"name":"Dining Room","south":1,"north":null,"west":3,"east":4,"objects":[]},{"id":3,"name":"Kitchen","south":null,"north":null,"west":null,"east":2,"objects":[{"name":"Knife"}]},{"id":4,"name":"Sun Room","south":null,"north":null,"west":2,"east":null,"objects":[{"name":"Potted Plant"}]}]
		},
		{
			"id":"test1",
			"rooms":[{"id":1,"name":"Hallway","south":null,"north":2,"west":null,"east":null,"objects":[]},{"id":2,"name":"Dining Room","south":1,"north":null,"west":3,"east":4,"objects":[]},{"id":3,"name":"Kitchen","south":null,"north":null,"west":null,"east":2,"objects":[{"name":"Knife"}]},{"id":4,"name":"Sun Room","south":null,"north":null,"west":2,"east":null,"objects":[{"name":"Potted Plant"}]}]
		}
	]

### Get a specific saved roomMap

#### Request

```http
GET localhost:9090/api/room-map/{roomMapId}
```

    curl --location --request GET 'localhost:9090/api/room-map/defaultRoomMap'


#### Response Body

    {
		"id":"defaultRoomMap",
		"rooms":[{"id":1,"name":"Hallway","south":null,"north":2,"west":null,"east":null,"objects":[]},{"id":2,"name":"Dining Room","south":1,"north":null,"west":3,"east":4,"objects":[]},{"id":3,"name":"Kitchen","south":null,"north":null,"west":null,"east":2,"objects":[{"name":"Knife"}]},{"id":4,"name":"Sun Room","south":null,"north":null,"west":2,"east":null,"objects":[{"name":"Potted Plant"}]}]
	}

## ROUTE-RESULT API

### Get list of all route result ids
#### Request

```http
GET localhost:9090/api/route/results
```

    curl --location --request GET 'localhost:9090/api/route/results'


#### Response Body

        {
        "routeResultIds":
                [
                "1655022906_start_room_id2_bfs",
                "1655022981_start_room_id2_dfs",
                "1655023602_start_room_id2_dfs",
                "1655023609_start_room_id2_dfs",
                "1655023676_start_room_id2_bfs"
		        ]
	    }

### Get a specific route result by id

#### Request

```http
GET localhost:9090/api/route/results/{routeResultId}
```

    curl --location --request GET 'localhost:9090/api/route/results/1655022906_start_room_id2_bfs'


#### Response Body (Content-Type:"text/plain")

     -------------------------------------------------------------------- 
	 ID     	 Room               	 Object  collected         
	 -------------------------------------------------------------------- 
	 2       	 Dining  Room       	 None                                 
	 1       	 Hallway               	 None                                 
	 2       	 Dining  Room       	 None                                 
	 4       	 Sun  Room             	 None                                 
	 2       	 Dining  Room       	 None                                 
	 3       	 Kitchen               	 Knife                               
	 2       	 Dining  Room       	 None                                 
	 5       	 Bedroom               	 None                                 
	 6       	 Bathroom             	 None                                 
	 5       	 Bedroom               	 None                                 
	 2       	 Dining  Room       	 None                                 
	 1       	 Hallway               	 None                                 
	 7       	 Living  room       	 Potted  Plant                 


### Get last n route results

#### Request

```http
GET 'localhost:9090/api/route/results/historical?size={sizePage}'
```

    curl --location --request GET 'localhost:9090/api/route/results/historical?size=3'


#### Response Body (Content-Type:"text/plain")

     --------------------------------------------------------------------
     ------------------ 1655117888_start_room_id7_dfs -------------------
     -------------------------------------------------------------------- 
     ID     	 Room               	 Object  collected         
     -------------------------------------------------------------------- 
     7       	 Living  room       	 Potted  Plant                 
     1       	 Hallway               	 None                                 
     2       	 Dining  Room       	 None                                 
     4       	 Sun  Room             	 None                                 
     6       	 Bathroom             	 None                                 
     5       	 Bedroom               	 None                                 
     6       	 Bathroom             	 None                                 
     4       	 Sun  Room             	 None                                 
     2       	 Dining  Room       	 None                                 
     3       	 Kitchen               	 Knife                               
     
     --------------------------------------------------------------------
     ------------------ 1655117920_start_room_id7_dfs -------------------
     -------------------------------------------------------------------- 
     ID     	 Room               	 Object  collected         
     -------------------------------------------------------------------- 
     7       	 Living  room       	 Potted  Plant                 
     1       	 Hallway               	 None                                 
     2       	 Dining  Room       	 None                                 
     4       	 Sun  Room             	 None                                 
     6       	 Bathroom             	 None                                 
     5       	 Bedroom               	 None                                 
     6       	 Bathroom             	 None                                 
     4       	 Sun  Room             	 None                                 
     2       	 Dining  Room       	 None                                 
     3       	 Kitchen               	 Knife                               
     
     --------------------------------------------------------------------
     ------------------ 1655117962_start_room_id7_bfs -------------------
     -------------------------------------------------------------------- 
     ID     	 Room               	 Object  collected         
     -------------------------------------------------------------------- 
     7       	 Living  room       	 Potted  Plant                 
     1       	 Hallway               	 None                                 
     7       	 Living  room       	 None                                 
     4       	 Sun  Room             	 None                                 
     2       	 Dining  Room       	 None                                 
     4       	 Sun  Room             	 None                                 
     6       	 Bathroom             	 None                                 
     5       	 Bedroom               	 None                                 
     6       	 Bathroom             	 None                                 
     4       	 Sun  Room             	 None                                 
     2       	 Dining  Room       	 None                                 
     3       	 Kitchen               	 Knife                               

