package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Request {
    private String Request;
    //private ArrayList<String> ListAttributes;

    private Map<String, String> MapAttributes;

    /*public ArrayList<String> getListAttributes() {
        return ListAttributes;
    }*/

    public Map<String, String> getMapAttributes() {
        return MapAttributes;
    }

    public String getRequest() {
        return Request;
    }
}
