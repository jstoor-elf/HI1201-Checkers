package Exceptions;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Alexander
 */
public class LoadException extends Exception {

    public LoadException() {
        super();
        Alert fileNotFound = new Alert(Alert.AlertType.ERROR,"File corrupted/file not found", ButtonType.CLOSE);
        fileNotFound.setTitle("Alert");
        fileNotFound.setHeaderText("");
        fileNotFound.resultConverterProperty();
        fileNotFound.showAndWait();
    }

}
