package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

public class PedidosController implements Initializable {
	
	@FXML
	private AnchorPane parent;
	
	@FXML
	private Rectangle arrowPedidos;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		arrowPedidos.setVisible(true);
	}
	
	
}
