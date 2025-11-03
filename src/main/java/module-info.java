module proceduralMovement {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.media;
    requires javafx.fxml;
    requires javafx.fxmlEmpty;
    requires java.desktop;

    opens org to javafx.fxml;
    exports org;
}