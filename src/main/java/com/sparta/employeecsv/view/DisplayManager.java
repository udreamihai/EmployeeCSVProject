package com.sparta.employeecsv.view;

import com.sparta.employeecsv.controller.CSVController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.util.Scanner;

import static com.sparta.employeecsv.CSVMain.logger;

public class DisplayManager {

    public JFrame frame;
    private JTextField filenameFld;
    private JLabel lblUniqueNumber;
    private JLabel lblDupNumber;
    private JLabel lblCurNumber;
    private JTextArea duplicateListFld;

    /**
     * Initialize the contents of the frame.
     */
    public void initialize(ActionListener buttonPress, WindowAdapter closeEvent) {

        frame = new JFrame();
        frame.setResizable(false);
        frame.setBounds(100, 100, 673, 507);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.setAlwaysOnTop(true);
        frame.addWindowListener(closeEvent);

        JLabel lblNewLabel = new JLabel("Enter file name:");
        lblNewLabel.setBounds(46, 77, 106, 14);
        frame.getContentPane().add(lblNewLabel);

        filenameFld = new JTextField();
        filenameFld.setBounds(141, 74, 348, 20);
        frame.getContentPane().add(filenameFld);
        filenameFld.setColumns(10);

        JLabel lblNewLabel_1 = new JLabel("Number of unique records:");
        lblNewLabel_1.setBounds(46, 150, 250, 14);
        frame.getContentPane().add(lblNewLabel_1);

        JLabel lblNewLabel_2 = new JLabel("Number of duplicate records:");
        lblNewLabel_2.setBounds(46, 180, 250, 14);
        frame.getContentPane().add(lblNewLabel_2);

        JLabel lblNewLabel_3 = new JLabel("Number of corrupted records:");
        lblNewLabel_3.setBounds(46, 120, 250, 14);
        frame.getContentPane().add(lblNewLabel_3);

        lblUniqueNumber = new JLabel("N/A");
        lblUniqueNumber.setBounds(240, 150, 78, 14);
        frame.getContentPane().add(lblUniqueNumber);

        lblDupNumber = new JLabel("N/A");
        lblDupNumber.setBounds(240, 180, 78, 14);
        frame.getContentPane().add(lblDupNumber);

        lblCurNumber = new JLabel("N/A");
        lblCurNumber.setBounds(240, 120, 78, 14);
        frame.getContentPane().add(lblCurNumber);

        JLabel lblNewLabel_4 = new JLabel("Duplicated records listed:");
        lblNewLabel_4.setBounds(46, 214, 200, 14);
        frame.getContentPane().add(lblNewLabel_4);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(46, 239, 570, 218);
        frame.getContentPane().add(scrollPane);

        duplicateListFld = new JTextArea();
        scrollPane.setViewportView(duplicateListFld);

        JLabel lblNewLabel_5 = new JLabel("Employees CSV");
        lblNewLabel_5.setFont(new Font("Tahoma", Font.PLAIN, 28));
        lblNewLabel_5.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_5.setBounds(10, 11, 637, 40);
        frame.getContentPane().add(lblNewLabel_5);

        JButton btnStart = new JButton("Start");
        btnStart.setBounds(500, 73, 89, 23);
        frame.getContentPane().add(btnStart);
        btnStart.addActionListener(buttonPress);

    }

    /**
     * display number of duplicate records on the GUI
     *
     * @param numberOfDuplicates Integer value of number of duplicate record
     */
    public void setDuplicateNumber(int numberOfDuplicates) {
        lblDupNumber.setText(String.valueOf(numberOfDuplicates));
    }

    /**
     * display number of unique records on the GUI
     *
     * @param numberOfUnique Integer value of number of unique record
     */
    public void setUniqueNumber(int numberOfUnique) {
        lblUniqueNumber.setText(String.valueOf(numberOfUnique));
    }

    public void setCorruptedNumber(int numberOfCorrupted) {
        lblCurNumber.setText(String.valueOf(numberOfCorrupted));
    }

    /**
     * Gets a list of object with duplicate records and toString Function. Prints it on the Text Area in
     * GUI for duplicate record list
     */
    public void listDuplicates(String duplicates) {
        duplicateListFld.setText(duplicates);
    }

    /**
     * @return the file name provided by the user
     */
    public String getFilename() {
        return filenameFld.getText();
    }

    /**
     * @return the amount of threads entered by the user
     */
    public String getThreadCount() {

        System.out.print("Please enter the desired number of threads (1 - 100): ");

        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();

        return input;

    }

    /**
     * Simply display an error message when thread count is not in the range
     */
    public void displayInvalidThreadMsg() {
        System.out.print("Invalid thread count please input a number between 1 and 100!");
    }

    /**
     * Display the timings of the reading from the database
     */
    public void displayReadingTime(long startTime, long endTime) {
        System.out.println("Reading records took: " + ((double) (endTime - startTime)) / 1_000_000_000 + " seconds");
        logger.info("Reading records took: " + ((double) (endTime - startTime)) / 1_000_000_000 + " seconds");
    }

    /**
     * Displays the timings of the writing to the database
     */
    public void displayWritingTime(long startTime, long endTime) {
        System.out.println("Writing records to database took: " + ((double) (endTime - startTime)) / 1_000_000_000 + " seconds");
        logger.info("Writing records to database took: " + ((double) (endTime - startTime)) / 1_000_000_000 + " seconds");
    }

    /**
     * Displays the options to the user when it comes to selecting records from the database
     */
    public void getSelectOption(CSVController controller){
        Scanner sc = new Scanner(System.in);
        System.out.print("\nSelect all employees?(ALL) or select a single employee?(SINGLE) or quit(EXIT): ");

        String allEmployeeOrSingle = sc.next();

        switch (allEmployeeOrSingle.toLowerCase()){
            case "all" -> {
                System.out.println(controller.getEmployees());
            }
            case "single" -> {
                System.out.print("Enter the specific employee ID: ");
                int empID = sc.nextInt();
                if (controller.getEmployeeById(empID) == null)
                    System.out.println("The record with provided employee ID does not exist");
                else
                    System.out.println(controller.getEmployeeById(empID));
            }
            case "exit", "quit", "close" -> {
                System.out.println("\nClosing the application...");
                System.exit(0);
            }
            default -> {
                logger.warn("User entered an invalid option for selecting employees");
                System.out.println("Input invalid, please try again");
                getSelectOption(controller);
            }
        }
    }
}

