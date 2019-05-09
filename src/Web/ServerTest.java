package Web;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import Web.PropFinder;

/*
 * 服务器主程序
 */
public class ServerTest {
	public static void main(String[] args) {
		try {
			//实例化 服务器套接字对象
			String port = PropFinder.getValue("port");
			ServerSocket server = new ServerSocket(Integer.parseInt(port));
			System.out.println("服务器已经开启,端口为: " + port);
			
			while(true) {
				//接受客户端连接，然后启动服务
				Socket socket = server.accept();
				
				Thread th = new Thread(new MyRunnable(socket));
				th.start();
			}
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
}





