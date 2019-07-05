package com.briup.bigdata.bd1901.woss.test;

import org.apache.log4j.Logger;

public class Log4JTest{
    public static void main(String[] args){
        // 获取log4j中的Logger的对象
        // Logger rootLogger=Logger.getRootLogger();
        Logger logger=Logger.getLogger("abc");
        logger.debug("这是debug日志");
        logger.info("这是info日志");
        logger.warn("这是warn日志");
        logger.error("这是error日志");
        logger.fatal("这是fatal日志");
    }
}
