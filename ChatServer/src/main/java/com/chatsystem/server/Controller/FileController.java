package com.chatsystem.server.Controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import com.chatsystem.server.Model.Message.MessageType;
import com.chatsystem.server.network.Request;
import com.chatsystem.server.network.Response;
import com.chatsystem.server.services.FileService;

public class FileController {

    private final FileService fileService;

    public FileController(){
        this.fileService = new FileService();
    }

    public Request saveFile(Request request, InputStream in) throws IOException{
        if(request.getMessage().getMessage_type() == MessageType.TEXT || in == null){
            System.out.println("Error at FileController line 23");
            return null;
        }
        // check file format
        switch (request.getMessage().getFile_format()) {
            case txt:
            case jpg:
            case png:
            case docx:
            case mp3:
            case mp4:
            case pdf:
            case rar:
            case zip:
            case xlsx:
                break;

            default:
                System.out.println("Error at FileController line 41");
                return null;
        }
        // generate file name
        String fileName = fileService.generateRandomFileName(request.getMessage().getFile_format());
        // save file to uploads
        String path = fileService.saveFile(request.getMessage().getMessage_type(), fileName, in, request.getMessage().getFile_length());
        if(path == null){
            System.out.println("Error at FileController line 49");
            return null;
        }
        // put the path in the request
        request.getMessage().setFile_path_server(path);
        System.out.println("Request Prepared with server file path");
        return request;
    }

    // get file from server 
    public byte[] getFile(Response response) throws IOException{
        String path = response.getMessageObj().getFile_path_server();
        if(path == null || path.isEmpty()){
            response.setMessage("can not u");
            return null;
            }
            // get file from server as bytes
            return fileService.readFile(path);
    }
}
