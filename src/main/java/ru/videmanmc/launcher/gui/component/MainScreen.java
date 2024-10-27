package ru.videmanmc.launcher.gui.component;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import lombok.SneakyThrows;

public class MainScreen implements LauncherScreen {

    @SneakyThrows
    @Override
    public void show(Stage stage) {
        Scene scene = new Scene(new Group(), 600, 600);
        stage.setScene(scene);
        Button play = new Button("ИГРАТЬ");

        ((Group) scene.getRoot()).getChildren().add(play);
        stage.show();
    }
}
