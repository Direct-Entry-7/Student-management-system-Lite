package lk.ijse.dep7.sms_lite.controller;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.dep7.sms_lite.tm.StudentTM;

import java.sql.*;

public class StudentFormController {
    public TextField txtId;
    public TextField txtName;
    public TextField txtPhoneNumber;
    public ListView lstNumbers;
    public Button btnClear;
    public Button btnRemove;
    public Button btnSave;
    public TableView<StudentTM> tblStudents;
    private Connection connection;

    private PreparedStatement pstmInsertStudent;

    public void initialize(){
        tblStudents.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblStudents.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblStudents.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("delete"));

        ChangeListener<String> listener = (observable, oldValue, newValue) -> {
            String id = txtId.getText();
            String name = txtName.getText();
            String phoneNumber = txtPhoneNumber.getText();

            btnSave.setDisable(!(id.matches("C\\d{3}") && name.matches("[A-Za-z ]{3,}") && phoneNumber.matches("\\d{10}")));
        };


        txtId.textProperty().addListener(listener);
        txtName.textProperty().addListener(listener);
        txtPhoneNumber.textProperty().addListener(listener);

        tblStudents.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selectedStudent) -> {
            if (selectedStudent != null) {
                txtId.setText(selectedStudent.getId());
                txtName.setText(selectedStudent.getName());
                txtId.setDisable(true);
                btnSave.setText("Update");
            } else {
                btnSave.setText("Save");
            }
        });

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dep7", "root", "mysql");
            pstmInsertStudent = connection.prepareStatement("INSERT INTO student VALUES (?,?)");
        } catch (SQLException | ClassNotFoundException ex) {
            new Alert(Alert.AlertType.ERROR, "Failed to connect to the database server.").showAndWait();
            ex.printStackTrace();
            System.exit(1);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }));

        try {
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT * FROM student");

            while (rst.next()) {
                String id = rst.getString("id");
                String name = rst.getString("name");
                tblStudents.getItems().add(new StudentTM(id, name, new Button()));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }


    public void btnClear_OnAction(ActionEvent actionEvent) {
    }

    public void btnRemove_OnAction(ActionEvent actionEvent) {
    }

    public void btnSave_OnAction(ActionEvent actionEvent) {
    }
}
