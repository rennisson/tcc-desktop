package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.entities.Cliente;
import model.entities.Pedido;
import model.services.ClienteService;

public class ClienteController implements Initializable, DataChangeListener {

	private ClienteService service;

	@FXML
	private AnchorPane parent;

	@FXML
	private Rectangle arrowCliente;

	@FXML
	private TableView<Cliente> tableViewCliente;

	@FXML
	private TableColumn<Cliente, Integer> tableColumnCodigo;

	@FXML
	private TableColumn<Cliente, String> tableColumnNome;

	@FXML
	private TableColumn<Cliente, String> tableColumnEmail;

	@FXML
	private TableColumn<Cliente, String> tableColumnTelefone;

	@FXML
	private TableColumn<Cliente, Cliente> tableColumnEDITAR;

	@FXML
	private TableColumn<Cliente, Cliente> tableColumnEXCLUIR;

	@FXML
	private Button btnNovoCliente;
	
	@FXML
	private Button btnLimparPesquisa;

	@FXML
	private TextField txtFiltroCliente;

	private ObservableList<Cliente> obsList;

	public void setClienteService(ClienteService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeNodes();
	}

	private void initializeNodes() {
		arrowCliente.setVisible(true);

		tableColumnCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumnTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
	}

	private void initEditButtons() {
		tableColumnEDITAR.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDITAR.setCellFactory(param -> new TableCell<Cliente, Cliente>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Cliente obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createDialogForm(obj, "/gui/ClienteForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	public void initRemoveButtons() {
		tableColumnEXCLUIR.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEXCLUIR.setCellFactory(param -> new TableCell<Cliente, Cliente>() {
			private final Button button = new Button("Excluir");

			@Override
			protected void updateItem(Cliente obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Cliente obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Tem certeza que quer deletar?");

		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Serviço estava nulo!");
			}
			try {
				service.remove(obj);
				updateTableView();
			} catch (DbIntegrityException e) {
				Alerts.showAlert("Erro ao remover objeto", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}

	@FXML
	private void onBtnNovoClienteAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Cliente obj = new Cliente();
		createDialogForm(obj, "/gui/ClienteForm.fxml", parentStage);
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service estava nulo");
		}
		List<Cliente> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewCliente.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	private void createDialogForm(Cliente obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			ClienteFormController controller = loader.getController();
			controller.setCliente(obj);
			controller.setClienteService(new ClienteService());
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("DETALHES DO CLIENTE");
			dialogStage.initStyle(StageStyle.UNDECORATED);
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IOException", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	public void onDataChanged() {
		updateTableView();
	}
	
	@FXML
	private void onBtnLimparPesquisaAction() {
		txtFiltroCliente.clear();
		updateTableView();
	}

	@FXML
	private void ontTxtFiltroClienteKeyPressed(KeyEvent e) {
		if (txtFiltroCliente.getText().isBlank()) {
			updateTableView();
		}
		
		try {
			Cliente cliente = service.findById(Integer.valueOf(txtFiltroCliente.getText() + e.getText()));
			obsList = FXCollections.observableArrayList(cliente);
			tableViewCliente.setItems(obsList);
			initEditButtons();
			initRemoveButtons();
		} catch (NullPointerException ex) {
			ex.getMessage();
		} catch (NumberFormatException ex) {
			ex.getMessage();
		}
	}
}
