module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires jdk.compiler;

    // Adding OkHttp and Gson dependencies
    requires okhttp3;
    requires com.google.gson;
    requires okio;

    opens org.example to javafx.fxml;
    exports org.example;
}
