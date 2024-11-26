package model;

import java.time.LocalDate;

public class Reminders {

	private String baslik;
	private String aciklama;
	private String nameSurname;
	private String telNo;
	private LocalDate tarih;
	private String önemlilik;
	private int reminderID;

	public int getReminderID() {
		return reminderID;
	}

	public void setReminderID(int reminderID) {
		this.reminderID = reminderID;
	}

	public Reminders(String baslik, String aciklama, String nameSurname, String telNo, LocalDate localDate,
			String önemlilik) {

		this.baslik = baslik;
		this.aciklama = aciklama;
		this.nameSurname = nameSurname;
		this.telNo = telNo;
		this.tarih = localDate;
		this.önemlilik = önemlilik;
		
	}

	// Boş constructor
	public Reminders() {
	}

	public String getÖnemlilik() {
		return önemlilik;
	}

	public void setÖnemlilik(String önemlilik) {
		this.önemlilik = önemlilik;
	}

	public String getBaslik() {
		return baslik;
	}

	public void setBaslik(String baslik) {
		this.baslik = baslik;
	}

	public String getAciklama() {
		return aciklama;
	}

	public void setAciklama(String aciklama) {
		this.aciklama = aciklama;
	}

	public String getNameSurname() {
		return nameSurname;
	}

	public void setNameSurname(String nameSurname) {
		this.nameSurname = nameSurname;
	}

	public String getTelNo() {
		return telNo;
	}

	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}

	public LocalDate getTarih() {
		return tarih;
	}

	public void setTarih(LocalDate tarih) {
		this.tarih = tarih;
	}

	@Override
	public String toString() {
		return "Reminders [baslik=" + baslik + ", aciklama=" + aciklama + ", nameSurname=" + nameSurname + ", telNo="
				+ telNo + ", tarih=" + tarih + ", önemlilik=" + önemlilik + ", reminderID=" + reminderID + "]";
	}

	

}
