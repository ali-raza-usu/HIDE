package testing;
import insertBuilders.InsertBuilder;
import insertBuilders.MPIInsertBuilder;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;

import javax.sql.rowset.*;

import mangler.DomainMangler;

import org.junit.Assert;
import org.junit.Test;

import selectbuilders.OHVPhase1SQLBuilder;

import com.sun.rowset.CachedRowSetImpl;
import agents.SourceAgent;
import agents.TargetAgent;
import databases.Database;
import databases.PostgreSQLDataBase;
import engine.ExtractionEngine;
import engine.OHVPhase1ExtractionEngine;
import extraction.Extraction;
import extraction.ResultSetIterator;
import filters.ExtractionCriterion.MappingInfo;
import filters.*;
import com.sun.rowset.*;

public class TestHIDE{
	@Test
	//It checks that criteria has been created successfully
	public void TestCriteriaCreation01(){
		String _sqlStatement = "select  id ,  address , name from person where CITY = Logan " +
				"and ZIPCODE BETWEEN 84321 AND 84341 or BIRTHDATE = 01/01/2013";
		ExtractionCriterion _criterian = new ExtractionCriterion();
		String _sourceName = "hl_core_v2_test";
		String _conStr = "jdbc:postgresql://localhost:5432/||postgres||abcdefgh";
		String _databaseScheme = null;
		
		Database _postgreDatabase = new PostgreSQLDataBase(_sourceName, _conStr, _databaseScheme);
		SourceAgent _sourceAgent = new SourceAgent(_postgreDatabase);
		//set the table and columns for query
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", "bigint");
		map.put("name", "character varying(50)");
		map.put("address", "character varying(50)");
		//create a criteria c_1
		Criteria c1= new Criteria();
		MatchFilter _cityFilter = new MatchFilter(FilterType.CITY, "Logan");
		BetweenFilter _zipCodeFilter = new BetweenFilter(FilterType.ZIPCODE, "84321", "84341");
		c1.getFilters().add(_cityFilter);
		c1.getFilters().add(_zipCodeFilter);
		//create a criteria c_2
		Criteria c2= new Criteria();
		MatchFilter _bdateFilter = new MatchFilter(FilterType.BIRTHDATE, "01/01/2013");
		c2.getFilters().add(_bdateFilter);
		//add the criteria to the ExtractionCriterias
		_criterian.getCriteriaList().add(c1);
		_criterian.getCriteriaList().add(c2);
		_sourceAgent.setSqlBuilder(new OHVPhase1SQLBuilder(_criterian));
		String query = _sourceAgent.getSqlBuilder().buildQuery();
		System.out.println("Q 1" + _sqlStatement);
		System.out.println("Q 2"+query);
		Assert.assertEquals(_sqlStatement.trim(), query.trim());
	}
	
	/*
	 * select first_name, last_name, birth_date, birth_city, birth_facility_Code from person where birth_city = 'PROVO' and birth_facility_Code between '138' and '142' or birth_date = '2009/01/10'
	 */
	@Test
	//SourceAgent executes the query and get the NotNull resultset
	public void TestSourceExecution02(){
		//1. creating an instance of a database by the PostgreSourceAgent
		String _sourceName = "hl_core_v2_test";
		String _conStr = "jdbc:postgresql://localhost:5432/||postgres||abcdefgh";
		String _databaseScheme = null;
	
		Database _postgreDatabase = new PostgreSQLDataBase(_sourceName, _conStr, _databaseScheme);
		SourceAgent _pgAgent = new SourceAgent(_postgreDatabase);
		//2. creating an extraction criteria
		ExtractionCriterion _criterian = new ExtractionCriterion();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("first_name", "character varying(50)");
		map.put("last_name", "character varying(50)");
		map.put("birth_date", "character(10)");
		map.put("birth_city", "character varying(100)");
		map.put("birth_facility_Code", "character varying(3)");
				//create a criteria_1
		Criteria c1= new Criteria();
		MatchFilter _cityFilter = new MatchFilter(FilterType.birth_city, "'PROVO'");
		BetweenFilter _zipCodeFilter = new BetweenFilter(FilterType.birth_facility_Code, "'138'", "'142'");
		c1.getFilters().add(_cityFilter);
		c1.getFilters().add(_zipCodeFilter);
		//create a criteria_2
		Criteria c2= new Criteria();
		MatchFilter _bdateFilter = new MatchFilter(FilterType.birth_date, "'2009/01/10'");
		c2.getFilters().add(_bdateFilter);
		//add the criterias to the ExtractionCriterias
		_criterian.getCriteriaList().add(c1);
		_criterian.getCriteriaList().add(c2);
		_pgAgent.setExtractionCriteria(_criterian);
		_pgAgent.setSqlBuilder(new OHVPhase1SQLBuilder(_criterian));
		_pgAgent.getSqlBuilder().buildQuery();
		//3. creating a ResultSet through sourceAgent
		ResultSetIterator _resultSet = _pgAgent.getCursor(null);
		Assert.assertNotNull(_resultSet);
	}
	
