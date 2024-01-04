package application.controller;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ResourceBundle;

import application.dao.UserDAO;
import application.dao.UserDAOImpl;
import application.dto.User;
import application.dto.UserChild;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class UserManagementController implements Initializable{

    @FXML
    private Label labelHome;
    @FXML
    private TableView<UserChild> tableView;

    UserDAO dao = new UserDAOImpl();
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // tableView Data 설정
        Platform.runLater(() -> {
            ObservableList<UserChild> userList = FXCollections.observableArrayList();
            for (UserChild u : dao.userManage()) {
                userList.add(u);
            }
            // 첫 번째 TableColumn에 CheckBox 추가
            TableColumn<UserChild, Boolean> checkBoxColumn = new TableColumn<>("");
            checkBoxColumn.setMinWidth(30);
            checkBoxColumn.setCellValueFactory(new PropertyValueFactory<>("checked"));

            // 체크박스 셀의 내용을 표시하는 방법 지정 (옵션)
            checkBoxColumn.setCellFactory(CheckBoxTableCell.forTableColumn(checkBoxColumn));

            // TableView에 CheckBox 열 추가
            tableView.getColumns().add(0, checkBoxColumn);

            ObservableList<TableColumn<UserChild, ?>> columnList = tableView.getColumns();
            Field[] fields = UserChild.class.getDeclaredFields();
            for (int i=1; i<fields.length; i++) {
                String fieldName = fields[i].getName();
                TableColumn<UserChild, ?> columnName = columnList.get(i);
                columnName.setCellValueFactory(new PropertyValueFactory<>(fieldName));
            }

            tableView.setItems(userList);
        }); // tableView Data 설정

        
        

        // HealthKiosk 버튼 클릭
        labelHome.setOnMouseClicked(e-> {
            Stage stage = null;
            FXMLLoader loader = null;
            Parent homePage = null;
            try {
                stage = new Stage(StageStyle.DECORATED);

                loader = new FXMLLoader(getClass().getResource("/application/fxml/HomePage.fxml"));
                homePage = loader.load();

                stage.setScene(new Scene(homePage));
                stage.setTitle("홈 페이지");
                stage.setResizable(false);
                stage.show();
                Stage oldStage = (Stage)labelHome.getScene().getWindow();
                oldStage.close();

            } catch (IOException e1) {
                e1.printStackTrace();
                return;
            }
        });// HealthKiosk 버튼 클릭


    }
    
}
