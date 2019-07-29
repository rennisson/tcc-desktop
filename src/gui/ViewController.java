package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class ViewController implements Initializable {
	
	@FXML
	protected AnchorPane root;
	
	@FXML
	protected AnchorPane ancora;
	
	@FXML
	BorderPane borderPane;
	
	@FXML
	private Button btnPedidos;
	
	@FXML
	private Button btnEstoque;
	
	@FXML
	private Button btnFinanceiro;
	
	@FXML
	private ImageView imgMin;
	
	@FXML
	private ImageView imgMax;
	
	@FXML
	private ImageView imgClose;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		borderPane.setRight(null);
		borderPane.setBottom(null);	
		
		AnchorPane pane1 = null;
		try {
			pane1 = FXMLLoader.load(getClass().getResource("/gui/Pedidos.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		root.getChildren().setAll(pane1); // coloca a view dos Pedidos na tela!!!
	}
	
	public void onBtnPedidosAction() throws IOException {		
		AnchorPane pane1 = FXMLLoader.load(getClass().getResource("/gui/Pedidos.fxml"));
		root.getChildren().setAll(pane1);
	}
	
	public void onBtnEstoqueAction() throws IOException {
		AnchorPane pane1 = FXMLLoader.load(getClass().getResource("/gui/Estoque.fxml"));
		root.getChildren().setAll(pane1);
	}
	
	public void onBtnFinanceiroAction() throws IOException {
		AnchorPane pane1 = FXMLLoader.load(getClass().getResource("/gui/Financeiro.fxml"));
		root.getChildren().setAll(pane1);
	}
	
	@FXML
	public void min(MouseEvent event) {
		Stage s = (Stage) ((Node) event.getSource()).getScene().getWindow();
		s.setIconified(true);
	}
	
	@FXML
	public void max(MouseEvent event) {
		Stage s = (Stage) ((Node) event.getSource()).getScene().getWindow();
		
		if (s.isFullScreen()) {
			s.setFullScreen(false);
		} else {
			s.setFullScreen(true);
		}
	}

	@FXML
	public void close(MouseEvent event) {
		Stage s = (Stage) ((Node) event.getSource()).getScene().getWindow();
		s.close();
	}
	
	private double xOffset = 0; 
	private double yOffset = 0;
	
	@FXML
	public void move(MouseEvent event) {
		xOffset = event.getSceneX();
        yOffset = event.getSceneY();
	}
	
	@FXML
	public void stay(MouseEvent event) {
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
	}
	
}
