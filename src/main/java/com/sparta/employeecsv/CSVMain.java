package com.sparta.employeecsv;

import com.sparta.employeecsv.controller.CSVController;
import com.sparta.employeecsv.view.DisplayManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;

public class CSVMain {

    public static Logger logger = LogManager.getLogger("CSV-Logger");

    private static DisplayManager displayManager;
    private static CSVController controller;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {

        controller = new CSVController();
        logger.debug("Successfully created controller");
        displayManager = new DisplayManager();
        logger.debug("Successfully created view");

        controller.setupDatabase();

        int threadCount = getThreadCountMain();

        // This executes after the button is pressed
        ActionListener buttonEvent = e -> {

            String filename = displayManager.getFilename();

            long readStartTime = System.nanoTime();
            controller.readFile(filename);
            displayManager.displayReadingTime(readStartTime, System.nanoTime());

            displayManager.setDuplicateNumber(controller.getDuplicateCount());
            displayManager.setUniqueNumber(controller.getEmployeeCount());
            displayManager.setCorruptedNumber(controller.getCorruptedCount());

            displayManager.listDuplicates(controller.getDuplicatesString());

            long writeStartTime = System.nanoTime();
            long writeEndTime = controller.insertRecordsToDatabaseThreads(threadCount);
            displayManager.displayWritingTime(writeStartTime, writeEndTime);

             while(true){
                 displayManager.getSelectOption(controller);
             }
        };

        WindowAdapter closeEvent = new WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                controller.cleanUpDatabase();
                System.exit(0);
            }
        };

        displayManager.initialize(buttonEvent, closeEvent);
        logger.debug("JFrame was initialized");
        displayManager.frame.setVisible(true);
    }

    /**
     * Get the amount of threads from the user
     */
    public static int getThreadCountMain() {

        String threadCountStr = displayManager.getThreadCount();
        boolean isValidThreadCount = controller.checkThreadCount(threadCountStr);

        while (!isValidThreadCount) {
            logger.warn("Invalid thread count input retrying");
            displayManager.displayInvalidThreadMsg();
            threadCountStr = displayManager.getThreadCount();
            isValidThreadCount = controller.checkThreadCount(threadCountStr);
        }
        return controller.parseThreadCount(threadCountStr);
    }
}