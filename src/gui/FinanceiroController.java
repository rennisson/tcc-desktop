package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

public class FinanceiroController implements Initializable {

	@FXML
	AnchorPane parent;
	
	@FXML
	private Rectangle arrowFinanceiro;
	
	@FXML
	ComboBox<String> cbAno;
	
	ObservableList<String> anos = 
		    FXCollections.observableArrayList(
		        "2019",
		        "2020",
		        "2021",
		        "2022",
		        "2023",
		        "2024",
		        "2025",
		        "2026",
		        "2027",
		        "2028",
		        "2029",
		        "2030",
		        "2031");
	
	@FXML
	ComboBox<String> cbMes;
	
	ObservableList<String> meses = 
		    FXCollections.observableArrayList(
		        "Janeiro",
		        "Fevereiro",
		        "Março",
		        "Abril",
		        "Maio",
		        "Junho",
		        "Julho",
		        "Agosto",
		        "Setembro",
		        "Outubro",
		        "Novembro",
		        "Dezembro");

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		arrowFinanceiro.setVisible(true);
		
		cbAno.setValue("Ano");
		cbAno.setItems(anos);
		
		cbMes.setValue("Mês");
		cbMes.setItems(meses);
	}
	
}
