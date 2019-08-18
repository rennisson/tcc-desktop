package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.entities.Pedido;

public class PedidoFormController implements Initializable {
	
	private Pedido entidade;
	
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
	
	@FXML
	private void onBtnSalvar() {
		System.out.println("Salvar");
	}
	
	@FXML
	private void onBtnCancelar() {
		System.out.println("Cancelar");
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
