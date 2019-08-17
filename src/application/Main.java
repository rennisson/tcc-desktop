package application;
	
import java.io.IOException;
import java.util.List;

import gui.FXResizeHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.dao.DaoFactory;
import model.dao.EnderecoDao;
import model.dao.PedidoDao;
import model.entities.Cliente;
import model.entities.Endereco;
import model.entities.Pedido;


public class Main extends Application {
	
	private static Scene mainScene;
	
	@Override
	public void start(Stage stage) throws Exception {
		try {
			Parent parent = FXMLLoader.load(getClass().getResource("/gui/View.fxml"));
			mainScene = new Scene(parent);
			mainScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(mainScene);
			stage.setTitle("Danessa Cakes");
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setMinHeight(500);
			stage.setMinWidth(1200);
			stage.centerOnScreen();
			
			FXResizeHelper listener = new FXResizeHelper(stage, 0, 10);
			
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Scene getMainScene() {
		return mainScene;
	}
	
	public static void main(String[] args) {
		launch(args);
		
		EnderecoDao enderecoDao = DaoFactory.createEnderecoDao();
		System.out.println("\n=== TEST 4: seller insert =====");
		Endereco newEnd = new Endereco("05801-110", "Rua vitalina", "SP", "São Paulo", "Jd. Mirante", "", "11", new Cliente(1, "remis", "remis", "00000"));
		enderecoDao.insert(newEnd);
		System.out.println("Inserted!");
	}
}
