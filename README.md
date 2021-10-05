<h1>Running</h1>
mvn clean install && docker build -t snapshot/flux . && docker-compose up

<h1>GET Flux</h1>
curl -i -X GET "http://localhost:8080?iterations=3&interval=500"

<h1>POST Flux</h1>
curl -i -X POST -H "Content-Type: application/json" -d "{\"iterations\":\"3\",\"interval\":\"500\"}" "http://localhost:8080"