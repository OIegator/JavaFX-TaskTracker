package com.javafxtasktracker;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.util.function.Consumer;

public class AddItem {
    private TextField title;
    private DatePicker datePicker;
    private TextField maxGrade;
    private Button addButton;

    public Pane addItemRoot(Consumer<TaskEntity> addItemCallback) {
        title = new TextField();
        title.setPromptText("Название");

        datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now());

        maxGrade = new TextField();
        maxGrade.setPromptText("Оценка");
        maxGrade.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                maxGrade.setText(newValue.replaceAll("\\D", ""));
            }
        });

        addButton = new Button("Добавить");

        setOnClickListeners(addItemCallback);
        maxGrade.setPrefWidth(60);
        HBox lastRow = new HBox(new Text("Максимальная оценка: "), maxGrade, addButton);
        lastRow.setSpacing(5);
        VBox root = new VBox(
                10,
                new HBox(new Text("Название задачи:  "), title),
                new HBox(new Text("Срок выполнения: "), datePicker),
                lastRow
        );

        root.setPadding(new Insets(10));

        title.setStyle("-fx-font-weight: bold;");

        //addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        DropShadow shadow = new DropShadow();
        addButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
                e -> addButton.setEffect(shadow)
        );
        addButton.addEventHandler(MouseEvent.MOUSE_EXITED,
                e -> addButton.setEffect(null)
        );

        return root;
    }


    private void setOnClickListeners(Consumer<TaskEntity> addItemCallback) {
        addButton.setOnAction(actionEvent -> {
            if (title.getText().isEmpty()) {
                showAlert("title");
            } else if (datePicker.getValue().isBefore(LocalDate.now())) {
                showAlert("date");
            } else if (maxGrade.getText().isEmpty()) {
                showAlert("grade");
            } else {
                addItem(addItemCallback);
            }
        });
    }

    private void showAlert(String nameError) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.getDialogPane().setStyle("-fx-background-color: #f2f2f2;");
        errorAlert.setTitle("Ошибка");
        errorAlert.setHeaderText(null);
        Label label = switch (nameError) {
            case "title" -> new Label("Название задачи должно быть заполнено");
            case "date" -> new Label("Выбрана прошедшая дата");
            case "grade" -> new Label("Поле оценки должно быть заполнено");
            default -> new Label("Ошибка");
        };

        label.setStyle("-fx-font-size: 14;");
        errorAlert.getDialogPane().setContent(label);
        errorAlert.showAndWait();
    }

    private void addItem(Consumer<TaskEntity> addItemCallback) {
        LocalDate date = datePicker.getValue();
        int grade = Integer.parseInt(maxGrade.getText());
        TaskEntity newTask = new TaskEntity(
                title.getText(),
                date,
                grade,
                ""
        );
        addItemCallback.accept(newTask);
    }
}
