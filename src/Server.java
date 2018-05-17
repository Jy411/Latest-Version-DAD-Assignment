
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;


public class Server
{
    private static Customer customer = null;
    
    public static void main(String[] args)
    {
        ServerSocket serverSocket;
        Socket socket;
        
        try
        {
            serverSocket = new ServerSocket(4200);
            System.out.println("Server is running...");
            
            while(true)
            {
                socket = serverSocket.accept();
                ConnectionHandler handler = new ConnectionHandler(socket);
                handler.start();
            }
        }
        
        catch(IOException e)
        {
            System.out.println("Exception occurred during server startup: " + e);
        }
    }
    
    public static class ConnectionHandler extends Thread
    {
        Socket server;
        ArrayList<Agent> agentList = new ArrayList<Agent>();
        ArrayList<Customer> customerList = new ArrayList<Customer>();
        ConnectionHandler(Socket socket)
        {
            server = socket;
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());


        @Override
        public void run()
        {
            try
            {
                System.out.println("Test");
                PrintWriter toClient = new PrintWriter(server.getOutputStream());
                BufferedReader fromClient = new BufferedReader(new InputStreamReader(server.getInputStream()));

                // input stream for socket for reading
               // ObjectInputStream clientInputStream = new ObjectInputStream(server.getInputStream());

                // output stream for socket for writing
               // ObjectOutputStream clientOutputStream = new ObjectOutputStream(server.getOutputStream());



                String usertype = fromClient.readLine();
                String username = null;

                // when customer chosen
                if("customer".equalsIgnoreCase(usertype))
                {
                    username = fromClient.readLine();
                    customer = new Customer(username, server);
                    customerList.add(customer);
                    boolean agentFound = false;
                    int x = 0;
                    
                    while(agentFound)
                    {
                        for(x = 0; x < agentList.size(); x ++)
                        {
                            if(agentList.get(x).getNumOfCustomers() < 2)
                            {
                                agentList.get(x).addCustomer(server);
                                agentFound = true;
                                toClient.println("agent found");
                                break;
                            }
                        }
                    }
                    
                    Socket agentSocket = agentList.get(x).getAgentSocket();
                    String userInput = null;
                    Date timestamp = new Date();
                    
                    while (!("/exit".equals(userInput)))
                    {
                        userInput = fromClient.readLine();
                        ChatMessage cm = new ChatMessage(userInput, username, timestamp.toString());
                        sendMessage(agentSocket, cm);
                    }
                }
                
                else if ("agent".equalsIgnoreCase(usertype))
                {
                    String id = fromClient.readLine();
                    System.out.println(id);
                    String pw = fromClient.readLine();

                    if(Authenticate(id, pw, server))
                    {
                        toClient.println("agent authentication successful");
                        toClient.flush();
                        Agent agent = new Agent(server);
                        agentList.add(agent);
                        String userInput = null;
                        Socket receiver = null;
                        int y, z;
                        
                        while(!("/exit".equals(userInput)))
                        {
                            if(agentList.get(agentList.indexOf(agent)).getNumOfCustomers() == 1 || agentList.get(agentList.indexOf(agent)).getNumOfCustomers() == 2)
                            {
                                toClient.println("customer found");

                                // loops through customerList
                                for(y = 0; y < customerList.size(); y ++)
                                {
                                    for(z = 0; z < agent.customerSocketList.size(); z ++)
                                    {
                                        if(customerList.get(y).getCustomerSocket() == agent.getCustomerSocket(z))
                                        {
                                            receiver = customerList.get(y).getCustomerSocket();
                                            username = customerList.get(y).getUsername();
                                            toClient.println(username);
                                            break;
                                        }
                                    }
                                }

/*                                if(!("/exit".equals(userInput)))
                                {
                                    ChatMessage cm;
                                    userInput = fromClient.readLine();

                                    // reads chatMessage object
                                    cm = (ChatMessage)clientInputStream.readObject();

                                    // sets the message into the chatMessage object
                                    cm.setMessage(userInput);
                                    cm.setTimeStamp(timeStamp);

                                    // write the message into the object
                                    clientOutputStream.writeObject(cm);

                                    sendMessage ( receiver, cm);

                                }*/
                            }
                        }
                    }
                }
            }
            
            catch(IOException e)
            {
                System.out.println("Exception occurred in run(): " + e);
            }
/*            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }*/
        }
    }
    
    public static boolean Authenticate(String id, String pw, Socket socket)
    {
        try
        {
            int attempts = 3;
            PrintWriter toClient = new PrintWriter(socket.getOutputStream());

            while(attempts != 0)
            {
                if("agent".equals(id) && "password".equals(pw))
                {
                    System.out.println("Agent authentication successful");
                    attempts = 0;
                    return true;
                }

                else
                {
                    attempts --;
                    System.out.println("Incorrect ID and/or Password.");
                    System.out.println("Remaining attempts: " + attempts);
                    toClient.println("Incorrect ID and/or Password! Please try again.\n" + 
                            "Remaining attempts: " + attempts);
                    toClient.flush();
                    
                    if(attempts == 0)
                    {
                        System.out.println("Agent login attempt failed.");
                        toClient.println("Agent authentication failed! Program will now terminate.");
                        return false;
                    }
                }
            }
        }
        
        catch(IOException e)
        {
            System.out.println("Exception occurred during authentication: " + e);
            return false;
        }
        return false;
    }

    public static void sendMessage (Socket socket, ChatMessage cm) throws IOException
    {
        ObjectOutputStream sOutput = new ObjectOutputStream(socket.getOutputStream());
        sOutput.writeObject(cm);
    }
    
    public static class Chat extends Thread 
    {
        boolean reading = true;
        Socket client;
        
        Chat(Socket socket)
        {
            client = socket;
            start();
        }
        
        @Override
        public void run()
        {
            try
            {
                BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));

                while(reading)
                {
                    input.readLine();
                }
            }
            
            catch(IOException e)
            {
                System.out.println("Exception occurred: " + e);
            }
        }
        
        public void kill()
        {
            reading = false;
        }
    }
}
