package ru.bsuedu.cad.lab;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("ru.bsuedu.cad.lab");

        DataBaseRenderer renderer = context.getBean(DataBaseRenderer.class);
        renderer.render();

        CategoryRequest request = context.getBean(CategoryRequest.class);
        request.printCategoriesWithMoreThanOneProduct();

        context.close();
    }
}