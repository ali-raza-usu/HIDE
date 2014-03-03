package insertBuilders;

import java.sql.ResultSet;

public abstract class InsertBuilder {

	ResultSet record = null;
	public InsertBuilder(ResultSet _record){
		record = _record;
	}
	public abstract String buildInsertClause();
	public abstract String buildColsClause();
	public abstract String buildValuesClasue(ResultSet _record);
	
	public String buildInsertQuery(){
		return buildInsertClause() + " ( "+ buildColsClause()+ " ) values ( " + buildValuesClasue(record) + " ) ;";
	}
}
