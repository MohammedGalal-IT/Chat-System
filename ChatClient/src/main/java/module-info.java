module com.chatsystem.client {
    requires transitive javafx.graphics;
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
//    requires transitive javafx.web;

//    requires org.controlsfx.controls;
//    requires com.dlsc.formsfx;
//    requires net.synedra.validatorfx;
//    requires org.kordamp.ikonli.javafx;
//    requires org.kordamp.bootstrapfx.core;
//    requires eu.hansolo.tilesfx;
    requires javafx.media;
    requires java.sql;
    requires com.google.gson;
    requires com.dustinredmond.fxalert;
    requires java.desktop;


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
    exports com.chatsystem.client.util;
    opens com.chatsystem.client.util to javafx.fxml;    
    exports com.chatsystem.client.util.viewUtil;
    opens com.chatsystem.client.util.viewUtil to javafx.fxml;

}