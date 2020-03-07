
package httpUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class HttpRequest {
  private final BufferedReader requestReader;
  private String requestType = "";
  private String requestProtocol = "";

  private String fullRequestPath = "";
  private String path = "";
  private String queryParams = "";

  public HttpRequest( final BufferedReader requestReader ) throws IOException {
    this.requestReader = requestReader;
    parse();
  }

  private static void log(final String msg) {
    System.out.println(msg);
  }
  
  
  private boolean parse() throws IOException {

    final String firstLine = this.requestReader.readLine();
    // log(firstLine);

    final StringTokenizer tokenizer = new StringTokenizer(firstLine);
    final String firstHeaderLineChunks[] = new String[3];
    for(int i = 0; i < 3; i++ ) {
      if(tokenizer.hasMoreTokens()){
        firstHeaderLineChunks[i] = tokenizer.nextToken();
      } else {
        return false;
      }
    }

    this.requestType = firstHeaderLineChunks[0];
    this.fullRequestPath = (firstHeaderLineChunks[1] != null) ? firstHeaderLineChunks[1] : "";
    this.requestProtocol = firstHeaderLineChunks[2];
    
    if (fullRequestPath.indexOf("?") == -1)  {
      this.path = fullRequestPath;
    } else {
      this.path = fullRequestPath.substring(0, fullRequestPath.indexOf("?"));
      this.queryParams = fullRequestPath.substring(fullRequestPath.indexOf("?")+1);
    }

    // log("path : " + this.path);
    // log("requestParams : " + this.queryParams);

    if(this.path.equals("/")) {
      this.path = "/index.html";
    }

    // read the data sent. We basically ignore it,
    // stop reading once a blank line is hit. This
    // blank line signals the end of the client HTTP
    // headers.
    // String str = ".";
    // while (!str.equals("")) {
    //   str = this.requestReader.readLine();
    //   // log(str);
    // }
    return true;

  }
  public String getPath() {
    return path;
  }
}
