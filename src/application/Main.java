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
import model.dao.ClienteDao;
import model.dao.DaoFactory;
import model.entities.Cliente;


public class Main extends Application {
	
	Scene scene;
	
	@Override
	public void start(Stage stage) throws Exception {
		try {
			Parent parent = FXMLLoader.load(getClass().getResource("/gui/View.fxml"));
			scene = new Scene(parent);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
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
	
	public static void main(String[] args) {
		launch(args);
		
		ClienteDao clienteDao = DaoFactory.createClienteDao();
		
		Cliente cliente = clienteDao.findById(2);
		System.out.println(cliente);
		
		System.out.println();
		
		cliente.setNome("gabriel hamilton");
		cliente.setEmail("gabrielgh@gmail.com");
		clienteDao.update(cliente);
		System.out.println(cliente);
		
		System.out.println();
		
		System.out.println("\n=== TEST 3: seller findAll =====");
		List<Cliente> list = clienteDao.findAll();
		for (Cliente obj : list) {
			System.out.println(obj);
		}
	}
}
