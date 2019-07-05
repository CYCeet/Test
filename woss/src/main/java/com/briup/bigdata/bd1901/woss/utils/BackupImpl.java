package com.briup.bigdata.bd1901.woss.utils;

import com.briup.util.BackUP;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;

public class BackupImpl implements BackUP{
    /**
     * 用于做备份
     * @param s 要做备份的文件路径
     * @param o 要做备份的对象
     * @param b 是否追加至之前的备份文件中
     * @throws Exception .
     */
    @Override
    public void store(String s,Object o,boolean b) throws Exception{
        File file=new File(s);
        ObjectOutputStream oos=
            new ObjectOutputStream(new FileOutputStream(file,b));
        oos.writeObject(o);
        oos.flush();
        oos.close();
    }

    /**
     * 从备份文件中读取对象
     * @param s 备份文件的路径
     * @param b 在读取完成之后是否删除原备份文件
     * @return 读取到的对象
     * @throws Exception .
     */
    @Override
    public Object load(String s,boolean b) throws Exception{
        File file=new File(s);
        ObjectInputStream ois=
            new ObjectInputStream(new FileInputStream(file));
        Object o=ois.readObject();
        // 如果参数b为true，则删除文件
        if(b) file.delete();
        return o;
    }

    @Override
    public void init(Properties properties){

    }
}
