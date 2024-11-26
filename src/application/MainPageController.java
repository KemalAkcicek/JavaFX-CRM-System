package application;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton.ButtonType;

import com.jfoenix.controls.JFXTextArea;
import com.mysql.cj.x.protobuf.MysqlxConnection.Close;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
//import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Customer;
import model.DataSource;
import model.Reminders;

public class MainPageController implements Initializable {

	@FXML
	private TableView<Customer> customerTable = new TableView<Customer>();

	@FXML
	private TableColumn<Customer, String> customerName = new TableColumn<Customer, String>("Müşteri adı");

	@FXML
	private TableColumn<Customer, String> customerSurname = new TableColumn<Customer, String>("Müşteri Soyadı");

	@FXML
	private TableColumn<Customer, String> customerTel = new TableColumn<Customer, String>("Müşteri Telefonu");

	@FXML
	private TableColumn<Customer, String> customerAdres = new TableColumn<Customer, String>("Müşteri Adresi");

	@FXML
	private TableColumn<Customer, String> customerMeslek = new TableColumn<Customer, String>("Müşteri Mesleği");

	@FXML
	private TableColumn<Customer, String> tableColumnButton = new TableColumn<Customer, String>("Teklif Yap");

	@FXML
	private JFXTextArea txtArea;

	@FXML
	private TextField titleText;

	@FXML
	private StackPane centerPane;

	@FXML
	private BorderPane borderMainPane;

	@FXML
	private VBox form;

	@FXML
	private TitledPane titledPane;

	@FXML
	private static Dialog<com.jfoenix.controls.JFXButton.ButtonType> dialogMainPage;

	public static void dialogGet(Dialog<com.jfoenix.controls.JFXButton.ButtonType> dialog) {
		dialogMainPage = dialog;
	}

	ScrollPane scrollPane = new ScrollPane();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		DataSource.getInstance().databaseOpen();

		titleText.setEditable(false);
		form = new VBox(10);

		homePage();

		customerName.setCellValueFactory(new PropertyValueFactory<>("name"));
		customerSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
		customerTel.setCellValueFactory(new PropertyValueFactory<>("telNo"));
		customerAdres.setCellValueFactory(new PropertyValueFactory<>("adres"));
		customerMeslek.setCellValueFactory(new PropertyValueFactory<>("meslek"));

		// Tablonun her satırına teklif yap buttonu ekliyoruz
		tableColumnButton.setCellFactory(new Callback<TableColumn<Customer, String>, TableCell<Customer, String>>() {

			@Override
			public TableCell<Customer, String> call(TableColumn<Customer, String> arg0) {

				return new TableCell<Customer, String>() {
					private final Button teklifYap = new Button("Teklif Yap");

					{
						teklifYap.setStyle("-fx-background-color: green; -fx-text-fill: white;");
						teklifYap.setOnAction(event -> {

							Customer customer = getTableRow().getItem();

							if (customer != null) {
								form.getChildren().clear();
								TextArea textArea = new TextArea();
								textArea.setPromptText("Tekliflerini Bu alana giriniz");
								Button save = new Button("Teklifi Kaydet");
								save.setStyle(
										"-fx-background-color: green; -fx-text-fill: white; -fx-cursor: hand;-fx-font-size: 16px");
								Button close = new Button("Geri Çık");
								close.setStyle(
										"-fx-background-color: red; -fx-text-fill: white; -fx-cursor: hand;-fx-font-size: 14px");
								form.getChildren().addAll(textArea, save, close);

								close.setOnAction(e -> {
									customerSpecialOffer(event);

								});
								save.setOnAction(e -> {
									String offer = textArea.getText();
									if (offer.isEmpty()) {
										Alert error = new Alert(AlertType.ERROR);
										error.setContentText("Teklif alanı boş geçilemez");
										error.show();

									} else {
										DataSource.getInstance().teklifKaydet(customer, offer);
										Alert alert = new Alert(AlertType.CONFIRMATION);
										alert.setContentText("Teklif başarıya kaydedildi");
										alert.show();
										customerSpecialOffer(event);
									}

								});

							}

						});
					}

					@Override
					protected void updateItem(String item, boolean empty) {

						super.updateItem(item, empty);
						// Burada tabloda değerler olan sütüna button ekliyoruz
						if (empty) {
							setGraphic(null);
						} else {
							setGraphic(teklifYap);
						}
					}

				};

			}
		});

