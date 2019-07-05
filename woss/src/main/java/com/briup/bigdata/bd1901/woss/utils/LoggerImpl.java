package com.briup.bigdata.bd1901.woss.utils;

import com.briup.util.Logger;
import java.util.Properties;

public class LoggerImpl implements Logger{
    private org.apache.log4j.Logger logger=
        org.apache.log4j.Logger.getLogger("abc");

    @Override
    public void debug(String s){
        this.logger.debug(s);
    }

    @Override
    public void info(String s){
        this.logger.info(s);
    }

    @Override
    public void warn(String s){
        this.logger.warn(s);
    }

    @Override
    public void error(String s){
        this.logger.error(s);
    }

    @Override
    public void fatal(String s){
        this.logger.fatal(s);
    }

    @Override
    public void init(Properties properties){

    }
}
