package extraction;
import java.sql.SQLException;
import javax.sql.rowset.CachedRowSet;

import com.sun.rowset.CachedRowSetImpl;

public class ResultSetIterator{
	private CachedRowSetImpl resultSet =null;
	
	public ResultSetIterator(){
		try {
			resultSet = new CachedRowSetImpl();
			resultSet.setPageSize(500);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	private String table = null;
	
	public CachedRowSet getResultSet() {
		return resultSet;
	}

	public void setResultSet(CachedRowSetImpl resultSet) {
		this.resultSet = resultSet;
		
		
	}

	public Extraction getNext(){
		Extraction extraction = new Extraction();
		extraction.setSetdataSourceId(getTable());
		try{
			if(resultSet.next() == false){
				if(resultSet.nextPage())
					resultSet.next();
			}
			extraction.setRecordId(resultSet.getRow()+ "");
			extraction.setRecordData(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return extraction;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}
}
