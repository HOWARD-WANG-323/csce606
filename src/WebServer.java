import com.google.gson.Gson;
import com.sun.tools.jconsole.JConsoleContext;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class WebServer {
    private static Map<String, User> sessions = new HashMap<>();

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
                } else if (apiPath.matches("^/ticketByEvent/\\d+$")) {
                    int eventId = Integer.parseInt(apiPath.split("/")[2]);
                    List<Ticket> tickets = Application.getInstance().getDataAdapter().loadTicketsByEventId(eventId);
                    String ticketsData = gson.toJson(tickets);
                    sendResponse(clientSocket, ticketsData, "application/json");
                    System.out.println("ticket data: " + ticketsData);
                }else if (apiPath.matches("^/event/\\d+$")) {
                    int eventId = Integer.parseInt(apiPath.split("/")[2]);
                    Event event = Application.getInstance().getDataAdapter().loadEvent(eventId);
                    String eventData = gson.toJson(event);
                    sendResponse(clientSocket, eventData, "application/json");
                }else if (apiPath.matches("^/allEvent/")) {
                    List<Event> eventList = Application.getInstance().getDataAdapter().loadAllEvents();
                    String eventData = gson.toJson(eventList);
                    sendResponse(clientSocket, eventData, "application/json");
                    System.out.println("event data: " + eventData);
                } else if (apiPath.matches("^/cards/\\d+$")) {
                    int userId = Integer.parseInt(apiPath.split("/")[2]);
                    List<Card> cardsList = Application.getInstance().getDataAdapter().loadCardsByUserID(userId);
                    String cardsData = gson.toJson(cardsList);
                    sendResponse(clientSocket, cardsData, "application/json");
                }
                else if (apiPath.matches("^/addresses/\\d+$")) {
                    int userId = Integer.parseInt(apiPath.split("/")[2]);
                    List<Address> addressList = Application.getInstance().getDataAdapter().loadAddressesByUserID(userId);
                    String addressData = gson.toJson(addressList);
                    sendResponse(clientSocket, addressData, "application/json");
                }else if (apiPath.matches("/getUserName")) {
                    //debug
                    for (Map.Entry<String, User> entry : sessions.entrySet()) {
                        String sessionId = entry.getKey();
                        User user = entry.getValue();

                        System.out.println("Session ID: " + sessionId);
                        System.out.println("User ID: " + user.getUserID());
                        System.out.println("Username: " + user.getUsername());
                        System.out.println("Password: " + user.getPassword()); // 注意：出于安全考虑，通常不建议打印密码
                        System.out.println("Full Name: " + user.getFullName());
                        System.out.println("----------------------------------");
                    }

                    String sessionId = headers.get("Cookie").split("sessionId=")[1];
                    User user = sessions.get(sessionId);
                    if (user != null) {
                        Map<String, String> responseData = new HashMap<>();
                        responseData.put("fullName", user.getFullName());
                        sendResponse(clientSocket, gson.toJson(responseData), "application/json", null);
                    } else {
                        Map<String, String> errorData = new HashMap<>();
                        errorData.put("error", "User not found");
                        sendResponse(clientSocket, gson.toJson(errorData), "application/json");
                    }
                } else if (apiPath.equals("/signout")) {
                    String sessionId = headers.get("Cookie").split("sessionId=")[1];
                    sessions.remove(sessionId); // 从 sessions Map 中移除会话
                    sendResponse(clientSocket, "Logged out successfully", "text/plain");
                }
                else if (apiPath.startsWith("/user")) {
                    try {
                        String[] params = apiPath.split("\\?")[1].split("&");
                        Map<String, String> paramMap = new HashMap<>();
                        for (String param : params) {
                            String[] keyValue = param.split("=");
                            if (keyValue.length == 2) {
                                // 解码 URL 编码的参数
                                String key = URLDecoder.decode(keyValue[0], "UTF-8");
                                String value = URLDecoder.decode(keyValue[1], "UTF-8");
                                paramMap.put(key, value);
                            }
                        }
                        String username = paramMap.get("username");
                        String password = paramMap.get("password");

                        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
                            sendResponse(clientSocket, "Both username and password are required.", "text/plain");
                            return;
                        }

                        User user = Application.getInstance().getDataAdapter().loadUser(username, password);
                        if (user != null) {
                            String sessionId = UUID.randomUUID().toString();
                            sessions.put(sessionId, user);
                            String userData = gson.toJson(user);
                            sendResponse(clientSocket, userData, "application/json", sessionId);
                        } else {
                            sendResponse(clientSocket, "Invalid username or password.", "text/plain");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        sendResponse(clientSocket, "Error processing request.", "text/plain");
                    }
                }
                else {
                    sendResponse(clientSocket, "Endpoint not found", "text/plain");
                }
            }else if ("POST".equals(httpMethod)) {
                StringBuilder requestBody = new StringBuilder();
                int contentLength = Integer.parseInt(headers.get("Content-Length"));
                for (int i = 0; i < contentLength; i++) {
                    requestBody.append((char) in.read());
                }

               if ("/ticket/".equals(apiPath)) {
                    System.out.println("Received JSON Data: " + requestBody.toString());
                    Ticket ticket = gson.fromJson(requestBody.toString(),Ticket.class);
                    Ticket ticket1 = Application.getInstance().getDataAdapter().loadTicket(ticket.getTicketID());
                    String sessionId = headers.get("Cookie").split("sessionId=")[1];
                    User user = sessions.get(sessionId);
                    ticket1.setUserID(user.getUserID());
                    ticket1.setStatus(ticket.getTicketStatus());
                    System.out.println(ticket.getPrice());
                    Application.getInstance().getDataAdapter().saveTicket(ticket1);
                    sendResponse(clientSocket, gson.toJson(ticket1), "text/plain");
                } else if ("/address/".equals(apiPath)) {
                    System.out.println("Received JSON Data: " + requestBody.toString());
                    Address address = gson.fromJson(requestBody.toString(),Address.class);
                    Application.getInstance().getDataAdapter().saveAddress(address);
                    sendResponse(clientSocket, "Received your JSON data", "text/plain");
                }
                else if ("/card/".equals(apiPath)) {
                    System.out.println("Received JSON Data: " + requestBody.toString());
                    Card card = gson.fromJson(requestBody.toString(),Card.class);
                    Application.getInstance().getDataAdapter().saveCard(card);
                    sendResponse(clientSocket, "Received your JSON data", "text/plain");
                }
                else if ("/payment/".equals(apiPath)) {
                    System.out.println("Received JSON Data: " + requestBody.toString());
                    Payment payment = gson.fromJson(requestBody.toString(),Payment.class);
                    String sessionId = headers.get("Cookie").split("sessionId=")[1];
                    User user = sessions.get(sessionId);
                    payment.setUserID(user.getUserID());
                    String paymentID = Application.getInstance().getDataAdapter().savePayment(payment);
                    sendResponse(clientSocket, paymentID, "text/plain");
                }
                //save new user
               else if("/signup/".equals(apiPath)){
                   System.out.println("Received JSON Data: " + requestBody.toString());
                   User newUser = gson.fromJson(requestBody.toString(), User.class);
                   if(checkUserData(newUser)){
                       Application.getInstance().getDataAdapter().saveUser(newUser);
                       Map<String, String> successData = new HashMap<>();
                       successData.put("message", "Registration successful");
                       successData.put("userID", String.valueOf(newUser.getUserID())); // 假设你有一个方法来获取用户ID
                       sendResponse(clientSocket, gson.toJson(successData), "application/json");
                   }
                   else{
                       Map<String, String> errorData = new HashMap<>();
                       errorData.put("error", "User already exists");
                       sendResponse(clientSocket, gson.toJson(errorData), "application/json");
                   }
               }
            }
            else {
                sendResponse(clientSocket, "Invalid request", "text/plain");
            }

            in.close();
            clientSocket.close();
        }
    }

    private static boolean checkUserData(User newUser) {
        //try tto get user from database, if doesn't exist, return true
        User user = Application.getInstance().getDataAdapter().loadUser(newUser.getUsername(),newUser.getPassword());
        return user == null;
    }
    private static void sendSignOutResponse(Socket socket) throws Exception {
        OutputStream out = socket.getOutputStream();

        StringBuilder responseHeaders = new StringBuilder();
        responseHeaders.append("HTTP/1.1 200 OK\r\n");
        responseHeaders.append("Content-Type: text/plain\r\n");
        responseHeaders.append("Set-Cookie: sessionId=; HttpOnly; SameSite=Strict; Max-Age=0\r\n"); // 清除客户端的 sessionId cookie
        responseHeaders.append("\r\n");

        out.write(responseHeaders.toString().getBytes("UTF-8"));
        out.write("Signed out successfully".getBytes("UTF-8"));
        out.flush();
        out.close();
    }
    private static void sendResponse(Socket socket, String responseBody, String contentType) throws Exception {
        sendResponse(socket, responseBody, contentType, null);
    }

    private static void sendResponse(Socket socket, String responseBody, String contentType, String sessionId) throws Exception {
        OutputStream out = socket.getOutputStream();

        StringBuilder responseHeaders = new StringBuilder();
        responseHeaders.append("HTTP/1.1 200 OK\r\n");
        responseHeaders.append("Content-Type: ").append(contentType).append("\r\n");
        responseHeaders.append("Access-Control-Allow-Origin: http://localhost:63342\r\n");
        responseHeaders.append("Access-Control-Allow-Credentials: true\r\n");
        responseHeaders.append("Access-Control-Allow-Headers: Content-Type\r\n");  // 允许 Content-Type 请求头
        responseHeaders.append("Content-Length: ").append(responseBody.getBytes("UTF-8").length).append("\r\n");

        if (sessionId != null) {
            responseHeaders.append("Set-Cookie: sessionId=").append(sessionId).append("; HttpOnly; SameSite=Strict\r\n");
        }

        out.write((responseHeaders.toString() + "\r\n" + responseBody).getBytes("UTF-8"));
        out.flush();
        out.close();
    }
}
