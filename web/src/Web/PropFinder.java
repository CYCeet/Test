package Web;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/*
 * 这个类专门负责 从配置文件中提取出来  服务器的基本信息  
 */
public class PropFinder {
	//Properties本质上 是一个双列集合(键-值)
	private static Properties prop;
	
	//静态代码块
	static {
		try {
			//实例化对象
			prop = new Properties();
			
			//载入 配置文件【两种方式】
			InputStream is = 
					PropFinder.class.getResourceAsStream("config.properties");
			//	is = new FileInputStream("src/com/briup/config/config.properties");
			prop.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//私有构造器
	private PropFinder() {}
	
	//根据用户传入的字段，提取到配置文件里面的信 息
	public static String getValue(String key) {
		return prop.getProperty(key);
	}
}
