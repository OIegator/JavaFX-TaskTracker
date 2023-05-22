package com.javafxtasktracker;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

public class TaskTracker extends Application {
    protected ListView<TaskEntity> listView;

    protected Callback<ListView<TaskEntity>, ListCell<TaskEntity>> cellFactory;

    Data data;

    @Override
    public void start(Stage stage) {

        data = new Data();
        listView = new ListView<>();

        for (TaskEntity task : data.getTasks()) {
            listView.getItems().add(task);
        }

        cellFactory = param -> new ListCell<>() {
            private final CheckBox checkBox = new CheckBox();
            private final Text title = new Text();
            private final Text date = new Text();
            private final Text reasonDateChangeText = new Text();
            private final Text outOfTimeText = new Text();
            private final Text statusText = new Text();
            private final Button deleteButton = new Button("⨉");

            @Override
            protected void updateItem(TaskEntity item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    setGraphic(
                            prepareViewHolder(item, title, date, checkBox, reasonDateChangeText, outOfTimeText, statusText, deleteButton)
                    );
                }
            }
        };
        listView.setCellFactory(cellFactory);
        listView.setStyle("-fx-control-inner-background-alt: -fx-control-inner-background ;");


        Button addTask = initAddTaskButton();

        showMainStage(stage, addTask);


    }

    public HBox prepareViewHolder(TaskEntity item, Text title, Text date, CheckBox checkBox, Text reasonDateChangeText, Text outOfTimeText, Text statusText, Button deleteButton) {
        title.setText(item.getName());
        checkBox.setSelected(item.isReady());
        checkBox.setDisable(item.isReady());

        deleteButton.setOnAction(actionEvent -> deleteOnClick(item));
        deleteButton.setPrefWidth(30);
        deleteButton.setPrefHeight(30);
        deleteButton.setStyle("-fx-background-radius: 15em; -fx-min-width: 30px; -fx-min-height: 30px; -fx-max-width: 30px; -fx-max-height: 30px; -fx-background-color: #7B241C; -fx-text-fill: white; -fx-font-size: 12px; -fx-font-weight: bold;");
        DropShadow shadow = new DropShadow();
        deleteButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> deleteButton.setEffect(shadow));
        deleteButton.addEventHandler(MouseEvent.MOUSE_EXITED, e -> deleteButton.setEffect(null));
        date.setText(item.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        reasonDateChangeText.setText(item.getReasonDateChange());

        outOfTimeText.setText(item.getState().getValue());

        statusText.setText(item.getStatus());

        VBox vbox2 = new VBox(5, checkBox);
        vbox2.setAlignment(Pos.CENTER_LEFT);
        checkBox.setStyle("-fx-font-size: 16px; -fx-border-width: 1px;");

        VBox vbox3 = new VBox(5, deleteButton);
        vbox3.setAlignment(Pos.CENTER);

        VBox vbox = new VBox(10,
                title,
                new HBox(10, date, reasonDateChangeText, outOfTimeText, statusText));

        checkBox.setOnAction(actionEvent -> checkBoxOnClick(item, checkBox.isSelected(), checkBox));
        HBox rootTask = new HBox(10, vbox2, vbox, vbox3);
        rootTask.setManaged(true);
        rootTask.setMaxWidth(Double.MAX_VALUE);
        rootTask.setMaxHeight(Double.MAX_VALUE);
        rootTask.setPrefWidth(Region.USE_COMPUTED_SIZE);
        rootTask.setPrefHeight(Region.USE_COMPUTED_SIZE);
        vbox.setMaxWidth(Double.MAX_VALUE);
        vbox.setMaxHeight(Double.MAX_VALUE);
        vbox.setPrefWidth(0.0);
        vbox.setPrefHeight(0.0);
        vbox.setSpacing(5.0);
        HBox.setHgrow(vbox, Priority.ALWAYS);

        title.setStyle("-fx-font-weight: bold;");
        outOfTimeText.setStyle("-fx-font-style: italic; -fx-fill: gray;");
        statusText.setStyle("-fx-font-style: italic; -fx-fill: gray;");

        rootTask.setStyle("-fx-background-color: white; -fx-background-radius: 5.0;");
        rootTask.setPadding(new Insets(10, 10, 10, 10));

        return rootTask;
    }




    private void checkBoxOnClick(TaskEntity item, boolean selected, CheckBox cb) {
        item.setOutOfTime(item.isReady() && LocalDate.now().isAfter(item.getDate()));

        if (selected) {
            // Show a new window to enter comment and grade
            showGradeWindow(item, cb);
        } else {
            item.setState(TaskEntity.State.in_progress);
            listView.refresh();
        }

        ArrayList<TaskEntity> taskList = new ArrayList<>(listView.getItems());
        data.setTasks(taskList);
        listView.refresh();
    }


    private void deleteOnClick(TaskEntity item) {
        listView.getItems().remove(item);
        ArrayList<TaskEntity> taskList = new ArrayList<>(listView.getItems());
        data.setTasks(taskList);
    }

    private void showMainStage(Stage stage, Button addTask) {
        StackPane root = new StackPane();

        // Create and configure the background image
        Image backgroundImage = new Image("C:/Users/Mi/IdeaProjects/JavaFX-TaskTracker/src/main/resources/images/bgr.jpg");
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true);
        BackgroundImage bgImage = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);

        // Create a container for the ListView
        VBox container = new VBox();
        container.getChildren().add(listView);
        VBox.setVgrow(listView, Priority.ALWAYS);
        listView.setBackground(new Background(bgImage));

        // Apply the background image to the container
        container.setBackground(new Background(bgImage));

        // Add the container and the addTask button to the root StackPane
        root.getChildren().addAll(container, addTask);
        StackPane.setAlignment(addTask, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(addTask, new Insets(10));

        Scene scene = new Scene(root, 500, 400);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/app.css")).toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Список задач");
        stage.show();
    }



    private Button initAddTaskButton() {
        Button addTask = new Button("+");
        addTask.setPrefWidth(45);
        addTask.setPrefHeight(45);
        addTask.setStyle("-fx-background-radius: 15em; -fx-min-width: 45px; -fx-min-height: 45px; -fx-max-width: 45px; -fx-max-height: 45px; -fx-background-color: #154360 ; -fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");

        addTask.setOnAction(actionEvent -> {
            Stage addTaskStage = new Stage();
            addTaskStage.initModality(Modality.APPLICATION_MODAL);
            addTaskStage.setResizable(false);
            addTaskStage.setTitle("Добавить задачу");

            Scene addTaskScene = new Scene(new AddItem().addItemRoot(taskEntity -> {
                listView.getItems().add(taskEntity);
                ArrayList<TaskEntity> taskList = new ArrayList<>(listView.getItems());
                data.setTasks(taskList);

                addTaskStage.close();


            }), 300, 120);


            addTaskStage.setScene(addTaskScene);
            addTaskStage.show();
        });



        DropShadow shadow = new DropShadow();
        addTask.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> addTask.setEffect(shadow));
        addTask.addEventHandler(MouseEvent.MOUSE_EXITED, e -> addTask.setEffect(null));

        return addTask;
    }


    private void showGradeWindow(TaskEntity item, CheckBox cb) {
        Stage gradeStage = new Stage();
        gradeStage.initModality(Modality.APPLICATION_MODAL);
        gradeStage.setResizable(false);

        TextField commentField = new TextField();
        TextField gradeField = new TextField();
        gradeField.setPrefWidth(50);

        item.setDate(LocalDate.now());

        Button okButton = new Button("OK");
        gradeField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                gradeField.setText(newValue.replaceAll("\\D", ""));
            }
        });
        okButton.setOnAction(event -> {
            item.setReady(true);
            String comment = commentField.getText();
            int grade;
            try {
                grade = Integer.parseInt(gradeField.getText());
                if(grade > item.getMaxGrade()) {
                    showErrorAlert("Неверная оценка", "Оценка не может превышать максимальную оценку.");
                    return;
                }
            } catch (NumberFormatException e) {
                showErrorAlert("Оценка не введена", "Поле оценки должно быть заполнено.");
                return;
            }


            item.setState(data.checkProgressByItem(item));
            item.setStatus(comment, grade, item.getMaxGrade());
            listView.refresh();

            gradeStage.close();
        });

        gradeStage.setOnCloseRequest(event -> {
            item.setReady(false);
            cb.setDisable(false);
        });

        HBox gradeBox = new HBox(10,
                new Label("Оценка (макс. " + item.getMaxGrade() + "):"),
                gradeField,
                okButton);

        VBox vbox = new VBox(10,
                new Label("Комментарий:"),
                commentField,
                gradeBox);

        vbox.setAlignment(Pos.CENTER_LEFT);
        vbox.setPadding(new Insets(10));

        Scene gradeScene = new Scene(vbox, 300, 120);
        gradeStage.setScene(gradeScene);
        gradeStage.setTitle("Введите комментарий и оценку");
        gradeStage.show();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public static void main(String[] args) {
        launch();
    }

}