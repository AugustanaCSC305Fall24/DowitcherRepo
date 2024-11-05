module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires jdk.compiler;
    requires com.google.gson;

    exports org.example;
    exports org.example.ui;
    exports org.example.data;

    opens org.example.ui to javafx.fxml;
    opens org.example.ui.practice to javafx.fxml;
    exports org.example.ui.practice;
}
