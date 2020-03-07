package httpUtils;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Date;

public class HttpResponse {
    private final BufferedOutputStream out;
    private byte[] status;
    private static byte[] serverName = "Server: Java web server\r\n".getBytes();
    private byte[] mimeTypeContent;

    private byte[] body;
    private byte[] bodyLength;
	// private HttpResponseStatusEnum responseEnum;
    private byte[] lastModifiedTime;

    public HttpResponse(final BufferedOutputStream out, final HttpResponseStatusEnum responseEnum) {
        this.out = out;
        // this.responseEnum = responseEnum;
        this.setStatus(responseEnum);
    }
    private void setStatus(final HttpResponseStatusEnum responseEnum) {
        String statusString = "";
        switch(responseEnum) {
            case HTTP200:
                statusString = "HTTP/1.1 200 OK\r\n";
                break;
            case HTTP404:
                statusString = "HTTP/1.1 404 Not Found\r\n";
                break;
            case HTTP400:
                break;
            case HTTP500:
                statusString = "HTTP/1.1 500 Internal Server Error\r\n";
                break;
            default:        
                System.out.println("Response does not match enum");

        }
        this.status = statusString.getBytes();
    }
    
    public void setLastModifiedTime(final long lastModifiedTime) {
        this.lastModifiedTime = ("Last-Modified: " + new Date(lastModifiedTime).toString() + "\r\n").getBytes();
    }
    public void setMimeType(final String mimeType) {
        
            this.mimeTypeContent = ("Content-Type: " + ((mimeType != null && mimeType.length() > 0) ? mimeType: "application/octet-stream" )  + ";  charset=utf-8\r\n").getBytes();
    }

    public void setBody(final byte[] file) {
        this.body = file;
        this.bodyLength = ("Content-Length: " + body.length + "\r\n").getBytes();
    }

    public void sendResponse() throws Error, IOException {

        if(this.status.length > 0) {
            out.write(this.status);
            final Date today = new Date();
            out.write(("Date: " + today + "\r\n").getBytes());
            out.write(HttpResponse.serverName);

            if(this.body != null && this.body.length > 0) {
                out.write(this.bodyLength);
                out.write(this.mimeTypeContent);
                out.write(this.lastModifiedTime);
                out.write("\r\n".getBytes());
                out.write(this.body);

            } else{
                out.write("\r\n".getBytes());
            }
            out.flush();
        } else {
            throw new Error("No status in the response");
        }

    }
}