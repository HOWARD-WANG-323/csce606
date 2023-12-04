import com.google.gson.Gson;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import redis.clients.jedis.Jedis;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DataAdapter {
    private Connection connection;

    private MongoDatabase database;

    private Jedis jedis;

    public DataAdapter(Connection connection) {
        this.connection = connection;

        String connectionString = "mongodb+srv://howardwzh323:wzh2023@cluster0.6vcds8r.mongodb.net/?retryWrites=true&w=majority";
        MongoClient mongoClient = MongoClients.create(connectionString);
        // 选择数据库和集合
        this.database = mongoClient.getDatabase("StoreApp");

        String redisHost = "redis-10173.c241.us-east-1-4.ec2.cloud.redislabs.com";
        int redisPort = 10173;
        String redisPassword = "aa0eTYMtnMUSiRGxjpqgWiYjIKQZWY7E";

        // Connecting to Redis
        jedis = new Jedis(redisHost, redisPort);
        jedis.auth(redisPassword);
        jedis.set("username = admin", "password");
    }




    public boolean saveAddress(Address address) {
        boolean isSaved = false;
        try {
            Gson gson = new Gson();
            // 判断是否是新地址或更新现有地址
            if (address.getAddressID() == 0) {
                // 加载用户的现有地址
                List<Address> existingAddresses = loadAddressesByUserID(address.getUserID());

                // 检查是否已存在相同的地址
                boolean addressExists = false;
                for (Address existingAddress : existingAddresses) {
                    if (existingAddress.equals(address)) { // 确保 Address 类实现了适当的 equals 方法
                        // 如果地址已存在，则更新地址ID和信息
                        address.setAddressID(existingAddress.getAddressID());
                        addressExists = true;
                        break;
                    }
                }
                // 如果是新地址，则生成新的 addressID 并添加到用户地址列表
                if (!addressExists) {
                    int newAddressId = Math.toIntExact(jedis.incr("addressIDCounter"));
                    address.setAddressID(newAddressId);
                    jedis.rpush("userAddresses:" + address.getUserID(), String.valueOf(newAddressId));
                    isSaved =true;
                }
            }
            String addressJson = gson.toJson(address);
            // 保存或更新地址信息
            jedis.set("address:" + address.getAddressID(), addressJson);
            isSaved =true;
        } catch (Exception e) {
            System.out.println("Error accessing Redis!");
            e.printStackTrace();
        }
        return isSaved;
    }


    public boolean saveCard(Card card) {
        boolean isSaved = false;
        try {
            Gson gson = new Gson();
            // 检查是否是新卡或更新现有卡
            if (card.getCardID() == 0) {
                // 加载用户的现有卡片
                List<Card> existingCards = loadCardsByUserID(card.getUserID());

                // 检查是否已存在相同的卡片
                boolean cardExists = false;
                for (Card existingCard : existingCards) {
                    if (existingCard.equals(card)) { // make sure CreditCard class implements appropriate equals method
                        // if the card already exists, update the card ID and information
                        card.setCardID(existingCard.getCardID());
                        cardExists = true;
                        break;
                    }
                }

                // 如果是新卡，生成新的 cardID 并添加到用户卡片列表
                if (!cardExists) {
                    int newCardId = Math.toIntExact(jedis.incr("cardIDCounter"));
                    card.setCardID(newCardId);
                    jedis.rpush("userCards:" + card.getUserID(), String.valueOf(newCardId));
                }
            }
            String cardJson = gson.toJson(card);
            // 保存或更新卡片信息
            jedis.set("card:" + card.getCardID(), cardJson);
            isSaved = true;
        } catch (Exception e) {
            System.out.println("Error accessing Redis!");
            e.printStackTrace();
        }
        return isSaved;
    }


    public User loadUser(String username, String password) {
        try {
            // 从 Redis 获取用户数据
            String userJson = jedis.get("user:" + username);
            if (userJson != null) {
                // 将 JSON 反序列化为 User 对象
                User user = new Gson().fromJson(userJson, User.class);

                // 验证密码（应使用哈希和盐进行安全验证）
                if (user.getPassword().equals(password)) {
                    return user;
                }
            }
        } catch (Exception e) {
            System.out.println("Error accessing Redis!");
            e.printStackTrace();
        }
        return null;
    }

    public List<Event> loadAllEvents() {
        List<Event> events = new ArrayList<>();
        try {
            List<String> eventIDs = jedis.lrange("event:", 0, -1);

            for (String eventID : eventIDs) {
                String eventJson = jedis.get("event:" + eventID);
                if (eventJson != null) {  // Check if the card data is not null
                    events.add(new Gson().fromJson(eventJson, Event.class));
                }
            }
            return events;
        } catch (Exception e) {
            System.out.println("Error accessing Redis!");
            e.printStackTrace();
        }
        return events;
    }

    public List<Ticket> loadTicketsByEventId(int eventId) {
        List<Ticket> tickets = new ArrayList<>();

        try {
            List<String> ticketIDs = jedis.lrange("eventTickets:" + eventId, 0, -1);
            for (String ticketID : ticketIDs) {
                String ticketJson = jedis.get("ticket:" + ticketID);
                if (ticketJson != null) {  // Check if the card data is not null
                    tickets.add(new Gson().fromJson(ticketJson, Ticket.class));
                }
            }
            return tickets;
        } catch (Exception e) {
            System.out.println("Error accessing Redis!");
            e.printStackTrace();
        }
        return tickets;
    }

    public List<Card> loadCardsByUserID(int userID) {
        try {
            List<String> cardIDs = jedis.lrange("userCards:" + userID, 0, -1);
            List<Card> cards = new ArrayList<>();

            for (String cardID : cardIDs) {
                String cardJson = jedis.get("card:" + cardID);
                if (cardJson != null) {  // Check if the card data is not null
                    cards.add(new Gson().fromJson(cardJson, Card.class));
                }
            }
            return cards;
        } catch (Exception e) {
            System.out.println("Error accessing Redis!");
            e.printStackTrace();
        }
        return null;
    }

    public void deleteCard(Card selectedCard) {
        try {
            // 从用户的卡列表中移除卡ID
            jedis.lrem("userCards:" + selectedCard.getUserID(), 1, String.valueOf(selectedCard.getCardID()));

            // 删除卡数据
            jedis.del("card:" + selectedCard.getCardID());
        } catch (Exception e) {
            System.out.println("Error accessing Redis!");
            e.printStackTrace();
        }
    }

    public void deleteAddress(Address selectedAddress) {
        try {
            // 从用户的地址列表中移除地址ID
            jedis.lrem("userAddresses:" + selectedAddress.getUserID(), 1, String.valueOf(selectedAddress.getAddressID()));

            // 删除地址数据
            jedis.del("address:" + selectedAddress.getAddressID());
        } catch (Exception e) {
            System.out.println("Error accessing Redis!");
            e.printStackTrace();
        }
    }

    public List<Address> loadAddressesByUserID(int userID) {
        try {
            // Fetch the list of address IDs for the user
            List<String> addressIDs = jedis.lrange("userAddresses:" + userID, 0, -1);

            // Load each address by its ID
            return addressIDs.stream().map(addressID -> {
                String addressJson = jedis.get("address:" + addressID);
                return new Gson().fromJson(addressJson, Address.class);
            }).collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println("Error accessing Redis!");
            e.printStackTrace();
        }
        return null;
    }

    public boolean savePayment(Payment payment) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO Payments (UserID, PaymentAmount, PaymentDate, PaymentStatus) VALUES (?, ?, ?, ?)");

            statement.setInt(1, payment.getUserID());
            statement.setDouble(2, payment.getPaymentAmount());
            statement.setString(3, payment.getPaymentDate());
            statement.setString(4, payment.getPaymentStatus());

            statement.execute();    // commit to the database;
            statement.close();

            return true; // save successfully!
        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
            return false; // cannot save!
        }
    }


    public boolean saveTicket(Ticket ticket) {
        boolean isSaved = false;
        try {
            Gson gson = new Gson();
            if (ticket.getTicketID() == 0) {
                List<Ticket> existingTickets = loadTicketsByEventId(ticket.getEventID());

                boolean ticketExists = false;
                for (Ticket existingTicket : existingTickets) {
                    if (existingTicket.equals(ticket)) {
                        // if the ticket already exists, update the ticket ID and information
                        ticket.setTicketID(existingTicket.getTicketID());
                        ticketExists = true;
                        break;
                    }
                }

                if (!ticketExists) {
                    int newTicketId = Math.toIntExact(jedis.incr("ticketIDCounter"));
                    ticket.setTicketID(newTicketId);
                    jedis.rpush("eventTickets:" + ticket.getEventID(), String.valueOf(newTicketId));
                }
            }
            String ticketJson = gson.toJson(ticket);
            jedis.set("ticket:" + ticket.getTicketID(), ticketJson);
            isSaved = true;
        } catch (Exception e) {
            System.out.println("Error accessing Redis!");
            e.printStackTrace();
        }
        return  isSaved;
    }

    public Event loadEvent(int eventID) {
        try {
            String eventJson = jedis.get("event:" + eventID);
            Gson gson = new Gson();
            if (eventJson != null) {
                return gson.fromJson(eventJson, Event.class);
            }
        } catch (Exception e) {
            System.out.println("Error accessing Redis!");
            e.printStackTrace();
        }
        return null;
    }

    public int getMaxPaymentID() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT MAX(PaymentID) FROM Payments");
            if (resultSet.next()) {
                int max = resultSet.getInt(1);
                resultSet.close();
                statement.close();
                return max;
            }
        } catch (SQLException e) {
            System.out.println("Database access error!");
            e.printStackTrace();
        }
        return 0;
    }

    public Ticket loadTicket(int ticketID) {
        try {
            String ticketJson = jedis.get("ticket:" + ticketID);
            Gson gson = new Gson();
            if (ticketJson != null) {
                return gson.fromJson(ticketJson, Ticket.class);
            }
        } catch (Exception e) {
            System.out.println("Error accessing Redis!");
            e.printStackTrace();
        }
        return null;
    }

    public boolean saveUser(User newUser) {
        boolean isSaved = false;
        try {
            Gson gson = new Gson();
            int newID = (int) (Math.random() * 1000000);
            String key = "user:" + newID;
            while (jedis.exists(key)) { // 检查 key 是否存在
                newID++; // 如果存在，newID 增加 1
                key = "user:" + newID; // 更新 key 以匹配新的 newID
            }
            newUser.setUserID(newID);
            String userJson = gson.toJson(newUser);
            jedis.set(key,userJson);
            isSaved = true;
        } catch (Exception e) {
            System.out.println("Error accessing Redis!");
            e.printStackTrace();
        }
        return  isSaved;
    }

    public boolean saveEvent(Event event) {
        boolean isSaved = false;
        try {
            Gson gson = new Gson();
            if (event.getEventID() == 0) {
                List<Event> existingEvents = loadAllEvents();

                boolean eventExists = false;
                for (Event existingEvent : existingEvents) {
                    if (existingEvent.equals(event)) {
                        // if the event already exists, update the event ID and information
                        event.setEventID(existingEvent.getEventID());
                        eventExists = true;
                        break;
                    }
                }

                if (!eventExists) {
                    int newEventId = Math.toIntExact(jedis.incr("eventIDCounter"));
                    event.setEventID(newEventId);
                }
            }
            String eventJson = gson.toJson(event);
            jedis.set("event:" + event.getEventID(), eventJson);
            isSaved = true;
        } catch (Exception e) {
            System.out.println("Error accessing Redis!");
            e.printStackTrace();
        }
        return  isSaved;
    }

    public void deleteEvent(Event event) {
        try {
            // 从用户的卡列表中移除卡ID
            String pattern = "eventTickets:" + event.getEventID() + "*";

            Set<String> keys = jedis.keys(pattern);
            for (String key : keys) {
                jedis.del(key);
            }

            // 删除卡数据
            jedis.del("event:" + event.getEventID());
        } catch (Exception e) {
            System.out.println("Error accessing Redis!");
            e.printStackTrace();
        }
    }

    public void deleteTicket(Ticket ticket) {
        try {
            // 从用户的卡列表中移除卡ID
            jedis.lrem("eventTickets:" + ticket.getEventID(), 1, String.valueOf(ticket.getTicketID()));

            // 删除卡数据
            jedis.del("card:" + ticket.getTicketID());
        } catch (Exception e) {
            System.out.println("Error accessing Redis!");
            e.printStackTrace();
        }
    }
}