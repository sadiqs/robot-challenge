# Robot Challenge

The task is implemented as a multi-module project.
 
 - `core`
   Acts as a library, provides essential functionality that
   can used to implement user consumable applications like APIs and CLIs.

 - `web`
   Uses `core` library to provide a text based HTTP API.

## Run Application
Use the below command to run the application,
```shell
./mvnw install && ./mvnw spring-boot:run -pl web
```

## Use Application
Can invoke the `/robot` endpoint with any tool. A bash script which uses `curl` command to 
invoke the API is provided in the `run/robot.sh` file for reference. 