package application;
	
import java.io.IOException;
import java.sql.Connection;

import db.DB;
import gui.FXResizeHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;


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
			FXResizeHelper listener = new FXResizeHelper(stage, 0, 10);
			stage.centerOnScreen();
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
		
		Connection conn = DB.getConnection();
		DB.closeConnection();
	}
}
