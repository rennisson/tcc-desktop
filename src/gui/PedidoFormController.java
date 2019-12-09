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
import model.entities.Endereco;
import model.entities.Pedido;
import model.entities.Produto;
import model.exceptions.ValidationException;
import model.services.EnderecoService;
import model.services.PedidoService;
import model.services.ProdutoService;

public class PedidoFormController implements Initializable {

	private Pedido entidade;
	
	private Endereco entidadeEndereco;

	private PedidoService pedidoService;
	
	private ProdutoService produtoService;

	private EnderecoService enderecoService;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private Label labelTitulo;

	@FXML
	private TextField txtCodigo;

	@FXML
	private ComboBox<Produto> comboBoxProduto;
	
	@FXML
	private TextField txtCliente;
	
	@FXML
	private Label labelErrorCliente;
	
	@FXML
	private TextField txtTelefone;
	
	@FXML
	private Label labelErrorTelefone;

	@FXML
	private TextField txtQuantidade;

	@FXML
	private Label labelErrorQuantidade;
	
	@FXML
	private ComboBox<String> comboBoxStatus;

	@FXML
	private TextField txtPrecoTotal;

	@FXML
	private Label labelErrorPrecoTotal;
	
	@FXML
	private TextField txtCodigoEndereco;
	
	@FXML
	private TextField txtCEP;
	
	@FXML
	private Label labelErrorCEP;
	
	@FXML
	private TextField txtRua;
	
	@FXML
	private Label labelErrorRua;
	
	@FXML
	private TextField txtNumero;
	
	@FXML
	private Label labelErrorNumero;
	
	@FXML
	private TextField txtComplemento;
	
	@FXML
	private Label labelErrorComplemento;
	
	@FXML
	private TextField txtBairro;
	
	@FXML
	private Label labelErrorBairro;
	
	@FXML
	private TextField txtCidade;
	
	@FXML
	private Label labelErrorCidade;
	
	@FXML
	private TextField txtEstado;
	
	@FXML
	private Label labelErrorEstado;

	@FXML
	private Button btnSalvar;

	@FXML
	private Button btnCancelar;

	private ObservableList<Produto> obsListProduto;

	public void setPedido(Pedido entidade) {
		this.entidade = entidade;
	}

