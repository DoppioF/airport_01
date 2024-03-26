package searcher.dto;

import java.util.Arrays;

public class QueriesInfo {

	private String[] queries;
	private boolean executeOnlyOneQuery;
	
	
	public String[] getQueries() {
		return queries;
	}
	public void setQueries(String[] queries) {
		this.queries = queries;
	}
	public boolean getExecuteOnlyOneQuery() {
		return executeOnlyOneQuery;
	}
	public void setExecuteOnlyOneQuery(boolean executeOnlyOneQuery) {
		this.executeOnlyOneQuery = executeOnlyOneQuery;
	}
	@Override
	public String toString() {
		return "QueriesInfo [queries=" + Arrays.toString(queries) + ", executeOnlyOneQuery=" + executeOnlyOneQuery
				+ "]";
	}
}
