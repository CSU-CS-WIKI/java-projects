package utilities;

import java.io.*;
import java.net.Socket;

public class ClientOperations {
    private Socket s;

    public Interface_own interf;
    private OnlineClients clients;
    public String user;
    private Info inf;

    public ClientOperations(Socket s) {
        this.s = s;
        this.clients = new OnlineClients();
    }
    

    //�ͻ���-������������Ϣ
    public void ClientReceiveMsg() throws IOException {

    }

}
