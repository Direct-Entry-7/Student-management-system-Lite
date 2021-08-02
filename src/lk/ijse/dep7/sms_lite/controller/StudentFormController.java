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
    private PreparedStatement pstmQueryID;

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
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sms_lite", "root", "root");
            pstmInsertStudent = connection.prepareStatement("INSERT INTO student VALUES (?,?)");
            pstmQueryID = connection.prepareStatement("SELECT id FROM student WHERE id=?");
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
                tblStudents.getItems().add(new StudentTM(id, name, new Button("Delete")));
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
        String id = txtId.getText();
        String name = txtName.getText();

        if (btnSave.getText().equals("Save")) {

            try {
                pstmQueryID.setString(1, id);;


                if (pstmQueryID.executeQuery().next()) {
                    new Alert(Alert.AlertType.ERROR, "Student ID already exists").show();
                    txtId.requestFocus();
                    return;
                }


                pstmInsertStudent.setString(1, id);
                pstmInsertStudent.setString(2, name);
                int affectedRows = pstmInsertStudent.executeUpdate();

                if (affectedRows == 1) {
                    tblStudents.getItems().add(new StudentTM(id, name, new Button("Delete")));
                    clearFields();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to save the student, retry").show();
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
//                String errMsg = "Failed to save the customer";
//                switch (ex.getErrorCode()){
//                    case 1062:
//                        errMsg = "Duplicate record, please check the customer ID and NIC";
//                }
                new Alert(Alert.AlertType.ERROR, "Failed to save the student, retry").show();
            }
        } else {


        }

    }

    private void clearFields(){
        txtId.setText("");
        txtName.setText("");
        txtPhoneNumber.setText("");
    }
}
