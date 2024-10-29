module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires jdk.compiler;

    // Adding OkHttp and Gson dependencies
    requires okhttp3;
    requires com.google.gson;

    opens org.example to javafx.fxml;
    exports org.example;
    exports org.example.ui;
    opens org.example.ui to javafx.fxml;
    exports org.example.data;
    opens org.example.data to javafx.fxml;
    exports org.example.utility;
    opens org.example.utility to javafx.fxml;
    exports org.example.ui.practice;
    opens org.example.ui.practice to javafx.fxml;
}
