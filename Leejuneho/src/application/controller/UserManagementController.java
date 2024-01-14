package application.controller;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import application.dao.UserDAO;
import application.dao.UserDAOImpl;
import application.dto.UserChild;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class UserManagementController implements Initializable{

    @FXML
    private Label labelHome;
    @FXML
    private TableView<UserChild> tableView;
    @FXML
    private MenuBar menuBar;
    @FXML
    private TextField searchField;
    
    UserDAO dao = new UserDAOImpl();
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // 회원목록 데이터 메뉴 바
        List<Menu> menus = new ArrayList<>();
        for (int i=0; i < menuBar.getMenus().size(); i++) {
            menus.add(menuBar.getMenus().get(i));
        }
        String menuText = String.format("전체 %02d", dao.countUser());
        menus.get(0).setText(menuText);
        menuText = String.format("활성 %02d", dao.statusUserNum(true));
        menus.get(1).setText(menuText);
        menuText = String.format("비활성 %02d", dao.statusUserNum(false));
        menus.get(2).setText(menuText);
        

        // tableView Data 설정
        Platform.runLater(() -> {
            ObservableList<UserChild> userList = FXCollections.observableArrayList();
            for (UserChild u : dao.userManage()) {
                userList.add(u);
            }
            // 첫 번째 TableColumn에 CheckBox 추가
            TableColumn<UserChild, Boolean> checkBoxColumn = (TableColumn<UserChild, Boolean>)tableView.getColumns().get(0);

            checkBoxColumn.setCellValueFactory(new PropertyValueFactory<>("checked"));

            // 체크박스 셀의 내용을 표시하는 방법 지정 (옵션)
            checkBoxColumn.setCellFactory(CheckBoxTableCell.forTableColumn(checkBoxColumn));

            checkBoxColumn.setEditable(false);

            ObservableList<TableColumn<UserChild, ?>> columnList = tableView.getColumns();
            Field[] fields = UserChild.class.getDeclaredFields();
            for (int i=1; i<fields.length; i++) {
                String fieldName = fields[i].getName();
                TableColumn<UserChild, ?> columnName = columnList.get(i);
                columnName.setCellValueFactory(new PropertyValueFactory<>(fieldName));
            }

            tableView.setItems(userList);
            
            
            
            // 시작일 설정
            

            // 테이블 뷰를 (한번) 클릭 (더블) 클릭
            tableView.setOnMouseClicked(e -> {
                MouseButton btn = e.getButton();
                // 체크 표시
                if (btn == MouseButton.PRIMARY && e.getClickCount() == 1) {
                    UserChild u = tableView.getSelectionModel().getSelectedItem();
                    if (u != null) {
                        u.setChecked(!u.isChecked());

                        tableView.refresh();
                    }
                }

                // 더블 클릭
                if (btn == MouseButton.PRIMARY && e.getClickCount() == 2) {   
                    
                	Stage stage = null;
                    FXMLLoader loader = null;
                    Parent userPage = null;
                    
                    try {
                    	UserChild user = tableView.getSelectionModel().getSelectedItem();
                        stage = new Stage(StageStyle.DECORATED);
        
                        loader = new FXMLLoader(getClass().getResource("/application/fxml/UserPage.fxml"));
                        userPage = loader.load();
                        stage.setScene(new Scene(userPage));
                        stage.setTitle("유저 페이지");
                        stage.setResizable(false);
                        stage.initModality(Modality.APPLICATION_MODAL);

                        UserController con= loader.getController();
                        con.setUserData(user);
                    	
                        stage.show();
        
                    } catch (IOException e1) {
                        e1.printStackTrace();
                        return;
                    }
                }
            });


            // txt 이름으로, 전화번호 뒷자리로 검색
            // 테이블 필터링
            FilteredList<UserChild> filteredList = new FilteredList<>(userList, p -> true);

            searchField.textProperty().addListener((obs, o, n) -> {
                filteredList.setPredicate(user -> {
                    if (n == null || n.isEmpty()) {
                        return true;
                    }
                    if (Pattern.matches("^[0-9]*$", n)) {
                        return user.getUserPhone().contains(n);
                    }
                    return user.getUserName().toLowerCase().contains(n.toLowerCase());
                });
                Platform.runLater(() -> {
                    tableView.setItems(filteredList);
                });
            }); // txt 이름으로, 전화번호 뒷자리로 검색

            // 전체, 활성, 비활성 메뉴 클릭시
            menus.get(0).getItems().get(0).setOnAction(e -> {
                Platform.runLater(() -> {
                    tableView.setItems(userList);
                });
            });

            menus.get(1).getItems().get(0).setOnAction(e -> {
                ObservableList<UserChild> actUserList = userList.filtered((u) -> {
                    return u.getUserStatus().equals("활성화");
                });
                Platform.runLater(() -> {
                    tableView.setItems(actUserList);
                });
            });

            menus.get(2).getItems().get(0).setOnAction(e -> {
                ObservableList<UserChild> inactUserList = userList.filtered((u) -> {
                    return u.getUserStatus().equals("비활성화");
                });
                Platform.runLater(() -> {
                    tableView.setItems(inactUserList);
                });
            }); // 전체, 활성, 비활성 메뉴 클릭시


            // TableCell에 대한 업데이트를 처리하는 셀 팩토리 설정
            checkBoxColumn.setCellFactory(col -> new CheckBoxTableCell<UserChild, Boolean>(){
                @Override
                public void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    
                    if (empty || getIndex() < 0) {
                        setGraphic(null);
                    } else {
                        UserChild user = getTableView().getItems().get(getIndex());
                        CheckBox checkBox = (CheckBox)getGraphic();
                        if (checkBox != null && user != null) {
                            checkBox.setSelected(user.isChecked());
                        }
                    }
                }
            });
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
        }); // HealthKiosk 버튼 클릭
    }
    
}
