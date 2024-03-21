//import customUtils.constants.strings.EjbConstants;

//package airport_01Ejb.controllers;
//
//import java.io.Serializable;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.ejb.LocalBean;
//import javax.ejb.Stateless;
//
//import airport_01Crud.crud.RoleCRUD;
//import airport_01Ejb.interfaces.RoleEjbInterface;
//import airport_01Model.models.classes.Role;
//
//@Stateless(name = EjbConstants.ROLE_EJB)
//@LocalBean
//public class RoleEjb implements Serializable, RoleEjbInterface {
//
//	private static final long serialVersionUID = 1L;
//
//	@Override
//	public List<Role> findAllRole() throws SQLException, Exception {
//		List<Role> roleList = new ArrayList<>();
//		
//		try {
//			roleList = RoleCRUD.instance().findAll();
//			return roleList;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return roleList;
//	}
//}
