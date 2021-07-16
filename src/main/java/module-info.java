/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

module Emit {
    requires javafx.controlsEmpty;
    requires javafx.controls;
    requires javafx.graphicsEmpty;
    requires javafx.graphics;
    requires javafx.baseEmpty;
    requires javafx.base;
    requires javafx.fxmlEmpty;
    requires javafx.fxml;
    //requires emit;
    
    //opens Emit to javafx.fxml;
    //exports Emit;
    
    opens com.mycompany.emit to javafx.fxml;
    exports com.mycompany.emit to javafx.graphics;
}
