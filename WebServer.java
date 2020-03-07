
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLConnection;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import httpUtils.HttpRequest;
import httpUtils.HttpResponse;
import httpUtils.HttpResponseStatusEnum;;

public class WebServer implements Runnable {
  private final Socket webServerSocket;
  
  // private boolean runningState = true;
  
  static ConcurrentHashMap<String, Integer> fileAccessCount;
  public WebServer(final Socket webServerSocket, final ConcurrentHashMap<String, Integer> fileAccessCount) {
    this.webServerSocket = webServerSocket;
    WebServer.fileAccessCount = fileAccessCount;
  }

  public void run() {

    try {
      // use serve instead of listen
      listen();
    } catch (final Exception e) {
      log("Error: " + e);
    }
  }

  protected void listen() {

      try {
        // Got a connection request from client
        // final Socket client = webServerSocket.accept();
        // Handle client request
        // This processing can be in a separate Thread
        // if we would like to handle multiple requests
        // in parallel.
        serveClient(webServerSocket);

      } catch (final Exception e) {
        log("Error in serving content: " + e);
      }
    // }
  }

  protected void serveClient(final Socket client) throws IOException {

    try {
      
      final HttpRequest request = readClientRequest(client);
      writeResponse(client, request);

    } catch (final IOException e) {

      final HttpResponse response = new HttpResponse( new BufferedOutputStream(client.getOutputStream()), HttpResponseStatusEnum.HTTP500);
      response.sendResponse();
      log("Error: socket read/write " + e.toString());
      
    }
 
  }

  protected HttpRequest readClientRequest(final Socket client) throws IOException {


    final BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
    final HttpRequest request = new HttpRequest(in);
    return request;

  }
  private void logDetails( final String path, final String clientIP) {
    synchronized(WebServerRunner.fileAccessCount) {
      int count = 1; 
      if(WebServer.fileAccessCount.containsKey(path)) {
        count =  WebServer.fileAccessCount.get(path) + 1;
      }
      WebServer.fileAccessCount.put(path, count);
      // log("Times " + filePath + " called is : " + count);
      log( path + "\t\t| " + clientIP + "\t| "  + count);
    }
  }
  protected void writeResponse(final Socket client, final HttpRequest request) throws IOException {

    // get file to be served
    final String servePath = "./www/" + request.getPath();
    final BufferedOutputStream socketOut = new BufferedOutputStream(client.getOutputStream());
    HttpResponse response; 
    try {

      final String mimeType = URLConnection.guessContentTypeFromName(servePath);
      final Path filePath = Paths.get(servePath);
      final boolean fileExists = Files.exists(filePath);
      if(fileExists) {
        
        final byte[] body = Files.readAllBytes(filePath);
        response = new HttpResponse(socketOut, HttpResponseStatusEnum.HTTP200);
        response.setLastModifiedTime(Files.getLastModifiedTime(filePath).toMillis());
        response.setMimeType(mimeType);
        response.setBody(body);
        logDetails(request.getPath() ,client.getRemoteSocketAddress().toString());

      } else {
        log("File not found: " + request.getPath());
        response = new HttpResponse(socketOut, HttpResponseStatusEnum.HTTP404);
      }

    } catch ( final IOException e ) {
      log("Error: " + e.toString());

      log("Error in creating response, sending 500");

      response = new HttpResponse(socketOut, HttpResponseStatusEnum.HTTP500);
    }

    response.sendResponse();
    client.close();

  }

  private static void log(final String msg) {
    System.out.println(msg);
  }

}
