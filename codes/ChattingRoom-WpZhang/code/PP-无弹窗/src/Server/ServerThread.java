package Server;

import utilities.ServerOperations;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread{
    private Socket s;
    private ServerSocket ss;
    ServerOperations operator;

    public ServerThread(Socket s, ServerSocket ss, ServerOperations operator){
        this.s = s;
        this.ss = ss;
        this.operator = operator;
    }

    @Override
    public void run(){
        while (true) {
            try {
                //�������Կͻ��˵���Ϣ
                operator.ServerReceiveMsg(ss, s);
            } catch (Exception e) {}
        }
    }
}