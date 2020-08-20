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
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

public class ClientInterface extends JFrame implements ActionListener {
	private Socket s;
	private PrintStream ps;
	private OnlineClients clients;
	private ClientInterface self;

	private ServerSocket ss = null;

	public String userName;

	private Container container;	//����
	private JTextPane ChatMsgArea;	//��������Ϣ��
	private JPanel mainPanel;
	private JPanel EditMsgArea;
	private JTextArea EditMsg;
	private JButton SendMsgBtn;
	private JScrollPane jsp;
	
	private JList<String> clientList; //��ʾ�û��б�
	private DefaultListModel<String> listModel; //�洢�����û��б�
	private ArrayList<String> openedClient;	//�������촰�ڵ��û��б�
	private ArrayList<ChatWindow> opendedChatWindow; //�Ѿ��򿪵����촰��
	private ArrayList<String> openedGroup; //�������촰�ڵ�Ⱥ���б�
	private ArrayList<GroupChatWindow> opendedGroupChatWindow; //�Ѿ��򿪵�Ⱥ�Ĵ���

	private Style baseStyle;	//�ı�������ʽ
	private Map<String, javax.swing.text.Style> styles;	//�û�-��ʽ��Ӧ��


	public ClientInterface(String user, OnlineClients olClients, ArrayList<Color> colors, Socket socket, PrintStream ps) {
		this.self = this;
		this.s = socket;
		this.ps = ps;
		this.userName = user;
		this.clients = olClients;
		this.listModel = new DefaultListModel<>();
		this.clientList = new JList();
		this.clientList.setFont(new Font("΢���ź�",Font.BOLD,14));
		this.openedClient = new ArrayList<>();
		this.opendedChatWindow = new ArrayList<>();
		this.openedGroup = new ArrayList<>();
		this.opendedGroupChatWindow = new ArrayList<>();
		this.EditMsgArea = new JPanel();
		this.mainPanel = new JPanel();

		this.ChatMsgArea = new JTextPane();
		this.ChatMsgArea.setEditable(false);

		this.EditMsg = new JTextArea();
		this.EditMsg.setWrapStyleWord(true);
		this.EditMsg.setLineWrap(true);
		this.EditMsg.setPreferredSize(new Dimension(500, 30));
		this.EditMsg.setFont(new Font("΢���ź�",Font.PLAIN,14));
		this.EditMsg.setBorder(new RoundBorder(new Color(247,141,63)));
		this.EditMsgArea.add(this.EditMsg, BorderLayout.WEST);
		this.EditMsgArea.setBackground(Color.WHITE);

		this.SendMsgBtn = new JButton("������Ϣ");
		this.SendMsgBtn.setPreferredSize(new Dimension(100,30));
		this.SendMsgBtn.setFont(new Font("΢���ź�",Font.BOLD,13));
		this.SendMsgBtn.setBackground(new Color(185,237,248));//���ñ���ɫ
		this.SendMsgBtn.setBorderPainted(false);
		addActionListener(this.SendMsgBtn);
		this.EditMsgArea.add(this.SendMsgBtn, BorderLayout.EAST);

		Style base_style = this.ChatMsgArea.getStyledDocument().addStyle(null, null);
		StyleConstants.setFontFamily(base_style, "΢���ź�");// Ϊstyle��ʽ������������
		StyleConstants.setFontSize(base_style, 18);// Ϊstyle��ʽ���������С
		this.styles = new HashMap<>();
		this.baseStyle = this.ChatMsgArea.addStyle("base",base_style);
		Style s1 = initStyle(new Color(33, 31, 48),"me");
		this.styles.put("me", s1);
		Style s2 = initStyle(new Color(189, 40, 69),"server");
		this.styles.put("server", s2);
		for(int i=0; i<colors.size(); i++){
			String n = this.clients.getUserName(i);
			Style tmp_s = initStyle(colors.get(i), n);
			this.styles.put(n, tmp_s);
		}
		this.clients.delUser(user);

		//���������б��ϵ�����¼�
		this.clientList.addMouseListener(
			new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					//˫������˽�ĶԻ���
					if (evt.getClickCount() == 2) {
						String target = clientList.getSelectedValue();
						if(!target.equals("����Ⱥ��")) {
							if(openedGroup.indexOf(target)!=-1) {
								GroupChatWindow gw = opendedGroupChatWindow.get(openedGroup.indexOf(target));
								//gw.showGroupChatWindow();
								gw.popWindow();
							}
						}
						else{
							checkBox c = new checkBox(self, clients);
							if(c.checkedUsers.size()>0) {
								//����Ⱥ�����ƺ��û���ɫ�б�
								ArrayList<String> tmp = new ArrayList<>();
								Map<String, javax.swing.text.Style> styleList = new HashMap<>();
								tmp.add(userName);
								for (int i = 0; i < c.checkedUsers.size(); i++) {
									String user = c.checkedUsers.get(i);
									tmp.add(user);
									styleList.put(user, styles.get(user));
								}
								Collections.sort(tmp);
								String groupName = tmp.get(0);
								for (int i = 1; i < tmp.size(); i++) {groupName = groupName + "_" + tmp.get(i);}

								GroupChatWindow gw = new GroupChatWindow(userName, groupName, c.checkedUsers, styleList, ps);
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
			setSize(800, 370);
			setTitle("PP Chating Room   " + this.userName);

			this.container = getContentPane();
			this.ChatMsgArea.setBorder(new RoundBorder(new Color(247,141,63)));
			this.jsp = new JScrollPane(this.ChatMsgArea);
			this.jsp.setPreferredSize(new Dimension(605, 280));
			this.jsp.setBorder(BorderFactory.createEmptyBorder());
			this.jsp.setBackground(Color.WHITE);

			for(int i=0; i<this.clients.getOnlineNames().size(); i++){
				if(this.clients.getUserName(i)!=this.userName) {
					String tmp = this.clients.getUserName(i);
					this.listModel.addElement(tmp);
				}
			}
			this.listModel.addElement("����Ⱥ��");

			this.mainPanel.setPreferredSize(new Dimension(640, 370));
			this.mainPanel.add(this.jsp, BorderLayout.NORTH);
			this.mainPanel.add(this.EditMsgArea, BorderLayout.SOUTH);
			this.mainPanel.setBackground(Color.WHITE);
			this.container.add(this.mainPanel, BorderLayout.WEST);

			this.clientList.setModel(this.listModel);
			this.clientList.setFixedCellWidth(150);
			this.container.add(this.clientList, BorderLayout.EAST);
			this.container.setBackground(Color.WHITE);

			//����ͨ������
			setResizable(true);
			setLocationRelativeTo(null);
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			setVisible(true);
		} catch (Exception e){
			System.out.println("��ʾ�������");
			e.printStackTrace();
		}
	}


	//��������ʽ
	public Style initStyle(Color c, String name){
		Style s = this.ChatMsgArea.addStyle(name, this.baseStyle);
		StyleConstants.setForeground(s, c);
		StyleConstants.setFontSize(s,16);
		return s;
	}

	//�����ı�ת��Ϊ��ʽ�ı������뵽����
	public void addStyledText(String text, String name){
		try {
			Style style = this.styles.get(name);
			this.ChatMsgArea.getStyledDocument().insertString(
					this.ChatMsgArea.getStyledDocument().getLength(),
					text, style
			);
		}catch (Exception e){e.printStackTrace();}
	}

	//���û���¼����ӵ��û��б�
	public void addUser(String userName, Color c){
		this.listModel.removeElement("����Ⱥ��");
		this.listModel.addElement(userName);
		this.listModel.addElement("����Ⱥ��");
		this.clientList.setModel(this.listModel);
		Style style = initStyle(c,userName);
		this.styles.put(userName, style);
	}
	//�û����ߺ�ɾ��
	public void delUser(String userName){
		this.listModel.removeElement(userName);
		this.clientList.setModel(this.listModel);
		try{
			int index = this.openedClient.indexOf(userName);
			this.openedClient.remove(index);
			this.opendedChatWindow.remove(index);
			this.styles.remove(userName);
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
			Map<String, javax.swing.text.Style> styleList = new HashMap<>();
			String tmp[] = groupName.split("_");
			for(int i=0; i<tmp.length; i++){
				members.add(tmp[i]);
				styleList.put(tmp[i], styles.get(tmp[i]));
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
				GroupChatWindow gw = new GroupChatWindow(this.userName, groupName, members, styleList,ps);
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
		String content = " ������("+ Operations.getCurrentTime() +"):" + msg + "\n";
		addStyledText(content, "server");
		//this.ChatMsgArea.append(content);
	}
	//��ʾ�û�˽����Ϣ
	public void addClientMsg(Message msg){
		String content = " "+msg.getUserFrom()+"����˵("+ Operations.getCurrentTime() +"):" + msg.getContent() + "\n";
		addStyledText(content, msg.getUserFrom());
		//this.ChatMsgArea.append(content);
	}
	//��ʾ������ȫ����Ϣ
	public void addChatRoomMsg(Message msg){
		String content = " "+msg.getUserFrom()+"˵("+ Operations.getCurrentTime() +"):" + msg.getContent() + "\n";
		addStyledText(content, msg.getUserFrom());
		//this.ChatMsgArea.append(content);
	}

	private void addActionListener(JButton btn) {
		// Ϊ��ť�󶨼�����
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//��ӵ��Ի���
				String content = EditMsg.getText();
				String toUser = clientList.getSelectedValue();
				if(toUser.equals("����Ⱥ��")){
					Message msg = new Message("ALL", userName, "ALL", content);
					//������Ϣ
					try {
						Operations.sendGroupChat(clients.getOnlineNames(), msg, ps);
						System.out.println("�������");
						GroupChatHistory.refreshHistory(userName, "ALL", msg);
						addStyledText(" ��˵("+ Operations.getCurrentTime() +")��"+content + "\n", "me");
						EditMsg.setText("");
					} catch (Exception E){E.printStackTrace();}
				}
				else {
					//������Ϣ����
					Message msg = new Message(userName, toUser, content);
					//������Ϣ
					try {
						Operations.sendChat(msg, ps);
						ChatHistory.refreshHistory(userName, toUser, msg);
						addStyledText(" �Ҷ�"+ toUser + "˵("+ Operations.getCurrentTime() +")��" + content + "\n", "me");
						//ChatMsgArea.append("�Ҷ�"+ toUser + "˵��" + content + "\n");
						EditMsg.setText("");
					} catch (Exception E) {
						E.printStackTrace();
					}
				}
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e){
	}


	public static void main (String[]args){
	}

}
