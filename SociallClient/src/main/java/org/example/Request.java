package org.example;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Request {
    private String Request;
    private String password;
    private String username;

    //private ArrayList<String> list;
    Request(String Request, String password, String username){
        this.Request = Request;
        this.password = password;
        this.username = username;
    }

}