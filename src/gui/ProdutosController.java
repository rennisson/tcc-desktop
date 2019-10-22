package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import model.entities.Pedido;
import model.entities.Produto;

public class ProdutosController implements Initializable {

	@FXML
	private AnchorPane parent;
	
	@FXML
	private Rectangle arrowFinanceiro;
	
	@FXML
	private ComboBox<String> cbAno;
	
	private ObservableList<String> anos = 
		    FXCollections.observableArrayList(
		        "2019",
		        "2020",
		        "2021",
		        "2022",
		        "2023",
		        "2024",
		        "2025",
		        "2026",
		        "2027",
		        "2028",
		        "2029",
		        "2030",
		        "2031");
	
	@FXML
	private ComboBox<String> cbMes;
	
	private ObservableList<String> meses = 
		    FXCollections.observableArrayList(
		        "Janeiro",
		        "Fevereiro",
		        "Março",
		        "Abril",
		        "Maio",
		        "Junho",
		        "Julho",
		        "Agosto",
		        "Setembro",
		        "Outubro",
		        "Novembro",
		        "Dezembro");
	
	@FXML
	private TableView<Pedido> tableViewFinanceiro;
	
	@FXML
	private TableColumn<Produto, String> tableColumnNomePedido;
	
	@FXML
	private TableColumn<Produto, Double> tableColumnPreco;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initComponents();
		initializeNodes();
	}
	
	private void initComponents() {
		arrowFinanceiro.setVisible(true);
		
		cbAno.setValue("Ano");
		cbAno.setItems(anos);
		
		cbMes.setValue("Mês");
		cbMes.setItems(meses);
	}

	private void initializeNodes() {
		tableColumnNomePedido.setCellValueFactory(new PropertyValueFactory<>("Pedido"));
		tableColumnPreco.setCellValueFactory(new PropertyValueFactory<>("Preco"));
	}

}
