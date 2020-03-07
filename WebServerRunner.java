import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
// import java.util.concurrent.ExecutorService;
// import java.util.concurrent.Executors;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class WebServerRunner {
    public static ConcurrentHashMap<String, Integer> fileAccessCount = new ConcurrentHashMap<String, Integer>();

    public static final int WEB_SERVER_PORT = 8888;
    // public static final int WEB_SERVER_THREADS = 1;

    private static void log(final String msg) {
        System.out.println(msg);
    }
    private static void exit(final String msg) {
        System.out.println(msg);
        System.exit(0);
      }
    
    public static void main(final String args[]) {
        
        ServerSocket webServerSocket;
        // ArrayList<Thread> pool = new ArrayList<Thread>();
        try {
            webServerSocket = new ServerSocket(WEB_SERVER_PORT);
            log("Started web server on port : " + WEB_SERVER_PORT);
   
            // ExecutorService threadPool = Executors.newFixedThreadPool(10);   

            while (true) {
                try {
                  // Got a connection request from client
                  final Socket client = webServerSocket.accept();
                  // Handle client request
                  // This processing can be in a separate Thread
                  // if we would like to handle multiple requests
                  // in parallel.
                  final Thread t = new Thread(new WebServer(client,fileAccessCount)); 
                  t.start();

                //   pool.add(t);

                // threadPool.execute(new WebServer(client));
                    
                //   log("\n\n\n No of threads: " + java.lang.Thread.activeCount());
                } catch (final Exception e) {
                  log("Error in serving content: " + e);
                //   threadPool.shutdown();     

                }
              }





            // for( int i = 0; i < WEB_SERVER_THREADS; i++) {
            // // Multi-threading 
            //     final Thread t = new Thread(new WebServer(webServerSocket)); 
            //     t.start();
            //     // t.interrupt();
            //     pool.add(t);
            // }



        } catch (final IOException e) {
            log("Error: " + e.toString());
            // for( Thread t: pool) {
            //     t.interrupt();
            // }
            exit("Shutting down server");
        }

    }
}