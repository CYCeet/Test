package Web;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import Web.PropFinder;

/*
 * ������������
 */
public class ServerTest {
	public static void main(String[] args) {
		try {
			//ʵ���� �������׽��ֶ���
			String port = PropFinder.getValue("port");
			ServerSocket server = new ServerSocket(Integer.parseInt(port));
			System.out.println("�������Ѿ�����,�˿�Ϊ: " + port);
			
			while(true) {
				//���ܿͻ������ӣ�Ȼ����������
				Socket socket = server.accept();
				
				Thread th = new Thread(new MyRunnable(socket));
				th.start();
			}
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
}





