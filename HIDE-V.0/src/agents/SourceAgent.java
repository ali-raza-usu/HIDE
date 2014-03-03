package agents;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import selectbuilders.SQLBuilder;

import databases.Database;
import extraction.Extraction;
import extraction.ResultSetIterator;
import mangler.strategies.Strategy;
import filters.Criteria;
import filters.ExtractionCriterion;


public class SourceAgent extends Agent{
	
	private Database database = null;
	private SQLBuilder sqlBuilder = null;
	
	public SQLBuilder getSqlBuilder() {
		return sqlBuilder;
	}

	public void setSqlBuilder(SQLBuilder sqlBuilder) {
		this.sqlBuilder = sqlBuilder;
	}

	public SourceAgent(Database _db){
		this.database = _db;
	}
	
	public Database getDatabase() {
		return database;
	}

	public void setDatabase(Database database) {
		this.database = database;
	}

	public void setExtractionCriteria(ExtractionCriterion _extCriteria){
		super.setExtractionCriteia(_extCriteria);
	}
	
	public void initializeRecordExtraction(){
	}
	
	
	public void mangle(Strategy _strategy, Extraction _extraction){
		_extraction.mangle(_strategy);
	}
	

	public ExtractionCriterion getExtractionCriteia() {
		return super.getExtractionCriteia();
	}


	public ResultSetIterator getCursor(String tableName){
		ResultSetIterator rs_iterator  = new ResultSetIterator();
		rs_iterator.setTable(tableName);
		try{
			String query =sqlBuilder.buildQuery();
			Statement stmt = database.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE); 
			rs_iterator.getResultSet().populate(stmt.executeQuery(query),1);
		}catch(Exception e){
			e.printStackTrace();
		}
		return rs_iterator;
	}
	

}
