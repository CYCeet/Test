package com.briup.bigdata.bd1901.woss.utils;

import com.briup.util.BackUP;
import com.briup.util.Configuration;
import com.briup.util.Logger;
import com.briup.woss.ConfigurationAWare;
import com.briup.woss.WossModule;
import com.briup.woss.client.Client;
import com.briup.woss.client.Gather;
import com.briup.woss.server.DBStore;
import com.briup.woss.server.Server;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ConfigurationImpl implements Configuration{
    private Map<String,WossModule> map=new HashMap<>();

    public ConfigurationImpl() throws Exception{
        // 解析XML文档
        SAXReader reader=new SAXReader();
        URL url=this.getClass().getClassLoader().getResource("conf.xml");
        if(url==null) throw new Exception("配置文件路径未找到！");
        Document document=reader.read(url);
        // 获取woss根元素的所有子元素，该集合中存储的元素有gather标签，
        // client标签等等
        List elements=document.getRootElement().elements();
        // 创建Properties对象，用于存储每个通过反射所创建的对象的属性
        Properties properties=new Properties();
        for(Object o: elements){
            Element e=(Element)o;
            String aClass=e.attribute("class").getValue();
            WossModule wm=(WossModule)Class.forName(aClass).newInstance();
            map.put(e.getName(),wm);
            for(Object oo: e.elements()){
                Element ee=(Element)oo;
                String tagName=ee.getName();
                String strValue=ee.getStringValue();
                properties.put(tagName,strValue);
            }
        }

        map.forEach((k,v)->{
            v.init(properties);
            if(v instanceof ConfigurationAWare){
                ConfigurationAWare ca=(ConfigurationAWare)v;
                ca.setConfiguration(this);
            }
        });
    }

    @Override
    public Logger getLogger() throws Exception{
        return (Logger)this.map.get("logger");
    }

    @Override
    public BackUP getBackup() throws Exception{
        return (BackUP)this.map.get("backup");
    }

    @Override
    public Gather getGather() throws Exception{
        return (Gather)map.get("gather");
    }

    @Override
    public Client getClient() throws Exception{
        return (Client)map.get("client");
    }

    @Override
    public Server getServer() throws Exception{
        return (Server)this.map.get("server");
    }

    @Override
    public DBStore getDBStore() throws Exception{
        return (DBStore)this.map.get("dbstore");
    }
}
