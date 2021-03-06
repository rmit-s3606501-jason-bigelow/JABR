package org.jabst.jabs;

import javafx.event.ActionEvent;//type of event
import javafx.event.EventHandler;//this activates when a button is pressed
import javafx.geometry.Insets;//insets = padding
import javafx.scene.control.*;//buttons, labels  etc.
import javafx.scene.layout.VBox;//layout manager
import javafx.scene.layout.HBox;
import javafx.scene.Scene;//area inside stage
import javafx.stage.WindowEvent;//when window closes
import javafx.stage.Stage;//window

import java.sql.SQLException;

public class CreateBusinessGUI {

    private static DatabaseManager dbm;

    public static void display(SessionManager session) {
        /* Data getters */
        dbm = session.getDatabaseManager();

        /* Commons elements */
        Insets ins = new Insets(3.0, 3.0, 3.0, 3.0);
        double width = 200;
        
        /* Setup window elements */
        Stage window = new Stage();
        VBox root = new VBox();//layout manager
            Label lUsername = new Label("Username:");
            TextField tfUName = new TextField();
            Label lPassword = new Label("Password:");
            PasswordField tfPassword = new PasswordField(); 
            Label lBusName = new Label("Business name:");
            TextField tfBusName = new TextField();
            Label lOwnerName = new Label("Owner name:");
            TextField tfOwnerName = new TextField();
            Label lAddress = new Label("Address:");
            TextField tfAddress = new TextField();
            Label lPhone = new Label("Phone number:");
            TextField tfPhone = new TextField();

            HBox hbButtons = new HBox();
                Button btCreate = new Button("Create");
                btCreate.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        /* Validate inputs given */
                        boolean valid = true;
                        valid &=
                        session.validateUsernameInput(tfUName.getText());
                        if (!valid) return;
                        System.out.println("Username valid");
                        valid &=
                        session.validatePasswordStrength(tfPassword.getText());
                        if (!valid) return;
                        System.out.println("Password valid");
                        valid &=
                        session.validateAddressInput(tfAddress.getText());
                        if (!valid) return;
                        System.out.println("Address valid");
                        valid &=
                        session.validatePhoneInput(tfPhone.getText());
                        if (!valid) return;
                        System.out.println("Phone valid, adding");
                        try {
                            dbm.registerBusiness(
                                tfUName.getText(),
                                tfPassword.getText(),
                                tfBusName.getText(),
                                tfOwnerName.getText(),
                                tfAddress.getText(),
                                tfPhone.getText()
                            );
                            window.close();
                        } catch (SQLException sqle) {
                            sqle.printStackTrace();
                        }
                    }
                });
                Button btCancel = new Button("Cancel");
                btCancel.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e) {
                        window.close();
                    }
                });
            hbButtons.getChildren().addAll(btCreate, btCancel);
        root.getChildren().addAll(
                lUsername, tfUName,
                lPassword, tfPassword,
                lBusName, tfBusName,
                lOwnerName, tfOwnerName,
                lAddress, tfAddress,
                lPhone, tfPhone,
                hbButtons
        );
        /* Root spacing and padding */
        root.setSpacing(2);
        root.setPadding(new Insets(5.0, 0.0, 5.0, 10.0));
        
        Scene scene = new Scene(root, 300, 300);//create area inside window
        /* Close window on request */
        window.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                System.out.println("Business Menu window Closed");
            }
        });
        /* Window visual setup */
        window.setTitle("JABST: Creating new business");
        window.setScene(scene);//add scene to window
        window.showAndWait();//put the window on the desktop
    }
}
