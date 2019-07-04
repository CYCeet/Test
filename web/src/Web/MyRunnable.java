package Web;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;

import Web.Request;

//�Զ���Runnable�࣬��д�̴߳�����
public class MyRunnable implements Runnable {
	private Socket socket;
	
	public MyRunnable(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		
		try {
			//1.���� �ͻ��˷��͹�����  ����
			Request req = new Request(socket);
			req.getMessage();
			
			//�˴���Ϊ���Դ���
			req.outRequestMessage();
			
			//��ȡ �������Դ�ļ�
			String url = req.getUrl();
			Map<String, String> bodyMap = req.bodyMap;
			if("/favicon.ico".equals(url)) {
				return;
			}

			//2.����Դ������Ӧ 
			Response res = new Response(socket, url, bodyMap);
			
			//��������
			res.sendMessage();
		} catch (Exception e) {
			
			e.printStackTrace();
		} finally {
			try {
				if(socket != null)
					socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
