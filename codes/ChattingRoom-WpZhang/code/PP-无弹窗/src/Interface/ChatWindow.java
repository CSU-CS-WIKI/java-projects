package Interface;

import utilities.ChatHistory;
import utilities.Message;
import utilities.Operations;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintStream;
import javax.swing.*;

public class ChatWindow extends JFrame implements ActionListener {
    private String fromUser;
    private String toUser;
    private PrintStream ps;
    public boolean state;

    private Container container;
    private JPanel mainPanel;
    private JTextArea ChatMsgArea;
    private JPanel EditMsgArea;
    private JTextArea EditMsg;
    private JButton SendMsgBtn;

    public ChatWindow(String fromUser, String toUser, PrintStream ps) {
        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        this.state = false;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.ps = ps;

        this.mainPanel = new JPanel();
        this.EditMsgArea = new JPanel();

        this.ChatMsgArea = new JTextArea();
        this.ChatMsgArea.setWrapStyleWord(true);
        this.ChatMsgArea.setLineWrap(true);
        this.ChatMsgArea.setEditable(false);
        this.ChatMsgArea.setPreferredSize(new Dimension(600, 280));

        this.EditMsg = new JTextArea();
        this.EditMsg.setWrapStyleWord(true);
        this.EditMsg.setLineWrap(true);
        this.EditMsg.setPreferredSize(new Dimension(500, 100));
        this.EditMsgArea.add(this.EditMsg, BorderLayout.WEST);

        this.SendMsgBtn = new JButton("������Ϣ");
        this.SendMsgBtn.setPreferredSize(new Dimension(100,100));
        addActionListener(this.SendMsgBtn);
        this.EditMsgArea.add(this.SendMsgBtn, BorderLayout.EAST);


        this.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent arg)
            {
                state = false;
            }
        });
    }

    public ChatWindow(String fromUser, String toUser) {
        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        this.state = false;
        this.fromUser = fromUser;
        this.toUser = toUser;

        this.mainPanel = new JPanel();
        this.EditMsgArea = new JPanel();

        this.ChatMsgArea = new JTextArea();
        this.ChatMsgArea.setWrapStyleWord(true);
        this.ChatMsgArea.setLineWrap(true);
        this.ChatMsgArea.setEditable(false);
        this.ChatMsgArea.setPreferredSize(new Dimension(600, 280));

        this.EditMsg = new JTextArea();
        this.EditMsg.setWrapStyleWord(true);
        this.EditMsg.setLineWrap(true);
        this.EditMsg.setPreferredSize(new Dimension(500, 100));
        this.EditMsgArea.add(this.EditMsg, BorderLayout.WEST);

        this.SendMsgBtn = new JButton("������Ϣ");
        this.SendMsgBtn.setPreferredSize(new Dimension(100,100));
        addActionListener(this.SendMsgBtn);
        this.EditMsgArea.add(this.SendMsgBtn, BorderLayout.EAST);

        this.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent arg)
            {
                state = false;
            }
        });
    }

    public void showChatWindow(){
        try{
            setSize(650, 450);
            setTitle("������" + this.toUser + "����");
            this.container = getContentPane();


            //���������¼
            ChatHistory history = ChatHistory.readHistory(this.fromUser, this.toUser);
            for(int i=0; i<history.getSize(); i++){
                Message msg = history.getMessage(i);
                String content = "";
                //�û��Լ���������Ϣ
                if(msg.getUserFrom().equals(this.fromUser))
                    content = "�ң�";
                //�Է���������Ϣ
                else content = this.toUser + "��";
                content += msg.getContent() + "\n";
                this.ChatMsgArea.append(content);
            }

            this.mainPanel.add(this.ChatMsgArea, BorderLayout.CENTER);
            this.mainPanel.add(this.EditMsgArea, BorderLayout.SOUTH);
            this.container.add(this.mainPanel);
            //����ͨ������
		/*
		ImageIcon icon = new ImageIcon("icon/icon2.png");
		setIconImage(icon.getImage());*/
            setResizable(true);
            setLocationRelativeTo(null);
            setVisible(true);
            this.state = true;
        } catch (Exception e){
            System.out.println("��ʾ�������");
            e.printStackTrace();
        }
    }

    public void addMessage(Message msg){
        String content = msg.getUserFrom() + ":" +msg.getContent() + "\n";
        ChatHistory.refreshHistory(this.fromUser, this.toUser, msg);
        this.ChatMsgArea.append(content);
    }


    private void addActionListener(JButton btn) {
        // Ϊ��ť�󶨼�����
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //��ӵ��Ի���
                String content = EditMsg.getText();
                //������Ϣ����
                Message msg = new Message(fromUser, toUser, content);
                //������Ϣ
                try {
                    Operations.sendChat(msg, ps);
                    ChatHistory.refreshHistory(fromUser, toUser, msg);
                    ChatMsgArea.append("�ң�"+content + "\n");
                    EditMsg.setText("");
                } catch (Exception E){E.printStackTrace();}
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e){
    }

    public static void main(String[] args){
        /*
        Message m1 = new Message("A", "B", "Hi,B!");
        Message m2 = new Message("B", "A", "Hello~ A!");
        Message m3 = new Message("B", "A", "How are you?");
        Message m4 = new Message("A", "B", "I'm fine, thank you!");

        ChatHistory h = new ChatHistory("A", "B");
        h.addMessage(m1);
        h.addMessage(m2);
        h.addMessage(m3);
        h.addMessage(m4);

        h.saveHistory();
         */
        try {
            ChatWindow w = new ChatWindow("A", "B");
            w.showChatWindow();
        } catch (Exception e){e.printStackTrace();}

    }
}
