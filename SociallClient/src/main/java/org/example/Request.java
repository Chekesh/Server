package org.example;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

public class Request {
    private String Request;

    //private ArrayList<String> ListAttributes;

    private Map<String, String> MapAttributes;
    Request(String Request, Map<String,String> MapAttributes){//ArrayList<String> ListAttributes){
        this.Request = Request;
        //this.ListAttributes = ListAttributes;
        this.MapAttributes = MapAttributes;
    }

}