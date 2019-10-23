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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.entities.Ingrediente;
import model.entities.Produto;
import model.exceptions.ValidationException;
import model.services.IngredienteService;
import model.services.ProdutoService;

public class ProdutoFormController implements Initializable {

	private Produto entidade;

	private ProdutoService produtoService;

	private IngredienteService ingredienteService;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtProduto;

	@FXML
	private ComboBox<Ingrediente> comboBoxItens;

	@FXML
	private Button btnAddItem;

	@FXML
	private Button btnExcluirItem;

	@FXML
	private ListView<String> listItens;

	@FXML
	private Button btnSalvar;

	@FXML
	private Button btnCancelar;

	private ObservableList<Ingrediente> obsListItens;

	private ObservableList<String> itens = FXCollections.observableArrayList();

	public void setProduto(Produto entidade) {
		this.entidade = entidade;
	}

	public void setServices(ProdutoService produtoService, IngredienteService ingredienteService) {
		this.produtoService = produtoService;
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

		try {
			entidade = getFormData();
			produtoService.saveOrUpdate(entidade);
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

	private Produto getFormData() {
		Produto obj = new Produto();

		ValidationException exception = new ValidationException("Erro de validação");

		obj.setNome(txtProduto.getText());

		String lista = listItens.getItems().toString();
		obj.setItens(lista);

		if (exception.getErrors().size() > 0) {
			throw exception;
		}
		return obj;
	}

	@FXML
	private void onBtnCancelar(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@FXML
	private void onBtnAddItemAction(ActionEvent event) {
		if (listItens.getItems().contains(comboBoxItens.getSelectionModel().getSelectedItem().getDescricao())) {
			listItens.setItems(itens);
		} else {
			itens.add(comboBoxItens.getSelectionModel().getSelectedItem().getDescricao());
			listItens.setItems(itens);
		}
	}

	@FXML
	private void onBtnExcluirItemAction(ActionEvent event) {
		listItens.getItems().remove(listItens.getSelectionModel().getSelectedIndex());
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		initializeComboBoxItens();
	}

	public void updateFormData() {
		if (entidade == null) {
			throw new IllegalStateException("Entidade nula");
		}

		txtProduto.setText(entidade.getNome());

		if (listItens.getItems().size() > 0) {
			String item = entidade.getItens();
			item = item.substring(1, item.length() - 1);

			String[] lista = item.split(", ");

			itens.addAll(lista);
			listItens.setItems(itens);
		}
	}

	public void loadAssociatedObjects() {
		if (ingredienteService == null) {
			throw new IllegalStateException("ProdutoService estava nulo");
		}

		List<Ingrediente> listItens = ingredienteService.findAll();
		obsListItens = FXCollections.observableArrayList(listItens);
		comboBoxItens.setItems(obsListItens);
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

	}

	private void initializeComboBoxItens() {
		Callback<ListView<Ingrediente>, ListCell<Ingrediente>> factory = lv -> new ListCell<Ingrediente>() {
			@Override
			protected void updateItem(Ingrediente item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getDescricao());
			}
		};
		comboBoxItens.setCellFactory(factory);
		comboBoxItens.setButtonCell(factory.call(null));
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
