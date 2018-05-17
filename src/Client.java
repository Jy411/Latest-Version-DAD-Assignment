import java.io.*;
import java.net.*;
import java.util.*;

public class Client
{
    public static void main(String[] args) throws IOException
    {
        Scanner clientInput = new Scanner(System.in);
        System.out.println("Enter IP Address of server: ");
        String serverAddress = clientInput.nextLine();
        String usertype;
        String username;
        String id;
        String pw;
        String message = null;
        
        try
        {
            Socket client;
            client = new Socket(serverAddress, 4200);
            
            PrintWriter toServer = new PrintWriter(client.getOutputStream(), true);
            BufferedReader fromServer = new BufferedReader(new InputStreamReader(client.getInputStream()));
            
            System.out.println("Connecting to server...");
            System.out.println("Welcome to Line Networks customer service!");
            System.out.println("Please select your role: [CUSTOMER/AGENT]");
            usertype = clientInput.nextLine();
            toServer.println(usertype);
            
            if("customer".equalsIgnoreCase(usertype))
            {
                try
                {
                    System.out.println("Please enter your name/nickname: ");
                    username = clientInput.nextLine(); 
                    toServer.println(username);
                    
                    System.out.println("Welcome, " + username + ", we are looking for an agent to attend to you...");
                    
                    if("agent found".equals(fromServer.readLine()))
                    {
                        System.out.println("We have found an agent for you!");
                        System.out.println("You may now begin your conversation with your agent. Type [/exit] to exit the chat.");
                        
                        while(!("/exit".equals(message)))
                        {
                            message = clientInput.nextLine();
                            toServer.println(message);
                        }
                    }
                }
                
                catch(Exception e)
                {
                    System.out.println("Exception occurred: " + e);
                }
            }
            
            else if("agent".equalsIgnoreCase(usertype))
            {
                try
                {
                    System.out.println("Please enter your ID: ");
                    id = clientInput.nextLine();
                    toServer.println(id);
                    System.out.println("Please enter your password: ");
                    pw = clientInput.nextLine();
                    toServer.println(pw);

                    if("agent authentication successful".equals(fromServer.read()))
                    {
                        System.out.println("Welcome agent!");
                        System.out.println("Waiting for customers to connect...");
                        
                        if("customer found".equals(fromServer.readLine()))
                        {
                            username = fromServer.readLine();
                            System.out.println(username + " has connected!");
                            
                        }
                    }
                    
                    else if("Authentication failed! Program will terminate.".equals(fromServer.readLine()))
                    {
                        System.exit(0);
                    }
                    
                    else
                    {
                        for(int i = 0; i < 3; i ++)
                        {
                            System.out.println("");
                        }
                    }
                } catch (Exception e){
                    System.out.println(e);
                }
            }
        } catch (Exception e){
            System.out.println(e);
        }
    }
}