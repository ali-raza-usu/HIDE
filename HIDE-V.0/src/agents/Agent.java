package agents;

import filters.ExtractionCriterion;

public abstract class Agent {
	
	private String connectionStr = null;
	private String databaseScheme = null;
	private ExtractionCriterion extractionCriteia = new ExtractionCriterion();
	
	public String getConnectionStr() {
		return connectionStr;
	}
	public void setConnectionStr(String connectionStr) {
		this.connectionStr = connectionStr;
	}
	public String getDatabaseScheme() {
		return databaseScheme;
	}
	public void setDatabaseScheme(String databaseScheme) {
		this.databaseScheme = databaseScheme;
	}
	public ExtractionCriterion getExtractionCriteia() {
		return extractionCriteia;
	}
	public void setExtractionCriteia(ExtractionCriterion extractionCriteia) {
		this.extractionCriteia = extractionCriteia;
	}
}


