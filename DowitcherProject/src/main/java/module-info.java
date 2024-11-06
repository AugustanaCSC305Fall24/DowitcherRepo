module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires jdk.compiler;
    requires com.google.gson;
    opens org.example.data to com.google.gson;
//    requires org.jsoup;

//    requires google.cloud.aiplatform;
//    requires proto.google.cloud.vertexai.v1;
//    requires proto.google.cloud.vertexai.v1;
//    requires google.cloud.vertexai;

    exports org.example;
    exports org.example.ui;
    exports org.example.data;

    opens org.example.ui to javafx.fxml;
    opens org.example.ui.practice to javafx.fxml;
    exports org.example.ui.practice;
}
