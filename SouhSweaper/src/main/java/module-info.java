module app.souhsweaper {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.media;


    opens app.souhsweaper to javafx.fxml;
    exports app.souhsweaper;
}