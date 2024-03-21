package airport_01Model.dto;

import java.io.Serializable;

public class RoleDto implements Serializable {

	private static final long serialVersionUID = -8227504119697497960L;
	private Long id;
	private String name;
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "RoleDto [id=" + id + ", name=" + name + "]";
	}
	
	
	
}
