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
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.entities.Cliente;
import model.exceptions.ValidationException;
import model.services.ClienteService;

public class ClienteFormController implements Initializable {
	
	private Cliente entidade;
	
	private ClienteService clienteService;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtCodigo;
	
	@FXML
	private TextField txtNome;
	
	@FXML
	private TextField txtEmail;
	
	@FXML
	private TextField txtTelefone;
	
	@FXML
	private Button btnSalvar;
	
	@FXML
	private Button btnCancelar;
	
	public void setCliente(Cliente entidade) {
		 this.entidade = entidade;
	}
	
	public void setClienteService(ClienteService clienteService) {
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
		
		if (clienteService == null) {
			throw new IllegalStateException("Serviço estava nulo!");
		}
		
		try {
			entidade = getFormData();
			clienteService.saveOrUpdate(entidade);
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

	private Cliente getFormData() {
		Cliente obj = new Cliente();
		
		ValidationException exception = new ValidationException("Erro de validação");
		
		obj.setCodigo(Utils.tryParseToInt(txtCodigo.getText()));
		obj.setNome(txtNome.getText());
		obj.setEmail(txtEmail.getText());
		obj.setTelefone(txtTelefone.getText());
		
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
		Constraints.setTextFieldMaxLength(txtNome, 60);
		Constraints.setTextFieldMaxLength(txtEmail, 60);
		Constraints.setTextFieldMaxLength(txtTelefone, 15);
	}
	
	public void updateFormData() {
		if (entidade == null) {
			throw new IllegalStateException("Entidade nula");
		}
		
		txtCodigo.setText(String.valueOf(entidade.getCodigo()));
		txtNome.setText(entidade.getNome());
		txtEmail.setText(entidade.getEmail());
		txtTelefone.setText(entidade.getTelefone());
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
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
