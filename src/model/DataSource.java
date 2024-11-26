package model;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class DataSource {

	public static final String USER_NAME = "root";
	public static final String PASSWORD = "Kemal.44";
	public static final String DB_NAME = "customer";
	public static final String DB_URL = "jdbc:mysql://localhost:3306/" + DB_NAME;

	// offer tablosu
	public static final String TABLE_OFFERS = "offer";
	public static final String OFFER_OFFER_ID = "offerID";
	public static final String OFFER_CUSTOMER_ID = "customerID";
	public static final String OFFERS_OFFER = "offers";

	// Customer tablosu için
	public static final String TABLE_CUSTOMER = "customerData";
	public static final String CUSTOMER_ID = "customerID";
	public static final String CUSTOMER_NAME = "name";
	public static final String CUSTOMER_SURNAME = "surname";
	public static final String CUSTOMER_PHONE = "phone";
	public static final String CUSTOMER_MESLEK = "meslek";
	public static final String CUSTOMER_ADRES = "adres";

	// Reminders tablosu için
	public static final String TABLE_REMİNDERS = "reminders";
	public static final String REMİNDER_ID = "reminderID";
	public static final String REMİNDER_BASLİK = "baslik";
	public static final String REMİNDER_ACİKLAMA = "aciklama";
	public static final String REMİNDER_NAME_SURNAME = "nameSurname";
	public static final String REMİNDER_TELNO = "telNo";
	public static final String REMİNDER_TARİH = "tarih";
	public static final String REMİNDER_ONEMLİLİK = "onemlilik";

	// TamamlanAN reminders tablosu için
	public static final String OKEY_TABLE_REMİNDERS = "remindersokey";
	public static final String OKEY_REMİNDER_ID = "reminderID";
	public static final String OKEY_REMİNDER_BASLİK = "baslik";
	public static final String OKEY_REMİNDER_ACİKLAMA = "aciklama";
	public static final String OKEY_REMİNDER_NAME_SURNAME = "nameSurname";
	public static final String OKEY_REMİNDER_TELNO = "telNo";
	public static final String OKEY_REMİNDER_TARİH = "tarih";
	public static final String OKEY_REMİNDER_ONEMLİLİK = "onemlilik";

	private DataSource() {
	};// Nesne oluşturmanın önüne geçiyoruz

	// Tek bir nesne üzerinde işlem yapmayı sağlıyoruz
	private static DataSource instance = new DataSource();

	public static DataSource getInstance() {
		return instance;
	}

	public Connection baglanti;

	public boolean databaseOpen() {

		try {
			baglanti = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
			System.out.println("Veritabanı bağlantısı açıldı");
			return true;
		} catch (SQLException e) {

			e.printStackTrace();
			return false;
		}

	}

	public void databaseClose() {

		if (baglanti == null) {

		} else {
			try {
				baglanti.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void addCustomer(String name, String surname, String phone, String meslek, String adres) {

		String sorgu = "INSERT INTO " + TABLE_CUSTOMER + "(" + CUSTOMER_NAME + "," + CUSTOMER_SURNAME + ","
				+ CUSTOMER_PHONE + "," + CUSTOMER_MESLEK + "," + CUSTOMER_ADRES + ") " + "VALUES(?,?,?,?,?)";

		// Burada preparedstatement kullanmamızın nedeni daha doğru sonuç almak için ve
		// daha efektif kullanmak için birde bu gibi işlemler try-catch daha fazla
		// efektiv kullanman lazım daha iyi sonuç almak için
		try (PreparedStatement preparedStatement = baglanti.prepareStatement(sorgu)) {

			preparedStatement.setString(1, name);
			preparedStatement.setString(2, surname);
			preparedStatement.setString(3, phone);
			preparedStatement.setString(4, meslek);
			preparedStatement.setString(5, adres);

			int rowsAffeceted = preparedStatement.executeUpdate();

			if (rowsAffeceted > 0) {
				System.out.println("İşlem başarılı");
				System.out.println(sorgu);
			} else {
				System.out.println("İşlem başarısız");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// Bütün müşterileri aldık
	public ArrayList<Customer> allCustomerShow() {

		String sorgu = "SELECT *FROM " + TABLE_CUSTOMER;

		try (Statement statement = baglanti.createStatement()) {
			System.out.println(sorgu);
			ResultSet resultSet = statement.executeQuery(sorgu);

			ArrayList<Customer> customers = new ArrayList<Customer>();

			while (resultSet.next()) {

				Customer tempCustomer = new Customer();
				tempCustomer.setCustomerId(resultSet.getInt(1));
				tempCustomer.setName(resultSet.getString(2));
				tempCustomer.setSurname(resultSet.getString(3));
				tempCustomer.setTelNo(resultSet.getString(4));
				tempCustomer.setAdres(resultSet.getString(5));
				tempCustomer.setMeslek(resultSet.getString(6));
				customers.add(tempCustomer);

			}

			return customers;

		} catch (SQLException e) {
			System.out.println("SQL EXCEPTİON OLDU");
			System.out.println(e.getMessage());
			return null;
		}

	}

	// Tüm hatırlatıcılar gösterme
	public ArrayList<Reminders> allRemindersShow() {
		String sorgu = "SELECT *FROM " + TABLE_REMİNDERS;

		try (Statement statement = baglanti.createStatement()) {
			ResultSet resultSet = statement.executeQuery(sorgu);

			ArrayList<Reminders> reminders = new ArrayList<Reminders>();

			// Burada tüm verileri arraylist içine atıyoruz bu arraylistin içinde de
			// reminder nesnelerimizi saklıyoruz
			while (resultSet.next()) {

				Reminders tempReminders = new Reminders();
				// Verileri alırken kolon sayısı birden başlar

				// Biz burada reminderId tabloda göstermesek bile tempReminders nesnesine
				// vermemeiz lazım yoksa reminderId default olarak 0 olur
				tempReminders.setReminderID(resultSet.getInt(1));

				tempReminders.setBaslik(resultSet.getString(2));
				tempReminders.setAciklama(resultSet.getString(3));
				tempReminders.setNameSurname(resultSet.getString(4));
				tempReminders.setTelNo(resultSet.getString(5));

				// Bizde nesne localdate tipinde veritabanında date tipinde o yüzden
				// veritabından alırken önce date tipinde gelen veriyi localdate çevirip öyle
				// tanımlıyoruz .Tam tersi durumda veritabınna yazma durumda tam tersini yaparız
				java.sql.Date sqlData = resultSet.getDate(6);
				LocalDate localDate = sqlData.toLocalDate();
				tempReminders.setTarih(localDate);

				tempReminders.setÖnemlilik(resultSet.getString(7));
				reminders.add(tempReminders);

			}
			System.out.println("Veri tabanı işlemi tamamdır");
			return reminders;

		} catch (SQLException e) {
			System.out.println("Hatırlatıcı veriisni alırken hata oluştur");
			System.out.println(e.getMessage());
			return null;
		}

	}

	public ArrayList<Reminders> tamamlananHatirlaticiGoster() {

		String sorgu = "SELECT* FROM " + OKEY_TABLE_REMİNDERS;

		try (Statement statement = baglanti.createStatement()) {

			ResultSet resultSet = statement.executeQuery(sorgu);

			ArrayList<Reminders> tempArraylist = new ArrayList<Reminders>();

			while (resultSet.next()) {

				Reminders tempReminders = new Reminders();
				tempReminders.setReminderID(resultSet.getInt(1));
				tempReminders.setBaslik(resultSet.getString(2));
				tempReminders.setAciklama(resultSet.getString(3));
				tempReminders.setNameSurname(resultSet.getString(4));
				tempReminders.setTelNo(resultSet.getString(5));

				java.sql.Date sqlData = resultSet.getDate(6);
				LocalDate localDate = sqlData.toLocalDate();
				tempReminders.setTarih(localDate);

				tempReminders.setÖnemlilik(resultSet.getString(7));
				tempArraylist.add(tempReminders);

			}
			System.out.println("Veriler alındı");
			return tempArraylist;

		} catch (SQLException e) {
			System.out.println("Tamamlanan hatırlaticıları alırken hata oluştur");
			System.out.println("Hata:" + e.getMessage());
			return null;
		}

	}

	public int hatirlaticiSayisi() {

		String sorgu = "SELECT COUNT(*) FROM " + TABLE_REMİNDERS;

		try (Statement statement = baglanti.createStatement()) {

			// Int değer almak için ilk olarak result içine attım oradan alıyoruz
			ResultSet resultSet = statement.executeQuery(sorgu);

			int toplam = 0;

			if (resultSet.next()) {
				toplam = resultSet.getInt(1);
			}
			System.out.println("Toplam hatırlatıc sayısı: " + toplam);
			return toplam;

		} catch (SQLException e) {
			System.out.println("Hatırlatıcı sayısı alınırken hata oluştu");
			System.out.println("Hata mesajı:" + e.getMessage());
			return 0;
		}
	}

	public int musteriSayisi() {

		String sorgu = "SELECT COUNT(*) FROM " + TABLE_CUSTOMER;

		try (Statement statement = baglanti.createStatement()) {

			// Int değer almak için ilk olarak result içine attım oradan alıyoruz
			ResultSet resultSet = statement.executeQuery(sorgu);

			int toplam = 0;

			if (resultSet.next()) {
				toplam = resultSet.getInt(1);
			}
			System.out.println("müşteri sayısı Toplam: " + toplam);
			return toplam;

		} catch (SQLException e) {
			System.out.println("Hatırlatıcı sayısı alınırken hata oluştu");
			System.out.println("Hata mesajı:" + e.getMessage());
			return 0;
		}
	}

	public int teklifYapılanMusteriSayisi() {

		String sorgu = "SELECT COUNT(*) FROM " + TABLE_OFFERS;

		try (Statement statement = baglanti.createStatement()) {

			// Int değer almak için ilk olarak result içine attım oradan alıyoruz
			ResultSet resultSet = statement.executeQuery(sorgu);

			int toplam = 0;

			if (resultSet.next()) {
				toplam = resultSet.getInt(1);
			}

			return toplam;

		} catch (SQLException e) {
			System.out.println("Hatırlatıcı sayısı alınırken hata oluştu");
			System.out.println("Hata mesajı:" + e.getMessage());
			return 0;
		}
	}

	public void teklifKaydet(Customer customer, String offer) {

		String sorgu = "INSERT INTO " + TABLE_OFFERS + "(" + OFFER_CUSTOMER_ID + "," + OFFERS_OFFER + ") "
				+ "VALUES(?,?)";

		System.out.println(sorgu);
		try (PreparedStatement preparedStatement = baglanti.prepareStatement(sorgu)) {
			preparedStatement.setInt(1, customer.getCustomerId());
			preparedStatement.setString(2, offer);

			System.out.println(sorgu);

			int rowsAffecet = preparedStatement.executeUpdate();
			System.out.println(rowsAffecet);

		} catch (SQLException e) {
			System.out.println("SQL EXCEPTİON HATASI VAR");
			System.out.println(e.getMessage());
		}

	}

	public void remindersAdd(Reminders reminders) {

		databaseOpen();

		String sorgu = "INSERT INTO " + TABLE_REMİNDERS + "(" + REMİNDER_BASLİK + "," + REMİNDER_ACİKLAMA + ","
				+ REMİNDER_NAME_SURNAME + "," + REMİNDER_TELNO + "," + REMİNDER_TARİH + "," + REMİNDER_ONEMLİLİK + ") "
				+ "VALUES(?,?,?,?,?,?)";
		System.out.println(sorgu);
		try (PreparedStatement preparedStatement = baglanti.prepareStatement(sorgu)) {

			preparedStatement.setString(1, reminders.getBaslik());
			preparedStatement.setString(2, reminders.getAciklama());
			preparedStatement.setString(3, reminders.getNameSurname());
			preparedStatement.setString(4, reminders.getTelNo());

			// Localdate date dönüştürdük çünkü veritabanına date olarak atmamaız lazım
			// datepicker bize localdate vei
			LocalDate tarih = reminders.getTarih();
			java.sql.Date sqlDate = java.sql.Date.valueOf(tarih);
			preparedStatement.setDate(5, sqlDate);

			preparedStatement.setString(6, reminders.getÖnemlilik());

			int rowsaffect = preparedStatement.executeUpdate();
			System.out.println(rowsaffect);

		} catch (SQLException e) {
			System.out.println("SQL EXCEPTİON HATASI OLDU");
			System.out.println(e.getMessage());

		}

	}

	public void tamamlananHatirlaticiAdd(Reminders tamamlananHatirlaticilar) {

		databaseOpen();

		String sorgu = "INSERT INTO " + OKEY_TABLE_REMİNDERS + "(" + OKEY_REMİNDER_BASLİK + "," + OKEY_REMİNDER_ACİKLAMA
				+ "," + OKEY_REMİNDER_NAME_SURNAME + "," + OKEY_REMİNDER_TELNO + "," + OKEY_REMİNDER_TARİH + ","
				+ OKEY_REMİNDER_ONEMLİLİK + ") " + "VALUES(?,?,?,?,?,?)";

		System.out.println("Tamamlanan hatırlatıcı");
		System.out.println(sorgu);
		try (PreparedStatement preparedStatement = baglanti.prepareStatement(sorgu)) {

			preparedStatement.setString(1, tamamlananHatirlaticilar.getBaslik());
			preparedStatement.setString(2, tamamlananHatirlaticilar.getAciklama());
			preparedStatement.setString(3, tamamlananHatirlaticilar.getNameSurname());
			preparedStatement.setString(4, tamamlananHatirlaticilar.getTelNo());

			// Localdate date dönüştürdük çünkü veritabanına date olarak atmamaız lazım
			// datepicker bize localdate vei
			LocalDate tarih = tamamlananHatirlaticilar.getTarih();
			java.sql.Date sqlDate = java.sql.Date.valueOf(tarih);
			preparedStatement.setDate(5, sqlDate);

			preparedStatement.setString(6, tamamlananHatirlaticilar.getÖnemlilik());

			Boolean truemu = preparedStatement.execute();
			System.out.println("TAMAMLANAN HATIRLATICI" + truemu);

		} catch (SQLException e) {
			System.out.println("SQL EXCEPTİON HATASI OLDU");
			System.out.println(e.getMessage());

		}

	}

	public boolean removeReminder(Reminders reminder) {

		String sorgu = "DELETE FROM " + TABLE_REMİNDERS + " WHERE " + REMİNDER_ID + "=" + reminder.getReminderID();
		System.out.println(reminder);
		System.out.println(sorgu);

		try (Statement statement = baglanti.createStatement()) {

			statement.executeUpdate(sorgu);

			return true;
		} catch (SQLException e) {
			System.out.println("Reminder silinirken hata alındı");
			System.out.println("Messega=" + e.getMessage());
			return false;
		}

	}

}
