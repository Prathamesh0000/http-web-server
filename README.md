# Http server in java

## Prerequisites 
openjdk 11.0.6 2020-01-14
OpenJDK Runtime Environment (build 11.0.6+10-post-Ubuntu-1ubuntu118.04.1)
OpenJDK 64-Bit Server VM (build 11.0.6+10-post-Ubuntu-1ubuntu118.04.1, mixed mode, sharing)

## How to run web server

```sh
$ make clean
$ make
$ make run
```


### WebServerRunner.java
This class is used to create port on specified port number and listen for any request
If any request found it creates a new thread and passes WebServer Object with the Socket of accept() call

### WebServer.java
This is a Thread class which is responsible for handling the each request, getting files, and serving them to user
After content is served the Thread and connection is closed

### httpUtils
#### HttpRequest
This class parses raw http request and creates HttpRequest object.
If there is no path mentioned in the request it switches to default path("index.html")

#### HttpResponse
This class is responsible for creating raw http response objects.
It can create 200, 404, and 500 response packets



## Performance test
Apache perfoemance tester was used to test parallel connections on this web servers
The test was done for two resource both parallelly using following command
```sh
$ (ab -k -c 25 -n 1000 http://localhost:8888/pdf-sample.pdf  & ab -k -c 25 -n 1000 http://localhost:8888/asf-logo.gif ) > performance-test.txt
```
There were total 25 parallel connections and served 1000 requests for each resourse 


The performance test results can be found in performance-test.txt and log can be found in log.txt