package gui;

import grid.Grid;
import grid.Segment;

import exceptions.ABadBoundsException;
import exceptions.ABadInputTypeException;
import exceptions.AlphaBadInputTypeException;
import exceptions.BBadBoundsException;
import exceptions.BBadInputTypeException;
import exceptions.CoefBadBoundsException;
import exceptions.CoefBadInputTypeException;
import exceptions.DistanceBadInputTypeException;
import exceptions.FuelBadInputTypeException;
import exceptions.TollBadInputTypeException;

import java.awt.Color;
import java.awt.Font;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import jxl.read.biff.BiffException;

import org.math.plot.*;
import org.math.plot.plotObjects.BaseLabel;
 
class XmlFilter extends javax.swing.filechooser.FileFilter {
        @Override
        public boolean accept(File file) {
            // Allow only directories, or files with ".xml" extension
            return file.isDirectory() || file.getAbsolutePath().endsWith(".xml");
        }
        @Override
        public String getDescription() {
            // This description will be displayed in the dialog,
            // hard-coded = ugly, should be done via I18N
            return "XML documents (*.xml)";
        }
} 

class XlsFilter extends javax.swing.filechooser.FileFilter {
        @Override
        public boolean accept(File file) {
            // Allow only directories, or files with ".xml" extension
            return file.isDirectory() || file.getAbsolutePath().endsWith(".xls");
        }
        @Override
        public String getDescription() {
            // This description will be displayed in the dialog,
            // hard-coded = ugly, should be done via I18N
            return ".xls documents (*.xls)";
        }
} 

public class mainForm extends javax.swing.JFrame {

	private static final long serialVersionUID = 1L;
	private Grid grid;
    private double bestTime;
    private double bestCost;
    private List<Segment> bestTimePath;
    private List<Segment> bestCostPath;
    private List<Segment> probList;
    private double probTime;
    private int numberOfFixedRadars;
    
    public mainForm() {
        initComponents();
        
        grid = new Grid();
    }

    public boolean isInteger(String input){  
        try{  
            Integer.parseInt(input);  
            return true;  
        }  
        catch(Exception e){  
            return false;  
        }  
    }  
    
    public boolean isDouble(String input){  
        try{  
            Double.parseDouble(input);
            return true;  
        }  
        catch(Exception e){  
            return false;  
        }  
    }
        
