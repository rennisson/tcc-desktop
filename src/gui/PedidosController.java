package gui;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import model.entities.Cliente;
import model.entities.Pedido;

public class PedidosController implements Initializable {
	
	@FXML
	private AnchorPane parent;
	
	@FXML
	private Rectangle arrowPedidos;
	
	@FXML
	private TableView<Pedido> tableViewPedidos;
	
	@FXML
	private TableColumn<Pedido, Date> tableColumnData;
	
	@FXML
	private TableColumn<Cliente, Integer> tableColumnCliente;
	
	@FXML
	private TableColumn<Pedido, String> tableColumnPedido;
	
	@FXML
	private TableColumn<Pedido, Double> tableColumnPreco;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initComponents();
		initializeNodes();
	}

	private void initComponents() {
		arrowPedidos.setVisible(true);
	}

	private void initializeNodes() {
		tableColumnData.setCellValueFactory(new PropertyValueFactory<>("Data"));
		tableColumnCliente.setCellValueFactory(new PropertyValueFactory<>("Cliente"));
		tableColumnPedido.setCellValueFactory(new PropertyValueFactory<>("Pedido"));
		tableColumnPreco.setCellValueFactory(new PropertyValueFactory<>("Preco"));
	}
	
	
}
