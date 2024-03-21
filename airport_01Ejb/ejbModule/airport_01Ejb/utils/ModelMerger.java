package airport_01Ejb.utils;

import airport_01Model.models.entities.Customer;

public class ModelMerger {

	public void mergeCustomerUneditableFields(Customer customerToMerge, Customer customerOnDB) {
		customerToMerge.setEmail(customerOnDB.getEmail());
		customerToMerge.setPassword(customerOnDB.getPassword());
		customerToMerge.setBirthDate(customerOnDB.getBirthDate());
		customerToMerge.setBirthCity(customerOnDB.getBirthCity());
	}
}
