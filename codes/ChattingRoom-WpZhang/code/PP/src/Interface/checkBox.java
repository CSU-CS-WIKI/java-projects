package Interface;

import utilities.OnlineClients;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;

public class checkBox{
    // ����
    private JDialog dialog;
    private Container cont;
    private JPanel pan = new JPanel();
    private JButton submit = new JButton("OK");
    private ArrayList<JCheckBox> users = new ArrayList<>();
    private OnlineClients clients;
    public ArrayList<String> checkedUsers = new ArrayList<>();

    public checkBox(ClientInterface frame, OnlineClients clients)
    {
        this.dialog = new JDialog(frame, "Choose Users", true);
        this.dialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.cont = this.dialog.getContentPane();
        this.clients = clients;

        for(int i=0; i<clients.getSize(); i++){
            JCheckBox jcb = new JCheckBox(clients.getUserName(i)); // ����һ����ѡ��
            this.users.add(jcb);
            pan.add(jcb,BorderLayout.WEST);
        }

        pan.add(submit, BorderLayout.EAST);
        cont.add(pan); // �������뵽����֮��

        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(int i=0; i<clients.getSize(); i++){
                    if(users.get(i).isSelected()){
                        checkedUsers.add(clients.getUserName(i));
                    }
                }
                dialog.dispose();
            }
        });

        this.dialog.setSize(200,80);
        this.dialog.setVisible(true);

    }

    public checkBox(ServerInterface frame, OnlineClients clients)
    {
        this.dialog = new JDialog(frame, "Choose Users", true);
        this.dialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.cont = this.dialog.getContentPane();
        this.clients = clients;

        for(int i=0; i<clients.getSize(); i++){
            JCheckBox jcb = new JCheckBox(clients.getUserName(i)); // ����һ����ѡ��
            this.users.add(jcb);
            pan.add(jcb,BorderLayout.WEST);
        }

        pan.add(submit, BorderLayout.EAST);
        cont.add(pan); // �������뵽����֮��

        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(int i=0; i<clients.getSize(); i++){
                    if(users.get(i).isSelected()){
                        checkedUsers.add(clients.getUserName(i));
                    }
                }
                dialog.dispose();
            }
        });

        this.dialog.setSize(200,80);
        this.dialog.setVisible(true);

    }
}
