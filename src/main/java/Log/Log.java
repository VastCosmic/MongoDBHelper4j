package Log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log {
    private static final Logger traceLogger = LogManager.getLogger("trace");
    private static final Logger debugLogger = LogManager.getLogger("debug");
    private static final Logger infoLogger = LogManager.getLogger("info");
    private static final Logger warnLogger = LogManager.getLogger("warn");
    private static final Logger errorLogger = LogManager.getLogger("error");
    private static final Logger fatalLogger = LogManager.getLogger("fatal");

    // Info 写Info日志方法
    public static void info (String msg) { infoLogger.info(msg); }

    // Error 写Error方法
    public static void error (String msg){
        errorLogger.error(msg);
    }

    // Debug 写Debug方法
    public static void debug (String msg){
        debugLogger.debug(msg);
    }

    // Warn 写Warn方法
    public static void warn (String msg){
        warnLogger.warn(msg);
    }

    // Trace 写Trace方法
    public static void trace (String msg){traceLogger.trace(msg);}

    // Fatal 写Fatal方法
    public static void fatal (String msg){fatalLogger.fatal(msg);}
}