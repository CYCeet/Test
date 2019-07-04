package Web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/*
 * 	请求类
 *	    把我们获取到的请求信息  拆分，封装成一个类 		
 */
public class Request {
	//和客户端 通信的 套接字 
	private Socket socket;
	//接收数据的 流对象
	private BufferedReader br;
	//请求行 拆分出来的信息
	private String method;
	private String url;
	private String httpVer;
	
	Map<String, String> headMap;
	Map<String, String> bodyMap;
	
	//请求体信息
	//使用双列集合存储
	public Request(Socket socket) throws IOException {
		this.socket = socket;
		br = new BufferedReader(
				new InputStreamReader(socket.getInputStream()));
		headMap = new HashMap<String, String>();
		bodyMap = new HashMap<String, String>();
	}	
	
	public void getMessage() throws IOException {
		//1.读取请求行并拆分
		String line = br.readLine();
		String[] arr = line.split(" ");
		method = arr[0];
		url = arr[1];
		httpVer = arr[2];
		
		//2.读取请求头 并拆分
		String head = null;
		while(true) {
			
			head = br.readLine();
			if("".equals(head)) {
				break;
			}
			String[] hArr = head.split(":");
			headMap.put(hArr[0], hArr[1]);
		}
		
		//3.读取请求体 并拆分
		if("GET".equals(method.toUpperCase())) {
			// get方式提交，表单提交的数据 存在 url中
			// 从url里面 拆分 body值
			// url数据大致为: /login?name=zs&code=123
			String[] array = url.split("[?]");
			//1. url不存在body
			if(array.length == 1)
				return;
			
			//2. url存在body  /a.txt?name=1&code=1
			//拆分 arr[1](name=1&code=1)
			parseBodyByStr(array[1]);
			url = array[0];
			return;
		}else if("POST".equals(method.toUpperCase())) {
			// post方式提交，如果有数据一定存在请求体中
			if(br.ready() == false)
				return;
		
			char[] buf = new char[1024];
			int len = br.read(buf);
			String bodyStr = new String(buf,0,len);
			//body中数据为: name=zs&code=123
			parseBodyByStr(bodyStr);
		}
	}

	//拆分请求体字符串数据  到map集合中
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
	
	

	//获取资源文件
	public String getUrl() {
		return url;
	}

	public String getMethod() {
		return method;
	}

	public String getHttpVer() {
		return httpVer;
	}

	//输出解析后的 请求信息
	public void outRequestMessage() {
		System.out.println("*********************");
		System.out.println("请求信息为: ");
		System.out.println("method: " + method);
		System.out.println("url: " + url);
		System.out.println("httpVer: " + httpVer);
		System.out.println("headMap: " + headMap);
		System.out.println("bodyMap: " + bodyMap);
		System.out.println("*********************");
	}
}



