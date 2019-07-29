package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

public class EstoqueController implements Initializable {
	
	@FXML
	AnchorPane parent;
	
	@FXML
	private Rectangle arrowEstoque;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		arrowEstoque.setVisible(true);
	}
	
}
