package com.mp3.mp3player;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

@SuppressWarnings("unused")
public class MyController implements Initializable {
    private static final String[] SPEEDS = {"25%", "50%", "75%", "100%", "125%", "150%", "175%", "200%"};
    private final File DIRECTORY = Paths.get(Objects.requireNonNull(getClass().getResource("songs")).toURI()).toFile();
    private final List<File> files = new ArrayList<>();
    private final BooleanProperty playEnabled = new SimpleBooleanProperty();
    @FXML
    private MediaPlayer mediaPlayer;
    @FXML
    private Label songLabel;
    @FXML
    private Button playButton, pauseButton, previousButton, nextButton, resetButton;
    @FXML
    private ComboBox<String> speedComboBox;
    @FXML
    private Slider volumeSlider;
    @FXML
    private ProgressBar songProgressBar;
    private int currentSongIndex;

    public MyController() throws URISyntaxException {
    }


    @FXML
    protected void playMedia() {
        mediaPlayer.play();
        playEnabled.setValue(false);
        updateFromScene();
    }

    @FXML
    protected void pauseMedia() {
        mediaPlayer.pause();
        playEnabled.setValue(true);
    }

    @FXML
    protected void nextMedia() {
        if (currentSongIndex < files.size() - 1)
            currentSongIndex++;
        else currentSongIndex = 0;
        setSong(true);
    }

    @FXML
    protected void previousMedia() {
        if (currentSongIndex > 0)
            currentSongIndex--;
        else currentSongIndex = files.size() - 1;
        setSong(true);
    }

    @FXML
    protected void resetMedia() {
        songProgressBar.setProgress(0);
        mediaPlayer.seek(Duration.seconds(0));
        playMedia();
    }

    @FXML
    protected void changeSpeed() {
        updateSpeed();
        playMedia();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (DIRECTORY.isDirectory() && DIRECTORY.listFiles() != null) {
            files.addAll(List.of(Objects.requireNonNull(DIRECTORY.listFiles())));
        }
        // initialize the speed menu
        speedComboBox.getItems().addAll(List.of(SPEEDS));

        // initialize the play,pause buttons to enable and disable listener based on whether the current song is
        // being played or its in paused state
        playEnabled.addListener((observableValue, oldValue, newValue) -> {
            playButton.setDisable(!newValue);
            pauseButton.setDisable(newValue);
        });
        // initialising the volume slider with volume changeAction
        volumeSlider.valueProperty().addListener((observableValue, oldValue, newValue) -> changeVolume());
        playEnabled.setValue(true);

        setSong(false);
    }

    private void setSong(boolean playSongAfterUpdate) {
        if (mediaPlayer != null) stopMedia();
        File file = files.get(currentSongIndex);
        Media media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.currentTimeProperty().addListener((observableValue, duration, t1) -> updateProgressBar());
        songLabel.setText(file.getName());
        if (playSongAfterUpdate) playMedia();
    }

    private void changeVolume() {
        mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
    }

    private void stopMedia() {
        mediaPlayer.stop();
    }

    // this method takes care of volume and speed change from scene
    private void updateFromScene() {
        changeVolume();
        updateSpeed();
    }

    private void updateSpeed() {
        String selectedSpeed = speedComboBox.getValue() != null ? speedComboBox.getValue() : SPEEDS[3];
        int actualSpeed = Integer.parseInt(selectedSpeed.substring(0, selectedSpeed.length() - 1));
        mediaPlayer.setRate(actualSpeed * 0.01);
    }

    private void updateProgressBar() {
        double timeElapsed = mediaPlayer.getCurrentTime().toSeconds();
        double totalTime = mediaPlayer.getTotalDuration().toSeconds();
        songProgressBar.setProgress(timeElapsed / totalTime);
        if (timeElapsed / totalTime == 1)
            nextMedia();
    }
}