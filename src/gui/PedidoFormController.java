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
import javafx.scene.input.KeyEvent;
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
	private TextField txtClienteCodigo;

	@FXML
	private Label labelErrorClienteCodigo;

	@FXML
	private TextField txtClienteNome;

	@FXML
	private TextField txtClienteEmail;

	@FXML
	private TextField txtQuantidade;

	@FXML
	private Label labelErrorQuantidade;

	@FXML
	private TextField txtPrecoTotal;

	@FXML
	private Label labelErrorPrecoTotal;

	@FXML
	private Button btnSalvar;

	@FXML
	private Button btnCancelar;

	private ObservableList<Produto> obsListProduto;

	@FXML
	private void ontTxtClienteKeyPressed(KeyEvent e) {
		try {
			Cliente cliente = clienteService.findById(Integer.valueOf(txtClienteCodigo.getText() + e.getText()));
			txtClienteEmail.setText(cliente.getEmail());
			txtClienteNome.setText(cliente.getNome());
			labelErrorClienteCodigo.setText("");
		} catch (NullPointerException ex) {
			labelErrorClienteCodigo.setText("Cliente requisitado não existe");
			txtClienteEmail.setText("");
			txtClienteNome.setText("");
		} catch (NumberFormatException ex) {
			labelErrorClienteCodigo.setText("Campo não pode ser vazio!");
			txtClienteEmail.setText("");
			txtClienteNome.setText("");
		}
	}

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
			throw new IllegalStateException("Serviço estava nulo!");
		}

		try {
			entidade = getFormData();
			pedidoService.saveOrUpdate(entidade);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		} catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		} catch (DbException e) {
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

		if (txtClienteCodigo.getText().isEmpty()) {
			exception.addError("cliente", "Campo não pode ser vazio!");
		} else {
			labelErrorClienteCodigo.setText("");
			Cliente cliente = instantiateCliente(Integer.valueOf(txtClienteCodigo.getText()), txtClienteNome.getText(),
					txtClienteEmail.getText());
			obj.setCliente(cliente);
			obj.setProduto(comboBoxProduto.getValue());
		}

		if (txtQuantidade.getText().isEmpty()) {
			exception.addError("quantidade", "Campo não pode ser vazio!");
		} else {
			labelErrorQuantidade.setText("");
		}
		obj.setQuantidade(Utils.tryParseToInt(txtQuantidade.getText()));

		if (txtPrecoTotal.getText().isEmpty()) {
			exception.addError("precoTotal", "Campo não pode ser vazio!");
		} else {
			labelErrorPrecoTotal.setText("");
		}
		obj.setPrecoTotal(Utils.tryParseToDouble(txtPrecoTotal.getText()));

		if (exception.getErrors().size() > 0) {
			throw exception;
		}

		return obj;
	}

	private Cliente instantiateCliente(Integer codigo, String nome, String email) {
		Cliente cliente = new Cliente();
		cliente.setCodigo(codigo);
		cliente.setNome(nome);
		cliente.setEmail(email);
		return cliente;
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
		Constraints.setTextFieldInteger(txtClienteCodigo);
		Constraints.setTextFieldDouble(txtPrecoTotal);
		initializeComboBoxProduto();
	}

	public void updateFormData() {
		if (entidade == null) {
			throw new IllegalStateException("Entidade nula");
		}

		if (entidade.getCliente() != null) {
			txtClienteCodigo.setText(String.valueOf(entidade.getCliente().getCodigo()));
			txtClienteNome.setText(String.valueOf(entidade.getCliente().getNome()));
			txtClienteEmail.setText(String.valueOf(entidade.getCliente().getEmail()));
		}

		if (entidade.getCodigo() != null) {
			txtClienteCodigo.setEditable(false);
			txtClienteNome.setEditable(false);
			txtClienteEmail.setEditable(false);
			txtClienteCodigo.setOnKeyPressed(null);
		}

		txtCodigo.setText(String.valueOf(entidade.getCodigo()));
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

		if (clienteService == null) {
			throw new IllegalStateException("PedidoService estava nulo");
		}

		List<Produto> listProduto = produtoService.findAll();
		obsListProduto = FXCollections.observableArrayList(listProduto);
		comboBoxProduto.setItems(obsListProduto);
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		if (fields.contains("cliente")) {
			labelErrorClienteCodigo.setText(errors.get("cliente"));
		}
		
		if (fields.contains("precoTotal")) {
			labelErrorPrecoTotal.setText(errors.get("precoTotal"));
		}
		
		if (fields.contains("quantidade")) {
			labelErrorQuantidade.setText(errors.get("quantidade"));
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
