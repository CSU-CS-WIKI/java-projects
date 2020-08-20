package Interface;

import utilities.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintStream;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.*;

public class ClientInterface extends JFrame implements ActionListener {
	private Info inf;
	private Socket s;
	private PrintStream ps;
	private OnlineClients clients;
	private ClientInterface self;

	private ServerSocket ss = null;

	public String userName;

	private Container container;	//����
	private JTextArea ServerMsgArea;	//��������Ϣ��
	private JList<String> clientList; //��ʾ�û��б�
	private DefaultListModel<String> listModel; //�洢�����û��б�
	private ArrayList<String> openedClient;	//�������촰�ڵ��û��б�
	private ArrayList<ChatWindow> opendedChatWindow; //�Ѿ��򿪵����촰��
	private ArrayList<String> openedGroup; //�������촰�ڵ�Ⱥ���б�
	private ArrayList<GroupChatWindow> opendedGroupChatWindow; //�Ѿ��򿪵�Ⱥ�Ĵ���


	public ClientInterface(Info inf, String user, OnlineClients olClients, Socket socket, PrintStream ps) {
		this.self = this;
		this.inf = inf;
		this.s = socket;
		this.ps = ps;
		this.userName = user;
		this.clients = olClients;
		this.clients.delUser(user);
		this.listModel = new DefaultListModel<>();
		this.clientList = new JList();
		this.openedClient = new ArrayList<>();
		this.opendedChatWindow = new ArrayList<>();
		this.openedGroup = new ArrayList<>();
		this.opendedGroupChatWindow = new ArrayList<>();

		//���������б��ϵ�����¼�
		this.clientList.addMouseListener(
			new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					//˫������˽�ĶԻ���
					if (evt.getClickCount() == 2) {
						String target = clientList.getSelectedValue();
						if(!target.equals("����Ⱥ��")) {
							if(openedGroup.indexOf(target)==-1) {
								ChatWindow w = new ChatWindow(inf.userName, target, ps);
								w.showChatWindow();
								openedClient.add(target);
								opendedChatWindow.add(w);
							}
							else{
								GroupChatWindow gw = opendedGroupChatWindow.get(openedGroup.indexOf(target));
								gw.showGroupChatWindow();
							}
						}
						else{
							checkBox c = new checkBox(self, clients);
							if(c.checkedUsers.size()>0) {
								//����Ⱥ������
								ArrayList<String> tmp = new ArrayList<>();
								tmp.add(userName);
								for (int i = 0; i < c.checkedUsers.size(); i++) {tmp.add(c.checkedUsers.get(i));}
								Collections.sort(tmp);
								String groupName = tmp.get(0);
								for (int i = 1; i < tmp.size(); i++) {groupName = groupName + "_" + tmp.get(i);}


								GroupChatWindow gw = new GroupChatWindow(inf.userName, groupName, c.checkedUsers, ps);
								gw.showGroupChatWindow();
								openedGroup.add(groupName);
								opendedGroupChatWindow.add(gw);
								listModel.removeElement("����Ⱥ��");
								listModel.addElement(groupName);
								listModel.addElement("����Ⱥ��");
								clientList.setModel(listModel);
							}
						}
					}
				}
			}
		);
	}


	public void showInterface(){
		try{
			setSize(800, 430);
			setTitle("PP Chating Room   " + this.userName);

			this.container = getContentPane();

			this.ServerMsgArea = new JTextArea();
			this.ServerMsgArea.setWrapStyleWord(true);
			this.ServerMsgArea.setLineWrap(true);
			this.ServerMsgArea.setEditable(false);
			this.ServerMsgArea.setPreferredSize(new Dimension(600, 280));
			this.container.add(this.ServerMsgArea, BorderLayout.WEST);

			for(int i=0; i<this.clients.getOnlineNames().size(); i++){
				if(this.clients.getUserName(i)!=this.userName) {
					String tmp = this.clients.getUserName(i);
					this.listModel.addElement(tmp);
				}
			}
			this.listModel.addElement("����Ⱥ��");

			this.clientList.setModel(this.listModel);
			this.clientList.setFixedCellWidth(150);
			this.container.add(this.clientList, BorderLayout.EAST);

			//����ͨ������
		/*
		ImageIcon icon = new ImageIcon("icon/icon2.png");
		setIconImage(icon.getImage());*/
			setResizable(true);
			setLocationRelativeTo(null);
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			setVisible(true);
		} catch (Exception e){
			System.out.println("��ʾ�������");
			e.printStackTrace();
		}
	}


	//���û���¼����ӵ��û��б�
	public void addUser(String userName){
		this.listModel.removeElement("����Ⱥ��");
		this.listModel.addElement(userName);
		this.listModel.addElement("����Ⱥ��");
		this.clientList.setModel(this.listModel);
	}
	//�û����ߺ�ɾ��
	public void delUser(String userName){
		this.listModel.removeElement(userName);
		this.clientList.setModel(this.listModel);
		try{
			int index = this.openedClient.indexOf(userName);
			this.openedClient.remove(index);
			this.opendedChatWindow.remove(index);
		}catch (Exception e){}
	}

	//���յ��û���Ϣ�󵯳�����
	public void jumpWindow(Message msg){
		String source = msg.getUserFrom();
		String groupName = msg.getGroupName();
		System.out.println("GroupName:"+groupName);
		if(groupName.equals("###")){
			if(this.openedClient.contains(source)){
				System.out.println("�Ѿ��򿪴���");
				int index = this.openedClient.indexOf(source);
				ChatWindow w = this.opendedChatWindow.get(index);
				w.addMessage(msg);
				if(!w.state){
					w.setVisible(true);
					w.state = true;
				}
			}
			else {
				System.out.println("û�д򿪴���");
				ChatHistory.refreshHistory(msg.getUserTo(), msg.getUserFrom(), msg);
				ChatWindow w = new ChatWindow(this.userName, source, ps);
				this.openedClient.add(source);
				this.opendedChatWindow.add(w);
				w.showChatWindow();
			}
		}
		else{
			ArrayList<String> members = new ArrayList<>();
			String tmp[] = groupName.split("_");
			for(int i=0; i<tmp.length; i++){
				members.add(tmp[i]);
			}
			members.remove(this.userName);

			if(this.openedGroup.contains(groupName)){
				System.out.println("�Ѿ���Ⱥ�Ĵ���");
				int index = this.openedGroup.indexOf(groupName);
				GroupChatWindow w = this.opendedGroupChatWindow.get(index);
				w.addMessage(msg);
				if(!w.state){
					w.setVisible(true);
					w.state = true;
				}
			}
			else {
				System.out.println("û�д�Ⱥ�Ĵ���");
				GroupChatHistory.refreshHistory(msg.getUserTo(), groupName, msg);
				GroupChatWindow gw = new GroupChatWindow(this.userName, groupName, members, ps);
				this.openedGroup.add(groupName);
				this.opendedGroupChatWindow.add(gw);
				gw.showGroupChatWindow();
				this.listModel.removeElement("����Ⱥ��");
				this.listModel.addElement(groupName);
				this.listModel.addElement("����Ⱥ��");
				this.clientList.setModel(this.listModel);
			}
		}
	}

	//��ʾ��������Ϣ
	public void addServerMsg(String msg){
		String content = "������:" + msg + "\n";
		this.ServerMsgArea.append(content);
	}

	@Override
	public void actionPerformed(ActionEvent e){
	}

	private JButton getBtn(JPanel panel) {
		int count = panel.getComponentCount();
		for (int i = 0; i < count; i++) {
			Component comp = panel.getComponent(i);
			if (comp instanceof JButton) {
				JButton btn = (JButton) comp;
				return btn;
			}
		}
		return null;
	}


	public static void main (String[]args){
		Socket s = new Socket();
		Info inf = new Info("A");
		ArrayList<String> tmpl = new ArrayList<>();
		PrintStream ps = null;
		try {
			ps= new PrintStream(s.getOutputStream());
		} catch (Exception e){};
		tmpl.add("A");
		tmpl.add("B");
		tmpl.add("C");
		OnlineClients ol = new OnlineClients(tmpl);
		ClientInterface w = new ClientInterface(inf, "A", ol, s, ps);
		w.showInterface();

	}

}
