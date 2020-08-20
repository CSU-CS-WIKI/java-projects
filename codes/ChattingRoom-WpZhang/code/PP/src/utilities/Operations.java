package utilities;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Operations {

    //����һ����Ϣ
    public static void sendMsg(String msg, PrintStream ps) throws IOException {
        ps.println(msg);
        //System.out.println("����\t"+msg);
    }
    //����1����Ϣ+��Ϣ����
    public static void sendMsg(String msgType, String msg, PrintStream ps) throws IOException {
        ps.println(msgType);
        ps.println(msg);
        //System.out.println("����"+msgType+"\t"+msg);
    }
    //����2����Ϣ+��Ϣ����
    public static void sendMsg(String msgType, String msg1, String msg2, PrintStream ps) throws IOException {
        ps.println(msgType);
        ps.println(msg1);
        ps.println(msg2);
        //System.out.println("����"+msgType+"\t"+msg1+'\t'+msg2);
    }
    //����3����Ϣ+��Ϣ����
    public static void sendMsg(String msgType, String msg1, String msg2, String msg3, PrintStream ps) throws IOException {
        ps.println(msgType);
        ps.println(msg1);
        ps.println(msg2);
        ps.println(msg3);
        //System.out.println("����"+msgType+"\t"+msg1+'\t'+msg2+'\t'+msg3);
    }

    //����������Ϣ
    public static void sendChat(Message msg, PrintStream ps) throws IOException{
        try {
            ps.println("chatMsg");
            ps.println(msg.getUserFrom());
            ps.println(msg.getUserTo());
            ps.println(msg.getContent());
        } catch (Exception e) {System.out.println("����������Ϣʧ��");e.printStackTrace();}
    }
    //����˽����Ϣ
    public static Message receiveChat(BufferedReader bf){
        try {
            String userFrom = bf.readLine();
            String userTo = bf.readLine();
            String message = bf.readLine();
            Message msg = new Message(userFrom, userTo, message);
            return msg;
        } catch (Exception e) {
            System.out.println("������Ϣ���մ���"+e.getMessage());
            return null;
        }
    }

    //����Ⱥ����Ϣ
    public static void sendGroupChat(ArrayList<String> clients, Message msg, PrintStream ps) throws IOException{
        try {
            for(int i=0; i<clients.size(); i++) {
                System.out.println("��"+clients.get(i)+"������Ϣ");
                ps.println("groupChatMsg");
                ps.println(msg.getGroupName());
                ps.println(msg.getUserFrom());
                ps.println(clients.get(i));
                ps.println(msg.getContent());
            }
        } catch (Exception e) {System.out.println("����������Ϣʧ��");e.printStackTrace();}
    }

    //����Ⱥ����Ϣ
    public static Message receiveGroupChat(BufferedReader bf){
        try {
            String groupName = bf.readLine();
            String userFrom = bf.readLine();
            String userTo = bf.readLine();
            String message = bf.readLine();
            Message msg = new Message(groupName, userFrom, userTo, message);
            return msg;
        } catch (Exception e) {
            System.out.println("������Ϣ���մ���"+e.getMessage());
            return null;
        }
    }

}
