package databases;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.regex.Pattern;

import agents.Agent;

import com.sun.rowset.CachedRowSetImpl;

import filters.ExtractionCriterion;
import filters.QRY_OP;

public abstract class Database {

	private String id = "";
	private String name;
	private DBScheme databaseType; 
	private String connectionInfo;
	private String databaseScheme;
	private Connection connection;
	
	public Connection getConnection() {
		return connection;
	}


	private String userName, password, url;
	
	
	public String getId() {
		return id;
	}

	
	public Database(String id, String name, DBScheme databaseType, String connectionInfo, String _databaseScheme) {
		this.id = id;
		this.name = name;
		this.databaseType = databaseType;
		this.connectionInfo = connectionInfo;
		this.databaseScheme = _databaseScheme;
		
		String[] tokens = connectionInfo.split("\\|\\|");
		if(tokens.length == 3){
			url = tokens[0];
			userName = tokens[1];
			password = tokens[2];
			setConnection(getConnection(url, userName, password, name));
		}

	}
	 
	
	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DBScheme getDatabaseType() {
		return databaseType;
	}

	public void setDatabaseType(DBScheme databaseType) {
		this.databaseType = databaseType;
	}

	public String getConnectionInfo() {
		return connectionInfo;
	}

	public void setConnectionInfo(String connectionInfo) {
		this.connectionInfo = connectionInfo;
	}

	
	/*/
	 * Each specialized database will be creating its own connection on the local machine
	 * input : _conString will be connection address for the target database
	 */
	public abstract Connection getConnection(String url, String userName, String password, String dbName);
	
	public void setConnection(Connection con){
		connection = con;
	}
	
	public boolean createDatabase(){
		boolean isCreated = false;
		try{
			if(connection != null){
				Statement stmt = connection.createStatement();
				String[] tokens = databaseScheme.split(";");
				for(String token: tokens)
					try{
						if(isCreated == false){
							Pattern pattern = Pattern.compile("create database");
							if (pattern.matcher(token.toLowerCase()).find()) {
								stmt.executeUpdate(token);
								connection.close();
								connection = getConnection(url, userName, password, name);
								stmt.close();
								stmt = connection.createStatement();
								isCreated = true;
								continue;
							}
						}
						stmt.executeUpdate(token);
					}catch(Exception e){}
			}
		}catch(Exception e){
		}
		return isCreated;
	}
}
