package Web;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;

import Web.Request;

//自定义Runnable类，重写线程处理方法
public class MyRunnable implements Runnable {
	private Socket socket;
	
	public MyRunnable(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		
		try {
			//1.接收 客户端发送过来的  请求
			Request req = new Request(socket);
			req.getMessage();
			
			//此代码为测试代码
			req.outRequestMessage();
			
			//获取 请求的资源文件
			String url = req.getUrl();
			Map<String, String> bodyMap = req.bodyMap;
			if("/favicon.ico".equals(url)) {
				return;
			}

			//2.对资源做出响应 
			Response res = new Response(socket, url, bodyMap);
			
			//返回数据
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
