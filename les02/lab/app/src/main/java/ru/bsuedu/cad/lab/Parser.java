package ru.bsuedu.cad.lab;

import java.util.List;

public interface Parser {
    List<Product> parse(String data);   // на вход строка, на выход список объектов Product
}