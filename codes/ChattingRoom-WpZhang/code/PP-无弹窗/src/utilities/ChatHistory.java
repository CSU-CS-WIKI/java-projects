package utilities;

import java.util.*;
import java.io.*;

/*
* �洢�û������¼
* */

public class ChatHistory implements Serializable {
	private String anotherUser; //��ͻ�������Ķ���
	private String thisUser;  //ʹ�øÿͻ��˵��û�
	private ArrayList<Message> history;
	
	public ChatHistory(String thisUser, String anotherUser) {
		this.thisUser = thisUser;
		this.anotherUser = anotherUser;
		this.history = new ArrayList<Message>();
	}
	
	public void addMessage(Message msg) {
		this.history.add(msg);
	}

	public int getSize(){return this.history.size();}

	public String getAnotherUser(){return this.anotherUser;}

	public Message getMessage(int index){return this.history.get(index);}

	public ArrayList<Message> getHistory() {
		return this.history;
	}

	public static ChatHistory readHistory(String thisUser, String anotherUser){
		//�ӱ����ļ���ȡ�����¼
		File file = new File("ChatHistory/"+thisUser+"/"+anotherUser+".dat");
		if(!file.exists()){
			ChatHistory history = new ChatHistory(thisUser, anotherUser);
			history.saveHistory();
			return history;
		}
		FileInputStream in;
		try {
			in = new FileInputStream(file);
			ObjectInputStream objIn = new ObjectInputStream(in);
			ChatHistory history = null;
			history = (ChatHistory)objIn.readObject();
            objIn.close();
            //System.out.println("Read chat history success!");
            return history;
		} catch (Exception e) {
		    System.out.println("Read chat history failed");
		    e.printStackTrace();
		    return null;
		} 
	}
	
	public void saveHistory() {
		//�洢�����¼������
		File file = new File("ChatHistory");
		if(!file.exists()) file.mkdir();
		file = new File("ChatHistory/"+this.thisUser);
		if(!file.exists()) file.mkdir();
		file =new File("ChatHistory/"+this.thisUser+"/"+anotherUser+".dat");

		FileOutputStream out;
		try {
		    out = new FileOutputStream(file);
		    ObjectOutputStream objOut = new ObjectOutputStream(out);
		    objOut.writeObject(this);
		    objOut.flush();
		    objOut.close();
		    //System.out.println("Write chat history success!");
		} catch (IOException e) {
		    System.out.println("Write chat history failed");
		    e.printStackTrace();
		}
	}

	public static void refreshHistory(String thisUser, String anotherUser, Message msg){
		ChatHistory history = ChatHistory.readHistory(thisUser, anotherUser);
		history.addMessage(msg);
		history.saveHistory();
		System.out.println("������ʷ��¼�ɹ�");
	}
}
