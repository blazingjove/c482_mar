module c482.c482_mar {
    requires javafx.controls;
    requires javafx.fxml;


    opens c482.c482_mar to javafx.fxml;
    exports c482.c482_mar;
    exports Controller;
    opens Controller to javafx.fxml;
    opens Model to javafx.base;
}