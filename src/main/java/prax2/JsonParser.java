package prax2;


import com.fasterxml.jackson.databind.ObjectMapper;
import prax2.orderpojo.Order;

import java.io.IOException;
import java.util.List;

class JsonParser {

    public static Order createOrder(String stringJson) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(stringJson, Order.class);
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String createJson(List<Order> orders) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(orders);
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String createJson(Order orders) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(orders);
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
/*
    public static void main(String[] args) {
        System.out.println(createOrder("{\"orderNumber\":" +
                "\"test\",\"orderRows\":[{\"price\": 1, \"quantity\":2, \"itemName\":\"name\"}]}"
                ));
    }*/
}
