package application;
	
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import gui.FXResizeHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.dao.ClienteDao;
import model.dao.DaoFactory;
import model.dao.EnderecoDao;
import model.entities.Cliente;
import model.entities.Endereco;


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
		ClienteDao clienteDao = DaoFactory.createClienteDao();
		
		Cliente cliente = clienteDao.findById(2);
		
		//Endereco newEnd = new Endereco("54321", "lago", "sp", "sao paulo", "pq do lago", "", "13", cliente);
		//enderecoDao.insert(newEnd);
		
		Endereco endereco = enderecoDao.findById(2);
		System.out.println(endereco);
		
		System.out.println("\n=== TEST 3: seller findAll =====");
		List<Endereco> list = enderecoDao.findAll();
		for (Endereco obj : list) {
			System.out.println(obj);
		}
		Scanner sc = new Scanner(System.in);
		System.out.println("\n=== TEST 6: seller delete =====");
		System.out.println("Enter id for delete test: ");
		int id = sc.nextInt();
		enderecoDao.deleteById(id);
		System.out.println("Delete completed");
		
		sc.close();
	}
}
