module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.compiler;
    requires com.google.gson;
    requires com.google.auth;
    requires com.google.auth.oauth2;
    requires tyrus.standalone.client;
    requires java.net.http;
    requires com.google.api.client;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;

    exports org.example;
    exports org.example.ui;
    exports org.example.data;
    opens org.example.data to com.google.gson;
    opens org.example.ui to javafx.fxml;
    opens org.example.ui.practice to javafx.fxml;
    exports org.example.ui.practice;
}
