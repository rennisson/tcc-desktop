package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.entities.Cliente;
import model.entities.Pedido;
import model.entities.Produto;
import model.exceptions.ValidationException;
import model.services.ClienteService;
import model.services.PedidoService;
import model.services.ProdutoService;

public class PedidoFormController implements Initializable {
	
	private Pedido entidade;
	
	private PedidoService pedidoService;
	
	private ProdutoService produtoService;
	
	private ClienteService clienteService;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtCodigo;
	
	@FXML
	private ComboBox<Produto> comboBoxProduto;
	
	@FXML
	private ComboBox<Cliente> comboBoxCliente;
	
	@FXML
	private TextField txtQuantidade;
	
	@FXML
	private TextField txtPrecoTotal;
	
	@FXML
	private Button btnSalvar;
	
	@FXML
	private Button btnCancelar;
	
	private ObservableList<Produto> obsListProduto;
	
	private ObservableList<Cliente> obsListCliente;
	
	public void setPedido(Pedido entidade) {
		 this.entidade = entidade;
	}
	
	public void setServices(PedidoService pedidoService, ProdutoService produtoService, ClienteService clienteService) {
		this.pedidoService = pedidoService;
		this.produtoService = produtoService;
		this.clienteService = clienteService;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	@FXML
	private void onBtnSalvar(ActionEvent event) {
		if (entidade == null) {
			throw new IllegalStateException("Entidade estava nula!");
		}
		
		if (pedidoService == null) {
			throw new IllegalStateException("Servi�o estava nulo!");
		}
		
		try {
			entidade = getFormData();
			pedidoService.saveOrUpdate(entidade);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		}
		catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		}
		catch (DbException e) {
			Alerts.showAlert("Erro ao salvar objeto", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	private Pedido getFormData() {
		Pedido obj = new Pedido();
		
		ValidationException exception = new ValidationException("Erro de valida��o");
		
		obj.setCodigo(Utils.tryParseToInt(txtCodigo.getText()));
		
		obj.setCliente(comboBoxCliente.getValue());
		obj.setProduto(comboBoxProduto.getValue());
		obj.setQuantidade(Utils.tryParseToInt(txtQuantidade.getText()));
		obj.setPrecoTotal(Utils.tryParseToDouble(txtPrecoTotal.getText()));
		
		if (exception.getErrors().size() > 0) {
			throw exception;
		}
		
		return obj;
	}

	@FXML
	private void onBtnCancelar(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtQuantidade);
		initializeComboBoxProduto();
		initializeComboBoxCliente();
	}
	
	public void updateFormData() {
		if (entidade == null) {
			throw new IllegalStateException("Entidade nula");
		}
		txtCodigo.setText(String.valueOf(entidade.getCodigo()));
		txtQuantidade.setText(String.valueOf(entidade.getQuantidade()));
		txtPrecoTotal.setText(String.valueOf(entidade.getPrecoTotal()));
		
		if (entidade.getCliente() == null) {
			comboBoxCliente.getSelectionModel().selectFirst();
		} else {
			comboBoxCliente.setValue(entidade.getCliente());
		}
		
		if (entidade.getProduto() == null) {
			comboBoxProduto.getSelectionModel().selectFirst();
		} else {
			comboBoxProduto.setValue(entidade.getProduto());
		}
	}
	
	public void loadAssociatedObjects() {
		if (produtoService == null) {
			throw new IllegalStateException("ProdutoService estava nulo");
		}
		
		if (clienteService == null) {
			throw new IllegalStateException("PedidoService estava nulo");
		}
		List<Produto> listProduto = produtoService.findAll();
		obsListProduto = FXCollections.observableArrayList(listProduto);
		comboBoxProduto.setItems(obsListProduto);
		
		List<Cliente> listCliente = clienteService.findAll();
		obsListCliente = FXCollections.observableArrayList(listCliente);
		comboBoxCliente.setItems(obsListCliente);
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
	}
	
	private void initializeComboBoxCliente() {
		Callback<ListView<Cliente>, ListCell<Cliente>> factory = lv -> new ListCell<Cliente>() {
			@Override
			protected void updateItem(Cliente item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : String.valueOf(item.getCodigo()));
			}
		};
		comboBoxCliente.setCellFactory(factory);
		comboBoxCliente.setButtonCell(factory.call(null));
	}
	
	private void initializeComboBoxProduto() {
		Callback<ListView<Produto>, ListCell<Produto>> factory = lv -> new ListCell<Produto>() {
			@Override
			protected void updateItem(Produto item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNome());
			}
		};
		comboBoxProduto.setCellFactory(factory);
		comboBoxProduto.setButtonCell(factory.call(null));
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
	
	@FXML
	public void close(MouseEvent event) {
		Stage s = (Stage) ((Node) event.getSource()).getScene().getWindow();
		s.close();
	}
}
