package com.mycompany.emit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;



public class PrimaryController implements Initializable{
    
    @FXML
    public DatePicker date;
    
    @FXML
    public TextField name;
    
    @FXML
    public TextField receipt;
    
    @FXML
    public ChoiceBox<String> roomType;
    
    @FXML
    public TextField nights;
    
    @FXML
    public ChoiceBox<String> roomNumber;
    
    @FXML
    public Label dateLabel;
    
    @FXML
    public Label nameLabel;
    
    @FXML
    public Label receiptLabel;
    
    @FXML
    public Label roomTypeLabel;
    
    @FXML
    public Label numNightsLabel;
    
    @FXML
    public Label roomNumberLabel;
    
    @FXML
    public Label emitLabel;
            
    public final String[] roomTypeStrings = {"Single Room", "Double Room", "Self-Contained 297", "Self-Contained 308"};
    
    public final String[] roomNumbers = {"1", "1A", "1B", "2", "3", "4", "4A", "5", "5A", "5B", "6", "7", "8", "9", "10", "10A", "11", "12", "12A", "SC 1", "SC 2", "SC 3", "SC 4", "BL 1", "BL 2"};
    
    public final String[] singleRooms = {"1", "2", "3", "4", "4A", "5", "6", "7", "8", "10A"};
        
    public final String[] doubleRooms = {"1A", "1B", "5A", "5B", "9", "10", "11", "12", "12A", "BL1", "BL2"};
        
    public final String[] sc297Rooms = {"SC1", "SC2"};
    
    public final String[] sc308Rooms = {"SC3", "SC4"};
    
    
    @FXML
    private void writeToCSV() throws IOException {
        dateLabel.setText("");
        nameLabel.setText("");
        receiptLabel.setText("");
        roomTypeLabel.setText("");emitLabel.setText("");
        numNightsLabel.setText("");
        roomNumberLabel.setText("");
        emitLabel.setText("");
        
        LocalDate bookingDate = date.getValue();
        String bookingName = name.getText();
        String bookingReceipt = receipt.getText();
        String bookingRoomType = roomType.getValue();
        String bookingNumNights = nights.getText();
        String bookingRoomNum = roomNumber.getValue();
        
        boolean dateBool = false;
        boolean nameBool = false;
        boolean receiptBool = false;
        boolean roomTypeBool = false;
        boolean numNightsBool = false;
        boolean roomNumBool = false;
        
        boolean numNightsNumBool = false;
        boolean receiptNumBool = false;
        
        if (bookingDate == null) dateBool = true;
        if (bookingName.isEmpty()) nameBool = true;
        if (bookingReceipt.isEmpty()) receiptBool = true;
        if (bookingRoomType == null) roomTypeBool = true;
        if (bookingNumNights.isEmpty()) numNightsBool = true;
        if (bookingRoomNum == null) roomNumBool = true;
        
        if (!bookingNumNights.matches("[0-9]+")) numNightsNumBool = true;
        if (!bookingReceipt.matches("[0-9]+")) receiptNumBool = true;
        
        
        if (dateBool == true || nameBool == true || receiptBool == true ||
            roomTypeBool == true || numNightsBool == true || 
            roomNumBool == true || numNightsNumBool == true || receiptNumBool == true) {
        
            if (dateBool == true) dateLabel.setText("Field Required");
            if (nameBool == true) nameLabel.setText("Field Required");
            if (receiptBool == true) receiptLabel.setText("Field Required");
            if (roomTypeBool == true) roomTypeLabel.setText("Field Required");
            if (numNightsBool == true) numNightsLabel.setText("Field Required");
            if (roomNumBool == true) roomNumberLabel.setText("Field Required");
            
            
            if (receiptNumBool == true && receiptBool == false) receiptLabel.setText("Only numbers");
            if (numNightsNumBool == true && numNightsBool == false) numNightsLabel.setText("Only numbers");
            
            System.out.println("Invalid input");
            
        } else {
            System.out.println("good to go");
            
            String bookingDateString = bookingDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        
            List<List<String>> records = createRow(bookingDateString, bookingName, bookingReceipt, bookingRoomType, bookingNumNights, bookingRoomNum);

            try{
                boolean newlyCreated = false;
                
                File file = new File("daily.xlsx");

                if(!file.exists()){
                   file.createNewFile();
                   newlyCreated = true;
                }

                FileWriter fw = new FileWriter(file,true);
                BufferedWriter bw = new BufferedWriter(fw);

                if (newlyCreated == true) {
                    bw.write("Date,Name,Receipt #,Num Nights,Sg Rm 187,Num Nights,Db Rm198,Num Nights,SC 297,Num Nights,SC 308,Rm #,Bed Levy,Amt Paid,Daily Intake,Balance");
                    bw.newLine();
                }

                for (List<String> record : records) {
                    bw.write(String.join(",", record));
                    bw.newLine();
                }
                
                bw.close();
                
                emitLabel.setText("emit successful!");

            } catch(IOException ioe){
               System.out.println("Exception occurred:");
            }
        }
        
        
    }
    
