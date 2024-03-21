package airport_01Model.models.entities;

import java.io.Serializable;
import java.util.List;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import customUtils.constants.db.DbConstants;

@Entity
@Table(name = DbConstants.RoleTable.TABLE_NAME)
public class Role implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = DbConstants.COLUMN_PK)
	private Long id;
	
	@Column(name = DbConstants.NAME)
	private String name;
	
	@OneToMany(mappedBy = DbConstants.RoleTable.TABLE_NAME, fetch = FetchType.LAZY)
	@JsonbTransient
	private List<Customer> customerList;
	
	
	
	public Role() {
		super();
	}

	public List<Customer> getCustomerList() {
		return customerList;
	}

	public void setCustomerList(List<Customer> customerList) {
		this.customerList = customerList;
	}

	public Role(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

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
		return "Role [id=" + id + ", name=" + name + ", customerList=" + 
				(customerList == null ? 0 : customerList.size()) + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		return
			null != obj
			&& obj instanceof Role
			&& ((Role) obj).getId() == this.id;
	}
}