		customerTable.getColumns().addAll(customerName, customerSurname, customerTel, customerAdres, customerMeslek,
				tableColumnButton);
		// Tabloların columnların tüm tabloyu kapsamasını sağladık
		customerTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

	}

	@FXML
	void musteriKaydet(ActionEvent event) {

		centerPane.getChildren().clear();
		form.getChildren().clear();
		// Tekrardan verileri eski haline getirdik konum olarak
		form.setAlignment(Pos.TOP_LEFT);

		form.setStyle("-fx-padding: 20;");

		// Form elemanları ekledik
		Label nameLabel = new Label("Müşteri Adı:");
		TextField nameField = new TextField();
		nameField.setPromptText("Müşteri Adını Girin");

		Label surnameLabel = new Label("Müşteri Soyadı:");
		TextField surnameField = new TextField();
		surnameField.setPromptText("Müşteri Soyadını Girin");

		Label phoneLabel = new Label("Telefon Numarası:");
		TextField phoneField = new TextField();
		phoneField.setPromptText("Telefon Numarasını Girin");

		Label adresLabel = new Label("Müşteri Adresi");
		TextField adresField = new TextField();
		adresField.setPromptText("Müşteri adresini giriniz");

		Label meslekLabel = new Label("Müşteri Mesleğini");
		TextField meslekField = new TextField();
		meslekField.setPromptText("Müşteri mesleğini giriniz");

		Button saveButton = new Button("Kaydet");
		saveButton.setStyle("-fx-background-color:#90EE90;");
		;
		saveButton.setOnAction(e -> {

			System.out.println("Veriler alındı");

			if ((nameField.getText().isEmpty() || surnameField.getText().isEmpty() || phoneField.getText().isEmpty()
					|| meslekField.getText().isEmpty() || adresField.getText().isEmpty())) {
				Alert warning = new Alert(AlertType.WARNING);
				warning.setContentText("Hatalı veya Boş alanı Girişi Yaptınız");
				warning.show();

			} else {
				DataSource.getInstance().databaseOpen();
				DataSource.getInstance().addCustomer(nameField.getText(), surnameField.getText(), phoneField.getText(),
						meslekField.getText(), adresField.getText());
				nameField.clear();
				adresField.clear();
				meslekField.clear();
				surnameField.clear();
				phoneField.clear();
				Alert success = new Alert(AlertType.CONFIRMATION);
				success.setContentText("Kayıt başarıyl oluşturuldu.....");
				success.show();

			}

		});

		form.getChildren().addAll(nameLabel, nameField, surnameLabel, surnameField, phoneLabel, phoneField, adresLabel,
				adresField, meslekLabel, meslekField, saveButton);

		centerPane.setStyle("-fx-background-color: #F0F0F0;");
		centerPane.getChildren().add(form);

	}

	@FXML
	void customerSpecialOffer(ActionEvent event) {

		centerPane.getChildren().clear();
		form.getChildren().clear();
		form.setAlignment(Pos.TOP_LEFT);

		Label customerBaslik = new Label("Tüm Müşteriler");
		customerBaslik.setId("hatirlatciID");

		form.getChildren().addAll(customerBaslik, customerTable);
		centerPane.getChildren().add(form);

		DataSource.getInstance().databaseOpen();
		Task<ObservableList<Customer>> task = new Task<ObservableList<Customer>>() {

			@Override
			protected ObservableList<Customer> call() throws Exception { //
				// TODO Auto-generated method stub
				return FXCollections.observableArrayList(DataSource.getInstance().allCustomerShow());
			}
		};
		new Thread(task).start();

		// Burada task içindeki verileri tabloya bağladık
		customerTable.itemsProperty().bind(task.valueProperty());

		task.setOnSucceeded(Event -> {
			System.out.println(task);
			System.out.println("İşlem başarılıdı");
		});
		centerPane.setStyle("-fx-background-color: #F0F0F0;");
	}

	@FXML
	void customerReminders(ActionEvent event) {

		centerPane.getChildren().clear();
		form.getChildren().clear();

		HBox hBox = new HBox(40);

		form.setStyle("-fx-padding: 10;");

		Button addReminders = new Button("Hatırlatıcı ekle");
		addReminders.setOnAction(e -> {
			hatırlatıcılarAl();
		});
		Button showReminders = new Button("Hatırlatıcıları göster");
		showReminders.setOnAction(e -> hatirlaticiGoster());

		addReminders.setId("add");
		showReminders.setId("show");

		hBox.getChildren().addAll(addReminders, showReminders);
		hBox.setAlignment(Pos.CENTER);

		form.getChildren().add(hBox);
		form.setAlignment(Pos.CENTER);

		centerPane.getChildren().add(form);
		centerPane.setStyle("-fx-background-color: #E3F2FD;");

	}

	private void hatirlaticiGoster() {

		centerPane.getChildren().clear();
		form.getChildren().clear();
		form.setAlignment(Pos.TOP_LEFT);

		Label labelField = new Label("HATIRLATICILAR");
		labelField.setId("hatirlatciID");

		TableView<Reminders> remindersTable = new TableView<Reminders>();
		TableColumn<Reminders, String> baslik = new TableColumn<Reminders, String>("Başlık");
		TableColumn<Reminders, String> aciklama = new TableColumn<Reminders, String>("Açıklama");
		TableColumn<Reminders, String> nameSurname = new TableColumn<Reminders, String>("Adı Soyadı");
		TableColumn<Reminders, String> telNo = new TableColumn<Reminders, String>("Telefon Numarası");
		TableColumn<Reminders, String> tarih = new TableColumn<Reminders, String>("Tarih");
		TableColumn<Reminders, String> onemlilik = new TableColumn<Reminders, String>("Önemlilik Durumu");
		TableColumn<Reminders, String> removeReminder = new TableColumn<Reminders, String>();

		aciklama.setPrefWidth(70);
		// Burada her satıra button koyuruyoruz
		removeReminder.setCellFactory(new Callback<TableColumn<Reminders, String>, TableCell<Reminders, String>>() {

			@Override
			public TableCell<Reminders, String> call(TableColumn<Reminders, String> arg0) {

				return new TableCell<Reminders, String>() {
					private final Button removeHatirlaci = new Button("Tamamlandı");

					{
						removeHatirlaci.setStyle("-fx-background-color: green; -fx-text-fill: white;");

						removeHatirlaci.setOnAction(e -> {

							// Hangi butona tıklandığı bulduk
							Reminders reminders = getTableRow().getItem();
							DataSource.getInstance().tamamlananHatirlaticiAdd(reminders);
							Boolean silindiMi = DataSource.getInstance().removeReminder(reminders);
							Alert alert = new Alert(AlertType.CONFIRMATION);
							alert.setContentText("Kaldırma işlemi başarıyla gerçekleşti");
							alert.show();
							hatirlaticiGoster();
							System.out.println("Silindi mi=" + silindiMi);

						});

					}

					@Override
					protected void updateItem(String item, boolean empty) {

						super.updateItem(item, empty);
						// Burada tabloda değerler olan sütüna button ekliyoruz
						if (empty) {
							setGraphic(null);
						} else {
							setGraphic(removeHatirlaci);
						}
					}

				};
			}
		});

		remindersTable.getColumns().addAll(baslik, aciklama, nameSurname, telNo, tarih, onemlilik, removeReminder);
		remindersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		Task<ObservableList<Reminders>> task2 = new Task<ObservableList<Reminders>>() {

			@Override
			protected ObservableList<Reminders> call() throws Exception {
				// TODO Auto-generated method stub
				return FXCollections.observableArrayList(DataSource.getInstance().allRemindersShow());
			}
		};
		new Thread(task2).start();

		remindersTable.itemsProperty().bind(task2.valueProperty());

		// Burada veriler observable liste şeklindeki taskın içinde alınıyor o yüzden
		// buradaki isimler sınıftakin aynısı olması lazım
		baslik.setCellValueFactory(new PropertyValueFactory<>("baslik"));
		aciklama.setCellValueFactory(new PropertyValueFactory<>("aciklama"));
		nameSurname.setCellValueFactory(new PropertyValueFactory<>("nameSurname"));
		telNo.setCellValueFactory(new PropertyValueFactory<>("telNo"));
		tarih.setCellValueFactory(new PropertyValueFactory<>("tarih"));
		onemlilik.setCellValueFactory(new PropertyValueFactory<>("önemlilik"));

		form.getChildren().addAll(labelField, remindersTable);
		centerPane.getChildren().add(form);

	}

	private void hatırlatıcılarAl() {

		centerPane.getChildren().clear();
		form.getChildren().clear();
		form.setAlignment(Pos.TOP_LEFT);

		Label remindBaslık = new Label("Başlık");
		TextField baslikField = new TextField();
		baslikField.setPromptText("Hatırlatmanın başlığını giriniz:");

		Label remindAciklama = new Label("Açıklama");
		TextArea aciklamaField = new TextArea();
		aciklamaField.setPromptText("Hatırlatmanın açıklamasını yazınız:");

		Label remindName = new Label("Müşterinin Adını ve Soyadını giriniz");
		TextField nameField = new TextField();
		nameField.setPromptText("Müşterinin adını ve soyadını giriniz:");

		Label remindTel = new Label("Müşterinin Telefon numarasını giriniz:");
		TextField telField = new TextField();
		telField.setPromptText("Müşterinin Telefonu giriniz");

		Label reminDate = new Label("Hatırtlatmanın tarihini giriniz");
		DatePicker datePickerField = new DatePicker();
		datePickerField.setPromptText("Lütfen tarih seçiniz");

		Label remindonemlilik = new Label("Önemlilik seviyesini giriniz");
		RadioButton dusuk = new RadioButton("Düşük");
		RadioButton orta = new RadioButton("Orta");
		RadioButton yuksek = new RadioButton("Yüksek");

		ToggleGroup onemlilkDegeri = new ToggleGroup();
		dusuk.setToggleGroup(onemlilkDegeri);
		orta.setToggleGroup(onemlilkDegeri);
		yuksek.setToggleGroup(onemlilkDegeri);
		orta.setSelected(true);

		// Datepicker gelen veri localdate alıyoruz ama biz bunu kaydetmemimiz için
		// java.sql.Date kullanmamaız lazım

		Button remindSave = new Button("Kaydet");
		remindSave.setStyle("-fx-background-color:green; -fx-text-fill:white;");
		remindSave.setOnAction(e -> {
			// Seçilen bir toggle almak
			ToggleButton selectToggle = (ToggleButton) onemlilkDegeri.getSelectedToggle();
			String onemlilikDegeri = selectToggle.getText();

			Reminders tempReminders = new Reminders(baslikField.getText(), aciklamaField.getText(), nameField.getText(),
					telField.getText(), datePickerField.getValue(), onemlilikDegeri);

			if (baslikField.getText().isEmpty() || aciklamaField.getText().isEmpty() || nameField.getText().isEmpty()
					|| telField.getText().isEmpty() || onemlilikDegeri.isEmpty()
					|| datePickerField.getValue() == null) {
				Alert error = new Alert(AlertType.ERROR);
				error.setContentText("Boş veya hatali bir değer girdiniz");
				error.show();

			} else {
				Alert confirmation = new Alert(AlertType.CONFIRMATION);
				confirmation.setContentText("Kayıt başarıyla kaydedildi");
				confirmation.show();
				DataSource.getInstance().remindersAdd(tempReminders);
				baslikField.clear();
				aciklamaField.clear();
				nameField.clear();
				telField.clear();
				datePickerField.setValue(null);

			}

		});

		Button remindClose = new Button("Geri Çık");
		remindClose.setStyle("-fx-background-color:red; -fx-text-fill:white;");
		remindClose.setOnAction(e -> {
			customerReminders(e);
		});

		form.getChildren().addAll(remindBaslık, baslikField, remindAciklama, aciklamaField, remindName, nameField,
				remindTel, telField, reminDate, datePickerField, remindonemlilik, dusuk, orta, yuksek, remindSave,
				remindClose);
		centerPane.getChildren().add(form);
	}

	@FXML
	void raporlamaButton(ActionEvent event) {

		DataSource.getInstance().databaseOpen();

		centerPane.getChildren().clear();
		form.getChildren().clear();
		form.setAlignment(Pos.TOP_LEFT);

		Label baslikLabel = new Label("RAPORLAR");
		baslikLabel.setId("baslikRapor");

		Label hatırlatıcıBaslık = new Label("Hatırlatıcı Analizi");
		hatırlatıcıBaslık.setId("hatirlaticiBaslik");

		Label tamamlananHatirlatici = new Label("Tamamlanan Hatırlatıcılar");
		tamamlananHatirlatici.setId("tamamlananHatirlatici");

		Task<ObservableList<Reminders>> raporlamaTask = new Task<ObservableList<Reminders>>() {

			@Override
			protected ObservableList<Reminders> call() throws Exception {
				return FXCollections.observableArrayList(DataSource.getInstance().tamamlananHatirlaticiGoster());
			}
		};
		new Thread(raporlamaTask).start();

		TextArea textArea = new TextArea();
		textArea.setStyle("-fx-background-color: #F0F0F0;");
		textArea.setEditable(false);

		// Burada threadler yaptığımız için succeeeded metotunu yazmamız lazım yoksa
		// null hatası alırız
		raporlamaTask.setOnSucceeded(e -> {
			ObservableList<Reminders> hatirlaticlar = raporlamaTask.getValue();

			textArea.appendText("Tamamalanan hatırlatıcılar hakkında genel bilgiler \n");

			for (Reminders temp : hatirlaticlar) {

				textArea.appendText("Başlık:" + temp.getBaslik() + "\n" + "Açıklama:" + temp.getAciklama() + "\n"
						+ "Adı Soyadı:" + temp.getNameSurname() + "\n" + "Telefon numarsısı:" + temp.getTelNo() + "\n"
						+ "Önemlilik derecesi: " + temp.getÖnemlilik() + "\n" + "Başarıyla Tamamlandı....." + "\n"
						+ "\n");

			}

		});
		TextArea textArea2 = new TextArea();
		int toplamHatırlatıcı = DataSource.getInstance().hatirlaticiSayisi();
		TextField textField = new TextField("Bekleyen hatırlatıcı sayisi= " + toplamHatırlatıcı);

		textField.setEditable(false);
		textField.setId("tamamlananHatirlatici");
		Button detaylarıIcin = new Button("Bekleyen hatırlatıcılar görmek için tıklayınız");
		detaylarıIcin.setStyle("-fx-background-color:#90EE90;");
		detaylarıIcin.setOnAction(e -> {
			hatirlaticiGoster();

		});

		int teklifYapılanmusteriSayisi = DataSource.getInstance().teklifYapılanMusteriSayisi();
		int musteriSayisi = DataSource.getInstance().musteriSayisi();
		Label musteriAnalizi = new Label("Müşteri Analizi");
		musteriAnalizi.setId("hatirlaticiBaslik");

		TextField customerMessage = new TextField("Toplam müşteri Sayısı: " + musteriSayisi);
		customerMessage.setEditable(false);

		TextField textFieldMessage = new TextField(
				"Toplam teklif yapılan müşteri Sayısı: " + teklifYapılanmusteriSayisi);
		textFieldMessage.setEditable(false);

		Button musterilerIcin = new Button("Müşteri listesi  görmek için tıklayınız");
		musterilerIcin.setStyle("-fx-background-color:#90EE90;");
		musterilerIcin.setOnAction(e -> {
			customerSpecialOffer(event);

		});

		scrollPane.setContent(form);
		scrollPane.setFitToHeight(true);
		scrollPane.setFitToWidth(true);

		centerPane.getChildren().add(scrollPane);

		form.getChildren().addAll(baslikLabel, hatırlatıcıBaslık, tamamlananHatirlatici, textArea, textField,
				detaylarıIcin, musteriAnalizi, customerMessage, textFieldMessage, musterilerIcin);

		centerPane.getChildren().addAll(form);
		centerPane.setStyle("-fx-background-color:#f7f9fc");

	}

	public void homePage() {
		centerPane.getChildren().clear();
		form.getChildren().clear();
		form.setAlignment(Pos.TOP_LEFT);

		Label label2 = new Label("Uygulamanızın ana sayfasına hoş geldiniz.");
		label2.setId("label2");

		Label label3 = new Label();

		String imagePath = getClass().getResource("/application/images/image2.png").toExternalForm();
		Image image = new Image(imagePath);

		ImageView imageView = new ImageView(image);

		imageView.setPreserveRatio(true);

		imageView.setFitWidth(700);
		imageView.setFitHeight(700);

		StackPane.setAlignment(imageView, Pos.CENTER);

		form.getChildren().addAll(label2, label3, imageView);
		form.setStyle("-fx-background-color: #F0F0F0;");
		centerPane.getChildren().add(form);

	}

	@FXML
	void cikisYap(ActionEvent event) {

		dialogMainPage.close();
		Platform.exit();

	}
}
