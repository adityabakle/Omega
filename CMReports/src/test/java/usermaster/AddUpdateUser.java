package usermaster;

import com.cellmania.cmreports.common.Encryptor;
import com.cellmania.cmreports.db.masters.UserMasterDTO;

public class AddUpdateUser {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		UserMasterDTO user = new UserMasterDTO();
		user.setUserName("aditya");
		user.setEmail("abakle@rim.com");
		user.setName("Aditya Bakle");
		user.setRoleId(1l);
		user.setEnabled(true);
		user.setUpdatedBy(2l);
		user.setPassword(Encryptor.encrypt("mm@dm@n", Encryptor.getSalt("aditya")));
	}

}
