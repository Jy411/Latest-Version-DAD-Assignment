
import java.net.Socket;


public class Customer
{
    private String username;
    private Socket customerSocket;

    public Socket getCustomerSocket()
    {
        return customerSocket;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public Customer(String username, Socket customerSocket)
    {
        this.username = username;
        this.customerSocket = customerSocket;
    }
}
