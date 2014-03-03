package filters;


public class MatchFilter extends Filter{

	public MatchFilter(FilterType attribute, String value1) {
		super(attribute, OperationType.EQUALS, value1, null);
	}

	@Override
	public String generateSql() {
		return this.getAttribute() + " = " + this.getValue1();
	}
	

}
