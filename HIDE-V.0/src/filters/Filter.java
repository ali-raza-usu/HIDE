package filters;


public abstract class Filter {

	private FilterType attribute;
	private OperationType operation;
	private String value1;
	private String value2;
	
	public Filter(FilterType attribute, OperationType operation, String value1,
			String value2) {
		super();
		this.attribute = attribute;
		this.operation = operation;
		this.value1 = value1;
		this.value2 = value2;
	}
	
	public FilterType getAttribute() {
		return attribute;
	}

	public void setAttribute(FilterType attribute) {
		this.attribute = attribute;
	}

	public OperationType getOperation() {
		return operation;
	}

	public void setOperation(OperationType operation) {
		this.operation = operation;
	}

	public String getValue1() {
		return value1;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
	}

	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}
	
	public abstract String generateSql();
}
