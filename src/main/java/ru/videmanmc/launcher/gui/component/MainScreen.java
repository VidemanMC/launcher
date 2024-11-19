package ru.videmanmc.launcher.gui.component;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.SneakyThrows;

public class MainScreen implements LauncherScreen {

    @SneakyThrows
    @Override
    public void show(Stage stage) {
        var vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setBackground(new Background(new BackgroundFill(Paint.valueOf("black"), new CornerRadii(10, 10, 10, 10, true), new Insets(-20))));

        Scene scene = new Scene(vbox, 600, 600);
        stage.setScene(scene);


        var textField = new TextField();
        textField.setPromptText("Введите никнейм");
        textField.setFocusTraversable(false);
        textField.setFont(Font.font(20));
        textField.setBackground(new Background(new BackgroundFill(Paint.valueOf("black"), null, null)));
        textField.setBorder(Border.stroke(Paint.valueOf("aqua")));
        textField.setStyle("-fx-text-fill: white;");
        textField.setAlignment(Pos.CENTER);

        Button play = new Button("ИГРАТЬ");
        play.setFont(Font.font(20));
        play.setTextFill(Paint.valueOf("white"));
        play.setBackground(new Background(new BackgroundFill(Paint.valueOf("magenta"), null, null)));
        play.setBorder(Border.stroke(Paint.valueOf("yellow")));
        play.setCursor(Cursor.CROSSHAIR);

        var reminder = new Text("Дизайна нет, но вы держитесь");
        reminder.setFont(Font.font(16));
        reminder.setFill(Paint.valueOf("white"));

        vbox.getChildren().add(textField);
        vbox.getChildren().add(play);
        vbox.getChildren().add(reminder);
        stage.show();
    }
}
