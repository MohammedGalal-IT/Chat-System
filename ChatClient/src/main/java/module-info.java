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


    opens com.chatsystem.client to javafx.fxml;
    exports com.chatsystem.client;
    exports com.chatsystem.client.Controller;
    opens com.chatsystem.client.Controller to javafx.fxml;
}