package utilities;

import java.io.*;

/*
* �洢"+this.userName+"��¼��Ϣ
*/
public class LoginInfo implements Serializable {
	private String userName;
	private String password;
	
	public LoginInfo(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}
	
	public String getUserName() {
		return this.userName;
	}
	public String getUserPassword() {
		return this.password;
	}
	
	public void saveLoginInfo() {
		//�����¼��Ϣ
		File dir = new File("LoginInfo");
		if (!dir.exists()) {
			dir.mkdir();
		}
		File file =new File("LoginInfo/"+this.userName+".dat");
		FileOutputStream out;
		try {
		    out = new FileOutputStream(file);
		    ObjectOutputStream objOut = new ObjectOutputStream(out);
		    objOut.writeObject(this);
		    objOut.flush();
		    objOut.close();
		    System.out.println("�ɹ�����"+this.userName+"��¼��Ϣ");
		} catch (IOException e) {
		    System.out.println("����"+this.userName+"��¼��Ϣʧ��");
		    e.printStackTrace();
		}
	}
	
	public static LoginInfo getLoginInfo(String userName) {
		//��ȡ"+this.userName+"��Ϣ
		File file = new File("LoginInfo/"+userName+".dat");
		FileInputStream in;
		try {
			in = new FileInputStream(file);
			ObjectInputStream objIn = new ObjectInputStream(in);
			LoginInfo inf = null;
			inf = (LoginInfo)objIn.readObject();
            objIn.close();
            System.out.println("�ɹ���ȡ"+userName+"��¼��Ϣ");
            return inf;
		} catch (Exception e) {
		    System.out.println("��ȡ"+userName+"��¼��Ϣʧ��");
		    e.printStackTrace();
		    return null;
		} 
	}
}
