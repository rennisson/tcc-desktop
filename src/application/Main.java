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
import model.entities.Cliente;


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
	}
}
