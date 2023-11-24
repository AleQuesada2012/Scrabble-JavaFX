module poo.rummikub {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens poo.scrabble to javafx.fxml;
    exports poo.scrabble;
}