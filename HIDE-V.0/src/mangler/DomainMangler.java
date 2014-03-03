package mangler;
import java.util.List;

import databases.Database;
import dataredactor.domains.PIIDomain;

public class DomainMangler {
	private Database _database = null;
	private List<PIIDomain> piiDomains = PIIDomain.createPIIDomainsList();
	
	public List<PIIDomain> getPiiDomains() {
		return piiDomains;
	}

	public void setPiiDomains(List<PIIDomain> piiDomains) {
		this.piiDomains = piiDomains;
	}

	public DomainMangler(Database _database){
		this._database = _database;
	}
	
	public Database getDatabase() {
		return _database;
	}

	public void setDatabase(Database _database) {
		this._database = _database;
	}	
}