	public void setServices(PedidoService pedidoService, EnderecoService enderecoService, ProdutoService produtoService) {
		this.pedidoService = pedidoService;
		this.enderecoService = enderecoService;
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

		if (pedidoService == null) {
			throw new IllegalStateException("Serviço estava nulo!");
		}
		
		if (enderecoService == null) {
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
			e.printStackTrace();
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
		
		boolean x = false;

		ValidationException exception = new ValidationException("Erro de validação");

		obj.setCodigo(Utils.tryParseToInt(txtCodigo.getText()));
		
		obj.setProduto(comboBoxProduto.getValue());
		
		if (txtCliente.getText() == null || txtCliente.getText().trim().equals("")) {
			exception.addError("cliente", "Campo não pode ser vazio!");
		}
		obj.setCliente(txtCliente.getText());
		
		if (txtQuantidade.getText() == null || txtQuantidade.getText().trim().equals("")) {
			exception.addError("quantidade", "Campo não pode ser vazio!");
		}
		obj.setQuantidade(Utils.tryParseToInt(txtQuantidade.getText()));
		
		if (txtTelefone.getText() == null || txtTelefone.getText().trim().equals("")) {
			exception.addError("telefone", "Campo não pode ser vazio!");
		}
		obj.setTelefone(txtTelefone.getText());
		
		obj.setStatus(comboBoxStatus.getSelectionModel().getSelectedItem().toString());

		if (txtPrecoTotal.getText() == null || txtPrecoTotal.getText().trim().equals("")) {
			exception.addError("precoTotal", "Campo não pode ser vazio!");
		}
		obj.setPrecoTotal(Utils.tryParseToDouble(txtPrecoTotal.getText()));
		
		if (txtCEP.getText() == null || txtCEP.getText().trim().equals("")) {
			exception.addError("cep", "Campo não pode ser vazio!");
			x = true;
		} if (txtRua.getText() == null || txtRua.getText().trim().equals("")) {
			exception.addError("rua", "Campo não pode ser vazio!");
			x = true;
		} if (txtNumero.getText() == null || txtNumero.getText().trim().equals("")) {
			exception.addError("numero", "Campo não pode ser vazio!");
			x = true;
		} if (txtBairro.getText() == null || txtBairro.getText().trim().equals("")) {
			exception.addError("bairro", "Campo não pode ser vazio!");
			x = true;
		} if (txtCidade.getText() == null || txtCidade.getText().trim().equals("")) {
			exception.addError("cidade", "Campo não pode ser vazio!");
			x = true;
		} if (txtEstado.getText() == null || txtEstado.getText().trim().equals("")) {
			exception.addError("estado", "Campo não pode ser vazio!");
			x = true;
		}
		
		if (x == false) {
			if (txtCodigoEndereco.getText().isBlank()) {
				entidadeEndereco = enderecoService.saveOrUpdate(getFormEnderecoData());
				obj.setEndereco(entidadeEndereco);
			}	
			else if (Integer.parseInt(txtCodigoEndereco.getText()) > 1) {
				entidadeEndereco = enderecoService.findById(Integer.parseInt(txtCodigoEndereco.getText()));
				Endereco endereco = new Endereco();
				
				endereco.setCodigo(Integer.parseInt(txtCodigoEndereco.getText()));
				endereco.setCep(txtCEP.getText());
				endereco.setRua(txtRua.getText());
				endereco.setNumero(txtNumero.getText());
				endereco.setBairro(txtBairro.getText());
				endereco.setCidade(txtCidade.getText());
				endereco.setEstado(txtEstado.getText());
				endereco.setComplemento(txtComplemento.getText());
				
				enderecoService.update(endereco);
			}
		}

		if (exception.getErrors().size() > 0) {
			throw exception;
		}

		return obj;
	}

	private Endereco getFormEnderecoData() {
		Endereco endereco = instantiateEndereco(txtCEP.getText(), txtRua.getText(), txtNumero.getText(), txtComplemento.getText(),
				txtBairro.getText(), txtCidade.getText(), txtEstado.getText());
		
		return endereco;
	}
	
	private Endereco instantiateEndereco(String cep, String rua, String numero, String complemento, String bairro,
			String cidade, String estado) {
		Endereco endereco = new Endereco();
		endereco.setCep(cep);
		endereco.setRua(rua);
		endereco.setNumero(numero);
		endereco.setComplemento(complemento);
		endereco.setBairro(bairro);
		endereco.setCidade(cidade);
		endereco.setEstado(estado);
		return endereco;
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
		Constraints.setTextFieldDouble(txtPrecoTotal);
		initializeComboBoxProduto();
		initializeComboBoxStatus();
		txtCodigo.setVisible(false);
		txtCodigoEndereco.setVisible(false);
	}

	public void updateFormData() {
		if (entidade == null) {
			throw new IllegalStateException("Entidade nula");
		}

		if (entidade.getEndereco() != null) {
			txtCodigoEndereco.setText(String.valueOf(entidade.getEndereco().getCodigo()));
			txtCEP.setText(String.valueOf(entidade.getEndereco().getCep()));
			txtRua.setText(String.valueOf(entidade.getEndereco().getRua()));
			txtNumero.setText(String.valueOf(entidade.getEndereco().getNumero()));
			txtComplemento.setText(String.valueOf(entidade.getEndereco().getComplemento()));
			txtBairro.setText(String.valueOf(entidade.getEndereco().getBairro()));
			txtCidade.setText(String.valueOf(entidade.getEndereco().getCidade()));
			txtEstado.setText(String.valueOf(entidade.getEndereco().getEstado()));
		}

		txtCodigo.setText(String.valueOf(entidade.getCodigo()));
		txtCliente.setText(entidade.getCliente());
		txtTelefone.setText(entidade.getTelefone());
		txtQuantidade.setText(String.valueOf(entidade.getQuantidade()));
		txtPrecoTotal.setText(String.valueOf(entidade.getPrecoTotal()));

		if (entidade.getNome() == null) {
			comboBoxProduto.getSelectionModel().selectFirst();
		} else {
			comboBoxProduto.setValue(entidade.getProduto());
		}
		
		if (entidade.getStatus() == null) {
			comboBoxStatus.getSelectionModel().selectFirst();
		} else {
			comboBoxStatus.setValue(entidade.getStatus());
		}
	}
	
	public void setTitulo(String titulo) {
		labelTitulo.setText(titulo);
	}

	public void loadAssociatedObjects() {
		if (enderecoService == null) {
			throw new IllegalStateException("ProdutoService estava nulo");
		}

		List<Produto> listProduto = produtoService.findAll();
		obsListProduto = FXCollections.observableArrayList(listProduto);
		comboBoxProduto.setItems(obsListProduto);
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		if (fields.contains("cliente")) {
			labelErrorCliente.setText(errors.get("cliente"));
		} else {
			labelErrorCliente.setText("");
		}
		
		if (fields.contains("telefone")) {
			labelErrorTelefone.setText(errors.get("telefone"));
		} else {
			labelErrorTelefone.setText("");
		}
		
		if (fields.contains("quantidade")) {
			labelErrorQuantidade.setText(errors.get("quantidade"));
		} else {
			labelErrorQuantidade.setText("");
		}
		
		if (fields.contains("precoTotal")) {
			labelErrorPrecoTotal.setText(errors.get("precoTotal"));
		} else {
			labelErrorPrecoTotal.setText("");
		}
		
		if (fields.contains("cep")) {
			labelErrorCEP.setText(errors.get("cep"));
		} else {
			labelErrorCEP.setText("");
		}
		
		if (fields.contains("rua")) {
			labelErrorRua.setText(errors.get("rua"));
		} else {
			labelErrorRua.setText("");
		}
		
		if (fields.contains("numero")) {
			labelErrorNumero.setText(errors.get("numero"));
		} else {
			labelErrorNumero.setText("");
		}
		
		if (fields.contains("complemento")) {
			labelErrorComplemento.setText(errors.get("complemento"));
		} else {
			labelErrorComplemento.setText("");
		}
		
		if (fields.contains("bairro")) {
			labelErrorBairro.setText(errors.get("bairro"));
		} else {
			labelErrorBairro.setText("");
		}
		
		if (fields.contains("cidade")) {
			labelErrorCidade.setText(errors.get("cidade"));
		} else {
			labelErrorCidade.setText("");
		}
		
		if (fields.contains("estado")) {
			labelErrorEstado.setText(errors.get("estado"));
		} else {
			labelErrorEstado.setText("");
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
	
	private void initializeComboBoxStatus() {
		ObservableList<String> status = FXCollections.observableArrayList("PENDENTE", "CONCLUIDO");
		comboBoxStatus.setItems(status);
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
