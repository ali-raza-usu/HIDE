package databases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

import filters.Criteria;
import filters.ExtractionCriterion;
import filters.QRY_OP;

import agents.SourceAgent;

public class PostgreSQLDataBase extends Database {
	
	public PostgreSQLDataBase(String _dbName,  String _conStr, String _databaseScheme) {
		super(null, _dbName, DBScheme.postgreSQL, _conStr, _databaseScheme);
	}

	@Override
	public Connection getConnection(String url, String userName, String password, String dbName) {
		Connection con = null;
	  try{
            Class.forName("org.postgresql.Driver");
            String conStr = url+dbName.toLowerCase();	             
            return DriverManager.getConnection(conStr, userName,password);
      }catch(Exception e){ 
    	  e.getStackTrace();
      }
		setConnection(con);
		return con;
	}		
}
