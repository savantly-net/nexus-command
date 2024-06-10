package net.savantly.nexus.flow.javet;

import com.caoccao.javet.interfaces.IJavetLogger;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class JavetLogger implements IJavetLogger{

    @Override
    public void debug(String arg0) {
        log.debug(arg0);
    }

    @Override
    public void error(String arg0) {
        log.error(arg0);
    }

    @Override
    public void error(String arg0, Throwable arg1) {
        log.error(arg0, arg1);
    }

    @Override
    public void info(String arg0) {
        log.info(arg0);
    }

    @Override
    public void warn(String arg0) {
        log.warn(arg0);
    }
    
}
