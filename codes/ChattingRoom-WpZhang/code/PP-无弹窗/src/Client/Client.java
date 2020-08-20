package Client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.*;
import java.io.*;
import javax.swing.*;
import java.lang.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import Interface.ClientInterface;
import Interface.JTextFieldHintListener;
import Interface.RoundBorder;
import utilities.*;

class Client extends JFrame{
	private Socket s = null;
	private OutputStream os;
	private InputStream is;
	public PrintStream ps;
	private BufferedReader br;

	
	private String user = null;

	private LoginFrame loginFrame;
	private ClientInterface interf;
	private OnlineClients clients;

	private boolean loginState;


    class LoginFrame extends JFrame implements ActionListener {
        private Container container;
        private JPanel mainPanel;

        private JLabel img;
        private JPanel imgArea;

        private JPanel loginArea;
        private JPanel inputArea;

        private JPanel userNameArea;
        private JPanel passwordArea;
        //private JLabel userNameLabel;
        //private JLabel passwordLabel;

        private JTextField userNameInput;
        private JTextField passwordInput;
        private JPanel btnArea;
        private JButton loginBtn;
        private JButton signupBtn;
        private JLabel loginResult;

        public LoginFrame(){
            this.mainPanel = new JPanel();
            this.mainPanel.setBackground(Color.WHITE);

            this.img = new JLabel(new ImageIcon("img/loginBg.jpg"));
            this.imgArea = new JPanel();
            this.imgArea.setPreferredSize(new Dimension(420, 150));
            this.imgArea.add(this.img);

            this.loginArea = new JPanel();
            this.loginArea.setPreferredSize(new Dimension(225, 150));
            this.loginArea.setBackground(Color.WHITE);
            this.inputArea = new JPanel();
            this.inputArea.setPreferredSize(new Dimension(220, 85));
            this.inputArea.setBackground(Color.WHITE);

            this.userNameArea = new JPanel();
            this.userNameArea.setPreferredSize(new Dimension(210, 35));
            //this.userNameLabel = new JLabel("�û���");
            //this.userNameLabel.setFont(new Font("΢���ź�",Font.BOLD, 15));
            //this.userNameLabel.setPreferredSize(new Dimension(50, 30));
            this.userNameInput = new JTextField();
            this.userNameInput.addFocusListener(new JTextFieldHintListener(this.userNameInput, "�û���"));
            this.userNameInput.setPreferredSize(new Dimension(200, 30));
            this.userNameInput.setFont(new Font("����",Font.PLAIN, 15));
            this.userNameInput.setBorder(new RoundBorder(Color.BLACK));
            //this.userNameArea.add(this.userNameLabel, BorderLayout.WEST);
            this.userNameArea.add(this.userNameInput, BorderLayout.EAST);
            this.userNameArea.setBackground(Color.WHITE);

            this.passwordArea = new JPanel();
            this.passwordArea.setPreferredSize(new Dimension(210, 35));
            //this.passwordLabel = new JLabel("����");
            //this.passwordLabel.setFont(new Font("΢���ź�",Font.BOLD, 15));
            //this.passwordLabel.setPreferredSize(new Dimension(50, 30));
            this.passwordInput = new JTextField();
            this.passwordInput.addFocusListener(new JTextFieldHintListener(this.passwordInput, "����"));
            this.passwordInput.setPreferredSize(new Dimension(200,30));
            this.passwordInput.setFont(new Font("����",Font.PLAIN, 15));
            this.passwordInput.setBorder(new RoundBorder(Color.BLACK));
            //this.passwordArea.add(this.passwordLabel, BorderLayout.WEST);
            this.passwordArea.add(this.passwordInput, BorderLayout.EAST);
            this.passwordArea.setBackground(Color.WHITE);


            this.btnArea = new JPanel();
            this.btnArea.setPreferredSize(new Dimension(220, 35));
            this.btnArea.setBackground(Color.WHITE);

            this.loginBtn = new JButton("��¼");
            this.loginBtn.setPreferredSize(new Dimension(90,30));
            this.loginBtn.setFont(new Font("΢���ź�",Font.BOLD, 14));
            //this.loginBtn.setBorder(new RoundBorder(new Color(51,153,255)));
            this.loginBtn.setBackground(new Color(51,153,255));
            this.loginBtn.setForeground(Color.WHITE);

            this.signupBtn = new JButton("ע��");
            this.signupBtn.setPreferredSize(new Dimension(90,30));
            this.signupBtn.setFont(new Font("΢���ź�",Font.BOLD, 14));
            //this.signupBtn.setBorder(new RoundBorder(new Color(51,153,255)));
            this.signupBtn.setBackground(new Color(51,153,255));
            this.signupBtn.setForeground(Color.WHITE);

            this.btnArea.add(loginBtn, BorderLayout.WEST);
            this.btnArea.add(signupBtn, BorderLayout.EAST);

            this.loginResult = new JLabel("",JLabel.CENTER);
            this.loginResult.setFont(new Font("����",Font.BOLD, 10));
            this.loginResult.setPreferredSize(new Dimension(220,20));

            this.inputArea.add(this.userNameArea);
            this.inputArea.add(this.passwordArea);
            this.loginArea.add(this.inputArea);
            this.loginArea.add(this.btnArea);
            this.loginArea.add(this.loginResult);

            this.mainPanel.add(this.imgArea);
            this.mainPanel.add(this.loginArea);

            this.loginBtn.addActionListener(this);
            this.signupBtn.addActionListener(this);
        }

