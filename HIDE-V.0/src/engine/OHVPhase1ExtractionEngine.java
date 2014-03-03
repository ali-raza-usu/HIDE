package engine;

import insertBuilders.MPIInsertBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import selectbuilders.OHVPhase1SQLBuilder;
import agents.SourceAgent;
import agents.TargetAgent;
import databases.Database;
import databases.PostgreSQLDataBase;
import extraction.Extraction;
import extraction.ResultSetIterator;
import filters.BetweenFilter;
import filters.Criteria;
import filters.ExtractionCriterion;
import filters.FilterType;
import filters.MatchFilter;
import filters.ExtractionCriterion.MappingInfo;

public class OHVPhase1ExtractionEngine extends ExtractionEngine{

	private SourceAgent sourceAgent = null;
	private TargetAgent targetAgent = null;
	
	private Database _postgreDatabase = null;
	private Database _manglerDatabase = null;
	private Database _destDatabase = null;
	
	public OHVPhase1ExtractionEngine(){
		intializeDatabases();
		initializeSourceAgent();
		initializeTargetAgent();
		super.getEngines().add(this);
	}
	
	public void intializeDatabases()
	{
		String _sourceName = "hl_core_v2_test";
		String _conStr = "jdbc:postgresql://localhost:5432/||postgres||abcdefgh";
		String _databaseScheme = null;
		_postgreDatabase = new PostgreSQLDataBase(_sourceName, _conStr, _databaseScheme);
	
		//1.b creating an instance of a mangled database
		String _manglerSourceName = "hl_core_v2_test";
		String _manglerConStr = "jdbc:postgresql://localhost:5432/||postgres||abcdefgh";
		String _manglerDatabaseScheme = null;
		_manglerDatabase = new PostgreSQLDataBase(_manglerSourceName, _manglerConStr, _manglerDatabaseScheme);
		
		//1.c creating an instance of a destination database
		String _destSourceName = "postgres";
		String _destConStr = "jdbc:postgresql://localhost:5432/||postgres||abcdefgh";
		String _destDatabaseScheme = null;
		_destDatabase = new PostgreSQLDataBase(_destSourceName, _destConStr, _destDatabaseScheme);
	}
	
	public void initializeSourceAgent(){
		sourceAgent = new SourceAgent(_postgreDatabase);
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
		sourceAgent.setExtractionCriteia(_criterian);
		sourceAgent.setSqlBuilder(new OHVPhase1SQLBuilder(sourceAgent.getExtractionCriteia()));	
	}
	
	public void initializeTargetAgent(){
		targetAgent = new TargetAgent(_destDatabase);
	}
	
	
	public void insertsIntoTarget(){
		//4. Mangle the record and rebuild the result set
		ResultSetIterator rs_iterator  = sourceAgent.getCursor(null);
		Extraction _extraction = rs_iterator.getNext();
		//while(rs_iterator.getNext()!=null)
		List<MappingInfo> domains = sourceAgent.getExtractionCriteia().getPiiList();
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
			targetAgent.setInsertBuilder(new MPIInsertBuilder(rs));
			targetAgent.insertIntoDatabase();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
