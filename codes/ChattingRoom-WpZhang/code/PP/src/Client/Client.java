package Client;

import java.net.*;
import java.io.*;
import javax.swing.*;
import java.lang.*;
import java.util.ArrayList;

import Interface.ClientInterface;
import utilities.*;

class Client extends JFrame{
	private Socket s = null;
	private OutputStream os;
	private InputStream is;
	public PrintStream ps;
	private BufferedReader br;

	
	private String user = null;
	private String password = null;

	private Info inf;
	private ClientInterface interf;
	private OnlineClients clients;
	
	public Client() throws Exception{
		/*���������������*/
		this.s = new Socket("127.0.0.1", 9888);
		this.os = s.getOutputStream();
		this.is = s.getInputStream();
		this.ps = new PrintStream(os);
		this.br = new BufferedReader(new InputStreamReader(this.is));
		/*��¼����*/
        //��¼
        while(true){
            this.user = (String)JOptionPane.showInputDialog(null, "�����û���", "��½", JOptionPane.PLAIN_MESSAGE);
            this.password = (String)JOptionPane.showInputDialog(null, "��������", "��½", JOptionPane.PLAIN_MESSAGE);
            if(this.user==null) continue;

            System.out.println("������½����");
            Operations.sendMsg("CheckUserInfo", this.user, this.password, this.ps);

            String loginState = br.readLine();
            if(loginState!=null) {
                if (loginState.equals("WrongPassword")) {
                    //���������ʾ����������Ϣ
                    JOptionPane.showMessageDialog(null, "�������");
                }
                else break;
            }
        }
        System.out.println("��½�ɹ���");

        try {
            System.out.println("\n��ʼ������Ϣ...\n");
            while(true) {
                String msgType = br.readLine();
                if(msgType != null) {
                    System.out.println("\n���յ� " + msgType);
                }
                if(msgType.equals("ReceiveUserInfo")){
                    System.out.println("׼�������û���Ϣ...");
                    String userName = br.readLine();
                    this.inf = new Info(userName);
                    System.out.println("�û���Ϣ�������");
                }
                else if(msgType.equals("ReceiveOnlineClients")){
                    System.out.println("׼�����������û��б�...");
                    int length = Integer.parseInt(br.readLine());
                    ArrayList<String> onlineClients = new ArrayList<>();
                    for(int i=0; i<length; i++){
                        onlineClients.add(br.readLine());
                    }
                    this.clients = new OnlineClients(onlineClients);
                    System.out.println("�����û��б������ɣ�"+this.clients.getOnlineNames());
                    ClientInterface interf = new ClientInterface(this.inf, this.user, this.clients, s, this.ps);
                    this.interf = interf;
                    System.out.println("��ʾ����\n");
                    this.interf.showInterface();
                }
                //����˽����Ϣ
                else if(msgType.equals("chatMsg")){
                    try {
                        Message msg = new Message(br.readLine(), br.readLine(), br.readLine());
                        //�ڽ��������Ϣ/�������촰��
                        if(msg.getUserFrom().equals("������")){
                            this.interf.addServerMsg(msg.getContent());
                        }
                        else{
                            this.interf.jumpWindow(msg);
                        }
                    } catch (Exception e) {
                        System.out.println("����������Ϣ����"+e.getMessage());
                    }
                }
                //���շ������㲥
                else if(msgType.equals("������")){
                    this.interf.addServerMsg(br.readLine());
                }
                //����Ⱥ����Ϣ
                else if(msgType.equals("groupChatMsg")){
                    try {
                        Message msg = new Message(br.readLine(), br.readLine(), this.user, br.readLine());
                        //�ڽ��������Ϣ/�������촰��
                        this.interf.jumpWindow(msg);
                    } catch (Exception e) {
                        System.out.println("����������Ϣ����"+e.getMessage());
                    }
                }
                //���û�������Ϣ
                else if(msgType.equals("NewUser")){
                    String userName = br.readLine();
                    System.out.println("���û����ߣ�"+userName);
                    clients.addUser(userName);
                    this.interf.addUser(userName);
                    this.interf.addServerMsg(userName+"������");
                }
                //�����û�����
                else if(msgType.equals("UserLogout")){
                    String userName = br.readLine();
                    System.out.println("�û����ߣ�"+userName);
                    clients.delUser(userName);
                    this.interf.delUser(userName);
                    this.interf.addServerMsg(userName+"������");
                }
                //ǿ������
                else if(msgType.equals("ForceLogout")){
                    System.out.println("��������ǿ�����ߣ�");
                    System.exit(0);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }
	}

	public static void main(String[] args) throws Exception{
		new Client();
	}

}
