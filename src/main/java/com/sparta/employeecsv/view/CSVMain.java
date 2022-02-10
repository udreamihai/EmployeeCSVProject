package com.sparta.employeecsv.view;

import com.sparta.employeecsv.controller.CSVController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;

public class CSVMain {

    public static Logger logger = LogManager.getLogger("CSV-Logger");

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {

                    CSVController controller = new CSVController();
                    controller.setupDatabase();
                    DisplayManager window = new DisplayManager(controller);

                    window.initialize();
                    window.frame.setVisible(true);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
