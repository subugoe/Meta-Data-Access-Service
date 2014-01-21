package de.unigoettingen.sub.mets.mongomapper.helper;

/**
 * Created by jpanzer.
 * mets_mongo_mapper.
 * <p/>
 * 12/2013
 */
public class MongoDataSource {


    private String server;
    private String port;
    private String dbName;
    private String userName;
    private String password;

    public MongoDataSource(String server, String port, String dbName, String userName, String password) {
        this.server = server;
        this.port = port;
        this.dbName = dbName;
        this.userName = userName;
        this.password = password;
    }


    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
