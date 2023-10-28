import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebServer {

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(8080);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // Read the request line
            String requestLine = in.readLine();
            if(requestLine == null){
                continue;
            }
            String[] requestParts = requestLine.split(" ");
            String httpMethod = requestParts[0];
            String apiPath = requestParts[1];

            // Read the headers
            Map<String, String> headers = new HashMap<>();
            String headerLine;
            while (!(headerLine = in.readLine()).isEmpty()) {
                String[] headerParts = headerLine.split(": ", 2);
                headers.put(headerParts[0], headerParts[1]);
            }
            Gson gson = new Gson();

            if ("GET".equals(httpMethod)) {
                if (apiPath.matches("^/ticket/\\d+$")) {
                    int ticketId = Integer.parseInt(apiPath.split("/")[2]);
                    Ticket ticket = Application.getInstance().getDataAdapter().loadTicket(ticketId);
                    String ticketData = gson.toJson(ticket);
                    sendResponse(clientSocket, ticketData, "application/json");
                } else if (apiPath.matches("^/event/\\d+$")) {
                    int eventId = Integer.parseInt(apiPath.split("/")[2]);
                    Event event = Application.getInstance().getDataAdapter().loadEvent(eventId);
                    String eventData = gson.toJson(event);
                    sendResponse(clientSocket, eventData, "application/json");
                } else if (apiPath.matches("^/cards/\\d+$")) {
                    int userId = Integer.parseInt(apiPath.split("/")[2]);
                    List<Card> cardsList = Application.getInstance().getDataAdapter().loadCardsByUserID(userId);
                    String cardsData = gson.toJson(cardsList);
                    sendResponse(clientSocket, cardsData, "application/json");
                } else if (apiPath.matches("^/event/\\d+$")) {
                    int eventId = Integer.parseInt(apiPath.split("/")[2]);
                    Event event = Application.getInstance().getDataAdapter().loadEvent(eventId);
                    String eventData = gson.toJson(event);
                    sendResponse(clientSocket, eventData, "application/json");
                }
                else if (apiPath.matches("^/addresses/\\d+$")) {
                    int userId = Integer.parseInt(apiPath.split("/")[2]);
                    List<Address> addressList = Application.getInstance().getDataAdapter().loadAddressesByUserID(userId);
                    String addressData = gson.toJson(addressList);
                    sendResponse(clientSocket, addressData, "application/json");
                }else if (apiPath.startsWith("/user")) {
                    try {
                        String[] params = apiPath.split("\\?")[1].split("&");
                        Map<String, String> paramMap = new HashMap<>();
                        for (String param : params) {
                            String[] keyValue = param.split("=");
                            if (keyValue.length == 2) {  // 确保key和value都存在
                                paramMap.put(keyValue[0], keyValue[1]);
                            }
                        }
                        String username = paramMap.get("username");
                        String password = paramMap.get("password");

                        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
                            sendResponse(clientSocket, "Both username and password are required.", "text/plain");
                            return;
                        }

                        User user = Application.getInstance().getDataAdapter().loadUser(username, password);
                        String userData = gson.toJson(user);
                        sendResponse(clientSocket, userData, "application/json");
                    } catch (Exception e) {
                        sendResponse(clientSocket, "Invalid request parameters.", "text/plain");
                    }
                }
                else {
                    sendResponse(clientSocket, "Endpoint not found", "text/plain");
                }
            }
            else {
                sendResponse(clientSocket, "Invalid request", "text/plain");
            }

            in.close();
            clientSocket.close();
        }
    }

    private static void sendResponse(Socket socket, String responseBody, String contentType) throws Exception {
        OutputStream out = socket.getOutputStream();
        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: " + contentType + "\r\n" +
                "Access-Control-Allow-Origin: http://localhost:63343" + "\r\n" +  // 修改这行
                "Access-Control-Allow-Credentials: true" + "\r\n" +  // 添加这行
                "Content-Length: " + responseBody.getBytes("UTF-8").length + "\r\n\r\n" +
                responseBody;
        out.write(response.getBytes("UTF-8"));
        out.flush();
        out.close();
    }
}
