module com.chatsystem.client {
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires javafx.media;
    requires java.sql;
    requires com.google.gson;
    requires com.dustinredmond.fxalert;


    opens com.chatsystem.client to javafx.fxml;
    exports com.chatsystem.client;
    exports com.chatsystem.client.app;
    opens com.chatsystem.client.app to javafx.fxml;
    exports com.chatsystem.client.Controller;
    opens com.chatsystem.client.Controller to javafx.fxml;
    exports com.chatsystem.client.test;
    opens com.chatsystem.client.test to javafx.fxml;
    exports com.chatsystem.client.Model to com.google.gson;
    exports com.chatsystem.client.network to com.google.gson;

}