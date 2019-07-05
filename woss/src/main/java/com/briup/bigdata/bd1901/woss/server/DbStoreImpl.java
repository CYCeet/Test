package com.briup.bigdata.bd1901.woss.server;

import com.briup.bigdata.bd1901.woss.client.GatherImpl;
import com.briup.bigdata.bd1901.woss.utils.BackupImpl;
import com.briup.util.BIDR;
import com.briup.woss.server.DBStore;
import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class DbStoreImpl implements DBStore{
    @Override
    public void saveToDB(Collection<BIDR> collection) throws Exception{
        BackupImpl backup=new BackupImpl();
        // 从备份文件中获取上次【发生异常的上次】备份的List集合
        // 追加至参数Collection中
        String bidrsListStr="C:\\bidrsList.txt";
        if(new File(bidrsListStr).exists()){
            List<BIDR> bidrsListFromFile=(List<BIDR>)backup.load(bidrsListStr,false);
            collection.addAll(bidrsListFromFile);
        }

        String driver="com.mysql.jdbc.Driver";
        String url="jdbc:mysql://127.0.0.1:5724/woss?useSSL=true";
        String user="root";
        String password="root";
        Class.forName(driver);
        Connection conn=DriverManager.getConnection(url,user,password);
        // 事务进行手动控制
        conn.setAutoCommit(false);
        try{
            String sql="insert into woss.bidrs values(?,?,?,?,?,?)";
            PreparedStatement ps=conn.prepareStatement(sql);

            for(int i=0;i<collection.size();i++){
                List<BIDR> list=(List<BIDR>)collection;
                BIDR bidr=list.get(i);
                ps.setString(1,bidr.getAAA_login_name());
                ps.setString(2,bidr.getLogin_ip());
                ps.setDate(3,new Date(bidr.getLogin_date().getTime()));
                ps.setDate(4,new Date(bidr.getLogout_date().getTime()));
                ps.setInt(5,bidr.getTime_deration());
                ps.setString(6,bidr.getNAS_ip());

                ps.addBatch();

                if(i%1000==0){
                    ps.executeBatch();
                    System.out.println("已经存储了"+i+"条数据！");
                }
            }

            ps.executeBatch();

            conn.commit();

            ps.close();
            conn.close();
        }catch(Exception e){
            // 如果数据库服务器在插入数据的过程中出现异常
            // 则将需要插入的数据【List/Collection集合】进行备份
            backup.store("C:\\bidrsList.txt",collection,false);

            // 由于发生了异常，数据提交不完整，所以事务回滚
            conn.rollback();
        }
    }

    @Override
    public void init(Properties properties){

    }

    public static void main(String[] args) throws Exception{
        new DbStoreImpl().saveToDB(new GatherImpl().gather());
    }
}
