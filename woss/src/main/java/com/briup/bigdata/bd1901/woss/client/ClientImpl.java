package com.briup.bigdata.bd1901.woss.client;

import com.briup.util.BIDR;
import com.briup.util.Configuration;
import com.briup.util.Logger;
import com.briup.woss.ConfigurationAWare;
import com.briup.woss.client.Client;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.Properties;

public class ClientImpl implements Client, ConfigurationAWare{
    private Logger logger;

    @Override
    public void send(Collection<BIDR> collection) throws Exception{
        String ip="127.0.0.1";
        int port=56789;
        Socket socket=new Socket(ip,port);
        logger.info("连接服务器，服务器IP："+ip+"，端口是："+port);
        OutputStream os=socket.getOutputStream();
        logger.info("通过socket获取到了输出流对象！");
        ObjectOutputStream oos=new ObjectOutputStream(os);
        oos.writeObject(collection);
        oos.flush();
        oos.close();
    }

    @Override
    public void init(Properties properties){

    }

    @Override
    public void setConfiguration(Configuration configuration){
        try{
            this.logger=configuration.getLogger();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
