module CRMOperationsProject {
	requires javafx.controls;
	requires javafx.fxml;
	requires com.jfoenix;
	requires javafx.graphics;
	requires javafx.base;
	requires java.sql;
	requires mysql.connector.j;

	// `model` paketini JavaFX'e açıyoruz
	// Burada farklı package altında bulunan sınıflar birbiri arasında kullanmasına
	// olanak sağlıyoruz. Yani model package farklı package tarafından kullanımasına
	// olanak sağlıyoruz
	opens model to javafx.base;

	opens application to javafx.graphics, javafx.fxml;
}
