curl -XPOST "http://localhost:8001/services" --header "Content-Type: application/json" --data '
{
  "name": "testy2",       
  "url": "http://host.docker.internal:8080/"
}'

curl -XPOST "http://localhost:8001/services/testy2/routes" --header "Content-Type: application/json" --data '
{
  "paths": ["/ap/testy"]
}
'
