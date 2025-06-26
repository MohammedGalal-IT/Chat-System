module com.chatsystem.server {

    requires com.google.gson;
    requires java.desktop;
    requires jbcrypt;
    requires java.sql;

    exports com.chatsystem.server;
    exports com.chatsystem.server.Model to com.google.gson;
    exports com.chatsystem.server.network to com.google.gson;
}