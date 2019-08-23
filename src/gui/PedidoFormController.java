package gui;

import java.net.URL;
import java.util.ResourceBundle;

import db.DbException;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.entities.Pedido;
import model.services.PedidoService;

public class PedidoFormController implements Initializable {
	
	private Pedido entidade;
	
	private PedidoService service;
	
	@FXML
	private TextField txtCodigo;
	
	@FXML
	private TextField txtDesc;
	
	@FXML
	private TextField txtQuantidade;
	
	@FXML
	private TextField txtPrecoTotal;
	
	@FXML
	private TextField txtClienteNome;
	
	@FXML
	private TextField txtEmail;
	
	@FXML
	private TextField txtTelefone;
	
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
			Utils.currentStage(event).close();
		}
		catch (DbException e) {
			Alerts.showAlert("Erro ao salvar objeto", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private Pedido getFormData() {
		Pedido obj = new Pedido();
		
		obj.setCodigo(Utils.tryParseToInt(txtCodigo.getText()));
		obj.setNome(txtDesc.getText());
		obj.setQuantidade(Utils.tryParseToInt(txtQuantidade.getText()));
		obj.setPrecoTotal(Utils.tryParseToDouble(txtPrecoTotal.getText()));
		
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
		Constraints.setTextFieldMaxLength(txtEmail, 60);
	}
	
	public void updateFormData() {
		if (entidade == null) {
			throw new IllegalStateException("Entidade nula");
		}
		txtCodigo.setText(String.valueOf(entidade.getCodigo()));
		txtDesc.setText(entidade.getNome());
		txtQuantidade.setText(String.valueOf(entidade.getQuantidade()));
		txtClienteNome.setText(entidade.getCliente().getNome());
		txtEmail.setText(entidade.getCliente().getEmail());
		txtTelefone.setText(entidade.getCliente().getTelefone());
	}
}
