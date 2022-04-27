package JFrameModule;


import util.ArrayUtils;
import util.JTableUtils;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import LogicModule.Logic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Frame extends JFrame{
    private JPanel panelMain;
    private JTable tableInput;
    private JTable tableOutput;
    private JButton buttonGetResultByInverseMatrix;
    private JButton buttonGetResultByKramer;
    private JButton buttonLoadFromFile;
    private JButton buttonGetResultByGauss;
    private final JFileChooser fileChooserOpen;


    public Frame() {
        this.setTitle("Task3");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panelMain);
        this.pack();


        JTableUtils.initJTableForArray(tableInput, 40, true, true, true, true);
        JTableUtils.resizeJTable(tableInput, 3,4, 30, 70);


        JTableUtils.initJTableForArray(tableOutput, 40, true, true, false, false);
        JTableUtils.resizeJTable(tableOutput, 1,1, 30, 70);
        tableOutput.setEnabled(false);

        fileChooserOpen = new JFileChooser();
        fileChooserOpen.setCurrentDirectory(new File("."));
        FileFilter filter = new FileNameExtensionFilter("Text files", "txt");
        fileChooserOpen.addChoosableFileFilter(filter);

        this.setVisible(true);
        this.setSize(860, 500);

        buttonGetResultByInverseMatrix.addActionListener(e -> {
            try {
                int[][] coefficients = JTableUtils.readIntMatrixFromJTable(tableInput);
                Logic.checkIfArrayIsNull(coefficients);

                int[][] matrixA = new int[coefficients.length][coefficients[0].length - 1];
                Logic.checkIfArrayIsSquare(matrixA);
                int[][] matrixB = new int[coefficients.length][1];

                for (int i = 0; i < coefficients.length; i++) {
                    for (int j = 0; j < coefficients[0].length - 1; j++) {
                        matrixA[i][j] = coefficients[i][j];
                    }
                }

                for (int i = 0; i < coefficients.length; i++) {
                    matrixB[i][0] = coefficients[i][coefficients[0].length-1];
                }

                double[][] result = Logic.getResultByInverseMatrixFrame(matrixA, matrixB);

                JTableUtils.writeArrayToJTable(tableOutput, result);
            } catch (Exception ex) {
                displayError("Ошибка в исходных данных");
            }
        });

        buttonGetResultByKramer.addActionListener(e -> {
            try {
                int[][] coefficients = JTableUtils.readIntMatrixFromJTable(tableInput);
                Logic.checkIfArrayIsNull(coefficients);

                int[][] matrixA = new int[coefficients.length][coefficients[0].length - 1];
                Logic.checkIfArrayIsSquare(matrixA);
                int[][] matrixB = new int[coefficients.length][1];

                for (int i = 0; i < coefficients.length; i++) {
                    for (int j = 0; j < coefficients[0].length - 1; j++) {
                        matrixA[i][j] = coefficients[i][j];
                    }
                }

                for (int i = 0; i < coefficients.length; i++) {
                    matrixB[i][0] = coefficients[i][coefficients[0].length-1];
                }

                double[][] result = Logic.getResultByKramerFrame(matrixA, matrixB);

                JTableUtils.writeArrayToJTable(tableOutput, result);
            } catch (Exception ex) {
                displayError("Ошибка в исходных данных");
            }
        });

        buttonGetResultByGauss.addActionListener(e -> {
            try {
                int[][] matrix = JTableUtils.readIntMatrixFromJTable(tableInput);
                Logic.checkIfArrayIsNull(matrix);


                double[][] result = Logic.getResultByGaussFrame(matrix);

                JTableUtils.writeArrayToJTable(tableOutput, result);
            } catch (Exception ex) {
                displayError("Ошибка в исходных данных");
            }
        });

        buttonLoadFromFile.addActionListener(actionEvent -> {
            try {
                if (fileChooserOpen.showOpenDialog(panelMain) == JFileChooser.APPROVE_OPTION) {
                    int[][] arr = ArrayUtils.readIntArray2FromFile(fileChooserOpen.getSelectedFile().getPath());

                    JTableUtils.writeArrayToJTable(tableInput, arr);
                }
            } catch (Exception e) {
                displayError("Ошибка в исходных данных");
            }
        });
    }



    private void displayError(String errorText) {
        JOptionPane.showMessageDialog(this, errorText,
                "Ошибка", JOptionPane.ERROR_MESSAGE);
    }
    private void displayMessage(String messageText) {
        JOptionPane.showMessageDialog(this, messageText,
                "Сообщение", JOptionPane.INFORMATION_MESSAGE);
    }
}
