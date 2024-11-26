package application;



import java.util.HashMap;
import java.util.Map.Entry;

public class User {

	public static HashMap<String, String> users = new HashMap<String, String>();

	private String userName;
	private String password;

	private User(String userName, String password) {

	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public static void createUser() {
		String key = "admin";
		String value = "123456";
		users.put(key, value);
	}

	public static boolean login(String userName, String password) {

		createUser();

		for (Entry<String, String> temp : users.entrySet()) {
			// Eşitik kontrol ederken primitive tipler(int,char,boolean) için == referans
			// tipliler için ise equals kullanılır
			if (userName.equals(temp.getKey()) && password.equals(temp.getValue())) {
				System.out.println("Başarıyla giriş yaptınız....");
				return true;
			} else {
				System.out.println("Hatalı giriş yaptınız");
				return false;
			}

		}
		return false;

	}

}
