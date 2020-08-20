package utilities;

import java.io.*;
import java.util.ArrayList;

/*
* �洢�û�������Ϣ
*/
public class Info implements Serializable{
	public String userName;	//�û���
	
	public Info(String userName) {
	    this.userName = userName;
	}

	public static Info readInfo(String userName) {
        try {
            File file = new File("UserInfo/"+userName+".dat");
            FileInputStream in = new FileInputStream(file);
            ObjectInputStream objIn = new ObjectInputStream(in);
            Info inf = null;
            inf = (Info)objIn.readObject();
            objIn.close();
            System.out.println("�ɹ���ȡ"+userName+"��Ϣ");
            return inf;
        } catch (Exception e) {
            System.out.println("��ȡ"+userName+"��Ϣʧ��");
            e.printStackTrace();
            return null;
        }
    }
	public void saveInfo(){
        try {
            File dir = new File("UserInfo/");
            if(!dir.exists()) dir.mkdir();

            File file = new File("UserInfo/"+this.userName+".dat");
            FileOutputStream out = new FileOutputStream(file);
            ObjectOutputStream objOut = new ObjectOutputStream(out);
            objOut.writeObject(this);
            objOut.flush();
            objOut.close();
            System.out.println("�ɹ�����"+userName+"��Ϣ");
        } catch (IOException e) {
            System.out.println("����"+userName+"��Ϣʧ��");
            e.printStackTrace();
        }
    }
}
