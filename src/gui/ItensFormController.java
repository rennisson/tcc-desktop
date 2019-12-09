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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.entities.Ingrediente;
import model.exceptions.ValidationException;
import model.services.IngredienteService;

public class ItensFormController implements Initializable {
	
	private Ingrediente entidade;
	
	private IngredienteService ingredienteService;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private Label labelTitulo;
	
	@FXML
	private TextField txtCodigo;
	
	@FXML
	private TextField txtNome;
	
	@FXML
	private Label labelErrorNome;
	
	@FXML
	private TextField txtPeso;
	
	@FXML
	private Label labelErrorPeso;
	
	@FXML
	private TextField txtQuantidade;
	
	@FXML
	private Label labelErrorQuantidade;
	
	@FXML
	private TextField txtPreco;
	
	@FXML
	private Label labelErrorPreco;
	
	@FXML
	private Button btnSalvar;
	
	@FXML
	private Button btnCancelar;
	
	public void setIngrediente(Ingrediente entidade) {
		 this.entidade = entidade;
	}
	
	public void setIngredienteService(IngredienteService ingredienteService) {
		this.ingredienteService = ingredienteService;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	@FXML
	private void onBtnSalvar(ActionEvent event) {
		if (entidade == null) {
			throw new IllegalStateException("Entidade estava nula!");
		}
		
		if (ingredienteService == null) {
			throw new IllegalStateException("Serviço estava nulo!");
		}
		
		try {
			entidade = getFormData();
			ingredienteService.saveOrUpdate(entidade);
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

	private Ingrediente getFormData() {
		Ingrediente obj = new Ingrediente();
		
		ValidationException exception = new ValidationException("Erro de validação");
		
		obj.setId(Utils.tryParseToInt(txtCodigo.getText()));
		
		if (txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			exception.addError("nome", "Campo não pode ser vazio!");
		}
		obj.setDescricao(txtNome.getText());
		
		if (txtPeso.getText() == null || txtPeso.getText().trim().equals("")) {
			exception.addError("peso", "Campo não pode ser vazio!");
		}
		obj.setPeso(Utils.tryParseToInt(txtPeso.getText()));
		
		if (txtQuantidade.getText() == null || txtQuantidade.getText().trim().equals("")) {
			exception.addError("quantidade", "Campo não pode ser vazio!");
		}
		obj.setQuantidade(Utils.tryParseToInt(txtQuantidade.getText()));
		
		if (txtPreco.getText() == null || txtPreco.getText().trim().equals("")) {
			exception.addError("preco", "Campo não pode ser vazio!");
		}
		obj.setPreco(Utils.tryParseToDouble(txtPreco.getText()));
		
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
		Constraints.setTextFieldInteger(txtPeso);
		Constraints.setTextFieldDouble(txtPreco);
		txtCodigo.setVisible(false);
	}
	
	public void updateFormData() {
		if (entidade == null) {
			throw new IllegalStateException("Entidade nula");
		}
		
		txtCodigo.setText(String.valueOf(entidade.getId()));		
		txtNome.setText(entidade.getDescricao());	
		txtPeso.setText(String.valueOf(entidade.getPeso()));
		txtQuantidade.setText(String.valueOf(entidade.getQuantidade()));
		txtPreco.setText(String.valueOf(entidade.getPreco()));
	}
	
	public void setTitulo(String titulo) {
		labelTitulo.setText(titulo);
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		if (fields.contains("nome")) {
			labelErrorNome.setText(errors.get("nome"));
		} else {
			labelErrorNome.setText("");
		}
		
		if (fields.contains("preco")) {
			labelErrorPreco.setText(errors.get("preco"));
		} else {
			labelErrorPreco.setText("");
		}
		
		if (fields.contains("peso")) {
			labelErrorPeso.setText(errors.get("peso"));
		} else {
			labelErrorPeso.setText("");
		}
		
		if (fields.contains("quantidade")) {
			labelErrorQuantidade.setText(errors.get("quantidade"));
		} else {
			labelErrorQuantidade.setText("");
		}
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
