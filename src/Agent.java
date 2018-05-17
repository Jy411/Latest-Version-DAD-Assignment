
import java.net.Socket;
import java.util.ArrayList;

public class Agent
{
    //declare attributes of Agent class
    private Socket agentSocket;
    public ArrayList<Socket> customerSocketList;
    private int numOfCustomers;
    private int customerIndex;

    //call agent socket for reference at customer side
    public Socket getAgentSocket()
    {
        return agentSocket;
    }

    //call number of customers that agent has for reference at customer side
    public int getNumOfCustomers()
    {
        return numOfCustomers;
    }
    
    public Socket getCustomerSocket(int index){
        return customerSocketList.get(index - 1);
    }

    //initialize agent object
    public Agent(Socket agentSocket)
    {
        this.agentSocket = agentSocket;
        numOfCustomers = 0;
        customerSocketList = new ArrayList<Socket>();
    }
    
    //add customer socket to customerSocketList when new customer joins, and increase numOfCustomers
    public void addCustomer(Socket customerSocket)
    {
        customerSocketList.add(customerSocket);
        numOfCustomers ++;
    }
    
    //remove customer socket from customerSocketList when customer leaves, and decrease numOfCustomers
    public void removeCustomer(Socket customerSocket)
    {
        customerSocketList.remove(customerSocketList.indexOf(customerSocket));
        numOfCustomers --;
    }
}