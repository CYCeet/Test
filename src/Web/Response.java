package Web;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.lang.reflect.*;

public class Response {
	private Socket socket;
	// 接收客户端所需要的数据的流对象
	private BufferedInputStream bis;
	private BufferedReader br;
	// 输出数据的流对象

	private PrintStream ps;
	private String url;

	Map<String, String> bodyMap;

	public Response(Socket socket, String url, Map<String, String> bodyMap) throws Exception {
		this.socket = socket;
		this.url = url;
		this.bodyMap = bodyMap;
		ps = new PrintStream(new PrintStream(socket.getOutputStream()));
	}

	public static boolean isContain(String s1, String s2) {
		return s1.contains(s2);
	}

	public void sendMessage() throws Exception {
		String filename = PropFinder.getValue("path") + url;
		File file = new File(filename);
		System.out.println("*********" + filename);
		// LoginPan();
		if ("/".equals(url)) {
			welcomeUrl();
		} else {
			if (file.exists()) {
				String[] array = filename.split("\\.");
				String str = array[1];
				System.out.println("*** str:" + str);
				if ("/login.html".equals(url)) {
					loginUrl();
				} else {
					if ("txt".equals(str)) {
						fileUrl();
					} else {
						tupianUrl();
					}
				}
			} else {
				// 判断是否为动态资源
				if ("/login".equals(url)) {
					 //select();
					// 如果是 则通过反射实例化一个对象
					String className = PropFinder.getValue("islogin");
					System.out.println("className: " + className);
					Class<?> clazz = Class.forName(className);
					// 对象调用方法，实现功能
					Constructor<?> c1 = clazz.getDeclaredConstructor();
					c1.setAccessible(true);
					Object o = c1.newInstance();
					System.out.println("o: " + o);
					
					//Object o = clazz.newInstance();
					Method method = clazz.getDeclaredMethod("select", String.class,
							String.class);
					method.setAccessible(true);
					System.out.println("method: " + method.getName());
					
					Object result = method.invoke(o, bodyMap.get("name"),
							bodyMap.get("id"));
					
					if ((boolean) result) {
						successUrl();
						System.out.println("登录信息正确，返回成功界面");
					} else {
						failUrl();
						System.out.println("登录信息错误，返回错误界面");
					}
				}
				// 如果不是动态资源
				else
					errorUrl();
			}
		}
		return;
	}

	public void select() throws Exception {
		if ("zs".equals(bodyMap.get("name")) && "123".equals(bodyMap.get("id"))) {
			successUrl();
		} else {
			failUrl();
		}
		return;
	}

	private void failUrl() throws Exception {
		ps.println("HTTP/1.1 200 OK");
		ps.println("Content-Type: text/html; charset=gb2312");
		ps.println();
		bis = new BufferedInputStream(new FileInputStream(PropFinder.getValue("fail")));
		byte[] array = new byte[1024 * 8];
		int len;
		while ((len = bis.read(array)) != -1) {
			ps.write(array, 0, len);
		}
		return;
	}

	private void fileUrl() throws Exception {
		ps.println("HTTP/1.1 200 OK");
		ps.println();
		br = new BufferedReader(new InputStreamReader(new FileInputStream(PropFinder.getValue("path") + url)));
		String s;
		while ((s = br.readLine()) != null) {
			ps.println(s);
		}
	}

	private void loginUrl() throws Exception {
		ps.println("HTTP/1.1 200 OK");
		ps.println("Content-Type: text/html; charset=gb2312");
		ps.println();
		bis = new BufferedInputStream(new FileInputStream(PropFinder.getValue("login")));
		byte[] array = new byte[1024 * 8];
		int len;
		while ((len = bis.read(array)) != -1) {
			ps.write(array, 0, len);
		}
		ps.close();

	}

	private void tupianUrl() throws Exception {
		ps.println("HTTP/1.1 200 OK");
		ps.println("Content-Type: image/png; charset=UTF-8");
		ps.println();
		bis = new BufferedInputStream(new FileInputStream(PropFinder.getValue("path") + url));
		byte[] array = new byte[1024 * 8];
		int len;
		while ((len = bis.read(array)) != -1) {
			ps.write(array, 0, len);
		}
	}

	private void successUrl() throws Exception {
		ps.println("HTTP/1.1 200 OK");
		ps.println("Content-Type: text/html; charset=gb2312");
		ps.println();
		bis = new BufferedInputStream(new FileInputStream(PropFinder.getValue("success")));
		byte[] array = new byte[1024 * 8];
		int len;
		while ((len = bis.read(array)) != -1) {
			ps.write(array, 0, len);
		}
		ps.flush();
	}

	private void welcomeUrl() throws Exception {
		ps.println("HTTP/1.1 200 OK");
		ps.println("Content-Type: text/html; charset=gb2312");
		ps.println();
		bis = new BufferedInputStream(new FileInputStream(PropFinder.getValue("welcomeUrl")));
		byte[] array = new byte[1024 * 8];
		int len;
		while ((len = bis.read(array)) != -1) {
			ps.write(array, 0, len);
		}
		return;
	}

	public void errorUrl() throws Exception {
		ps.println("HTTP/1.1 404 Notfound");
		ps.println();
		bis = new BufferedInputStream(new FileInputStream(PropFinder.getValue("errorUrl")));
		byte[] array = new byte[1024 * 8];
		int len;
		while ((len = bis.read(array)) != -1) {
			ps.write(array, 0, len);
		}
	}

}