	@Test
	//It checks that target database has been created successfully
	public void TestcreatePostGreDatabase03(){
		//1. execute createPostgreDatabase
		String _targetName = "HL_CORE_V2_TEST";
		String _conStr = "jdbc:postgresql://localhost:5432/||postgres||abcdefgh";
		String _databaseScheme = getHLCOREDatabaseStr();
		TargetAgent _pgAgent = new TargetAgent();
		Database _postgreDatabase = _pgAgent.createPostgreDatabase(_targetName, _conStr, _databaseScheme);
		Assert.assertTrue(_postgreDatabase.createDatabase());
	}
	
	@Test
	//It checks that database connection is working properly
	public void testDatabaseConnection04(){
		String _targetName = "HL_CORE_V2_TEST";
		String _conStr = "jdbc:postgresql://172.23.164.101:5432/||PP_TEST||usuhealth";
		String _databaseScheme = getHLCOREDatabaseStr();
		PostgreSQLDataBase pgDatabase = new PostgreSQLDataBase(_targetName, _conStr, _databaseScheme);
		Assert.assertNotNull(pgDatabase);
		
	}
	
	@Test
	public void testGetNextRecord05(){
		String _sourceName = "hl_core_v2_test";
		String _conStr = "jdbc:postgresql://localhost:5432/||postgres||abcdefgh";
		String _databaseScheme = null;
		Database _postgreDatabase = new PostgreSQLDataBase(_sourceName, _conStr, _databaseScheme);
		SourceAgent _pgAgent = new SourceAgent(_postgreDatabase);
		//creating an extraction criteria
		ExtractionCriterion _criterian = new ExtractionCriterion();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("first_name", "character varying(50)");
		map.put("last_name", "character varying(50)");
		map.put("birth_date", "character(10)");
		map.put("birth_city", "character varying(100)");
		map.put("birth_facility_Code", "character varying(3)");
		//_criterian.addPIIMappings("first_name", "names_domain");
		//_criterian.addPIIMappings("birth_date", "dob_domains");
		//create a criteria - c1
		Criteria c1= new Criteria();
		MatchFilter _cityFilter = new MatchFilter(FilterType.birth_city, "'PROVO'");
		BetweenFilter _zipCodeFilter = new BetweenFilter(FilterType.birth_facility_Code, "'138'", "'142'");
		c1.getFilters().add(_cityFilter);
		c1.getFilters().add(_zipCodeFilter);
		//create a criteria - c2
		Criteria c2= new Criteria();
		MatchFilter _bdateFilter = new MatchFilter(FilterType.birth_date, "'2009/01/10'");
		c2.getFilters().add(_bdateFilter);
		//add the criteria to ExtractionCriteria
		_criterian.getCriteriaList().add(c1);
		_criterian.getCriteriaList().add(c2);
		_pgAgent.setExtractionCriteria(_criterian);
		_pgAgent.setSqlBuilder(new OHVPhase1SQLBuilder(_criterian));
		_pgAgent.getSqlBuilder().buildQuery();
		//creating a ResultSet through sourceAgent
		ResultSetIterator _rsItr = _pgAgent.getCursor(null);
		Extraction _extraction = _rsItr.getNext();
		Assert.assertNotNull(_extraction);
		ResultSet record = _extraction.getRecordData();
		try{
			for(int i = 0; i<1; i++){
				Assert.assertNotNull(record.getString("first_name"));
				Assert.assertNotNull(record.getString("last_name"));
				Assert.assertNotNull(record.getString("birth_date"));
				Assert.assertNotNull(record.getString("birth_city"));
				Assert.assertNotNull(record.getString("birth_facility_Code"));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void TestMangleAnExtraction06(){
		//1. creating an instance of a database by the PostgreSourceAgent
		String _sourceName = "hl_core_v2_test";
		String _conStr = "jdbc:postgresql://localhost:5432/||postgres||abcdefgh";
		String _databaseScheme = null;
		Database _postgreDatabase = new PostgreSQLDataBase(_sourceName, _conStr, _databaseScheme);
		SourceAgent _pgAgent = new SourceAgent(_postgreDatabase);	
		//2. creating an extraction criteria
		ExtractionCriterion _criterian = new ExtractionCriterion();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("first_name", "character varying(50)");
		map.put("last_name", "character varying(50)");
		map.put("birth_date", "character(10)");
		map.put("birth_city", "character varying(100)");
		map.put("birth_facility_Code", "character varying(3)");
		_criterian.setMappingInfo("first_name", "first_names_domains", "oldname", "newname");
		_criterian.setMappingInfo("birth_date", "dob_domains", "oldname", "newname");
		//create a criteria_1
		Criteria c1= new Criteria();
		MatchFilter _cityFilter = new MatchFilter(FilterType.birth_city, "'PROVO'");
		BetweenFilter _zipCodeFilter = new BetweenFilter(FilterType.birth_facility_Code, "'138'", "'142'");
		c1.getFilters().add(_cityFilter);
		c1.getFilters().add(_zipCodeFilter);
		//create a criteria_2
		Criteria c2= new Criteria();
		MatchFilter _bdateFilter = new MatchFilter(FilterType.birth_date, "'2009/01/10'");
		c2.getFilters().add(_bdateFilter);
		//add the criterias to the ExtractionCriterias
		_criterian.getCriteriaList().add(c1);
		_criterian.getCriteriaList().add(c2);
				
				
		ResultSetIterator rs_iterator  = new ResultSetIterator();
		rs_iterator.setTable(null);
		
		try{
			_pgAgent.setSqlBuilder(new OHVPhase1SQLBuilder(_criterian));
			_pgAgent.getSqlBuilder().buildQuery();
			String query =_pgAgent.getSqlBuilder().buildQuery();
			//Connection conn =  DriverManager.getConnection("jdbc:postgresql://localhost:5432/hl_core_v2_test","postgres","abcdefgh");
			Statement stmt = _postgreDatabase.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE); 
			System.out.println(" QUERY : "+ query);
			ResultSet rs = stmt.executeQuery(query);
			rs_iterator.getResultSet().populate(rs);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		String _manglerSourceName = "hl_core_v2_test";
		String _manglerConStr = "jdbc:postgresql://localhost:5432/||postgres||abcdefgh";
		String _manglerDatabaseScheme = null;
		Database _manglerDatabase = new PostgreSQLDataBase(_manglerSourceName, _manglerConStr, _manglerDatabaseScheme);
		Extraction _extraction = rs_iterator.getNext();
		System.out.println("CHARM ID : " + _extraction.getColumnVal( _extraction, "charm_id"));
		List<MappingInfo> domains = _criterian.getPiiList();
		for(MappingInfo _domain : domains){
			String colVal = _extraction.processNonConformities(_extraction.getColumnVal(_extraction, _domain.getPiiCol()) );
			String mangledVal = _extraction.getMangledValue(_domain.getPiiDomain(),_domain.getMapCol(),_domain.getResultCol(), colVal, _manglerDatabase.getConnection());
			try{
				//How to temporary update the cell value in a local database table
				
				System.out.println("before update : PII Column "+  _domain.getPiiCol() + " : "+ rs_iterator.getResultSet().getString(_domain.getPiiCol()));
				System.out.println("Rowset Col val " + rs_iterator.getResultSet().getString(_domain.getPiiCol()));
				rs_iterator.getResultSet().updateString(_domain.getPiiCol().toUpperCase(), mangledVal);
				System.out.println("after update : " + rs_iterator.getResultSet().getString(_domain.getPiiCol()));
			}catch (SQLException e){
				e.printStackTrace();
			}
		}
		Assert.assertNotNull(_extraction);
	}
	
	/*
	1. You can’t clone the resultSet and perhaps result set always maintain a connection to the underlying database 
	2. However I found a way to update the current row if you include the primary key but it would only update the 
	resultset when you update the current row in underlying database, which I don’t want to.
	3. Perhaps the only option I can think is to copy the values from the result set to some List but it is a bad design and a compromise to scalability part.
	*/
	@Test
	public void TestInsertionOfMangledRecord07(){
		//1.a creating an instance of a database by the PostgreSourceAgent
		String _sourceName = "hl_core_v2_test";
		String _conStr = "jdbc:postgresql://localhost:5432/||postgres||abcdefgh";
		String _databaseScheme = null;
		Database _postgreDatabase = new PostgreSQLDataBase(_sourceName, _conStr, _databaseScheme);
	
		//1.b creating an instance of a mangled database
		String _manglerSourceName = "hl_core_v2_test";
		String _manglerConStr = "jdbc:postgresql://localhost:5432/||postgres||abcdefgh";
		String _manglerDatabaseScheme = null;
		Database _manglerDatabase = new PostgreSQLDataBase(_manglerSourceName, _manglerConStr, _manglerDatabaseScheme);
		
		//1.c creating an instance of a destination database
		String _destSourceName = "postgres";
		String _destConStr = "jdbc:postgresql://localhost:5432/||postgres||abcdefgh";
		String _destDatabaseScheme = null;
		Database _destDatabase = new PostgreSQLDataBase(_destSourceName, _destConStr, _destDatabaseScheme);
		
		SourceAgent _pgAgent = new SourceAgent(_postgreDatabase);
		TargetAgent _trgtAgent = new TargetAgent(_destDatabase);	
		
		//2. creating an extraction criteria
		ExtractionCriterion _criterian = new ExtractionCriterion();
		_criterian.setMappingInfo("first_name", "first_names_domains", "oldname", "newname");
		_criterian.setMappingInfo("birth_date", "dob_domains", "oldname", "newname");
		Criteria c1= new Criteria();	
		MatchFilter _cityFilter = new MatchFilter(FilterType.birth_city, "'PROVO'");
		BetweenFilter _zipCodeFilter = new BetweenFilter(FilterType.birth_facility_Code, "'138'", "'142'");
		c1.getFilters().add(_cityFilter);
		c1.getFilters().add(_zipCodeFilter);
		Criteria c2= new Criteria();
		MatchFilter _bdateFilter = new MatchFilter(FilterType.birth_date, "'2009/01/10'");
		c2.getFilters().add(_bdateFilter);
		//add the criteria to the ExtractionCriterias
		_criterian.getCriteriaList().add(c1);
		_criterian.getCriteriaList().add(c2);
		_pgAgent.setSqlBuilder(new OHVPhase1SQLBuilder(_criterian));
		//3. Creating an instance of result set Iterator
		ResultSetIterator rs_iterator  = _pgAgent.getCursor(null);
		
		//4. Mangle the record and rebuild the result set
		Extraction _extraction = rs_iterator.getNext();
		List<MappingInfo> domains = _criterian.getPiiList();
		for(MappingInfo _domain : domains){
			String colVal = _extraction.processNonConformities(_extraction.getColumnVal(_extraction, _domain.getPiiCol()) );
			String mangledVal = _extraction.getMangledValue(_domain.getPiiDomain(),_domain.getMapCol(),_domain.getResultCol(), colVal, _manglerDatabase.getConnection());
			try{
				System.out.println("before update : PII Column "+  _domain.getPiiCol() + " : "+ rs_iterator.getResultSet().getString(_domain.getPiiCol()));
				rs_iterator.getResultSet().updateString(_domain.getPiiCol().toUpperCase(), mangledVal);
				System.out.println("after update : " + rs_iterator.getResultSet().getString(_domain.getPiiCol()));
			}catch (SQLException e){
				e.printStackTrace();
			}
		}
		//5. Insert that record in the destination postgreSQL database
		try {
			ResultSet rs = rs_iterator.getResultSet();
			_trgtAgent.setInsertBuilder(new MPIInsertBuilder(rs));
			_trgtAgent.insertIntoDatabase();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testOHVPhase1Engine(){
		ExtractionEngine engine = new OHVPhase1ExtractionEngine();
		engine.iterates();
	}

	private String getHLCOREDatabaseStr(){
		 String dbScheme = "";
		try {
			String filePath = System.getProperty("user.dir") + "\\src\\testing\\hl_core.txt";
			System.out.println(filePath);
		    BufferedReader in = new BufferedReader(new FileReader(filePath));
		    String str;
		   
		    while ((str = in.readLine()) != null)
		        dbScheme += str;
		    in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dbScheme;
	}
	
}
