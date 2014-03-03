package insertBuilders;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class MPIInsertBuilder extends InsertBuilder{

	private String db_name = "public.";
	private String tablename = "";


	HashMap<String, String> map = new HashMap<String, String>();
	
	public MPIInsertBuilder(ResultSet _record){
		super(_record);
		definePersonTableMappings();
	}
	
	private void definePersonTableMappings(){
		tablename = "person";
		map.put("charm_id", "bigint");
		map.put("first_name", "character varying(50)");
		map.put("last_name", "character varying(50)");
		map.put("birth_date", "character(10)");
		map.put("birth_city", "character varying(100)");
		map.put("birth_facility_Code", "character varying(3)");
	}
	
	@Override
	public String buildValuesClasue(ResultSet _record) {
		String valuesClause = "";
		String[] colTypes = getColTypes(map);
		String[] colNames = getCols(map);
		int colnum = colTypes.length;
		String data = "";
		 for(int i=1; i<=colnum; i++){
		        String columnType = colTypes[i-1];
		           try {
					data = _record.getString(colNames[i-1]);
				} catch (SQLException e) {
					e.printStackTrace();
				}                
		             if(data == null || "".equals(data.trim())){  
		            	 valuesClause += "null";
		             }else{                            
		                 StringBuffer sb = new StringBuffer();
		                 for(int x=0; x<data.length(); x++) {
		                     if(data.substring(x,x+1).equals("'"))
		                         sb.append("''");
		                     else
		                         sb.append(data.substring(x,x+1));
		                 }
		                     data = sb.toString();

		                 if(columnType.startsWith("varying") ||columnType.startsWith("character") )
		                	 valuesClause += "'" + data + "'";    
		                 else if(columnType.startsWith("date"))
		                	 valuesClause += "to_date('" + data.substring(0,10) + "','YYYY-MM-DD')";    
		                 else
		                	 valuesClause += "" + data + "";               
		             }
		           if(i<colnum){
		        	   valuesClause += ",";                 
		           }
		       }
		 return valuesClause;
	}

	@Override
	public String buildInsertClause() {
		String insertClause = "";
		insertClause= "insert into " + db_name +tablename + " ";
		return insertClause;
		
	}

	@Override
	public String buildColsClause() {
		String colsClause = "";
		String[] cols = getCols(map);
		for(int i=1; i<=cols.length; i++){
	        String columnName = cols[i-1];           
	        colsClause += columnName;    
	           if(i<cols.length){
	        	   colsClause += ",";                 
	           }
	       }
		colsClause += "  ";
		return colsClause;
	}

	private String[] getColTypes(HashMap<String, String> map) {
		return map.values().toArray(new String[0]);
	}
	
	private String[] getCols(HashMap<String, String> map) {
		return map.keySet().toArray(new String[0]);
	}   
}
