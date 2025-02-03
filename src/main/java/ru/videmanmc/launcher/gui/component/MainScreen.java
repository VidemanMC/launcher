package ru.videmanmc.launcher.gui.component;

import com.google.inject.Inject;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.videmanmc.launcher.service.GameRunningService;

@RequiredArgsConstructor(onConstructor_ = @__(@Inject))
public class MainScreen implements LauncherScreen {

    private final GameRunningService gameRunningService; //todo add event system and remove this shit from there

    @SneakyThrows
    @Override
    public void show(Stage stage) {
        var vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);

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
        textField.requestFocus();


        var media = new Media(getClass().getClassLoader().getResource("ricardo_early_beta.mp4").toExternalForm());
        var player = new MediaPlayer(media);
        player.setCycleCount(10);
        var mediaView = new MediaView(player);
        mediaView.setFitWidth(600);
        mediaView.setFitHeight(600);
        mediaView.setPreserveRatio(false);
        mediaView.setVisible(false);

        Button play = new Button("ИГРАТЬ");
        play.setFont(Font.font(20));
        play.setTextFill(Paint.valueOf("white"));
        play.setBackground(new Background(new BackgroundFill(Paint.valueOf("magenta"), null, null)));
        play.setBorder(Border.stroke(Paint.valueOf("yellow")));
        play.setCursor(Cursor.HAND);

        play.setOnMouseClicked(event -> {
            if (textField.getText().isEmpty()) {
                textField.requestFocus();

                return;
            }

            new Thread(() -> gameRunningService.run(textField.getText())).start();

            player.setAutoPlay(true);
            mediaView.setVisible(true);

            vbox.getChildren().clear();
            vbox.getChildren().add(mediaView);
        });

        var reminder = new Text("Всё ещё впереди!");
        reminder.setFont(Font.font(16));
        reminder.setFill(Paint.valueOf("black"));


        vbox.getChildren().add(textField);
        vbox.getChildren().add(play);
        vbox.getChildren().add(reminder);
        stage.show();
    }
}
