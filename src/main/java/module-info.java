module com.mp3.mp3player {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.web;
    requires javafx.swing;
    requires javafx.swt;


    opens com.mp3.mp3player to javafx.fxml;
    exports com.mp3.mp3player;
}