        public void showInterface(){
            setSize(420, 350);
            setTitle("������");

            this.mainPanel.setFocusable(true);
            this.container = this.getContentPane();
            this.container.add(this.mainPanel);
            this.container.setPreferredSize(new Dimension(420, 350));

            setResizable(true);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setVisible(true);
        }

        @Override
        public void actionPerformed(ActionEvent e){
            if(e.getSource()==this.loginBtn){
                String userName = this.userNameInput.getText();
                String password = this.passwordInput.getText();
                System.out.println(userName);
                System.out.println(password);

                if(userName!=null){
                    try {
                        Operations.sendMsg("CheckUserInfo", userName, password, ps);
                        String res = br.readLine();
                        System.out.println("res:"+res);
                        if (res != null) {
                            if (res.equals("WrongPassword")) {
                                //���������ʾ����������Ϣ
                                //JOptionPane.showMessageDialog(null, "�������");
                                setLoginResult("�������");
                            }
                            else if(res.equals("NoneUser")) {
                                setLoginResult("�û�������");
                            }
                            else{
                                close();
                                user = userName;
                                loginState = true;
                            }
                        }
                    }catch (Exception e1){e1.printStackTrace();}
                }
            }
            else if(e.getSource()==this.signupBtn){
                String userName = this.userNameInput.getText();
                String password = this.passwordInput.getText();
                System.out.println(userName);
                System.out.println(password);

                if(userName!=null){
                    try {
                        Operations.sendMsg("Signup", userName, password, ps);
                        String res = br.readLine();
                        if (res != null) {
                            if(res.equals("UserExists")){
                                setLoginResult("�û��Ѵ���");
                            }
                            else {
                                close();
                                user = userName;
                                loginState = true;
                            }
                        }
                    }catch (Exception e1){e1.printStackTrace();}
                }
            }
        }

        public void setLoginResult(String result){
            this.loginResult.setText(result);
        }

        public void close(){
            setVisible(false);
        }

    }

	public Client() throws Exception{
		/*���������������*/
		this.s = new Socket("127.0.0.1", 9888);
		this.os = s.getOutputStream();
		this.is = s.getInputStream();
		this.ps = new PrintStream(os);
		this.br = new BufferedReader(new InputStreamReader(this.is));
		/*��¼����*/
        //��¼
        this.loginState = false;
        this.loginFrame = new LoginFrame();
        this.loginFrame.showInterface();
        while (!this.loginState){
            TimeUnit.MILLISECONDS.sleep(100);//����
        }
        listen();
	}

	public void listen(){
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
                    this.user = userName;
                    System.out.println("�û���Ϣ�������");
                }
                else if(msgType.equals("ReceiveOnlineClients")){
                    System.out.println("׼�����������û��б�...");
                    int length = Integer.parseInt(br.readLine());
                    ArrayList<String> onlineClients = new ArrayList<>();
                    ArrayList<Color> clientColors = new ArrayList<>();
                    for(int i=0; i<length; i++){
                        onlineClients.add(br.readLine());
                        String[] color = br.readLine().split(",");
                        System.out.println(color[0]+color[1]+color[2]);
                        Color c = new Color(Integer.valueOf(color[0]),Integer.valueOf(color[1]),Integer.valueOf(color[2]));
                        clientColors.add(c);
                    }
                    this.clients = new OnlineClients(onlineClients);
                    System.out.println("�����û��б������ɣ�"+this.clients.getOnlineNames());
                    ClientInterface interf = new ClientInterface(this.user, this.clients, clientColors, s, this.ps);
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
                            this.interf.addClientMsg(msg);
                        }
                    } catch (Exception e) {
                        System.out.println("����������Ϣ����"+e.getMessage());
                    }
                }
                //���շ������㲥
                else if(msgType.equals("������")){
                    this.interf.addServerMsg(br.readLine());
                }
                //����Ⱥ��Ϣ
                else if(msgType.equals("groupChatMsg")){
                    try {
                        Message msg = new Message(br.readLine(), br.readLine(), this.user, br.readLine());
                        if(msg.getGroupName().equals("ALL")){
                            this.interf.addChatRoomMsg(msg);
                        }
                        else {
                            //�ڽ��������Ϣ/�������촰��
                            this.interf.jumpWindow(msg);
                        }
                    } catch (Exception e) {
                        System.out.println("����������Ϣ����"+e.getMessage());
                    }
                }
                //���û�������Ϣ
                else if(msgType.equals("NewUser")){
                    String userName = br.readLine();
                    String[] color = br.readLine().split(",");
                    Color c = new Color(Integer.valueOf(color[0]),Integer.valueOf(color[1]),Integer.valueOf(color[2]));
                    System.out.println("���û����ߣ�"+userName);
                    clients.addUser(userName);
                    this.interf.addUser(userName, c);
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
