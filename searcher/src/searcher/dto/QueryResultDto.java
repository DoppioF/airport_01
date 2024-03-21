package searcher.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QueryResultDto<T> implements Serializable {

	private static final long serialVersionUID = 7879812724957446317L;
	private Class<T> dtoType;
	private List<T> dtoList;
	
	public QueryResultDto(Class<T> dtoType) {
		this.dtoType = dtoType;
		dtoList = new ArrayList<>();
	}
	
	public Class<T> getDtoType() {
		return dtoType;
	}
	public void setDtoType(Class<T> dtoType) {
		this.dtoType = dtoType;
	}
	public List<T> getDtoList() {
		return dtoList;
	}
	public void setDtoList(List<T> dtoList) {
		this.dtoList = dtoList;
	}
	
	
}
