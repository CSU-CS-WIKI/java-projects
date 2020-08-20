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
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

public class GroupChatWindow extends JFrame implements ActionListener {
    private String thisUser;
    private String groupName;
    private ArrayList<String> members;
    public boolean state;
    private PrintStream ps;

    private Container container;
    private JPanel mainPanel;
    private JTextPane ChatMsgArea;
    private JScrollPane jsp;
    private JPanel EditMsgArea;
    private JTextArea EditMsg;
    private JButton SendMsgBtn;

    private Style baseStyle;	//�ı�������ʽ
    private Map<String, Style> styles;	//�û�-��ʽ��Ӧ��

    public GroupChatWindow(String thisUser, String groupName, ArrayList<String> members, Map<String, javax.swing.text.Style> styleList, PrintStream ps) {
        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        this.thisUser = thisUser;
        this.groupName = groupName;
        this.members = members;
        this.ps = ps;
        this.state = false;

        this.mainPanel = new JPanel();
        this.EditMsgArea = new JPanel();

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
        this.baseStyle = this.ChatMsgArea.addStyle("base",base_style);
        this.styles = styleList;
        Style s1 = initStyle(new Color(33, 31, 48),"me");
        this.styles.put("me", s1);

        this.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent arg)
            {
                state = false;
                setVisible(false);
            }
        });
    }

    public void popWindow(){
        this.state = true;
        setVisible(true);
    }

    public void showGroupChatWindow(){
        try{
            setSize(650, 370);
            setTitle(this.groupName);
            this.container = getContentPane();
            this.ChatMsgArea.setBorder(new RoundBorder(new Color(247,141,63)));
            this.jsp = new JScrollPane(this.ChatMsgArea);
            this.jsp.setPreferredSize(new Dimension(605, 280));
            this.jsp.setBorder(BorderFactory.createEmptyBorder());
            this.jsp.setBackground(Color.WHITE);

            //���������¼
            GroupChatHistory history = GroupChatHistory.readHistory(this.thisUser, this.groupName);
            for(int i=0; i<history.getSize(); i++){
                Message msg = history.getMessage(i);
                String content = "";
                //�û��Լ���������Ϣ
                if(msg.getUserFrom().equals(this.thisUser)) {
                    content = " �ң�" + msg.getContent() + "\n";
                    addStyledText(content, "me");
                }
                //�Է���������Ϣ
                else{
                    content = " "+msg.getUserFrom() + "��" + msg.getContent() + "\n";
                    addStyledText(content, msg.getUserFrom());
                }
                //this.ChatMsgArea.append(content);
            }

            this.mainPanel.add(this.jsp, BorderLayout.CENTER);
            this.mainPanel.add(this.EditMsgArea, BorderLayout.SOUTH);
            this.mainPanel.setBackground(Color.WHITE);
            this.container.add(this.mainPanel);
            this.container.setBackground(Color.WHITE);
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

    public void addMessage(Message msg){
        String content = msg.getUserFrom() + ":" +msg.getContent() + "\n";
        GroupChatHistory.refreshHistory(this.thisUser, this.groupName, msg);
        //this.ChatMsgArea.append(content);
        addStyledText(content, msg.getUserFrom());
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
                    addStyledText(" �ң�"+content + "\n","me");
                    //ChatMsgArea.append("�ң�"+content + "\n");
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
