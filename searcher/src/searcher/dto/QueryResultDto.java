package searcher.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.json.bind.annotation.JsonbTransient;

public class QueryResultDto<T> implements Serializable {

	private static final long serialVersionUID = 7879812724957446317L;
	
	@JsonbTransient
	private Class<T> dtoType;
	private List<T> dtoList;
	private String dtoTypeName;
	
	public QueryResultDto(Class<T> dtoType) {
		this.dtoType = dtoType;
		dtoList = new ArrayList<>();
		dtoTypeName = dtoType.getSimpleName();
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

	public String getDtoTypeName() {
		return dtoTypeName;
	}

	public void setDtoTypeName(String dtoTypeName) {
		this.dtoTypeName = dtoTypeName;
	}

	@Override
	public String toString() {
		return "QueryResultDto [dtoType=" + dtoType + ", dtoList=" + dtoList + ", dtoTypeName=" + dtoTypeName + "]";
	}
	
}
