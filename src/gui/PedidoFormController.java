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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Pedido;
import model.exceptions.ValidationException;
import model.services.PedidoService;

public class PedidoFormController implements Initializable {
	
	private Pedido entidade;
	
	private PedidoService service;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtCodigo;
	
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
	
	public void setPedido(Pedido entidade) {
		 this.entidade = entidade;
	}
	
	public void setPedidoService(PedidoService service) {
		this.service = service;
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
		Constraints.setTextFieldMaxLength(txtDesc, 150);
	}
	
	public void updateFormData() {
		if (entidade == null) {
			throw new IllegalStateException("Entidade nula");
		}
		txtCodigo.setText(String.valueOf(entidade.getCodigo()));
		txtDesc.setText(entidade.getNome());
		txtQuantidade.setText(String.valueOf(entidade.getQuantidade()));
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		if (fields.contains("Descrição")) {
			labelErrorDesc.setText(errors.get("Descrição"));
		}
	}
}
