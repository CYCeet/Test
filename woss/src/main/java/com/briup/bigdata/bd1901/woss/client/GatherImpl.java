package com.briup.bigdata.bd1901.woss.client;

import com.briup.bigdata.bd1901.woss.utils.ConfigurationImpl;
import com.briup.util.BIDR;
import com.briup.util.BackUP;
import com.briup.util.Configuration;
import com.briup.util.Logger;
import com.briup.woss.ConfigurationAWare;
import com.briup.woss.client.Client;
import com.briup.woss.client.Gather;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class GatherImpl implements Gather, ConfigurationAWare{
    private Logger logger;
    private static Client client;
    private String path;
    private BackUP backup;
    private static Gather gather;

    @Override
    public Collection<BIDR> gather() throws Exception{
        // BackupImpl backup=new BackupImpl();
        // 编写采集程序
        // 采集数据，逐行封装成BIDR对象，
        // 不完整的BIDR对象：只包含上线信息，没有下线信息
        // 完整的BIDR对象：既包含上线信息，又包含下线信息，还有在线时长
        // 将不完整的BIDR对象存放至Map集合，等待匹配
        // 如果采集到后面的数据和Map中的某个BIDR对象成功匹配，形成了
        // 完整的BIDR对象，将该对象存放至Collection集合，从Map集合中
        // 删除该对象所对应的不完整的对象。

        // InputStream is=
        //     this.getClass()
        //         .getClassLoader()
        //         .getResourceAsStream(path);

        // if(is==null) {
        //     logger.error(path+"文件不存在，程序结束！");
        //     return null;
        // }
        BufferedReader reader=
            new BufferedReader(
                new InputStreamReader(
                    new FileInputStream(
                        new File(path))));

        // 在采集之前，需要读取备份文件中的上次已经采集过的字节数并跳过
        String byteCountStr="C:\\byteCount.txt";
        Long byteCountFromFile=0L;
        if(new File(byteCountStr).exists()){
            byteCountFromFile=(Long)backup.load(byteCountStr,false);
            reader.skip(byteCountFromFile);
        }else{
            logger.warn("警告：文件"+byteCountStr+"不存在。");
        }

        // 逐行读取
        Map<String,BIDR> map=new HashMap<>();
        List<BIDR> list=new ArrayList<>();

        String bidrsMapStr="C:\\bidrsMap.txt";
        if(new File(bidrsMapStr).exists()){
            Map<String,BIDR> bidrsMapFromFile=
                (Map<String,BIDR>)backup.load(bidrsMapStr,false);
            map.putAll(bidrsMapFromFile);
        }

        // 用于记录本次采集的字节数
        long byteCount=0;
        String line;
        while((line=reader.readLine())!=null){
            // 把读取到的每一行的字节长度相加
            byteCount+=line.getBytes().length;
            // 并且把回车换行的2个字节也相加
            byteCount+=2;

            String[] strs=line.split("[|]");
            if("7".equals(strs[2])){
                String loginIP=strs[4];
                BIDR bidr=new BIDR();
                bidr.setAAA_login_name(strs[0]);
                bidr.setLogin_ip(loginIP);
                bidr.setNAS_ip(strs[1]);

                long loginDate=Long.parseLong(strs[3]);
                loginDate*=1000;
                Timestamp ts=new Timestamp(loginDate);
                bidr.setLogin_date(ts);

                // 不完整整的信息存放至Map集合
                map.put(loginIP,bidr);

            }else if("8".equals(strs[2])){
                String loginIP=strs[4];
                BIDR bidr=map.get(loginIP);
                if(bidr==null) continue;

                long logoutDate=Long.parseLong(strs[3]);
                logoutDate*=1000;
                Timestamp ts=new Timestamp(logoutDate);
                bidr.setLogout_date(ts);

                long timeDuration=logoutDate-bidr.getLogin_date().getTime();
                // 将在线时长转化成分钟
                timeDuration /= (1000*60);
                bidr.setTime_deration((int)timeDuration);

                // 把完整的BIDR对象存储至List集合
                list.add(bidr);
                // 把已经匹配完整的BIDR对象在Map集合中
                // 所对应的不完整的BIDR对象移除
                map.remove(loginIP);
            }
        }
        // 记录本次采集过的字节数
        backup.store("C:\\byteCount.txt",(byteCount+byteCountFromFile),false);

        // 备份包含不完整BIDR对象的Map集合
        backup.store("C:\\bidrsMap.txt",map,false);

        return list;
    }

    @Override
    public void init(Properties properties){
        // 系统配置和初始化
        this.path=properties.getProperty("rawdata-file");
    }

    public static void main(String[] args) throws Exception{
        new ConfigurationImpl();
        Collection<BIDR> bidrs=GatherImpl.gather.gather();
        client.send(bidrs);
    }

    @Override
    public void setConfiguration(Configuration configuration){
        try{
            client=configuration.getClient();
            this.logger=configuration.getLogger();
            this.backup=configuration.getBackup();
            gather=configuration.getGather();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
