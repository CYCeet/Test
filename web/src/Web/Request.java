package Web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/*
 * 	������
 *	    �����ǻ�ȡ����������Ϣ  ��֣���װ��һ���� 		
 */
public class Request {
	//�Ϳͻ��� ͨ�ŵ� �׽��� 
	private Socket socket;
	//�������ݵ� ������
	private BufferedReader br;
	//������ ��ֳ�������Ϣ
	private String method;
	private String url;
	private String httpVer;
	
	Map<String, String> headMap;
	Map<String, String> bodyMap;
	
	//��������Ϣ
	//ʹ��˫�м��ϴ洢
	public Request(Socket socket) throws IOException {
		this.socket = socket;
		br = new BufferedReader(
				new InputStreamReader(socket.getInputStream()));
		headMap = new HashMap<String, String>();
		bodyMap = new HashMap<String, String>();
	}	
	
	public void getMessage() throws IOException {
		//1.��ȡ�����в����
		String line = br.readLine();
		String[] arr = line.split(" ");
		method = arr[0];
		url = arr[1];
		httpVer = arr[2];
		
		//2.��ȡ����ͷ �����
		String head = null;
		while(true) {
			
			head = br.readLine();
			if("".equals(head)) {
				break;
			}
			String[] hArr = head.split(":");
			headMap.put(hArr[0], hArr[1]);
		}
		
		//3.��ȡ������ �����
		if("GET".equals(method.toUpperCase())) {
			// get��ʽ�ύ�����ύ������ ���� url��
			// ��url���� ��� bodyֵ
			// url���ݴ���Ϊ: /login?name=zs&code=123
			String[] array = url.split("[?]");
			//1. url������body
			if(array.length == 1)
				return;
			
			//2. url����body  /a.txt?name=1&code=1
			//��� arr[1](name=1&code=1)
			parseBodyByStr(array[1]);
			url = array[0];
			return;
		}else if("POST".equals(method.toUpperCase())) {
			// post��ʽ�ύ�����������һ��������������
			if(br.ready() == false)
				return;
		
			char[] buf = new char[1024];
			int len = br.read(buf);
			String bodyStr = new String(buf,0,len);
			//body������Ϊ: name=zs&code=123
			parseBodyByStr(bodyStr);
		}
	}

	//����������ַ�������  ��map������
	private void parseBodyByStr(String bStr) {
		String[] barr = bStr.split("&");
		for (String s : barr) {
			String[] arr = s.split("=");
			if(arr.length != 2){
				bodyMap.put(arr[0], "");
			}else{
				bodyMap.put(arr[0], arr[1]);
			}
		}
		
	}
	
	

	//��ȡ��Դ�ļ�
	public String getUrl() {
		return url;
	}

	public String getMethod() {
		return method;
	}

	public String getHttpVer() {
		return httpVer;
	}

	//���������� ������Ϣ
	public void outRequestMessage() {
		System.out.println("*********************");
		System.out.println("������ϢΪ: ");
		System.out.println("method: " + method);
		System.out.println("url: " + url);
		System.out.println("httpVer: " + httpVer);
		System.out.println("headMap: " + headMap);
		System.out.println("bodyMap: " + bodyMap);
		System.out.println("*********************");
	}
}



