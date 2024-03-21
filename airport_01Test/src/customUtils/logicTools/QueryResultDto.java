package customUtils.logicTools;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QueryResultDto implements Serializable {

	private static final long serialVersionUID = 7879812724957446317L;
	private Class<?> dtoType;
	private List<?> dtoList = new ArrayList<>();
	
	public QueryResultDto(Class<?> dtoType) {
		this.dtoType = dtoType;
	}
	
	public Class<?> getDtoType() {
		return dtoType;
	}
	public void setDtoType(Class<?> dtoType) {
		this.dtoType = dtoType;
	}
	public List<?> getDtoList() {
		return dtoList;
	}
	public void setDtoList(List<?> dtoList) {
		this.dtoList = dtoList;
	}
	
	
}
