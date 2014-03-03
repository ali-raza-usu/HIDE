package filters;


public class BetweenFilter extends Filter{

	public BetweenFilter(FilterType attribute, String value1, String value2) {
		super(attribute, OperationType.BETWEEN, value1, value2);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String generateSql() {
		return this.getAttribute() + " BETWEEN " + this.getValue1() + " AND " + this.getValue2();
	}

}
