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
import model.entities.Produto;
import model.services.IngredienteService;
import model.services.ProdutoService;

public class ProdutosController implements Initializable, DataChangeListener {
	
	private ProdutoService service;

	@FXML
	private AnchorPane parent;
	
	@FXML
	private Rectangle arrowFinanceiro;
	
	@FXML
	private TableView<Produto> tableViewProdutos;
	
	@FXML
	private TableColumn<Produto, String> tableColumnProduto;
	
	@FXML
	private TableColumn<Produto, Produto> tableColumnEDITAR;
	
	@FXML
	private TableColumn<Produto, Produto> tableColumnEXCLUIR;
	
	@FXML
	private Button btnNovoProduto;
	
	@FXML
	private Button btnLimpar;
	
	@FXML
	private TextField txtFiltroProdutos;
	
	private ObservableList<Produto> obsList;
	
	public void setProdutoService(ProdutoService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeNodes();
	}

	private void initializeNodes() {
		arrowFinanceiro.setVisible(true);
		tableColumnProduto.setCellValueFactory(new PropertyValueFactory<>("nome"));
	}
	
	private void initEditButtons() {
		tableColumnEDITAR.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDITAR.setCellFactory(param -> new TableCell<Produto, Produto>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Produto obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createDialogForm(obj, "/gui/ProdutoForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	public void initRemoveButtons() {
		tableColumnEXCLUIR.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEXCLUIR.setCellFactory(param -> new TableCell<Produto, Produto>() {
			private final Button button = new Button("Excluir");

			@Override
			protected void updateItem(Produto obj, boolean empty) {
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
	
	private void removeEntity(Produto obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Tem certeza que quer deletar?");
		
		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Serviço estava nulo!");
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
	
	@FXML
	private void onBtnNovoProdutoAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Produto obj = new Produto();
		createDialogForm(obj, "/gui/ProdutoForm.fxml", parentStage);
	}
	
	@FXML
	private void ontTxtFiltroProdutosKeyPressed(KeyEvent e) {
		if (txtFiltroProdutos.getText().isBlank()) {
			updateTableView();
		}
		
		try {
			List<Produto> pedidos = service.findByName(txtFiltroProdutos.getText() + e.getText());
			obsList = FXCollections.observableArrayList(pedidos);
			tableViewProdutos.setItems(obsList);
			initEditButtons();
			initRemoveButtons();
		} catch (NullPointerException ex) {
			ex.getMessage();
		} catch (NumberFormatException ex) {
			ex.getMessage();
		}
	}
	
	@FXML
	private void onBtnLimparPesquisaAction() {
		txtFiltroProdutos.clear();
		updateTableView();
	}
	
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service estava nulo");
		}
		
		List<Produto> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewProdutos.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}
	
	
	private void createDialogForm(Produto obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			ProdutoFormController controller = loader.getController();
			controller.setProduto(obj);
			controller.setServices(new ProdutoService(), new IngredienteService());
			controller.loadAssociatedObjects();
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
			e.printStackTrace();
			Alerts.showAlert("IOException", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

}