    private void initComponents() {

        jLabelAlpha = new javax.swing.JLabel();
        jTextFieldAlpha = new javax.swing.JTextField();
        jLabelFuel = new javax.swing.JLabel();
        jTextFieldFuel = new javax.swing.JTextField();
        jLabelToll = new javax.swing.JLabel();
        jTextFieldToll = new javax.swing.JTextField();
        jLabelDist = new javax.swing.JLabel();
        jTextFieldDist = new javax.swing.JTextField();
        jLabelA = new javax.swing.JLabel();
        jTextFieldA = new javax.swing.JTextField();
        jLabelB = new javax.swing.JLabel();
        jTextFieldB = new javax.swing.JTextField();
        jButtonReadXls = new javax.swing.JButton();
        jButtonReadXml = new javax.swing.JButton();
        jButtonOptTime = new javax.swing.JButton();
        jButtonOptCost = new javax.swing.JButton();
        jPanelDeterministicResults = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaDeterministicResults = new javax.swing.JTextArea();
        jButtonVisualizeOptCost = new javax.swing.JButton();
        jButtonVisualizeOptTime = new javax.swing.JButton();
        jButtonVisualizeRadars = new javax.swing.JButton();
        jButtonQuit = new javax.swing.JButton();
        jPanelProbPproblem = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaProbResults = new javax.swing.JTextArea();
        jLabelCoef = new javax.swing.JLabel();
        jTextFieldCoef = new javax.swing.JTextField();
        jLabelProbResults = new javax.swing.JLabel();
        jButtonVisualizeProbResults = new javax.swing.JButton();
        jButtonCalculate = new javax.swing.JButton();
        jLabelMobileRadars = new javax.swing.JLabel();
        jSpinnerRadars = new javax.swing.JSpinner();
        jButtonDefineParameters = new javax.swing.JButton();
        jFileChooserXml = new javax.swing.JFileChooser();
        jFileChooserXls = new javax.swing.JFileChooser();
        jButtonNewProblem = new javax.swing.JButton();
        jMenuBar = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuItemNewProblem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuItemReadFromXml = new javax.swing.JMenuItem();
        jMenuItemReadFromXls = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItemExit = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Choix du meilleur itinéraire");

        jLabelAlpha.setText("Alpha:");

        jTextFieldAlpha.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextFieldAlpha.setText("5");

        jLabelFuel.setText("Carburant [€/L]:");

        jTextFieldFuel.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextFieldFuel.setText("1.3");

        jLabelToll.setText("Péage [€]:");

        jTextFieldToll.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextFieldToll.setText("3");

        jLabelDist.setText("Distance par segment [km]:");

        jTextFieldDist.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextFieldDist.setText("100");

        jLabelA.setText("A:");

        jTextFieldA.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextFieldA.setText("0.0625");

        jLabelB.setText("B:");

        jTextFieldB.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextFieldB.setText("1.875");

        jButtonReadXls.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Excel.png"))); // NOI18N
        jButtonReadXls.setText("Lire de .xls");
        jButtonReadXls.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReadXlsActionPerformed(evt);
            }
        });

        jButtonReadXml.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/xml.gif"))); // NOI18N
        jButtonReadXml.setText("Lire de XML");
        jButtonReadXml.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReadXmlActionPerformed(evt);
            }
        });

        jButtonOptTime.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/stopwatch_start.png"))); // NOI18N
        jButtonOptTime.setText("Trouver le temps optimal");
        jButtonOptTime.setEnabled(false);
        jButtonOptTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOptTimeActionPerformed(evt);
            }
        });

        jButtonOptCost.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Euro.jpg"))); // NOI18N
        jButtonOptCost.setText("Trouver le coût optimal");
        jButtonOptCost.setEnabled(false);
        jButtonOptCost.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOptCostActionPerformed(evt);
            }
        });

        jPanelDeterministicResults.setBorder(javax.swing.BorderFactory.createTitledBorder("Résultats"));

        jTextAreaDeterministicResults.setColumns(20);
        jTextAreaDeterministicResults.setFont(new java.awt.Font("Courier New", 0, 11));
        jTextAreaDeterministicResults.setRows(5);
        jTextAreaDeterministicResults.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jScrollPane1.setViewportView(jTextAreaDeterministicResults);

        jButtonVisualizeOptCost.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Diagram.png"))); // NOI18N
        jButtonVisualizeOptCost.setText("Visualiser le coût optimal");
        jButtonVisualizeOptCost.setEnabled(false);
        jButtonVisualizeOptCost.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVisualizeOptCostActionPerformed(evt);
            }
        });

        jButtonVisualizeOptTime.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Diagram.png"))); // NOI18N
        jButtonVisualizeOptTime.setText("Visualiser le temps optimal");
        jButtonVisualizeOptTime.setEnabled(false);
        jButtonVisualizeOptTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVisualizeOptTimeActionPerformed(evt);
            }
        });

        jButtonVisualizeRadars.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Diagram.png"))); // NOI18N
        jButtonVisualizeRadars.setText("Visualiser les radars");
        jButtonVisualizeRadars.setEnabled(false);
        jButtonVisualizeRadars.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVisualizeRadarsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelDeterministicResultsLayout = new javax.swing.GroupLayout(jPanelDeterministicResults);
        jPanelDeterministicResults.setLayout(jPanelDeterministicResultsLayout);
        jPanelDeterministicResultsLayout.setHorizontalGroup(
            jPanelDeterministicResultsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDeterministicResultsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelDeterministicResultsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 642, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelDeterministicResultsLayout.createSequentialGroup()
                        .addComponent(jButtonVisualizeRadars)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonVisualizeOptTime)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonVisualizeOptCost)))
                .addContainerGap())
        );
        jPanelDeterministicResultsLayout.setVerticalGroup(
            jPanelDeterministicResultsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDeterministicResultsLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelDeterministicResultsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonVisualizeOptCost)
                    .addComponent(jButtonVisualizeOptTime)
                    .addComponent(jButtonVisualizeRadars))
                .addContainerGap())
        );

        jButtonQuit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Exit.png"))); // NOI18N
        jButtonQuit.setText("Quitter");
        jButtonQuit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonQuitActionPerformed(evt);
            }
        });

        jPanelProbPproblem.setBorder(javax.swing.BorderFactory.createTitledBorder("Problème probabiliste:"));

        jTextAreaProbResults.setColumns(20);
        jTextAreaProbResults.setFont(new java.awt.Font("Courier New", 0, 11));
        jTextAreaProbResults.setRows(5);
        jTextAreaProbResults.setEnabled(false);
        jScrollPane2.setViewportView(jTextAreaProbResults);

        jLabelCoef.setText("Coefficient:");
        jLabelCoef.setEnabled(false);

        jTextFieldCoef.setText("0.8");
        jTextFieldCoef.setEnabled(false);
        jTextFieldCoef.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldCoefActionPerformed(evt);
            }
        });

        jLabelProbResults.setText("Résultats:");
        jLabelProbResults.setEnabled(false);

        jButtonVisualizeProbResults.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Diagram.png"))); // NOI18N
        jButtonVisualizeProbResults.setText("Visualiser les résultats");
        jButtonVisualizeProbResults.setEnabled(false);
        jButtonVisualizeProbResults.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVisualizeProbResultsActionPerformed(evt);
            }
        });

        jButtonCalculate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/calculator.png"))); // NOI18N
        jButtonCalculate.setText("Calculer");
        jButtonCalculate.setEnabled(false);
        jButtonCalculate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCalculateActionPerformed(evt);
            }
        });

        jLabelMobileRadars.setText("Nombre de radars mobiles:");
        jLabelMobileRadars.setEnabled(false);

        jSpinnerRadars.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jSpinnerRadars.setEnabled(false);
        jSpinnerRadars.setValue(20);

        javax.swing.GroupLayout jPanelProbPproblemLayout = new javax.swing.GroupLayout(jPanelProbPproblem);
        jPanelProbPproblem.setLayout(jPanelProbPproblemLayout);
        jPanelProbPproblemLayout.setHorizontalGroup(
            jPanelProbPproblemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelProbPproblemLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelProbPproblemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelProbPproblemLayout.createSequentialGroup()
                        .addComponent(jLabelCoef)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldCoef, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelMobileRadars)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinnerRadars, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabelProbResults)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 642, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelProbPproblemLayout.createSequentialGroup()
                        .addComponent(jButtonCalculate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonVisualizeProbResults)))
                .addContainerGap())
        );
        jPanelProbPproblemLayout.setVerticalGroup(
            jPanelProbPproblemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelProbPproblemLayout.createSequentialGroup()
                .addGroup(jPanelProbPproblemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldCoef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelCoef)
                    .addComponent(jSpinnerRadars, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelMobileRadars))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelProbResults)
                .addGap(5, 5, 5)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelProbPproblemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonVisualizeProbResults)
                    .addComponent(jButtonCalculate))
                .addContainerGap())
        );

        jButtonDefineParameters.setText("Définir les paramètres");
        jButtonDefineParameters.setEnabled(false);
        jButtonDefineParameters.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDefineParametersActionPerformed(evt);
            }
        });

        jFileChooserXml.setDialogTitle("Sélectionnez le fichier xml");
        jFileChooserXml.setFileFilter(new XmlFilter());

        jFileChooserXls.setFileFilter(new XlsFilter());

        jButtonNewProblem.setText("Nouveau problème");
        jButtonNewProblem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewProblemActionPerformed(evt);
            }
        });

        jMenuFile.setText("Fichier");

        jMenuItemNewProblem.setText("Nouveau problème");
        jMenuItemNewProblem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemNewProblemActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemNewProblem);
        jMenuFile.add(jSeparator2);

        jMenuItemReadFromXml.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/xml.gif"))); // NOI18N
        jMenuItemReadFromXml.setText("Lire de .xml");
        jMenuItemReadFromXml.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemReadFromXmlActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemReadFromXml);

        jMenuItemReadFromXls.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Excel.png"))); // NOI18N
        jMenuItemReadFromXls.setText("Lire de .xls");
        jMenuItemReadFromXls.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemReadFromXlsActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemReadFromXls);
        jMenuFile.add(jSeparator1);

        jMenuItemExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Exit.png"))); // NOI18N
        jMenuItemExit.setText("Quittez");
        jMenuItemExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemExitActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemExit);

        jMenuBar.add(jMenuFile);

        setJMenuBar(jMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanelProbPproblem, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelDeterministicResults, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabelAlpha)
                                    .addComponent(jLabelFuel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextFieldFuel)
                                    .addComponent(jTextFieldAlpha, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelB)
                                    .addComponent(jLabelA))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jTextFieldB)
                                    .addComponent(jTextFieldA, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButtonReadXls, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonReadXml)
                                .addGap(17, 17, 17)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabelDist)
                                    .addComponent(jLabelToll))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jTextFieldToll, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextFieldDist, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 86, Short.MAX_VALUE)
                                .addComponent(jButtonDefineParameters))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jFileChooserXls, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                                .addComponent(jButtonOptTime)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonOptCost)))
                        .addGap(28, 28, 28))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonNewProblem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonQuit)))
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(36, 36, 36)
                    .addComponent(jFileChooserXml, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(637, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldAlpha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelAlpha))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabelFuel)
                            .addComponent(jTextFieldFuel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabelA)
                                .addComponent(jTextFieldA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabelToll)
                                .addComponent(jTextFieldToll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButtonDefineParameters)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelB)
                            .addComponent(jTextFieldB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelDist)
                            .addComponent(jTextFieldDist, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonReadXls)
                            .addComponent(jButtonReadXml))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonOptTime)
                            .addComponent(jButtonOptCost)))
                    .addComponent(jFileChooserXls, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelDeterministicResults, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelProbPproblem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonQuit)
                    .addComponent(jButtonNewProblem))
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(272, 272, 272)
                    .addComponent(jFileChooserXml, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(315, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>                        

    private void jButtonQuitActionPerformed(java.awt.event.ActionEvent evt) {                                            
        System.exit(0);
    }                                           

    @SuppressWarnings("static-access")
	private void jButtonReadXmlActionPerformed(java.awt.event.ActionEvent evt) {                                               
        try{
            //initializing from XML
        
            //text - the text on the jTextArea
            //tempText - the temporary text
            String text = "";
            String tempText;
            boolean b = true;
        
            int returnVal = jFileChooserXml.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = jFileChooserXml.getSelectedFile();
                text = text.concat(file.getAbsolutePath() + "\n");
                try {
                    // What to do with the file, e.g. display it in a TextArea
                    b = grid.initializeFromXml(file.getAbsolutePath());
                    this.jTextAreaDeterministicResults.read(new FileReader( file.getAbsolutePath() ), null );
                }    
            
                catch (IOException ex) {
                    System.out.println("Problème d'accès du fichier " + file.getAbsolutePath() + "\n");
                    
                    StringWriter sw = new StringWriter();
                    ex.printStackTrace(new PrintWriter(sw));
                    String msg = "Problème d'accès du fichier \n";
                    msg = msg.concat(sw.toString());
                    msg = msg.substring(0, 500) + " ...";
            
                    JFrame frame = new JFrame();
                    JOptionPane.showMessageDialog(frame,
                        msg,
                            "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                    b = false;
                }
                
                catch (java.lang.NullPointerException ex){
                    System.out.println("Nullpointer exception! \n");
                    
                    StringWriter sw = new StringWriter();
                    ex.printStackTrace(new PrintWriter(sw));
                    String msg = "Nullpointer exception! \n";
                    msg = msg.concat(sw.toString());
                    msg = msg.substring(0, 500) + " ...";
            
                    JFrame frame = new JFrame();
                    JOptionPane.showMessageDialog(frame,
                        msg,
                            "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                    b = false;
                }
            } else {
                System.out.println("File access cancelled by user.");
                b = false;
            }
        
            if (b == true){
                //adding m and n to the text
                tempText = ("\nm = " + Integer.toString(grid.getM()) + "\n");
                text = text.concat(tempText);
                tempText = ("n = " + Integer.toString(grid.getN()) + "\n");
                text = text.concat(tempText);
        
                //adding the horizontal speed data to the text
                text = text.concat("\n\nLes limitations de vitesse sur les segments horizontaux: \n");
                tempText = "";
                for (int i=0; i<grid.getM(); i++){		
                    for (int j=0; j<=grid.getN(); j++)
                        tempText = tempText.concat(Integer.toString(grid.getSpeedHorizontal(i, j)).format("%5d",grid.getSpeedHorizontal(i, j)));        
                    tempText = tempText.concat("\n");	
                }
        
                text = text.concat(tempText);
                tempText = "";
        
                //adding the vertical speed data to the text
                text = text.concat("\n\nLes limitations de vitesse sur les segments verticaux: \n");
                for (int i=0; i<=grid.getM(); i++){
                    for (int j=0; j<grid.getN(); j++)
                    tempText = tempText.concat(Integer.toString(grid.getSpeedVertical(i, j)).format("%5d",grid.getSpeedVertical(i, j)));
                    tempText = tempText.concat("\n");
                }
        
                text = text.concat(tempText);
                tempText = "";
        
                //count the number of fixed radars
                this.numberOfFixedRadars = 0;
        
                //count horizontal radars
                for (int i=0; i<grid.getM(); i++)
                    for (int j=0; j<=grid.getN(); j++)
                        if (grid.getHorizontalFixedRadar(i, j) != 0)
                            this.numberOfFixedRadars++;
        
                //count vertical radars
                for (int i=0; i<=grid.getM(); i++)
                    for (int j=0; j<grid.getN(); j++)
                        if (grid.getVerticalFixedRadar(i, j) != 0)
                            this.numberOfFixedRadars++;
        
                text = text.concat("\nLe nombre de radars fixes: " + this.numberOfFixedRadars);
        
                //adding the horizontal radars to the text
                text = text.concat("\n\nRadars fixes horizontaux: \n");
                for(int i=0; i<grid.getM(); i++){
                    for(int j=0; j<=grid.getN(); j++){
                        tempText = tempText.concat(Integer.toString(grid.getHorizontalFixedRadar(i, j)).format("%5d", grid.getHorizontalFixedRadar(i, j)));
                    }	
                    tempText = tempText.concat("\n");
                }
        
                text = text.concat(tempText);
                tempText = "";
        
                //adding the vertical radars to the text
                text = text.concat("\n\nRadars fixes verticales: \n");
            
                for(int i=0; i<=grid.getM(); i++){
                    for(int j=0; j<grid.getN(); j++){
                        tempText = tempText.concat(Integer.toString(grid.getVerticalFixedRadar(i, j)).format("%5d", grid.getVerticalFixedRadar(i, j)));
                    }	
                    tempText = tempText.concat("\n");
                }
        
                text = text.concat(tempText);
                tempText = "";
        
                this.jTextAreaDeterministicResults.setText(text);
                this.jButtonVisualizeRadars.setEnabled(true);
                this.jButtonReadXls.setEnabled(false);
                this.jButtonReadXml.setEnabled(false);
                this.jButtonDefineParameters.setEnabled(true);
                this.jMenuItemReadFromXls.setEnabled(false);
                this.jMenuItemReadFromXml.setEnabled(false);
            }
        }
        
        catch(Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String msg = sw.toString();
            msg = msg.substring(0, 500) + " ...";
            
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame,
                msg,
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }                                              

    private void jButtonOptTimeActionPerformed(java.awt.event.ActionEvent evt) {                                               
        try{
            //add parameters to text
            String text = this.jTextAreaDeterministicResults.getText();
                
            grid.setOptCostHorizontal();
            grid.setOptCostVertical();
        
            grid.setOptTimeHorizontal();
            grid.setOptTimeVertical();
		
            grid.setOptTimeHorizontal();
            grid.setOptTimeVertical();
        
            grid.setOptimalTime(0, 0);
            this.bestTimePath = grid.getOptimalTimePathSegments(0, ((grid.getM()+1)*(grid.getN()+1) - 1));
            text = text.concat("\n\nLa vitesse optimale chemin est: \n");
            text = text.concat(".----------------------------------------------------------.\n");
            text = text.concat("|Segment   |    Vitesse|    Temps|      Coût| Radars| Péage|\n");
            text = text.concat("|----------------------------------------------------------|\n");
            for (int i=0; i<this.bestTimePath.size(); i++){
                double timeDouble = (double)Math.round(this.bestTimePath.get(i).getTime() * 100);
                timeDouble = timeDouble / 100;
                String timeTempString = String.format("%9f", timeDouble);
                String timeString = timeTempString.substring(0, (timeTempString.length() - 4));

                double costDouble = (double) Math.round(this.bestTimePath.get(i).getCost() * 100);
                costDouble = costDouble / 100;
                String costTempString = String.format("%10f", costDouble);
                String costString = costTempString.substring(0, (costTempString.length() - 4));
            
                int speedInt = (int) this.bestTimePath.get(i).getSpeed();      
                String speedString = String.format("%4d", speedInt);
            
                int radars;
                if (this.bestTimePath.get(i).getLetter() == 'H')
                    radars = grid.getHorizontalFixedRadar(this.bestTimePath.get(i).getX(), this.bestTimePath.get(i).getY());
                else
                    radars = grid.getVerticalFixedRadar(this.bestTimePath.get(i).getX(), this.bestTimePath.get(i).getY());
                String radarsString = String.format("%6d", radars);
            
                String tollString;
                if (this.bestTimePath.get(i).getLetter() == 'H'){
                    if(grid.getSpeedHorizontal(this.bestTimePath.get(i).getX(), this.bestTimePath.get(i).getY()) == 130)
                        tollString = "Oui";
                    else
                        tollString = "Non";
                }
                else{
                    if(grid.getSpeedVertical(this.bestTimePath.get(i).getX(), this.bestTimePath.get(i).getY()) == 130)
                        tollString = "Oui";
                    else
                        tollString = "Non";
                }
            
                text = text.concat("|" + this.bestTimePath.get(i).toString() + " | ");
                text = text.concat(speedString + " km/h | ");
                text = text.concat(timeString + " h | ");
                text = text.concat(costString + " € |");
                text = text.concat(radarsString + " |");
                text = text.concat("  " + tollString + " |\n");
            }
        
            text = text.concat("'----------------------------------------------------------'\n");

            this.bestTime = 0.0;
            double cost = 0.0;
        
            for(int i=0; i<this.bestTimePath.size(); i++){
                this.bestTime = this.bestTime + this.bestTimePath.get(i).getTime();
                cost = cost + this.bestTimePath.get(i).getCost();
            }
        
            text = text.concat("\nLe temps optimal est: " + Double.toString(this.bestTime) + " h\n");
            text = text.concat("Le coût est: " + Double.toString(cost) + " €\n");
            text = text.concat("----------------------------------------------\n");

            this.jTextFieldA.setEnabled(false);
            this.jTextFieldAlpha.setEnabled(false);
            this.jTextFieldB.setEnabled(false);
            this.jTextFieldDist.setEnabled(false);
            this.jTextFieldFuel.setEnabled(false);
            this.jTextFieldToll.setEnabled(false);
            this.jButtonOptTime.setEnabled(false);
            this.jButtonVisualizeOptTime.setEnabled(true);
            this.jPanelProbPproblem.setEnabled(true);
            this.jLabelCoef.setEnabled(true);
            this.jTextFieldCoef.setEnabled(true);
            this.jLabelMobileRadars.setEnabled(true);
            this.jSpinnerRadars.setEnabled(true);
            this.jButtonCalculate.setEnabled(true);
            this.jLabelProbResults.setEnabled(true);
            this.jTextAreaProbResults.setEnabled(true);
            this.jTextAreaDeterministicResults.setText(text);
        }
        
        catch(Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String msg = sw.toString();
            msg = msg.substring(0, 500) + " ...";
            
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame,
                msg,
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }                                              

    private void jButtonOptCostActionPerformed(java.awt.event.ActionEvent evt) {                                               
        try{
            String text = this.jTextAreaDeterministicResults.getText();
        
            grid.setOptCostHorizontal();
            grid.setOptCostVertical();
        
            grid.setOptimalCost(0, 0);
        
            this.bestCostPath = grid.getOptimalCostPathSegments(0, ((grid.getM()+1)*(grid.getN()+1) - 1));
            text = text.concat("\n\nLe cout optimal chemin est: \n");
            text = text.concat(".-------------------------------------------------------------------------------------.\n");
            text = text.concat("|  Segment| Lim. de vitesse| Vitesse d'automobiliste|   Temps|     Cout| Radars| Péage|\n");
            text = text.concat("|-------------------------------------------------------------------------------------|\n");
            for (int i=0; i<this.bestCostPath.size(); i++){
                text = text.concat("|" + this.bestCostPath.get(i).toString() + "|");
            
                int speedLimit;
                if (this.bestCostPath.get(i).getLetter() == 'H')
                    speedLimit = grid.getSpeedHorizontal(this.bestCostPath.get(i).getX(), this.bestCostPath.get(i).getY());
                else
                    speedLimit = grid.getSpeedVertical(this.bestCostPath.get(i).getX(), this.bestCostPath.get(i).getY());
                String speedLimitString = String.format("%10d", speedLimit);
                text = text.concat(speedLimitString + " km/h |");

                double speedDouble = Math.round(this.bestCostPath.get(i).getSpeed() * 100);
                speedDouble = speedDouble / 100;
                String speedTempString = String.format("%22f", speedDouble);
                String speedString = speedTempString.substring(0, (speedTempString.length() - 4));
                text = text.concat(speedString + " km/h |");
            
                double timeDouble = (double)Math.round(this.bestCostPath.get(i).getTime() * 100);
                timeDouble = timeDouble / 100;
                String timeTempString = String.format("%10f", timeDouble);
                String timeString = timeTempString.substring(0, (timeTempString.length() - 4));
                text = text.concat(timeString + " h|");
            
                double costDouble = (double) Math.round(this.bestCostPath.get(i).getCost() * 100);
                costDouble = costDouble / 100;
                String costTempString = String.format("%11f", costDouble);
                String costString = costTempString.substring(0, (costTempString.length() - 4));
                text = text.concat(costString + " €|");
            
                int radars;
                if (this.bestCostPath.get(i).getLetter() == 'H')
                    radars = grid.getHorizontalFixedRadar(this.bestCostPath.get(i).getX(), this.bestCostPath.get(i).getY());
                else
                    radars = grid.getVerticalFixedRadar(this.bestCostPath.get(i).getX(), this.bestCostPath.get(i).getY());
            
                String radarsString = String.format("%6d", radars);
                text = text.concat(radarsString + " |");  
            
                String tollString;
                if (this.bestCostPath.get(i).getLetter() == 'H'){
                    if(grid.getSpeedHorizontal(this.bestCostPath.get(i).getX(), this.bestCostPath.get(i).getY()) == 130)
                        tollString = "Oui";
                    else
                        tollString = "Non";
                }
            
                else{
                    if(grid.getSpeedVertical(this.bestCostPath.get(i).getX(), this.bestCostPath.get(i).getY()) == 130)
                        tollString = "Oui";
                    else
                        tollString = "Non";
                }
            
                text = text.concat("  " + tollString + " |\n");
            }    
            
            text = text.concat("'-------------------------------------------------------------------------------------'\n");
       
            double time = 0.0;
            this.bestCost = 0.0;
        
            for(int i=0; i<this.bestCostPath.size(); i++){
                time = time + this.bestCostPath.get(i).getTime();
                this.bestCost = this.bestCost + this.bestCostPath.get(i).getCost();
            }
        
            text = text.concat("\nLe cout optimal est: " + Double.toString(this.bestCost) + " €\n");
            text = text.concat("Le temps est: " + Double.toString(time) + " h\n");
            text = text.concat("------------------------------------------------------------------------------\n");

            this.jButtonOptCost.setEnabled(false);
            this.jButtonVisualizeOptCost.setEnabled(true);
            this.jTextAreaDeterministicResults.setText(text);
        }
        
        catch(Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String msg = sw.toString();
            msg = msg.substring(0, 500) + " ...";
            
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame,
                msg,
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }                                              

    private void jButtonDefineParametersActionPerformed(java.awt.event.ActionEvent evt) {                                                        
        try{
            String text = this.jTextAreaDeterministicResults.getText();
        
            if(!isDouble(this.jTextFieldAlpha.getText()) || Double.parseDouble(this.jTextFieldAlpha.getText()) <= 0)
                throw new AlphaBadInputTypeException();
            if(!isDouble(this.jTextFieldA.getText()))
                throw new ABadInputTypeException();
            if((Double.parseDouble(this.jTextFieldA.getText()) <= 0) || (Double.parseDouble(this.jTextFieldA.getText()) > 1))
                throw new ABadBoundsException();
            if(!isDouble(this.jTextFieldB.getText()))
                throw new BBadInputTypeException();
            if((Double.parseDouble(this.jTextFieldB.getText()) < 0) || (Double.parseDouble(this.jTextFieldB.getText()) > 10))
                throw new BBadBoundsException();
            if(!isDouble(this.jTextFieldToll.getText()) || Double.parseDouble(this.jTextFieldToll.getText()) < 0)
                throw new TollBadInputTypeException();
            if(!isDouble(this.jTextFieldFuel.getText()) || Double.parseDouble(this.jTextFieldFuel.getText()) <= 0)
                throw new FuelBadInputTypeException();
            if(!isDouble(this.jTextFieldDist.getText()) || Double.parseDouble(this.jTextFieldDist.getText()) <= 0)
                throw new DistanceBadInputTypeException();
            
            text = text.concat("\na = " + this.jTextFieldA.getText() + "\n");
            text = text.concat("b = " + this.jTextFieldB.getText() + "\n");
            text = text.concat("alpha = " + Double.parseDouble(this.jTextFieldAlpha.getText()) + "\n");
            text = text.concat("La distance per segment [km]: " + this.jTextFieldDist.getText() + "\n");
            text = text.concat("Le cout de carburant [€/km]: " + this.jTextFieldFuel.getText() + "\n");
            text = text.concat("Le cout de la péage [€]: " + this.jTextFieldToll.getText() + "\n");
        
            //initialize the parameters
            grid.setA(Double.parseDouble(this.jTextFieldA.getText()));
            grid.setAlpha(Double.parseDouble(this.jTextFieldAlpha.getText()));
            grid.setB(Double.parseDouble(this.jTextFieldB.getText()));
            grid.setDist(Double.parseDouble(this.jTextFieldDist.getText()));
            grid.setFuel(Double.parseDouble(this.jTextFieldFuel.getText()));
            grid.setToll(Double.parseDouble(this.jTextFieldToll.getText()));
        
            grid.setOptSpeed();
            text = text.concat("\nLa vitesse optimale est: " + Double.toString(grid.getOptSpeed()) + " km/h\n");
            text = text.concat("----------------------------------------");
            this.jTextFieldA.setEnabled(false);
            this.jTextFieldAlpha.setEnabled(false);
            this.jTextFieldB.setEnabled(false);
            this.jTextFieldDist.setEnabled(false);
            this.jTextFieldFuel.setEnabled(false);
            this.jTextFieldToll.setEnabled(false);
            this.jButtonOptTime.setEnabled(false);
            this.jButtonOptTime.setEnabled(true);
            this.jButtonOptCost.setEnabled(true);
            this.jButtonDefineParameters.setEnabled(false);
        
            this.jTextAreaDeterministicResults.setText(text);
        } 
        
        catch (AlphaBadInputTypeException ex){
        }
        
        catch (ABadInputTypeException ex){
        }
        
        catch (ABadBoundsException ex){
        }
        
        catch (BBadInputTypeException ex){
        }
        
        catch (BBadBoundsException ex){
        }
        
        catch (TollBadInputTypeException ex){
        }
        
        catch (FuelBadInputTypeException ex){
        }
        
        catch (DistanceBadInputTypeException ex){
        }
        
        catch(Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String msg = sw.toString();
            msg = msg.substring(0, 500) + " ...";
            
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame,
                msg,
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }                                                       

    private void jButtonCalculateActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        try{
            if (!isDouble(this.jTextFieldCoef.getText()))
                throw new CoefBadInputTypeException();
            if (Double.parseDouble(this.jTextFieldCoef.getText()) <0 || Double.parseDouble(this.jTextFieldCoef.getText()) > 2)
                throw new CoefBadBoundsException();
            grid.setHorizontalV();
            grid.setVerticalV();
        
            grid.setMobileRadarNumber((Integer) this.jSpinnerRadars.getValue());
            String text = ("Nombre de radars mobiles: " + Integer.toString(grid.getMobileRadarNumber()) + "\n");
            text = text.concat("Coefficient: " + Double.parseDouble(this.jTextFieldCoef.getText()) + "\n");
            text = text.concat(this.jTextFieldCoef.getText() + " * To = " + Double.parseDouble(this.jTextFieldCoef.getText()) * this.bestTime + " h\n");
        
            grid.setExpectation();
            text = text.concat("Esperance: " + Double.toString(grid.getExpectation()) + "\n");
        
            text = text.concat("Le chemin le moins coûteux <= " + this.jTextFieldCoef.getText() + " * To:\n");
        
            grid.setHorizontalC();
            grid.setVerticalC();
            grid.setHorizontalT();
            grid.setVerticalT();
        
            this.probList = grid.getProbTime(this.bestTime, Double.parseDouble(this.jTextFieldCoef.getText()));
            text = text.concat(".-------------------------------------------------------------------------------------.\n");
            text = text.concat("|  Segment| Lim. de vitesse| Vitesse d'automobiliste|   Temps|     Cout| Radars| Péage|\n");
            text = text.concat("|-------------------------------------------------------------------------------------|\n");
       
            for (int i=0; i<this.probList.size(); i++){
                text = text.concat(this.probList.get(i).toString() + " |");
                
                int speedLimit;
                if (this.probList.get(i).getLetter() == 'H')
                    speedLimit = grid.getSpeedHorizontal(this.probList.get(i).getX(), this.probList.get(i).getY());
                else
                    speedLimit = grid.getSpeedVertical(this.probList.get(i).getX(), this.probList.get(i).getY());
                String speedLimitString = String.format("%10d", speedLimit);
                text = text.concat(speedLimitString + " km/h |");
                
                double speedDouble = Math.round(this.probList.get(i).getSpeed() * 100);
                speedDouble = speedDouble / 100;
                String speedTempString = String.format("%22f", speedDouble);
                String speedString = speedTempString.substring(0, (speedTempString.length() - 4));
                text = text.concat(speedString + " km/h |");
                
                double timeDouble = (double)Math.round(this.probList.get(i).getTime() * 100);
                timeDouble = timeDouble / 100;
                String timeTempString = String.format("%10f", timeDouble);
                String timeString = timeTempString.substring(0, (timeTempString.length() - 4));
                text = text.concat(timeString + " h|");
            
                double costDouble = (double) Math.round(this.probList.get(i).getCost() * 100);
                costDouble = costDouble / 100;
                String costTempString = String.format("%11f", costDouble);
                String costString = costTempString.substring(0, (costTempString.length() - 4));
                text = text.concat(costString + " €|");
                
                int radars;
                if (this.probList.get(i).getLetter() == 'H')
                    radars = grid.getHorizontalFixedRadar(this.probList.get(i).getX(), this.probList.get(i).getY());
                else
                    radars = grid.getVerticalFixedRadar(this.probList.get(i).getX(), this.probList.get(i).getY());
                String radarsString = String.format("%6d", radars);
                text = text.concat(radarsString + " |");
                
                String tollString;
                if (this.probList.get(i).getLetter() == 'H'){
                    if(grid.getSpeedHorizontal(this.probList.get(i).getX(), this.probList.get(i).getY()) == 130)
                        tollString = "Oui";
                    else
                        tollString = "Non";
                }
                else{
                    if(grid.getSpeedVertical(this.probList.get(i).getX(), this.probList.get(i).getY()) == 130)
                        tollString = "Oui";
                    else
                        tollString = "Non";
                }
                text = text.concat("  " + tollString + " |\n");
            }
        
            text = text.concat("'-------------------------------------------------------------------------------------'\n");

            this.probTime = grid.getMinProbTime();
            text = text.concat("\nLe coût est: " + grid.getMinProbCost() + " €\n");
            text = text.concat("Le temps est: " + grid.getMinProbTime() + "h \n");
        
            this.jTextAreaProbResults.setText(text);
        
            this.jButtonVisualizeProbResults.setEnabled(true);
        }
        
        catch (CoefBadInputTypeException ex){
        }
        
        catch (CoefBadBoundsException ex){
        }
        
        catch(Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String msg = sw.toString();
            msg = msg.substring(0, 500) + " ...";
            
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame,
                msg,
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }                                                

    private void jButtonVisualizeRadarsActionPerformed(java.awt.event.ActionEvent evt) {                                                       
        try{
            List<Double> x = new ArrayList<Double>();
            List<Double> y = new ArrayList<Double>();
        
            for (int i=0; i<grid.getM(); i++)
                for (int j=0; j<=grid.getN(); j++)
                    if (grid.getHorizontalFixedRadar(i, j) != 0){
                        x.add((double) i);
                        y.add((double) j);
                        x.add((double) i+1);
                        y.add((double) j);
                    }
        
            System.out.println("\nX length = " + x.size());
        
            for (int i=0; i<=grid.getM(); i++)
                for (int j=0; j<grid.getN(); j++)
                    if (grid.getVerticalFixedRadar(i, j) != 0){
                        x.add((double) i);
                        y.add((double) j);
                        x.add((double) i);
                        y.add((double) j+1);
                    } 
        
            System.out.println("\nX length = " + x.size() + "\n");
        
            for (int i=0; i<x.size(); i++)
                System.out.print(x.get(i) + " ");
            System.out.print("\n");
            for (int i=0; i<y.size(); i++)
                System.out.print(y.get(i) + " ");

            double [][] xy = new double [2][x.size()];
            for (int i=0; i<2; i++)
                for (int j=0; j<x.size(); j++)
                    if (i == 0)
                        xy[i][j] = x.get(i);
                    else
                        xy[i][j] = y.get(i);
        
            System.out.print("\n\n");
            for (int j=0; j<x.size(); j++){
                System.out.print(x.get(j) + " ");
            }
        
            System.out.print("\n");
            for (int j=0; j<x.size(); j++){
                System.out.print(y.get(j) + " ");
            }
        
            System.out.println("\n\nNumber of fixed radars: " + this.numberOfFixedRadars + "\n");
 
            // create your PlotPanel (you can use it as a JPanel)
            Plot2DPanel plot = new Plot2DPanel();
 
            // define the legend position
            //plot.addLegend("SOUTH");
 
            // add a line plot to the PlotPanel
            double[] X = new double [x.size()];
            double[] Y = new double [y.size()];
            
            for(int i=0; i<x.size(); i++){
                X[i] = x.get(i);
                Y[i] = y.get(i);
            }
        
            //plot.addLinePlot("my plot", X, Y);
            plot.addScatterPlot("Les points", X, Y);

            for (int i=0; i<X.length; i=i+2){
                double[] tempX = new double[2];
                double[] tempY = new double[2];
            
                tempX[0] = X[i];
                tempY[0] = X[i+1];
            
                tempX[1] = Y[i];
                tempY[1] = Y[i+1];
            
                System.out.print("\n" + i + " ");
            
                plot.addLinePlot("Radar " + Integer.toString(i/2+1), Color.RED,tempX, tempY);
            }
        
            // put the PlotPanel in a JFrame like a JPanel
            JFrame frame = new JFrame("Les radars fixes");
            frame.setSize(600, 600);
            frame.setContentPane(plot);
            frame.setVisible(true);
        }
        
        catch(Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String msg = sw.toString();
            msg = msg.substring(0, 500) + " ...";
            
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame,
                msg,
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }                                                      

    private void jButtonVisualizeOptTimeActionPerformed(java.awt.event.ActionEvent evt) {                                                        
        // define your data
	double[] x = new double [this.bestTimePath.size() + 1];
	double[] y = new double [this.bestTimePath.size() + 1];
        double [][] xy = new double [this.bestTimePath.size() + 1][2];
         
	// create your PlotPanel (you can use it as a JPanel)
	Plot2DPanel plot = new Plot2DPanel();
        
        double bestTimeCost = 0;
        for (int i=0; i<this.bestTimePath.size(); i++)
            bestTimeCost = bestTimeCost + this.bestTimePath.get(i).getCost();
        bestTimeCost = (double) Math.round(bestTimeCost*100);
        
        double bestTimeDouble = (double)Math.round(this.bestTime*100);
        BaseLabel title = new BaseLabel("Le temps optimal est " + bestTimeDouble/100 + " h, le cout est " + bestTimeCost/100 + " €", Color.RED, 0.5, 1.1);
        title.setFont(new Font("Courier", Font.BOLD, 12));
        plot.addPlotable(title);
        
        for(int i=0; i<this.bestTimePath.size(); i++){
            x[i] = this.bestTimePath.get(i).getX();
            y[i] = this.bestTimePath.get(i).getY();
            if (i == (this.bestTimePath.size()-1)){
                if(this.bestTimePath.get(i).getLetter() == 'H'){
                    x[i+1] = x[i] + 1;
                    y[i+1] = y[i];
                }
                else{
                    x[i+1] = x[i];
                    y[i+1] = y[i] + 1;
                }
            }       
        }
        
        for (int i=0; i<=this.bestTimePath.size(); i++){
            for (int j=0; j<2; j++){
                if(j==0)
                    xy[i][j] = x[i];
                else
                    xy[i][j] = y[i];
            }
        }
        
        for (int i=0; i<this.bestTimePath.size(); i++){
            double timeDouble = (double)Math.round(this.bestTimePath.get(i).getTime()*100);
            String timeString = Double.toString(timeDouble/100);
            plot.addLabel("             " + timeString + " h", Color.DARK_GRAY, xy[i]);
        }
	
        // define the legend position
        plot.addLegend("SOUTH");
 
        // add a line plot to the PlotPanel
        plot.addLinePlot("Le chemin", x, y);
        plot.addScatterPlot("Les points", xy);

        // put the PlotPanel in a JFrame like a JPanel
        JFrame frame = new JFrame("Le trajet de temps minimal");
        frame.setSize(600, 600);
        frame.setContentPane(plot);
        frame.setVisible(true);
    }                                                       

    private void jButtonVisualizeOptCostActionPerformed(java.awt.event.ActionEvent evt) {                                                        
        // define your data
	double[] x = new double [this.bestCostPath.size() + 1];
	double[] y = new double [this.bestCostPath.size() + 1];
        double [][] xy = new double [this.bestCostPath.size() + 1][2];
        
        for(int i=0; i<this.bestCostPath.size(); i++){
            x[i] = this.bestCostPath.get(i).getX();
            y[i] = this.bestCostPath.get(i).getY();
            if (i == (this.bestCostPath.size()-1)){
                if(this.bestCostPath.get(i).getLetter() == 'H'){
                    x[i+1] = x[i] + 1;
                    y[i+1] = y[i];
                }
                else{
                    x[i+1] = x[i];
                    y[i+1] = y[i] + 1;
                }
            }       
        }
        
        for (int i=0; i<=this.bestCostPath.size(); i++){
            for (int j=0; j<2; j++){
                if(j==0)
                    xy[i][j] = x[i];
                else
                    xy[i][j] = y[i];
            }
        }
 
	// create your PlotPanel (you can use it as a JPanel)
	Plot2DPanel plot = new Plot2DPanel();
        
        double bestCostTime = 0;
        for (int i=0; i<this.bestCostPath.size(); i++)
            bestCostTime = bestCostTime + this.bestCostPath.get(i).getTime();
        bestCostTime = Math.round(bestCostTime*100);
        
        double bestCostDouble = (double)Math.round(this.bestCost*100);
        BaseLabel title = new BaseLabel("Le cout optimal est " + bestCostDouble/100 + " €, le temp est " + bestCostTime/100 + " h", Color.RED, 0.5, 1.1);
        title.setFont(new Font("Courier", Font.BOLD, 12));
        plot.addPlotable(title);
        
        for (int i=0; i<this.bestCostPath.size(); i++){
            //double costDouble = this.bestCostPath.get(i).getSpeed();
            //String costString = Double.toString(costDouble);
            double costDouble = (double)Math.round(this.bestCostPath.get(i).getCost()*100);
            String costString = Double.toString(costDouble/100);
            plot.addLabel("             " + costString + " €", Color.DARK_GRAY, xy[i]);
        }
 
	// define the legend position
	plot.addLegend("SOUTH");
 
	// add a line plot to the PlotPanel
	plot.addLinePlot("Le chemin", x, y);
        plot.addScatterPlot("Les points", xy);
        
	// put the PlotPanel in a JFrame like a JPanel
        JFrame frame = new JFrame("Le trajet de coût minimal");
	frame.setSize(600, 600);
	frame.setContentPane(plot);
	frame.setVisible(true);
    }                                                       

    private void jButtonVisualizeProbResultsActionPerformed(java.awt.event.ActionEvent evt) {                                                            
        try{
            // define your data
            double[] x = new double [this.probList.size() + 1];
            double[] y = new double [this.probList.size() + 1];
            double [][] xy = new double [this.probList.size() + 1][2];
        
            for(int i=0; i<this.probList.size(); i++){
                x[i] = this.probList.get(i).getX();
                y[i] = this.probList.get(i).getY();
                if (i == (this.probList.size()-1)){
                    if(this.probList.get(i).getLetter() == 'H'){
                        x[i+1] = x[i] + 1;
                        y[i+1] = y[i];
                    }
                    else{
                        x[i+1] = x[i];
                        y[i+1] = y[i] + 1;
                    }
                }       
            }
        
            for (int i=0; i<=this.probList.size(); i++){
                for (int j=0; j<2; j++){
                    if(j==0)
                        xy[i][j] = x[i];
                    else
                        xy[i][j] = y[i];
                }
            }
        
            // create your PlotPanel (you can use it as a JPanel)
            Plot2DPanel plot = new Plot2DPanel();
            
            double bestCostTime = Math.round(this.probTime*100);
        
            double bestCostDouble = (double)Math.round(grid.getMinProbCost()*100);
            BaseLabel title = new BaseLabel("Le cout optimal est " + bestCostDouble/100 + " €, le temps est " + bestCostTime/100 + " h", Color.RED, 0.5, 1.1);
            title.setFont(new Font("Courier", Font.BOLD, 12));
            plot.addPlotable(title);
        
            for (int i=0; i<this.probList.size(); i++){
                int speedInt = (int) this.probList.get(i).getSpeed();
                String speedString = Integer.toString(speedInt);
                plot.addLabel("             " + speedString + " km/h", Color.DARK_GRAY, xy[i]);
            }
 
            // define the legend position
            plot.addLegend("SOUTH");
 
            // add a line plot to the PlotPanel
            plot.addLinePlot("Le chemin", x, y);
            plot.addScatterPlot("Les points", xy);
        
            // put the PlotPanel in a JFrame like a JPanel
            JFrame frame = new JFrame("Le trajet optimal");
            frame.setSize(600, 600);
            frame.setContentPane(plot);
            frame.setVisible(true);
        }
        
        catch(Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String msg = sw.toString();
            msg = msg.substring(0, 500) + " ...";
            
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame,
                msg,
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }                                                           

    private void jMenuItemExitActionPerformed(java.awt.event.ActionEvent evt) {                                              
        System.exit(0);
    }                                             

    private void jMenuItemNewProblemActionPerformed(java.awt.event.ActionEvent evt) {                                                    
        //setting visibility to text fields
        this.jTextFieldA.setEnabled(true);
        this.jTextFieldAlpha.setEnabled(true);
        this.jTextFieldB.setEnabled(true);
        this.jTextFieldCoef.setEnabled(false);
        this.jTextFieldDist.setEnabled(true);
        this.jTextFieldFuel.setEnabled(true);
        this.jTextFieldToll.setEnabled(true);
        
        //setting visibility to labels
        this.jLabelA.setEnabled(true);
        this.jLabelAlpha.setEnabled(true);
        this.jLabelB.setEnabled(true);
        this.jLabelCoef.setEnabled(false);
        this.jLabelDist.setEnabled(true);
        this.jLabelFuel.setEnabled(true);
        this.jLabelMobileRadars.setEnabled(false);
        this.jLabelProbResults.setEnabled(false);
        this.jLabelToll.setEnabled(true);
        
        //setting visibility to buttons
        this.jButtonCalculate.setEnabled(false);
        this.jButtonDefineParameters.setEnabled(false);
        this.jButtonOptCost.setEnabled(false);
        this.jButtonOptTime.setEnabled(false);
        this.jButtonQuit.setEnabled(true);
        this.jButtonReadXls.setEnabled(true);
        this.jButtonReadXml.setEnabled(true);
        this.jButtonVisualizeOptCost.setEnabled(false);
        this.jButtonVisualizeOptTime.setEnabled(false);
        this.jButtonVisualizeProbResults.setEnabled(false);
        this.jButtonVisualizeRadars.setEnabled(false);
        
        //setting visibility to spinner
        this.jSpinnerRadars.setEnabled(false);
        
        //setting default values to text fields
        this.jTextFieldA.setText("0.0625");
        this.jTextFieldAlpha.setText("5");
        this.jTextFieldB.setText("1.875");
        this.jTextFieldCoef.setText("0.8");
        this.jTextFieldDist.setText("100");
        this.jTextFieldFuel.setText("1.3");
        this.jTextFieldToll.setText("3");
        
        //setting default value to spinner
        this.jSpinnerRadars.setValue(20);
        
        //resetting text areas
        this.jTextAreaDeterministicResults.setText("");
        this.jTextAreaProbResults.setText("");
        
        //setting visibility for menus
        this.jMenuItemReadFromXls.setEnabled(true);
        this.jMenuItemReadFromXml.setEnabled(true);
        
        grid = new Grid();
    }                                                   

    @SuppressWarnings("static-access")
	private void jButtonReadXlsActionPerformed(java.awt.event.ActionEvent evt) {                                               
        try{
            //initializing from xls
        
            //text - the text on the jTextArea
            //tempText - the temporary text
            String text = "";
            String tempText;
            boolean b = true;
                        
            int returnVal = jFileChooserXls.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = jFileChooserXls.getSelectedFile();
                text = text.concat(file.getAbsolutePath() + "\n");
                try {
                    // What to do with the file, e.g. display it in a TextArea
                    b = grid.initializeFromXls(file.getAbsolutePath());
                    this.jTextAreaDeterministicResults.read(new FileReader( file.getAbsolutePath() ), null );
                } 
                
                catch (IOException ex) {
                    System.out.println("Problème d'accès du fichier " + file.getAbsolutePath() + "\n");
                    
                    StringWriter sw = new StringWriter();
                    ex.printStackTrace(new PrintWriter(sw));
                    String msg = "Problème d'accès du fichier \n";
                    msg = msg.concat(sw.toString());
                    msg = msg.substring(0, 500) + " ...";
            
                    JFrame frame = new JFrame();
                    JOptionPane.showMessageDialog(frame,
                        msg,
                            "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                    b = false;
                }
            
                catch (java.lang.NullPointerException ex){
                    System.out.println("Nullpointer exception! \n");
                    
                    StringWriter sw = new StringWriter();
                    ex.printStackTrace(new PrintWriter(sw));
                    String msg = "Nullpointer exception! \n";
                    msg = msg.concat(sw.toString());
                    msg = msg.substring(0, 500) + " ...";
            
                    JFrame frame = new JFrame();
                    JOptionPane.showMessageDialog(frame,
                        msg,
                            "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                    b = false;
                }
            
                catch (BiffException ex){
                    StringWriter sw = new StringWriter();
                    ex.printStackTrace(new PrintWriter(sw));
                    String msg = "Biff exception! \n";
                    msg = msg.concat(sw.toString());
                    msg = msg.substring(0, 500) + " ...";
            
                    JFrame frame = new JFrame();
                    JOptionPane.showMessageDialog(frame,
                        msg,
                            "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                    b = false;
                }
                
                catch(Exception ex){
                    StringWriter sw = new StringWriter();
                    ex.printStackTrace(new PrintWriter(sw));
                    String msg = "Erreur! \n";
                    msg = msg.concat(sw.toString());
                    msg = msg.substring(0, 500) + " ...";
            
                    JFrame frame = new JFrame();
                    JOptionPane.showMessageDialog(frame,
                        msg,
                            "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                    b = false;
                }
            } 
            else {
                System.out.println("File access cancelled by user.");
                b = false;
            }
        
            if (b==true){
                //adding m and n to the text
                tempText = ("m = " + Integer.toString(grid.getM()) + "\n");
                text = text.concat(tempText);
                tempText = ("n = " + Integer.toString(grid.getN()) + "\n");
                text = text.concat(tempText);
       
                //adding the horizontal speed data to the text
                text = text.concat("\n\nLes limitations de vitesse sur les segments horizontaux: \n");
                tempText = "";
                for (int i=0; i<grid.getM(); i++){		
                    for (int j=0; j<=grid.getN(); j++)
                        tempText = tempText.concat(Integer.toString(grid.getSpeedHorizontal(i, j)).format("%5d",grid.getSpeedHorizontal(i, j)));        
                    tempText = tempText.concat("\n");	
                }
        
                text = text.concat(tempText);
                tempText = "";
        
                //adding the vertical speed data to the text
                text = text.concat("\n\nLes limitations de vitesse sur les segments verticaux: \n");
                for (int i=0; i<=grid.getM(); i++){
                    for (int j=0; j<grid.getN(); j++)
                        tempText = tempText.concat(Integer.toString(grid.getSpeedVertical(i, j)).format("%5d",grid.getSpeedVertical(i, j)));
                    tempText = tempText.concat("\n");
                }
        
                text = text.concat(tempText);
                tempText = "";
        
                //count the number of fixed radars
                this.numberOfFixedRadars = 0;
        
                //count horizontal radars
                for (int i=0; i<grid.getM(); i++)
                    for (int j=0; j<=grid.getN(); j++)
                        if (grid.getHorizontalFixedRadar(i, j) != 0)
                            this.numberOfFixedRadars++;
        
                //count vertical radars
                for (int i=0; i<=grid.getM(); i++)
                    for (int j=0; j<grid.getN(); j++)
                        if (grid.getVerticalFixedRadar(i, j) != 0)
                            this.numberOfFixedRadars++;
        
                text = text.concat("\nLe nombre de radars fixes: " + this.numberOfFixedRadars);
        
                //adding the horizontal radars to the text
                text = text.concat("\n\nHorizontal fixed radars: \n");
                for(int i=0; i<grid.getM(); i++){
                    for(int j=0; j<=grid.getN(); j++){
                        tempText = tempText.concat(Integer.toString(grid.getHorizontalFixedRadar(i, j)).format("%5d", grid.getHorizontalFixedRadar(i, j)));
                    }	
                    tempText = tempText.concat("\n");
                }
        
                text = text.concat(tempText);
                tempText = "";
        
                //adding the vertical radars to the text
                text = text.concat("\n\nVertical fixed radars: \n");
                for(int i=0; i<=grid.getM(); i++){
                    for(int j=0; j<grid.getN(); j++){
                        tempText = tempText.concat(Integer.toString(grid.getVerticalFixedRadar(i, j)).format("%5d", grid.getVerticalFixedRadar(i, j)));
                    }	
                    tempText = tempText.concat("\n");
                }
        
                text = text.concat(tempText);
                tempText = "";
        
                this.jTextAreaDeterministicResults.setText(text);
                this.jButtonVisualizeRadars.setEnabled(true);
                this.jButtonReadXls.setEnabled(false);
                this.jButtonReadXml.setEnabled(false);
                this.jButtonDefineParameters.setEnabled(true);
                this.jMenuItemReadFromXls.setEnabled(false);
                this.jMenuItemReadFromXml.setEnabled(false);
            }
        }
        
        catch(Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String msg = sw.toString();
            msg = msg.substring(0, 500) + " ...";
            
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame,
                msg,
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }                                              

    @SuppressWarnings("static-access")
	private void jMenuItemReadFromXmlActionPerformed(java.awt.event.ActionEvent evt) {                                                     
        try{
            //initializing from XML
        
            //text - the text on the jTextArea
            //tempText - the temporary text
            String text = "";
            String tempText;
            boolean b = true;
        
            int returnVal = jFileChooserXml.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = jFileChooserXml.getSelectedFile();
                text = text.concat(file.getAbsolutePath() + "\n");
                try {
                    // What to do with the file, e.g. display it in a TextArea
                    b = grid.initializeFromXml(file.getAbsolutePath());
                    this.jTextAreaDeterministicResults.read(new FileReader( file.getAbsolutePath() ), null );
                }    
            
                catch (IOException ex) {
                    System.out.println("Problème d'accès du fichier " + file.getAbsolutePath() + "\n");
                    
                    StringWriter sw = new StringWriter();
                    ex.printStackTrace(new PrintWriter(sw));
                    String msg = "Problème d'accès du fichier \n";
                    msg = msg.concat(sw.toString());
                    msg = msg.substring(0, 500) + " ...";
            
                    JFrame frame = new JFrame();
                    JOptionPane.showMessageDialog(frame,
                        msg,
                            "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                    b = false;
                }
                
                catch (java.lang.NullPointerException ex){
                    System.out.println("Nullpointer exception! \n");
                    
                    StringWriter sw = new StringWriter();
                    ex.printStackTrace(new PrintWriter(sw));
                    String msg = "Nullpointer exception! \n";
                    msg = msg.concat(sw.toString());
                    msg = msg.substring(0, 500) + " ...";
            
                    JFrame frame = new JFrame();
                    JOptionPane.showMessageDialog(frame,
                        msg,
                            "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                    b = false;
                }
            } else {
                System.out.println("File access cancelled by user.");
                b = false;
            }
        
            if (b == true){
                //adding m and n to the text
                tempText = ("\nm = " + Integer.toString(grid.getM()) + "\n");
                text = text.concat(tempText);
                tempText = ("n = " + Integer.toString(grid.getN()) + "\n");
                text = text.concat(tempText);
        
                //adding the horizontal speed data to the text
                text = text.concat("\n\nLes limitations de vitesse sur les segments horizontaux: \n");
                tempText = "";
                for (int i=0; i<grid.getM(); i++){		
                    for (int j=0; j<=grid.getN(); j++)
                        tempText = tempText.concat(Integer.toString(grid.getSpeedHorizontal(i, j)).format("%5d",grid.getSpeedHorizontal(i, j)));        
                    tempText = tempText.concat("\n");	
                }
        
                text = text.concat(tempText);
                tempText = "";
        
                //adding the vertical speed data to the text
                text = text.concat("\n\nLes limitations de vitesse sur les segments verticaux: \n");
                for (int i=0; i<=grid.getM(); i++){
                    for (int j=0; j<grid.getN(); j++)
                    tempText = tempText.concat(Integer.toString(grid.getSpeedVertical(i, j)).format("%5d",grid.getSpeedVertical(i, j)));
                    tempText = tempText.concat("\n");
                }
        
                text = text.concat(tempText);
                tempText = "";
        
                //count the number of fixed radars
                this.numberOfFixedRadars = 0;
        
                //count horizontal radars
                for (int i=0; i<grid.getM(); i++)
                    for (int j=0; j<=grid.getN(); j++)
                        if (grid.getHorizontalFixedRadar(i, j) != 0)
                            this.numberOfFixedRadars++;
        
                //count vertical radars
                for (int i=0; i<=grid.getM(); i++)
                    for (int j=0; j<grid.getN(); j++)
                        if (grid.getVerticalFixedRadar(i, j) != 0)
                            this.numberOfFixedRadars++;
        
                text = text.concat("\nLe nombre de radars fixes: " + this.numberOfFixedRadars);
        
                //adding the horizontal radars to the text
                text = text.concat("\n\nRadars fixes horizontaux: \n");
                for(int i=0; i<grid.getM(); i++){
                    for(int j=0; j<=grid.getN(); j++){
                        tempText = tempText.concat(Integer.toString(grid.getHorizontalFixedRadar(i, j)).format("%5d", grid.getHorizontalFixedRadar(i, j)));
                    }	
                    tempText = tempText.concat("\n");
                }
        
                text = text.concat(tempText);
                tempText = "";
        
                //adding the vertical radars to the text
                text = text.concat("\n\nRadars fixes verticales: \n");
            
                for(int i=0; i<=grid.getM(); i++){
                    for(int j=0; j<grid.getN(); j++){
                        tempText = tempText.concat(Integer.toString(grid.getVerticalFixedRadar(i, j)).format("%5d", grid.getVerticalFixedRadar(i, j)));
                    }	
                    tempText = tempText.concat("\n");
                }
        
                text = text.concat(tempText);
                tempText = "";
        
                this.jTextAreaDeterministicResults.setText(text);
                this.jButtonVisualizeRadars.setEnabled(true);
                this.jButtonReadXls.setEnabled(false);
                this.jButtonReadXml.setEnabled(false);
                this.jButtonDefineParameters.setEnabled(true);
                this.jMenuItemReadFromXls.setEnabled(false);
                this.jMenuItemReadFromXml.setEnabled(false);
            }
        }
        
        catch(Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String msg = sw.toString();
            msg = msg.substring(0, 500) + " ...";
            
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame,
                msg,
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }                                                    

    @SuppressWarnings("static-access")
	private void jMenuItemReadFromXlsActionPerformed(java.awt.event.ActionEvent evt) {                                                     
         try{
            //initializing from xls
        
            //text - the text on the jTextArea
            //tempText - the temporary text
            String text = "";
            String tempText;
            boolean b = true;
                        
            int returnVal = jFileChooserXls.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = jFileChooserXls.getSelectedFile();
                text = text.concat(file.getAbsolutePath() + "\n");
                try {
                    // What to do with the file, e.g. display it in a TextArea
                    b = grid.initializeFromXls(file.getAbsolutePath());
                    this.jTextAreaDeterministicResults.read(new FileReader( file.getAbsolutePath() ), null );
                } 
                
                catch (IOException ex) {
                    System.out.println("Problème d'accès du fichier " + file.getAbsolutePath() + "\n");
                    
                    StringWriter sw = new StringWriter();
                    ex.printStackTrace(new PrintWriter(sw));
                    String msg = "Problème d'accès du fichier \n";
                    msg = msg.concat(sw.toString());
                    msg = msg.substring(0, 500) + " ...";
            
                    JFrame frame = new JFrame();
                    JOptionPane.showMessageDialog(frame,
                        msg,
                            "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                    b = false;
                }
            
                catch (java.lang.NullPointerException ex){
                    System.out.println("Nullpointer exception! \n");
                    
                    StringWriter sw = new StringWriter();
                    ex.printStackTrace(new PrintWriter(sw));
                    String msg = "Nullpointer exception! \n";
                    msg = msg.concat(sw.toString());
                    msg = msg.substring(0, 500) + " ...";
            
                    JFrame frame = new JFrame();
                    JOptionPane.showMessageDialog(frame,
                        msg,
                            "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                    b = false;
                }
            
                catch (BiffException ex){
                    StringWriter sw = new StringWriter();
                    ex.printStackTrace(new PrintWriter(sw));
                    String msg = "Biff exception! \n";
                    msg = msg.concat(sw.toString());
                    msg = msg.substring(0, 500) + " ...";
            
                    JFrame frame = new JFrame();
                    JOptionPane.showMessageDialog(frame,
                        msg,
                            "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                    b = false;
                }
                
                catch(Exception ex){
                    StringWriter sw = new StringWriter();
                    ex.printStackTrace(new PrintWriter(sw));
                    String msg = "Erreur! \n";
                    msg = msg.concat(sw.toString());
                    msg = msg.substring(0, 500) + " ...";
            
                    JFrame frame = new JFrame();
                    JOptionPane.showMessageDialog(frame,
                        msg,
                            "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                    b = false;
                }
            } 
            else {
                System.out.println("File access cancelled by user.");
                b = false;
            }
        
            if (b==true){
                //adding m and n to the text
                tempText = ("m = " + Integer.toString(grid.getM()) + "\n");
                text = text.concat(tempText);
                tempText = ("n = " + Integer.toString(grid.getN()) + "\n");
                text = text.concat(tempText);
       
                //adding the horizontal speed data to the text
                text = text.concat("\n\nLes limitations de vitesse sur les segments horizontaux: \n");
                tempText = "";
                for (int i=0; i<grid.getM(); i++){		
                    for (int j=0; j<=grid.getN(); j++)
                        tempText = tempText.concat(Integer.toString(grid.getSpeedHorizontal(i, j)).format("%5d",grid.getSpeedHorizontal(i, j)));        
                    tempText = tempText.concat("\n");	
                }
        
                text = text.concat(tempText);
                tempText = "";
        
                //adding the vertical speed data to the text
                text = text.concat("\n\nLes limitations de vitesse sur les segments verticaux: \n");
                for (int i=0; i<=grid.getM(); i++){
                    for (int j=0; j<grid.getN(); j++)
                        tempText = tempText.concat(Integer.toString(grid.getSpeedVertical(i, j)).format("%5d",grid.getSpeedVertical(i, j)));
                    tempText = tempText.concat("\n");
                }
        
                text = text.concat(tempText);
                tempText = "";
        
                //count the number of fixed radars
                this.numberOfFixedRadars = 0;
        
                //count horizontal radars
                for (int i=0; i<grid.getM(); i++)
                    for (int j=0; j<=grid.getN(); j++)
                        if (grid.getHorizontalFixedRadar(i, j) != 0)
                            this.numberOfFixedRadars++;
        
                //count vertical radars
                for (int i=0; i<=grid.getM(); i++)
                    for (int j=0; j<grid.getN(); j++)
                        if (grid.getVerticalFixedRadar(i, j) != 0)
                            this.numberOfFixedRadars++;
        
                text = text.concat("\nLe nombre de radars fixes: " + this.numberOfFixedRadars);
        
                //adding the horizontal radars to the text
                text = text.concat("\n\nHorizontal fixed radars: \n");
                for(int i=0; i<grid.getM(); i++){
                    for(int j=0; j<=grid.getN(); j++){
                        tempText = tempText.concat(Integer.toString(grid.getHorizontalFixedRadar(i, j)).format("%5d", grid.getHorizontalFixedRadar(i, j)));
                    }	
                    tempText = tempText.concat("\n");
                }
        
                text = text.concat(tempText);
                tempText = "";
        
                //adding the vertical radars to the text
                text = text.concat("\n\nVertical fixed radars: \n");
                for(int i=0; i<=grid.getM(); i++){
                    for(int j=0; j<grid.getN(); j++){
                        tempText = tempText.concat(Integer.toString(grid.getVerticalFixedRadar(i, j)).format("%5d", grid.getVerticalFixedRadar(i, j)));
                    }	
                    tempText = tempText.concat("\n");
                }
        
                text = text.concat(tempText);
                tempText = "";
        
                this.jTextAreaDeterministicResults.setText(text);
                this.jButtonVisualizeRadars.setEnabled(true);
                this.jButtonReadXls.setEnabled(false);
                this.jButtonReadXml.setEnabled(false);
                this.jButtonDefineParameters.setEnabled(true);
                this.jMenuItemReadFromXls.setEnabled(false);
                this.jMenuItemReadFromXml.setEnabled(false);
            }
        }
        
        catch(Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String msg = sw.toString();
            msg = msg.substring(0, 500) + " ...";
            
            JFrame frame = new JFrame();
            JOptionPane.showMessageDialog(frame,
                msg,
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }                                                    

    private void jButtonNewProblemActionPerformed(java.awt.event.ActionEvent evt) {                                                  
        //setting visibility to text fields
        this.jTextFieldA.setEnabled(true);
        this.jTextFieldAlpha.setEnabled(true);
        this.jTextFieldB.setEnabled(true);
        this.jTextFieldCoef.setEnabled(false);
        this.jTextFieldDist.setEnabled(true);
        this.jTextFieldFuel.setEnabled(true);
        this.jTextFieldToll.setEnabled(true);
        
        //setting visibility to labels
        this.jLabelA.setEnabled(true);
        this.jLabelAlpha.setEnabled(true);
        this.jLabelB.setEnabled(true);
        this.jLabelCoef.setEnabled(false);
        this.jLabelDist.setEnabled(true);
        this.jLabelFuel.setEnabled(true);
        this.jLabelMobileRadars.setEnabled(false);
        this.jLabelProbResults.setEnabled(false);
        this.jLabelToll.setEnabled(true);
        
        //setting visibility to buttons
        this.jButtonCalculate.setEnabled(false);
        this.jButtonDefineParameters.setEnabled(false);
        this.jButtonOptCost.setEnabled(false);
        this.jButtonOptTime.setEnabled(false);
        this.jButtonQuit.setEnabled(true);
        this.jButtonReadXls.setEnabled(true);
        this.jButtonReadXml.setEnabled(true);
        this.jButtonVisualizeOptCost.setEnabled(false);
        this.jButtonVisualizeOptTime.setEnabled(false);
        this.jButtonVisualizeProbResults.setEnabled(false);
        this.jButtonVisualizeRadars.setEnabled(false);
        
        //setting visibility to spinner
        this.jSpinnerRadars.setEnabled(false);
        
        //setting default values to text fields
        this.jTextFieldA.setText("0.0625");
        this.jTextFieldAlpha.setText("5");
        this.jTextFieldB.setText("1.875");
        this.jTextFieldCoef.setText("0.8");
        this.jTextFieldDist.setText("100");
        this.jTextFieldFuel.setText("1.3");
        this.jTextFieldToll.setText("3");
        
        //setting default value to spinner
        this.jSpinnerRadars.setValue(20);
        
        //resetting text areas
        this.jTextAreaDeterministicResults.setText("");
        this.jTextAreaProbResults.setText("");
        
        //setting visibility for menus
        this.jMenuItemReadFromXls.setEnabled(true);
        this.jMenuItemReadFromXml.setEnabled(true);
        
        grid = new Grid();
    }                                                 

    private void jTextFieldCoefActionPerformed(java.awt.event.ActionEvent evt) {                                               
        // TODO add your handling code here:
    }                                              

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(mainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(mainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(mainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(mainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new mainForm().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify                     
    private javax.swing.JButton jButtonCalculate;
    private javax.swing.JButton jButtonDefineParameters;
    private javax.swing.JButton jButtonNewProblem;
    private javax.swing.JButton jButtonOptCost;
    private javax.swing.JButton jButtonOptTime;
    private javax.swing.JButton jButtonQuit;
    private javax.swing.JButton jButtonReadXls;
    private javax.swing.JButton jButtonReadXml;
    private javax.swing.JButton jButtonVisualizeOptCost;
    private javax.swing.JButton jButtonVisualizeOptTime;
    private javax.swing.JButton jButtonVisualizeProbResults;
    private javax.swing.JButton jButtonVisualizeRadars;
    private javax.swing.JFileChooser jFileChooserXls;
    private javax.swing.JFileChooser jFileChooserXml;
    private javax.swing.JLabel jLabelA;
    private javax.swing.JLabel jLabelAlpha;
    private javax.swing.JLabel jLabelB;
    private javax.swing.JLabel jLabelCoef;
    private javax.swing.JLabel jLabelDist;
    private javax.swing.JLabel jLabelFuel;
    private javax.swing.JLabel jLabelMobileRadars;
    private javax.swing.JLabel jLabelProbResults;
    private javax.swing.JLabel jLabelToll;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenu jMenuFile;
    private javax.swing.JMenuItem jMenuItemExit;
    private javax.swing.JMenuItem jMenuItemNewProblem;
    private javax.swing.JMenuItem jMenuItemReadFromXls;
    private javax.swing.JMenuItem jMenuItemReadFromXml;
    private javax.swing.JPanel jPanelDeterministicResults;
    private javax.swing.JPanel jPanelProbPproblem;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JSpinner jSpinnerRadars;
    private javax.swing.JTextArea jTextAreaDeterministicResults;
    private javax.swing.JTextArea jTextAreaProbResults;
    private javax.swing.JTextField jTextFieldA;
    private javax.swing.JTextField jTextFieldAlpha;
    private javax.swing.JTextField jTextFieldB;
    private javax.swing.JTextField jTextFieldCoef;
    private javax.swing.JTextField jTextFieldDist;
    private javax.swing.JTextField jTextFieldFuel;
    private javax.swing.JTextField jTextFieldToll;
    // End of variables declaration                   
}