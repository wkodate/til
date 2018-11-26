package com.wkodate.junit;

/**
 * Created by wkodate on 2018/11/26.
 */
public class Frameworks {
    public static boolean isSupport(ApplicationServer appServer, Database db) {
        switch (appServer) {
            case GlassFish:
                return true;
            case Tomcat:
                return db == Database.MySQL;
            case JBoss:
                return db == Database.DB2 || db == Database.PostgreSQL;
            default:
                return false;
        }
    }
}
