package agents;
import java.sql.ResultSet;
import java.sql.Statement;

import insertBuilders.InsertBuilder;
import insertBuilders.MPIInsertBuilder;
import databases.Database;
import databases.PostgreSQLDataBase;


public class TargetAgent extends Agent{
	/*
	 * Making assumptions
	 * Input: Takes the input with value database name that you want to create on the target machine
	 * Returns: An instance of a Specialized database which actually exists on the target machine
	 * Every database would know what type of database it is. means SQL server, Postgre etc.
	 * Example: OHV is an example.
	 * */
	
	public TargetAgent(){}
	
	public TargetAgent(Database _database){
		this.database = _database;
	}
	private InsertBuilder insertBuilder = null;
	private Database database = null;
	//replace multiple constructors with intention revealing method names
	public Database createPostgreDatabase(String _targetName,  String _conStr, String _databaseScheme){
		return new PostgreSQLDataBase(_targetName,  _conStr, _databaseScheme);
	}
	public InsertBuilder getInsertBuilder() {
		return insertBuilder;
	}
	public void setInsertBuilder(InsertBuilder insertBuilder) {
		this.insertBuilder = insertBuilder;
	}
	public Database getDatabase() {
		return database;
	}
	public void setDatabase(Database database) {
		this.database = database;
	}
	
	public void insertIntoDatabase(){
	 try {
			Statement stmt = database.getConnection().createStatement();
			String insertQuery =  getInsertBuilder().buildInsertQuery();
			stmt.executeUpdate(insertQuery);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
