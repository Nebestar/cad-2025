Отчет по лабораторной работе №2: Spring Framework (Аннотации и АОП)
Цель работы
Освоение инструментов автоматического конфигурирования Spring (Component Scanning) и внедрение сквозной бизнес-логики с использованием Аспектно-Ориентированного Программирования (АОП).

Ход работы
Настройка зависимостей: В build.gradle добавлены необходимые библиотеки Spring (Spring Context, AspectJ), обеспечивающие работу контейнера и обработку аспектов.

Переход на аннотации: Выполнен отказ от централизованного класса конфигурации (AppConfig) в пользу сканирования компонентов @ComponentScan. Все классы, участвующие в логике приложения, размечены стереотипными аннотациями @Component или @Service.

Внешняя конфигурация: Реализовано считывание имени файла из application.properties с помощью аннотации @Value. Это позволило исключить "зашитые" в код константы.

HTML-режим: Создана новая реализация интерфейса Renderer — HTMLTableRenderer. Для выбора приоритетной реализации использована аннотация @Primary.

Жизненный цикл бинов: С помощью аннотации @PostConstruct реализован метод инициализации в ResourceFileReader, выводящий время готовности компонента к работе.

АОП-логирование: Разработан аспект LoggingAspect, который через аннотацию @Around перехватывает вызов метода парсинга CSV-файла и вычисляет затраченное на него время.

Выводы
В результате работы были изучены преимущества декоративного программирования в Spring. Автоматическое связывание зависимостей (@Autowired) существенно упростило структуру проекта, снизив связность (coupling) между классами. Применение АОП позволило отделить задачу логирования производительности от основной бизнес-логики, что делает код более поддерживаемым и чистым.

Диаграмма Mermaid:

```mermaid
classDiagram
class App {
+main(String[] args)
}
class AppConfig {
<<Configuration/ComponentScan>>
}
class CSVParser {
<<Component>>
+parse(String data) List
}
class ResourceFileReader {
<<Component>>
+read() String
+init() void
}
class HTMLTableRenderer {
<<Component, Primary>>
+render() void
}
class ConsoleTableRenderer {
<<Component>>
+render() void
}
class LoggingAspect {
<<Aspect>>
+logPerformance(ProceedingJoinPoint joinPoint) Object
}
    App --> AppConfig
    HTMLTableRenderer ..|> Renderer
    ConsoleTableRenderer ..|> Renderer
    ResourceFileReader ..|> Reader
    CSVParser ..|> Parser
    
    HTMLTableRenderer --> ProductProvider : @Autowired
    LoggingAspect ..> CSVParser : @Around
    note for ResourceFileReader "@PostConstruct"