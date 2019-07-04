package Web;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/*
 * �����ר�Ÿ��� �������ļ�����ȡ����  �������Ļ�����Ϣ  
 */
public class PropFinder {
	//Properties������ ��һ��˫�м���(��-ֵ)
	private static Properties prop;
	
	//��̬�����
	static {
		try {
			//ʵ��������
			prop = new Properties();
			
			//���� �����ļ������ַ�ʽ��
			InputStream is = 
					PropFinder.class.getResourceAsStream("config.properties");
			//	is = new FileInputStream("src/com/briup/config/config.properties");
			prop.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//˽�й�����
	private PropFinder() {}
	
	//�����û�������ֶΣ���ȡ�������ļ�������� Ϣ
	public static String getValue(String key) {
		return prop.getProperty(key);
	}
}
