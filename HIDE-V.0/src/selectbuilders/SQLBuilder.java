package selectbuilders;

import filters.ExtractionCriterion;

public abstract class SQLBuilder {
	
	ExtractionCriterion extractionCriteria = null;
	
	public SQLBuilder(ExtractionCriterion _criteria){
		extractionCriteria = _criteria;
	}
	
	public abstract String getSelectClause();
	public abstract String getFromClause();
	public abstract String getWhereClause();
	
	
	
	public String buildQuery() {
		String query = getSelectClause() + getFromClause() +getWhereClause() + " ";
		return query;
	}

}
