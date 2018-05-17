import java.io.*;

public class ChatMessage implements Serializable{
    private String message;
    private String userName;
    private String timeStamp;

    ChatMessage(String message, String username, String timestamp){
        this.message = message;
        this.userName = username;
        this.timeStamp = timestamp;
    }

    public String getMessage(){
        return message;
    }

    public String getUserName(){
        return userName;
    }

    public String getTimeStamp(){
        return timeStamp;
    }


    public void setMessage(String message) {
        this.message = message;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}

/*
    //Method for main
    public static sendMessage (Socket socket, ChatMessage cm){
        ObjectOutputStream sOutput = new ObjectOutputStream(socket.getOutputStream());
        sOutput.writeObject(cm);

    }
        System.out.println("Enter -1 to exit");
        String userInput = input.nextLine();
    while (!("-1".equals(userInput))){
        cm(userInput, username, timestamp);
        sendMessage (socket, cm);
        userInput = input.nextLine();
        }*/
