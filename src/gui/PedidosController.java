package gui;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import model.entities.Cliente;
import model.entities.Pedido;
import model.services.PedidoService;

public class PedidosController implements Initializable {
	
	private PedidoService service;
	
	@FXML
	private AnchorPane parent;
	
	@FXML
	private Rectangle arrowPedidos;
	
	@FXML
	private TableView<Pedido> tableViewPedidos;
	
	@FXML
	private TableColumn<Pedido, Integer> tableColumnCodigo;
	
	@FXML
	private TableColumn<Cliente, String> tableColumnCliente;
	
	@FXML
	private TableColumn<Pedido, String> tableColumnPedido;
	
	@FXML
	private TableColumn<Pedido, Integer> tableColumnQuantidade;
	
	@FXML
	private TableColumn<Pedido, Double> tableColumnPreco;
	
	private ObservableList<Pedido> obsList;
	
	public void setPedidoService(PedidoService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initComponents();
		initializeNodes();
	}

	private void initComponents() {
		arrowPedidos.setVisible(true);
	}

	private void initializeNodes() {
		tableColumnCodigo.setCellValueFactory(new PropertyValueFactory<>("Codigo"));
		tableColumnCliente.setCellValueFactory(new PropertyValueFactory<>("Cliente"));
		tableColumnPedido.setCellValueFactory(new PropertyValueFactory<>("Nome"));
		tableColumnQuantidade.setCellValueFactory(new PropertyValueFactory<>("Quantidade"));
		tableColumnPreco.setCellValueFactory(new PropertyValueFactory<>("precoTotal"));
	}
	
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service estava nulo");
		}
		List<Pedido> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewPedidos.setItems(obsList);
	}
}
