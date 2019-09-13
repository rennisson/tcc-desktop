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
import model.entities.Pedido;
import model.entities.Produto;
import model.exceptions.ValidationException;
import model.services.PedidoService;
import model.services.ProdutoService;

public class PedidoFormController implements Initializable {
	
	private Pedido entidade;
	
	private PedidoService service;
	
	private ProdutoService produtoService;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtCodigo;
	
	@FXML
	private TextField txtCliente;
	
	@FXML
	private ComboBox<Produto> comboBoxProduto;
	
	@FXML
	private TextField txtDesc;
	
	@FXML
	private Label labelErrorDesc;
	
	@FXML
	private TextField txtQuantidade;
	
	@FXML
	private TextField txtPrecoTotal;
	
	@FXML
	private Button btnSalvar;
	
	@FXML
	private Button btnCancelar;
	
	private ObservableList<Produto> obsList;
	
	public void setPedido(Pedido entidade) {
		 this.entidade = entidade;
	}
	
	public void setServices(PedidoService service, ProdutoService produtoService) {
		this.service = service;
		this.produtoService = produtoService;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	@FXML
	private void onBtnSalvar(ActionEvent event) {
		if (entidade == null) {
			throw new IllegalStateException("Entidade estava nula!");
		}
		
		if (service == null) {
			throw new IllegalStateException("Serviço estava nulo!");
		}
		
		try {
			entidade = getFormData();
			service.saveOrUpdate(entidade);
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
		
		ValidationException exception = new ValidationException("Erro de validação");
		
		obj.setCodigo(Utils.tryParseToInt(txtCodigo.getText()));
		
		if (txtDesc.getText() == null || txtDesc.getText().trim().equals("")) {
			exception.addError("Descrição", "O campo não pode ser vazio!");
		}
		
		obj.getCliente().setCodigo(Utils.tryParseToInt(txtCliente.getText()));
		obj.setNome(txtDesc.getText());
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
	}
	
	public void updateFormData() {
		if (entidade == null) {
			throw new IllegalStateException("Entidade nula");
		}
		txtCliente.setText(String.valueOf(entidade.getCliente()));
		txtCodigo.setText(String.valueOf(entidade.getCodigo()));
		//txtDesc.setText(entidade.getNome());
		txtQuantidade.setText(String.valueOf(entidade.getQuantidade()));
		txtPrecoTotal.setText(String.valueOf(entidade.getPrecoTotal()));
		
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
		List<Produto> list = produtoService.findAll();
		obsList = FXCollections.observableArrayList(list);
		comboBoxProduto.setItems(obsList);
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		if (fields.contains("Descrição")) {
			labelErrorDesc.setText(errors.get("Descrição"));
		}
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
