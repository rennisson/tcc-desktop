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
import model.entities.Ingrediente;
import model.services.IngredienteService;

public class ItensController implements Initializable, DataChangeListener {
	
	private IngredienteService service;
	
	@FXML
	private AnchorPane parent;
	
	@FXML
	private Rectangle arrowEstoque;
	
	@FXML
	private TableView<Ingrediente> tableViewEstoque;
	
	@FXML
	private TableColumn<Ingrediente, String> tableColumnNome;
	
	@FXML
	private TableColumn<Ingrediente, Integer> tableColumnQuantidade;
	
	@FXML
	private TableColumn<Ingrediente, Double> tableColumnPreco;
	
	@FXML
	private TableColumn<Ingrediente, Integer> tableColumnPeso;
	
	@FXML
	private TableColumn<Ingrediente, Ingrediente> tableColumnEDITAR;
	
	@FXML
	private TableColumn<Ingrediente, Ingrediente> tableColumnEXCLUIR;
	
	@FXML
	private Button btnNovoItem;
	
	@FXML
	private Button btnLimparPesquisa;

	@FXML
	private TextField txtFiltroEstoque;
	
	private ObservableList<Ingrediente> obsList;
	
	public void setIngredienteService(IngredienteService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeNodes();
	}

	private void initializeNodes() {		
		arrowEstoque.setVisible(true);
		
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("descricao"));
		tableColumnQuantidade.setCellValueFactory(new PropertyValueFactory<>("Quantidade"));
		tableColumnPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
		tableColumnPeso.setCellValueFactory(new PropertyValueFactory<>("Peso"));
	}
	
	private void initEditButtons() {
		tableColumnEDITAR.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDITAR.setCellFactory(param -> new TableCell<Ingrediente, Ingrediente>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Ingrediente obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createDialogForm(obj, "/gui/ItensForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	public void initRemoveButtons() {
		tableColumnEXCLUIR.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEXCLUIR.setCellFactory(param -> new TableCell<Ingrediente, Ingrediente>() {
			private final Button button = new Button("Excluir");

			@Override
			protected void updateItem(Ingrediente obj, boolean empty) {
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
	
	private void removeEntity(Ingrediente obj) {
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
	private void onBtnNovoItemAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Ingrediente obj = new Ingrediente();
		createDialogForm(obj, "/gui/ItensForm.fxml", parentStage);
	}
	
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service estava nulo");
		}
		List<Ingrediente> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewEstoque.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}
	
	private void createDialogForm(Ingrediente obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			ItensFormController controller = loader.getController();
			controller.setIngrediente(obj);
			controller.setIngredienteService(new IngredienteService());
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("NOVO ITEM");
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
		txtFiltroEstoque.clear();
		updateTableView();
	}

	@FXML
	private void ontTxtFiltroEstoqueKeyPressed(KeyEvent e) {
		if (txtFiltroEstoque.getText().isBlank()) {
			updateTableView();
		}
		
		int id = 0;
		boolean x = false;
		
		try {
			id = Integer.parseInt(txtFiltroEstoque.getText() + e.getText());
			x = true;
		}
		catch (NumberFormatException ex) {
		}
		
		if (x == false) {
			try {
				List<Ingrediente> ingrediente = service.findByNome(txtFiltroEstoque.getText() + e.getText());
				obsList = FXCollections.observableArrayList(ingrediente);
				tableViewEstoque.setItems(obsList);
				initEditButtons();
				initRemoveButtons();
			} catch (NullPointerException ex) {
				ex.getMessage();
			} catch (NumberFormatException ex) {
				ex.getMessage();
			}
		}
		else if (x == true) {
			try {
				List<Ingrediente> ingrediente = service.findById(id);
				obsList = FXCollections.observableArrayList(ingrediente);
				tableViewEstoque.setItems(obsList);
				initEditButtons();
				initRemoveButtons();
			} catch (NullPointerException ex) {
				ex.getMessage();
			} catch (NumberFormatException ex) {
				ex.getMessage();
			}
		}
	}
	
}
