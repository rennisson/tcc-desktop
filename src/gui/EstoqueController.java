package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import model.entities.Ingrediente;

public class EstoqueController implements Initializable {
	
	@FXML
	private AnchorPane parent;
	
	@FXML
	private Rectangle arrowEstoque;
	
	@FXML
	private TableView<Ingrediente> tableViewEstoque;
	
	@FXML
	private TableColumn<Ingrediente, String> tableColumnNome;
	
	@FXML
	private TableColumn<Ingrediente, Integer> tableColumnQuantidade;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initComponents();
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("Nome"));
		tableColumnQuantidade.setCellValueFactory(new PropertyValueFactory<>("Quantidade"));
	}

	private void initComponents() {
		arrowEstoque.setVisible(true);
	}
	
}
