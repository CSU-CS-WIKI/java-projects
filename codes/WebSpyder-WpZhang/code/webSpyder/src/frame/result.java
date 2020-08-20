package frame;

import crawler.Crawler;

import javax.swing.*;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class result extends JFrame implements ActionListener {
    private Container container;
    private JPanel mainPanel;

    private JTextArea displayArea;
    //private JScrollPane js;
    private JScrollPane jsp;

    private JPanel setWordsPanel;
    private JTextField sensitiveWords;
    private JScrollPane sensitiveWordsScroll;
    private JButton highlightBtn;
    private JButton chooseSWFileBtn;
    private String originContent;

    public result(){
        this.mainPanel = new JPanel();
        this.mainPanel.setPreferredSize(new Dimension(800, 600));

        this.displayArea = new JTextArea();
        this.displayArea.setWrapStyleWord(true);
        this.displayArea.setLineWrap(true);
        this.displayArea.setEditable(false);
        Font font=new Font("����",Font.PLAIN,18);
        this.displayArea.setFont(font);
        this.jsp = new JScrollPane(this.displayArea);
        this.jsp.setPreferredSize(new Dimension(790, 500));
        this.jsp.setBorder(BorderFactory.createEmptyBorder());

        this.setWordsPanel = new JPanel();
        this.setWordsPanel.setPreferredSize(new Dimension(790, 45));
        this.sensitiveWords = new JTextField();
        this.sensitiveWords.setFont(new Font("΢���ź�",Font.BOLD,16));
        this.sensitiveWords.setForeground(new Color(247,141,63));
        this.sensitiveWords.setBorder(BorderFactory.createEmptyBorder());


        this.sensitiveWordsScroll = new JScrollPane(this.sensitiveWords);
        this.sensitiveWordsScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.sensitiveWordsScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        this.sensitiveWordsScroll.setPreferredSize(new Dimension(520, 40));

        this.highlightBtn = new JButton("��ʾ���д�");
        this.highlightBtn.setPreferredSize(new Dimension(115, 40));
        this.highlightBtn.setBackground(new Color(185,237,248));//���ñ���ɫ
        //this.highlightBtn.setBorderPainted(false);

        this.chooseSWFileBtn = new JButton("ѡ�����дʿ�");
        this.chooseSWFileBtn.setPreferredSize(new Dimension(115, 40));
        this.chooseSWFileBtn.setBackground(new Color(57,186,232));//���ñ���ɫ
        //this.chooseSWFileBtn.setBorderPainted(false);

        this.setWordsPanel.add(this.sensitiveWordsScroll, BorderLayout.WEST);
        this.setWordsPanel.add(this.highlightBtn, BorderLayout.CENTER);
        this.setWordsPanel.add(this.chooseSWFileBtn, BorderLayout.EAST);
        this.setWordsPanel.setBackground(Color.WHITE);

        this.mainPanel.setBackground(Color.WHITE);
        highlightBtn.addActionListener(this);
        chooseSWFileBtn.addActionListener(this);
    }

    public void showWindow(){
        setSize(800, 600);
        setTitle("��ȡ���");

        this.container = getContentPane();
        this.mainPanel.add(this.jsp,BorderLayout.CENTER);
        this.mainPanel.add(this.setWordsPanel,BorderLayout.SOUTH);
        this.container.add(this.mainPanel,BorderLayout.CENTER);

        setResizable(true);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void setText(String text){
        this.displayArea.setText(text);
        this.originContent = text;
    }

    public void highlightSensitiveWords(String[] sensitiveWords){
        Highlighter highLighter = this.displayArea.getHighlighter();
        highLighter.removeAllHighlights();
        String content = this.originContent;
        DefaultHighlighter.DefaultHighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(new Color(243,129,129));

        for(int i=0; i<sensitiveWords.length; i++){
            int count = 0;
            String keyWord = sensitiveWords[i];
            int pos = 0;
            while ((pos = content.indexOf(keyWord, pos)) >= 0)
            {
                try {
                    pos += keyWord.length();
                    count ++;
                }catch (Exception e){e.printStackTrace();}
            }
            String result = keyWord + ":" + count + "\t";
            content += result;
        }
        this.displayArea.setText(content);

        for(int i=0; i<sensitiveWords.length; i++){
            String keyWord = sensitiveWords[i];
            int pos = 0;
            while ((pos = content.indexOf(keyWord, pos)) >= 0)
            {
                try {
                    highLighter.addHighlight(pos, pos + keyWord.length(), painter);
                    pos += keyWord.length();
                }catch (Exception e){e.printStackTrace();}
            }
        }


    }

    private String readFile(){
        JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.showDialog(new JLabel(), "ѡ��");
        File file = chooser.getSelectedFile();
        String content = null;
        if(file!=null) {
            if (!file.exists() || file.isDirectory()) {
                JOptionPane.showMessageDialog(null, "�ļ������ڻ���һ�������ļ�", "����", JOptionPane.ERROR_MESSAGE);
            }

            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String tmp = br.readLine();
                content = tmp;
                while (tmp != null) {
                    tmp = br.readLine();
                    if(tmp!=null) content += tmp;
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return content;
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==highlightBtn){
            String[] sw = this.sensitiveWords.getText().split(",");
            if(sw[0].length()>0)
                highlightSensitiveWords(sw);
        }
        else if(e.getSource()==chooseSWFileBtn){
            try {
                String content = readFile();
                this.sensitiveWords.setText(content);

                String[] sw = content.split(",");
                highlightSensitiveWords(sw);
            }catch (Exception e1){}
        }
    }
}
