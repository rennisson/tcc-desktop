package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.services.ClienteService;
import model.services.IngredienteService;
import model.services.PedidoService;
import model.services.ProdutoService;

public class ViewController implements Initializable {

	@FXML
	protected AnchorPane root;

	@FXML
	private BorderPane borderPane;

	@FXML
	private Button btnPedidos;

	@FXML
	private Button btnItens;

	@FXML
	private Button btnProdutos;
	
	@FXML
	private Button btnClientes;

	@FXML
	private ImageView imgMin;

	@FXML
	private ImageView imgMax;

	@FXML
	private ImageView imgClose;

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		loadView("/gui/Pedidos.fxml", (PedidosController controller) -> {
			controller.setPedidoService(new PedidoService());
			controller.updateTableView();
		});
	}

	public void onBtnPedidosAction() throws IOException {
		loadView("/gui/Pedidos.fxml", (PedidosController controller) -> {
			controller.setPedidoService(new PedidoService());
			controller.updateTableView();
		});
	}

	public void onBtnItensAction() throws IOException {
		loadView("/gui/Itens.fxml", (ItensController controller) -> {
			controller.setIngredienteService(new IngredienteService());
			controller.updateTableView();
		});
	}

	public void onBtnProdutosAction() throws IOException {
		loadView("/gui/Produtos.fxml", (ProdutosController controller) -> {
			controller.setProdutoService(new ProdutoService());
			controller.updateTableView();
		});
	}
	
	public void onBtnClientesAction() throws IOException {
		loadView("/gui/Cliente.fxml", (ClienteController controller) -> {
			controller.setClienteService(new ClienteService());
			controller.updateTableView();
		});
	}

	@FXML
	public void min(MouseEvent event) {
		Stage s = (Stage) ((Node) event.getSource()).getScene().getWindow();
		s.setIconified(true);
	}

	@FXML
	public void max(MouseEvent event) {
		Stage s = (Stage) ((Node) event.getSource()).getScene().getWindow();

		if (s.isFullScreen()) {
			s.setFullScreen(false);
		} else {
			s.setFullScreen(true);
		}
	}

	@FXML
	public void close(MouseEvent event) {
		Stage s = (Stage) ((Node) event.getSource()).getScene().getWindow();
		s.close();
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

	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			AnchorPane newAnchor = loader.load();
			root.getChildren().setAll(newAnchor);
			
			T controller = loader.getController();
			initializingAction.accept(controller);
		}
		catch (IOException e) {
			Alerts.showAlert("IOException", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

}
