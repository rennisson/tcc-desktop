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
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.entities.Pedido;
import model.services.PedidoService;

public class PedidosController implements Initializable, DataChangeListener {

	private PedidoService service;

	@FXML
	private AnchorPane parent;

	@FXML
	private Rectangle arrowPedidos;

	@FXML
	private Button btnNovoPedido;

	@FXML
	private TableView<Pedido> tableViewPedidos;

	@FXML
	private TableColumn<Pedido, Integer> tableColumnCodigo;

	@FXML
	private TableColumn<Pedido, String> tableColumnCliente;

	@FXML
	private TableColumn<Pedido, String> tableColumnPedido;

	@FXML
	private TableColumn<Pedido, Integer> tableColumnQuantidade;

	@FXML
	private TableColumn<Pedido, Double> tableColumnPreco;

	@FXML
	private TableColumn<Pedido, Pedido> tableColumnEDITAR;

	@FXML
	private TableColumn<Pedido, Pedido> tableColumnEXCLUIR;

	private ObservableList<Pedido> obsList;

	public void setPedidoService(PedidoService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initComponents();
		initializeNodes();
	}

	private void initComponents() {
		arrowPedidos.setVisible(true);
	}

	private void initializeNodes() {
		tableColumnCodigo.setCellValueFactory(new PropertyValueFactory<>("Codigo"));
		tableColumnCliente
				.setCellValueFactory((param) -> new SimpleStringProperty(param.getValue().getCliente().getNome()));
		tableColumnPedido.setCellValueFactory(new PropertyValueFactory<>("Nome"));
		tableColumnQuantidade.setCellValueFactory(new PropertyValueFactory<>("Quantidade"));
		tableColumnPreco.setCellValueFactory(new PropertyValueFactory<>("precoTotal"));
	}

	private void initEditButtons() {
		tableColumnEDITAR.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDITAR.setCellFactory(param -> new TableCell<Pedido, Pedido>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Pedido obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createDialogForm(obj, "/gui/PedidoForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	public void initRemoveButtons() {
		tableColumnEXCLUIR.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEXCLUIR.setCellFactory(param -> new TableCell<Pedido, Pedido>() {
			private final Button button = new Button("Excluir");

			@Override
			protected void updateItem(Pedido obj, boolean empty) {
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

	private void removeEntity(Pedido obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirma��o", "Tem certeza que quer deletar?");
		
		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Servi�o estava nulo!");
			}
			try {
				service.remove(obj);
				updateTableView();
			}
			catch (DbIntegrityException e) {
				Alerts.showAlert("Erro ao remover objeto", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service estava nulo");
		}
		List<Pedido> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewPedidos.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	private void createDialogForm(Pedido obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			PedidoFormController controller = loader.getController();
			controller.setPedido(obj);
			controller.setPedidoService(new PedidoService());
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("DETALHES DO PEDIDO");
			dialogStage.initStyle(StageStyle.UNDECORATED);
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			Alerts.showAlert("IOException", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}
}
