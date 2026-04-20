SAMPLE CMDS:

curl -X GET http://localhost:8080/api/v1/rooms

Output:
[]

curl -X POST http://localhost:8080/api/v1/rooms -H "Content-Type: application/json" -d "{\"name\": \"Library Quiet Study\", \"capacity\": 50}"

curl -X GET http://localhost:8080/api/v1/rooms

Output:

[{"capacity":50,"id":"5fb4e030-72ee-44c5-bc46-2e9c7948d97a","name":"Library Quiet Study","sensorIds":[]}]
