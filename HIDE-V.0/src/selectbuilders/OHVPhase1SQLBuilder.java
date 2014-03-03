package selectbuilders;
import java.util.List;

import filters.Criteria;
import filters.ExtractionCriterion;
import filters.QRY_OP;

public class OHVPhase1SQLBuilder extends SQLBuilder{

	String selectClause = "select ";
	String fromClause = " from ";
	String tableName = "";
	String query = "";
	QRY_OP opr = null;
	String[] cols = null;
	public OHVPhase1SQLBuilder(ExtractionCriterion _criteria) {
		super(_criteria);
		definePersonTable();
		opr = QRY_OP.SET_COL;
	}

	private void definePersonTable(){
		 tableName = "person";
		cols = new String[]{"charm_id", "first_name", "last_name", "birth_date", "birth_city", "birth_facility_Code"};
	}
	
	@Override
	public String getWhereClause() {
		String whereClause = " where ";
		StringBuffer strBuf = new StringBuffer();
		List<Criteria> criteriaList = extractionCriteria.getCriteriaList();
		int len = criteriaList.size();
		//Not have knowledge of DataMapper. PersonDataMapper should contain DataMap and a DataMap contains many ColumnMap
		for(int i =0; i<len-1; i++){
			strBuf.append(criteriaList.get(i).buildCriteria() + " or ");
		}
		strBuf.append(criteriaList.get(len-1).buildCriteria());
		whereClause+= strBuf.toString();
		return whereClause;
	}

	
	@Override
	public String getFromClause() {
		return fromClause + tableName;
		// It would list all the joins
	}

@Override
	public String getSelectClause(){
		if(opr == QRY_OP.ALL_COL)
			selectClause += " * ";
		else{
		for(int i =0; i<cols.length-1; i++)
			selectClause += " "+ cols[i] + " , ";
		selectClause += " "+ cols[cols.length-1] + " ";	
		}
		return selectClause;
	}
}
