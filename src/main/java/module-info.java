module dev.dcardenas.javafxloginmfa {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires java.sql;
    requires password4j;
    requires java.desktop;

    exports dev.dcardenas.javafxloginmfa.ui;
    opens dev.dcardenas.javafxloginmfa.ui to javafx.fxml;
    opens dev.dcardenas.javafxloginmfa to javafx.fxml;
    exports dev.dcardenas.javafxloginmfa;
}