package filters;
import java.util.*;


public class ExtractionCriterion {	
	private List<MappingInfo> piiList = new ArrayList<MappingInfo>();
	
	private String SQLQuery = "";
	
	public void setSQLQuery(String _query){
		SQLQuery = _query;
	}
	public String getSQLQuery(){
		return SQLQuery;
	}
	
	public List<MappingInfo> getPiiList() {
		return piiList;
	}

	public void setPiiList(List<MappingInfo> piiList) {
		this.piiList = piiList;
	}

	private List<Criteria> criteriaList = new ArrayList<Criteria>();

	public List<Criteria> getCriteriaList() {
		return criteriaList;
	}

	public void setCriteriaList(List<Criteria> criteriaList){
		this.criteriaList = criteriaList;
	}

	public void setMappingInfo(String _piiCol, String _domain, String mapCol,String resultCol) {
		piiList.add(new MappingInfo(_piiCol, mapCol, resultCol, _domain));
	}

	public class MappingInfo{
		
		public MappingInfo(String piiCol, String mapCol, String resultCol, String piiDomain) {
			super();
			this.piiCol = piiCol;
			this.mapCol = mapCol;
			this.resultCol = resultCol;
			this.piiDomain = piiDomain;
		}
		
		private String piiCol = "";
		private String mapCol = "";
		private String resultCol = "";
		private String piiDomain;
		public String getPiiCol() {
			return piiCol;
		}
		public void setPiiCol(String piiCol) {
			this.piiCol = piiCol;
		}
		public String getPiiDomain() {
			return piiDomain;
		}
		public void setPIIDomain(String _piiDomain) {
			this.piiDomain = _piiDomain;
		}
		public String getMapCol() {
			return mapCol;
		}
		public String getResultCol() {
			return resultCol;
		}
		public void setMapCol(String mapCol) {
			this.mapCol = mapCol;
		}
		public void setResultCol(String resultCol) {
			this.resultCol = resultCol;
		}
	}
}
