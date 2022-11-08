package com.example.lab3databases;

import static org.junit.Assert.*;

import org.junit.Test;
//import javax.script.ScriptException;

public class Evaluate {

    @Test
    public void evaluate_product() {
        Product p = new Product("Name", 0);
        p.setProductName("HotDog");
        String actual = p.getProductName();
        String expected = "HotDog";
        assertEquals("Error, values don't match", expected, actual);
    }
    @Test
    public void evaluate_product2() {
        Product p = new Product("Name", 0);
        p.setProductPrice(20);
        double actual = p.getProductPrice();
        double expected = 20;
        assertEquals("Error, values don't match", expected, actual, 0.00001);
    }
}
