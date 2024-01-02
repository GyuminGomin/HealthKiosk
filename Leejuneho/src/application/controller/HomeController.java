package application.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

public class HomeController implements Initializable {

	@FXML
	private ComboBox<String> comboBox;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		ObservableList<String> oldList = comboBox.getItems();
		System.out.println(oldList);
		ObservableList<String> newList = FXCollections.observableArrayList("고객 관리", "출석부");
		comboBox.setItems(newList);

	}

}
