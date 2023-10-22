import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.StringTokenizer;
import com.google.gson.*;

class WebServer
{
 public static void main (String args[]) throws Exception {
     String requestMessageLine;
     String fileName;
     // check if a port number is given as the first command line argument
     // if not argument is given, use port number 6789
     int myPort = 8080;
     if (args.length > 0) {
         try {
             myPort = Integer.parseInt(args[0]);
         } catch (ArrayIndexOutOfBoundsException e) {
             System.out.println("Need port number as argument");
             System.exit(-1);
         } catch (NumberFormatException e) {
             System.out.println("Please give port number as integer.");
             System.exit(-1);
         }
     }

     // set up connection socket
     ServerSocket listenSocket = new ServerSocket(myPort);

     // listen (i.e. wait) for connection request
     System.out.println("Web server waiting for request on port " + myPort);

     while (true) {
         Socket connectionSocket = listenSocket.accept();
         Object object;
         // set up the read and write end of the communication socket
         BufferedReader inFromClient = new BufferedReader(
                 new InputStreamReader(connectionSocket.getInputStream()));
         DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
         // retrieve first line of request and set up for parsing
         requestMessageLine = inFromClient.readLine();
         System.out.println("Request: " + requestMessageLine);
         if(requestMessageLine != null){
             StringTokenizer tokenizedLine = new StringTokenizer(requestMessageLine, " /");
             if (tokenizedLine.nextToken().equals("GET")) {
                 fileName = tokenizedLine.nextToken();
                 System.out.println(tokenizedLine.countTokens());
                 // remove leading slash from line if exists
                 if (fileName.startsWith("/") == true)
                     fileName = fileName.substring(1);
                 System.out.println("load: " + fileName);
                 if(tokenizedLine.countTokens() > 2){
                     if(fileName.equals("ticket")){
                         int id = Integer.parseInt(tokenizedLine.nextToken());
                         Ticket ticket = Application.getInstance().getDataAdapter().loadTicket(id);
                         System.out.println(ticket);
                         System.out.println(ResponseCode.SUCCESS.getMsg());
                         System.out.println(ResponseCode.SUCCESS.getCode());

                         Result result =new Result(ResponseCode.SUCCESS.getCode(),ResponseCode.SUCCESS.getMsg(),ticket);
                         Gson gson = new Gson();
                         String json = gson.toJson(result);
                         outToClient.writeBytes("HTTP/1.1 200 OK\r\n");
                         outToClient.writeBytes("Content-Type: text/html\r\n");
                         outToClient.writeBytes("Content-Length: " + json.length() + "\r\n");
                         outToClient.writeBytes("\r\n");
                         outToClient.writeBytes(json);
                         System.out.println("finishSend");
                         System.out.println(json);
                         System.out.println(result);
                     }
                 }
             }
         }
         // read and print out the rest of the request
         connectionSocket.close();
     }
 }
/*   else
     {
      System.out.println ("Bad Request Message");
     }  */


}

      
          
