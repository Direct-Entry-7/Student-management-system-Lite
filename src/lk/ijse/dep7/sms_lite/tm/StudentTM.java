package lk.ijse.dep7.sms_lite.tm;

import javafx.scene.control.Button;

public class StudentTM {
    private String id;
    private String name;
    private Button delete;

    public StudentTM() {
    }

    public StudentTM(String id, String name, Button delete) {
        this.id = id;
        this.name = name;
        this.delete = delete;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Button getDelete() {
        return delete;
    }

    public void setDelete(Button delete) {
        this.delete = delete;
    }
}
