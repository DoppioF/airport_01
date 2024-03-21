//package airport_01Crud.crud;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.persistence.Query;
//
//import airport_01Model.models.classes.Role;
//import static airport_01Crud.connection.EntityManagerProvider.*;
//
//public class RoleCRUD {
//
//	@SuppressWarnings("unchecked")
//	public List<Role> findAllRole() {
//		List<Role> roleList = new ArrayList<>();
//		try {
//			beginEntityManagerTransaction();
//			
//			Query query = getEntityManager().createNativeQuery("SELECT * FROM role", Role.class);
//			roleList = query.getResultList();
//			
//			commitTransaction();
//			
//			return roleList;
//		} catch (Exception e) {
//			e.printStackTrace();
//			rollbackTransaction();
//		} finally {
//			getEntityManager().close();
//		}
//		
//		return roleList;
//	}
//}
