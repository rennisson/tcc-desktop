package gui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class ViewController {
	
	@FXML
	private Button btnPedidos;
	
	@FXML
	private Button btnEstoque;
	
	@FXML
	private Button btnFinanceiro;
	
	@FXML
	private Pane arrowPedidos;
	
	@FXML
	private Pane arrowEstoque;

	@FXML
	private Pane arrowFinanceiro;
	
	@FXML
	protected AnchorPane root;
	
	@FXML
	protected AnchorPane ancora;
	
	public void onBtnPedidosAction() throws IOException {		

		arrowPedidos.setVisible(true);
		arrowEstoque.setVisible(false);
		arrowFinanceiro.setVisible(false);
		
		AnchorPane pane = FXMLLoader.load(getClass().getResource("/gui/Pedidos.fxml"));
		root.getChildren().setAll(pane);
	}
	
	public void onBtnEstoqueAction() {
		arrowPedidos.setVisible(false);
		arrowEstoque.setVisible(true);
		arrowFinanceiro.setVisible(false);
	}
	
	public void onBtnFinanceiroAction() {
		arrowFinanceiro.setVisible(true);
		arrowEstoque.setVisible(false);
		arrowPedidos.setVisible(false);
	}
}