    private List<List<String>> createRow(String bookingDateString, String bookingName, String bookingReceipt, String bookingRoomType, String bookingNumNights, String bookingRoomNum) {
        List<List<String>> records = null;
        
        if (bookingRoomType.equals("Single Room")) {
            records = Arrays.asList(
                Arrays.asList(bookingDateString, bookingName, bookingReceipt, bookingNumNights, "", "", "", "", "", "", "", bookingRoomNum)
            );
        } else if (bookingRoomType.equals("Double Room")) {
            records = Arrays.asList(
                Arrays.asList(bookingDateString, bookingName, bookingReceipt, "", "", bookingNumNights, "", "", "", "", "", bookingRoomNum)
            );
        } else if (bookingRoomType.equals("Self-Contained 297")) {
            records = Arrays.asList(
                Arrays.asList(bookingDateString, bookingName, bookingReceipt, "", "", "", "", bookingNumNights, "", "", "", bookingRoomNum)
            );
        } else if (bookingRoomType.equals("Self-Contained 308")) {
            records = Arrays.asList(
                Arrays.asList(bookingDateString, bookingName, bookingReceipt, "", "", "", "", "", "", bookingNumNights, "", bookingRoomNum)
            );
        }
        
        return records;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        dateLabel.setText("");
        nameLabel.setText("");
        receiptLabel.setText("");
        roomTypeLabel.setText("");
        numNightsLabel.setText("");
        roomNumberLabel.setText("");
        emitLabel.setText("");
        
        roomType.getItems().addAll(roomTypeStrings);
        roomNumber.getItems().addAll(roomNumbers);
        
        roomType.getSelectionModel().selectedItemProperty().addListener(
        (ObservableValue<? extends String> ov, String old_val, String new_val) -> {
            if (new_val.equals("Single Room")) {
                roomNumber.getItems().clear();
                roomNumber.getItems().addAll(singleRooms);
            } else if (new_val.equals("Double Room")) {
                roomNumber.getItems().clear();
                roomNumber.getItems().addAll(doubleRooms);
            } else if (new_val.equals("Self-Contained 297")) {
                roomNumber.getItems().clear();
                roomNumber.getItems().addAll(sc297Rooms);
            } else if (new_val.equals("Self-Contained 308")) {
                roomNumber.getItems().clear();
                roomNumber.getItems().addAll(sc308Rooms);
            }
        });
        
        date.valueProperty().addListener((ObservableValue<? extends LocalDate> ov, LocalDate old_val, LocalDate new_val) -> {
            dateLabel.setText("");
            emitLabel.setText("");
        });
        
        name.textProperty().addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
            nameLabel.setText("");
            emitLabel.setText("");
        });
        
        receipt.textProperty().addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
            receiptLabel.setText("");
            emitLabel.setText("");
        });
        
        roomType.valueProperty().addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
            roomTypeLabel.setText("");
            emitLabel.setText("");
        });
        
        nights.textProperty().addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
            numNightsLabel.setText("");
            emitLabel.setText("");
        });
        
        roomNumber.valueProperty().addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
            roomNumberLabel.setText("");
            emitLabel.setText("");
        });
    }
}
