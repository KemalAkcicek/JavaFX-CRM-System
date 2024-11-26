package application;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXButton.ButtonType;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Dialog;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class LoginController {

	@FXML
	private PasswordField password;

	@FXML
	private TextField userName;

	@FXML
	private JFXButton button;

	
	@FXML
	private Pane anaPencere;

	@FXML
	void loginButton(ActionEvent actionEvent) throws IOException {

		boolean sonuc = User.login(userName.getText(), password.getText());
		userName.setText("");
		password.setText("");

		if (sonuc) {
			// Yükleyeceğimiz fxml dosyasyını yazıyoruz
			Dialog<ButtonType> dialog = new Dialog<ButtonType>();
			dialog.initOwner(anaPencere.getScene().getWindow());

			MainPageController.dialogGet(dialog);

			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(getClass().getResource("MainPage.fxml"));

			dialog.setTitle("Main Page");
			dialog.getDialogPane().setContent(fxmlLoader.load());

			dialog.getDialogPane().getButtonTypes().add(javafx.scene.control.ButtonType.CLOSE);

			// Mevcut stage kapatmayı sağladık
			Stage currentStage = (Stage) button.getScene().getWindow();
			currentStage.close();

			dialog.showAndWait();
		} else {

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Hata Bildirimi");
			alert.setContentText("Kullanıcı Adı veya Parola hatalı, Lütfen tekrar deneyiniz...");
			alert.show();
		}

	}
}
