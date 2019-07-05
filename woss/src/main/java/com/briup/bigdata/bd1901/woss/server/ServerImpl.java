package com.briup.bigdata.bd1901.woss.server;

import com.briup.bigdata.bd1901.woss.utils.ConfigurationImpl;
import com.briup.util.BIDR;
import com.briup.util.Configuration;
import com.briup.util.Logger;
import com.briup.woss.ConfigurationAWare;
import com.briup.woss.server.DBStore;
import com.briup.woss.server.Server;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class ServerImpl implements Server, ConfigurationAWare{
    private DBStore dbStore;
    private Logger logger;
    private static Server server;
    private int port;


    public static void main(String[] args) throws Exception{
        new ConfigurationImpl();
        server.revicer();
    }

    @Override
    public Collection<BIDR> revicer() throws Exception{
        ServerSocket ss=new ServerSocket(this.port);
        logger.info("WOSS服务器启动了，等待客户端连接！");
        Socket socket=ss.accept();
        InputStream is=socket.getInputStream();
        ObjectInputStream ois=new ObjectInputStream(is);
        Object o=ois.readObject();
        List<BIDR> list=(List<BIDR>)o;
        System.out.println(list.size());
        // 此处应该调用入库模块中的方法进行持久化操作
        dbStore.saveToDB(list);
        return list;
    }

    @Override
    public void shutdown(){

    }

    @Override
    public void init(Properties properties){
        this.port=Integer.parseInt(properties.getProperty("port"));
    }

    @Override
    public void setConfiguration(Configuration configuration){
        try{
            server=configuration.getServer();
            this.dbStore=configuration.getDBStore();
            this.logger=configuration.getLogger();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
