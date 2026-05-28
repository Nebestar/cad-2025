package ru.bsuedu.cad.lab;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        Renderer renderer = context.getBean(Renderer.class);

        renderer.render();

        ((AnnotationConfigApplicationContext) context).close();
    }
}