package filters;


public class GreaterThanFilter extends Filter{

	public GreaterThanFilter(FilterType attribute, String value1) {
		super(attribute, OperationType.GREATERTHEN, value1, null);
	}

	@Override
	public String generateSql() {
		return this.getAttribute() + " > " + this.getValue1();
	}

}
