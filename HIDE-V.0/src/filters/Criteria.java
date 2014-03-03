package filters;
import java.util.ArrayList;
import java.util.List;

public class Criteria {
	//Extraction criteria should return us the whole and Complete SQL statement not just the where clause
	//SourceAgent will than use than extraction criteria to generate the Extractions
	private List<Filter> filters = new ArrayList<Filter>();
	private int limit;
	
	public List<Filter> getFilters() {
		return filters;
	}
	public void setFilters(List<Filter> filters) {
		this.filters = filters;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String buildCriteria() {
		StringBuffer strBuf = new StringBuffer();
		//Not have knowledge of DataMapper. PersonDataMapper should contain DataMap and a DataMap contains many ColumnMap
		for(int i =0; i<filters.size()-1; i++){
			strBuf.append(filters.get(i).generateSql() + " and ");
		}
		strBuf.append(filters.get(filters.size()-1).generateSql());
		return strBuf.toString();
	}
}
