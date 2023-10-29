import com.google.gson.Gson;

import java.awt.desktop.AppForegroundListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
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
                }
                else if (apiPath.matches("^/event/\\d+$")) {
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
                else if (apiPath.matches("^/address/\\d+$")) {
                    int userId = Integer.parseInt(apiPath.split("/")[2]);
                    List<Address> addressList = Application.getInstance().getDataAdapter().loadAddressesByUserID(userId);
                    String addressData = gson.toJson(addressList);
                    sendResponse(clientSocket, addressData, "application/json");
                }else if (apiPath.matches("/getUserName")) {
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
                }
                else if (apiPath.startsWith("/user")) {
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
                        if (user != null) {
                            String sessionId = UUID.randomUUID().toString();
                            sessions.put(sessionId, user);
                            System.out.println("sessionId: " + sessionId);
                            String userData = gson.toJson(user);
                            sendResponse(clientSocket, userData, "application/json",sessionId);
                            System.out.println("user data: " + userData);
                        } else {
                            sendResponse(clientSocket, "Invalid username or password.", "text/plain");
                            return;
                        }


                    } catch (Exception e) {
                        sendResponse(clientSocket, "Invalid request parameters.", "text/plain");
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

                if ("/ticket/".equals(apiPath) && "application/json".equals(headers.get("Content-Type"))) {
                    System.out.println("Received JSON Data: " + requestBody.toString());
                    Ticket ticket = gson.fromJson(requestBody.toString(),Ticket.class);
                    Application.getInstance().getDataAdapter().saveTicket(ticket);

                    // 在这里，你可以进一步解析和处理JSON数据...
                    // 这只是一个简单的响应
                    sendResponse(clientSocket, "Received your JSON data", "text/plain");
                } else if ("/address/".equals(apiPath) && "application/json".equals(headers.get("Content-Type"))) {
                    System.out.println("Received JSON Data: " + requestBody.toString());
                    Address address = gson.fromJson(requestBody.toString(),Address.class);
                    Application.getInstance().getDataAdapter().saveAddress(address);

                    // 在这里，你可以进一步解析和处理JSON数据...
                    // 这只是一个简单的响应
                    sendResponse(clientSocket, "Received your JSON data", "text/plain");
                }
                else if ("/card/".equals(apiPath) && "application/json".equals(headers.get("Content-Type"))) {
                    System.out.println("Received JSON Data: " + requestBody.toString());
                    Card card = gson.fromJson(requestBody.toString(),Card.class);
                    Application.getInstance().getDataAdapter().saveCard(card);

                    // 在这里，你可以进一步解析和处理JSON数据...
                    // 这只是一个简单的响应
                    sendResponse(clientSocket, "Received your JSON data", "text/plain");
                }
                else if ("/payment/".equals(apiPath) && "application/json".equals(headers.get("Content-Type"))) {
                    System.out.println("Received JSON Data: " + requestBody.toString());
                    Payment payment = gson.fromJson(requestBody.toString(),Payment.class);
                    Application.getInstance().getDataAdapter().savePayment(payment);
                    // 在这里，你可以进一步解析和处理JSON数据...
                    // 这只是一个简单的响应
                    sendResponse(clientSocket, "Received your JSON data", "text/plain");
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
        sendResponse(socket, responseBody, contentType, null);
    }

    private static void sendResponse(Socket socket, String responseBody, String contentType, String sessionId) throws Exception {
        OutputStream out = socket.getOutputStream();

        StringBuilder responseHeaders = new StringBuilder();
        responseHeaders.append("HTTP/1.1 200 OK\r\n");
        responseHeaders.append("Content-Type: ").append(contentType).append("\r\n");
        responseHeaders.append("Access-Control-Allow-Origin: http://localhost:63343\r\n");
        responseHeaders.append("Access-Control-Allow-Credentials: true\r\n");
        responseHeaders.append("Content-Length: ").append(responseBody.getBytes("UTF-8").length).append("\r\n");

        if (sessionId != null) {
            responseHeaders.append("Set-Cookie: sessionId=").append(sessionId).append("; HttpOnly; SameSite=Strict\r\n");
        }

        out.write((responseHeaders.toString() + "\r\n" + responseBody).getBytes("UTF-8"));
        out.flush();
        out.close();
    }
}
