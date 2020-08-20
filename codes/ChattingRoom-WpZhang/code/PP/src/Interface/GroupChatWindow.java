package Interface;

import utilities.GroupChatHistory;
import utilities.Message;
import utilities.Operations;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.*;

public class GroupChatWindow extends JFrame implements ActionListener {
    private String thisUser;
    private String groupName;
    private ArrayList<String> members;
    public boolean state;
    private PrintStream ps;

    private Container container;
    private JPanel mainPanel;
    private JTextArea ChatMsgArea;
    private JPanel EditMsgArea;
    private JTextArea EditMsg;
    private JButton SendMsgBtn;

    public GroupChatWindow(String thisUser, String groupName, ArrayList<String> members, PrintStream ps) {
        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        this.thisUser = thisUser;
        this.groupName = groupName;
        this.members = members;
        this.ps = ps;
        this.state = false;

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

    public GroupChatWindow(String thisUser, String groupName, ArrayList<String> members) {
        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        this.thisUser = thisUser;
        this.groupName = groupName;
        this.members = members;
        this.state = false;


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

    public void showGroupChatWindow(){
        try{
            setSize(650, 450);
            setTitle(this.groupName);
            this.container = getContentPane();

            //���������¼
            GroupChatHistory history = GroupChatHistory.readHistory(this.thisUser, this.groupName);
            for(int i=0; i<history.getSize(); i++){
                Message msg = history.getMessage(i);
                String content = "";
                //�û��Լ���������Ϣ
                if(msg.getUserFrom().equals(this.thisUser))
                    content = "�ң�";
                //�Է���������Ϣ
                else content = msg.getUserFrom() + "��";
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
        GroupChatHistory.refreshHistory(this.thisUser, this.groupName, msg);
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
                Message msg = new Message(groupName, thisUser, groupName, content);
                //������Ϣ
                try {
                    Operations.sendGroupChat(members, msg, ps);
                    System.out.println("�������");
                    GroupChatHistory.refreshHistory(thisUser, groupName, msg);
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
        ArrayList<String> members = new ArrayList<>();
        members.add("��־��");
        members.add("��ε��");
        members.add("���");

        Collections.sort(members);
        for(int i=0; i<members.size(); i++){
            System.out.println(members.get(i));
        }
    }
}
