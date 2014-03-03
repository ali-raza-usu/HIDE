package extraction;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import mangler.strategies.Strategy;

public class Extraction {
	
	String setdataSourceId = null;
	String recordId = null;
	ResultSet recordData = null;
	
	public String getSetdataSourceId() {
		return setdataSourceId;
	}
	public void setSetdataSourceId(String setdataSourceId) {
		this.setdataSourceId = setdataSourceId;
	}
	public String getRecordId() {
		return recordId;
	}
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	public ResultSet getRecordData() {
		return recordData;
	}
	public void setRecordData(ResultSet recordData) {
		this.recordData = recordData;
	}
	
	public void mangle(Strategy _strategy){
		_strategy.mangle(recordData);
	}
	
	 public String getMangledValue(String domain, String oldCol, String newCol, String col_val, Connection con){
	        String newVal = "";
	        col_val = processNonConformities(col_val); 
	        String query = " select "+newCol+" from "+ domain + " where " + oldCol +" = '" +col_val + "'" ;
	        try{
	            ResultSet result = null;
	            PreparedStatement select_stmt = con.prepareStatement(query);                        
	            result = select_stmt.executeQuery();
	            if(result.next()){
	                newVal = result.getString(newCol);
	            }
	            return newVal;
	        }catch(Exception e){
	            return newVal;
	        }
	    }
	 
	 public static String processNonConformities(String data) {
			if (data == null || "".equals(data.trim())) {
				data = "null";
			}else{
				StringBuffer sb = new StringBuffer();
				sb.append(data);
				for (int index = 0; index < data.length(); index++) {
					if (data.charAt(index) == '\'')
						sb.replace(index, index + 1, "");
				}
				data = sb.toString();						 
			}
			return data;
		}
	 
	 public String getColumnVal(Extraction _extraction, String col){
			String val = null;
			try {
				val = _extraction.getRecordData().getString(col);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return val;
		}
}
