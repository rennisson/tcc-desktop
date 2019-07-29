package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

public class FinanceiroController implements Initializable {

	@FXML
	AnchorPane parent;
	
	@FXML
	private Rectangle arrowFinanceiro;
	
	@FXML
	ComboBox<String> cbAno;
	
	@FXML
	ComboBox<String> cbMes;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		arrowFinanceiro.setVisible(true);
	}
	
}
