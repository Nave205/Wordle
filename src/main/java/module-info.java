module dev.evan.wordle {
    requires javafx.controls;
    requires javafx.fxml;


    opens dev.evan.wordle to javafx.fxml;
    exports dev.evan.wordle;
}