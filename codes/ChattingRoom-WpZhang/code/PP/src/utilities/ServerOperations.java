package utilities;

import Interface.ServerInterface;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerOperations {
    private ServerSocket ss;

    //public ClientInterface interf;
    private ServerInterface interf;

    public ServerOperations(ServerSocket ss) {
        interf = new ServerInterface();
        interf.clients = new OnlineClients();
        this.ss = ss;
        this.interf.showInterface();
    }

    //������-��ʵ�û���Ϣ
    public static String CheckUserInfo(String user, String password) {
        int result;
        result = checkUser(user);
        if(result==0) return "3"; //�û�������
        else {
            LoginInfo inf = LoginInfo.getLoginInfo(user);
            if(inf.getUserPassword().equals(password)) return "1";  //�û�������������ȷ
            else return "2";  //�û����ڵ��������
        }
    }
    //������-����û��Ƿ����
    public static int checkUser(String userName) {
        File dir = new File("LoginInfo");
        if (!dir.exists()) {
            dir.mkdir();
            return 0;
        }
        else {
            File[] infs = dir.listFiles();
            for(int i=0; i<infs.length; i++) {
                if (infs[i].getName().equals(userName+".dat")) return 1;
            }
            return 0;
        }
    }
    //������-�û���¼
    public void login(Socket s, String userName, String password, String result) {
        System.out.println("�û����ڵ�½...");
        Info inf;
        if(result.equals("3")) {
            //�����û�
            LoginInfo loginInf = new LoginInfo(userName, password);
            loginInf.saveLoginInfo();
            inf = new Info(userName);
            inf.saveInfo();
        }
        else inf = Info.readInfo(userName);
        sendUserLoginInfo(userName);
        try {
            System.out.println("��½���...");
            int index = interf.clients.getIndex(s);
            Operations.sendMsg("LoginSuccess", interf.clients.getUserPs(index));
        } catch (Exception e) {System.out.println(e.getMessage());}
        //���������б�֪ͨ���ߵ��û�
        try {
            sendUserInfo(s, inf);
            sendOnlineClients(s);
        }catch (Exception e){e.printStackTrace();}

    }
    //������-�û�����
    public void logout(String userName){
        interf.clients.delUser(userName);
        sendUserLogoutInfo(userName);
    }



    //������-��ͻ��˷����û���Ϣ
    public void sendUserInfo(Socket s, Info inf) {
        System.out.println("׼�����û������û���Ϣ...");
        try {
            int index = interf.clients.getIndex(s);
            Operations.sendMsg("ReceiveUserInfo", inf.userName, interf.clients.getUserPs(index));
            System.out.println("�û���Ϣ�������");
        } catch (Exception e) {System.out.println("��ʾ�û������û���Ϣʧ�ܣ�");System.out.println(e.getMessage());}
    }
    //������-�����û����͵�ǰ�����û�
    public void sendOnlineClients(Socket s){
        System.out.println("׼�����û����������û��б�...");
        try {
            int index = interf.clients.getIndex(s);
            PrintStream ps = interf.clients.getUserPs(index);
            Operations.sendMsg("ReceiveOnlineClients", ps);
            System.out.println("���ͽ�������");
            int length = interf.clients.getSize();
            //ps.println(length);
            Operations.sendMsg(String.valueOf(length), ps);
            for(int i=0; i<length; i++){
                Operations.sendMsg(interf.clients.getUserName(i), ps);
            }
            //int index = interf.clients.getIndex(s);
            //Operations.sendMsg("ReceiveOnlineClients", "1", interf.clients.getUserName(0), interf.clients.getUserPs(index));
            System.out.println("�����û��б������");
        } catch (Exception e) {e.printStackTrace();}
    }


    //�������㲥
    public void broadCastMsg(String msg){
        for(int i=0; i<interf.clients.getSize(); i++){
            Socket tmpS = interf.clients.getUserSocket(i);
            try {
                int index = interf.clients.getIndex(tmpS);
                System.out.println("��"+interf.clients.getUserName(i)+"�㲥��Ϣ...");
                Operations.sendMsg("������", msg, interf.clients.getUserPs(index));
            } catch (Exception e){
                System.out.println("��"+interf.clients.getUserName(i)+"�㲥��Ϣ����");
            }
        }
        System.out.println("�ѹ㲥���û�������Ϣ");
    }
    //������-���������ߵĿͻ��˷������û�������Ϣ
    public void sendUserLoginInfo(String userName) {
        for(int i=0; i<interf.clients.getSize(); i++){
            Socket tmpS = interf.clients.getUserSocket(i);
            try {
                int index = interf.clients.getIndex(tmpS);
                System.out.println("��"+interf.clients.getUserName(i)+"�㲥���û�������Ϣ...");
                Operations.sendMsg("NewUser", userName, interf.clients.getUserPs(index));

            } catch (Exception e){
                System.out.println("��"+interf.clients.getUserName(i)+"�������û�������Ϣ����");
            }
        }
        System.out.println("�ѹ㲥���û�������Ϣ");
    }
    //������-���������ߵĿͻ��˷����û�������Ϣ
    public void sendUserLogoutInfo(String userName) {
        for(int i=0; i<interf.clients.getSize(); i++){
            Socket tmpS = interf.clients.getUserSocket(i);
            try {
                Operations.sendMsg("UserLogout", userName, interf.clients.getUserPs(i));
            } catch (Exception e){
                System.out.println("�����û�������Ϣ����");
                System.out.println(e.getMessage());
            }
        }
    }
    //������-ǿ�������û�
    public void forceLogoutUser(String userName) {
        Socket userSocket = interf.clients.getUserSocket(userName);
        try{
            int index = interf.clients.getIndex(userName);
            Operations.sendMsg("ForceLogout", userName, interf.clients.getUserPs(index));
        }catch (Exception e) {System.out.println("ǿ�������û�ʧ�ܣ�");}
        logout(userName);
    }



    //������ת��Ⱥ����Ϣ
    public static void sendGroupChat(Message msg, PrintStream ps) throws IOException{
        try {
            ps.println("groupChatMsg");
            ps.println(msg.getGroupName());
            ps.println(msg.getUserFrom());
            ps.println(msg.getContent());
        } catch (Exception e) {System.out.println("����������Ϣʧ��");e.printStackTrace();}
    }

    //������-������������Ϣ
    public void ServerReceiveMsg(ServerSocket ss, Socket s) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        PrintStream ps = new PrintStream(s.getOutputStream());
        while(true) {
            try {
                String msgType = br.readLine();
                if(msgType != null) System.out.println("\n���յ�  " + msgType + " ����");
                //�û���¼
                if(msgType.equals("CheckUserInfo")){
                    String user = br.readLine();
                    String password = br.readLine();
                    String result = CheckUserInfo(user, password);
                    if(result.equals("2")) {
                        Operations.sendMsg( "WrongPassword", ps);
                        return;
                    }
                    else{
                        interf.clients.addUser(user, s, br, ps);
                        interf.addUser(user);
                        System.out.println("�ѽ��û����������б�:"+interf.clients.getOnlineNames());
                        login(s, user, password, result);
                    }
                }
                //ת��˽����Ϣ
                else if(msgType.equals("chatMsg")){
                    Message msg = Operations.receiveChat(br);
                    String target = msg.getUserTo();
                    PrintStream targetPs = interf.clients.getUserPs(interf.clients.getIndex(target));
                    Operations.sendChat(msg, targetPs);
                }
                //ת��Ⱥ����Ϣ
                else if(msgType.equals("groupChatMsg")){
                    Message msg = Operations.receiveGroupChat(br);
                    String target = msg.getUserTo();
                    PrintStream targetPs = interf.clients.getUserPs(interf.clients.getIndex(target));
                    sendGroupChat(msg, targetPs);
                }


            }catch (Exception e) {
                String user = interf.clients.getUserName(s);
                logout(user);
                s.close();
                System.out.println("\n"+user+"�ѶϿ�����...");
                break;
            }
        }

    }

}

