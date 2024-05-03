package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class MenuText extends JFrame {
    private JCheckBox checkText;
	private JCheckBox checkXML;
    private ArrayList<JCheckBox> checkPDF;
    private ArrayList<String> filesToParse;
    private String parseToWhat;
    
    public MenuText(String s,Main m) {

    	setTitle("Menu de selection de PDF");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        File directory = new File(s);
        // Parcourez les fichiers du répertoire
        File[] files = directory.listFiles();
        checkPDF = new ArrayList<JCheckBox>();
        filesToParse = new ArrayList<String>();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".pdf")) {
                	checkPDF.add(new JCheckBox(file.getName()));
                }
            }
        }
        
        // Création des JCheckBox
        checkText = new JCheckBox("fichier texte");
        checkXML = new JCheckBox("fichier XML");
        checkText.setSelected(true);
        checkXML.setSelected(true);
        JButton btnSelectAll= new JButton("Tout sélectionner");
        JButton btnDeselectAll = new JButton("Tout désélectionner");
        // Ajout d'un écouteur d'événement pour les JCheckBox
        ItemListener itemListener = new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                updateLabel();
            }
        };
        btnSelectAll.addActionListener(new ActionListener() { 
      	  public void actionPerformed(ActionEvent e) { 
          	for(JCheckBox jcb : checkPDF) {
        		jcb.setSelected(true);
        	}
      	  } 
      	} );
        btnDeselectAll.addActionListener(new ActionListener() { 
    	  public void actionPerformed(ActionEvent e) { 
          	for(JCheckBox jcb : checkPDF) {
        		jcb.setSelected(false);
        	}
      	  } 
      	} );
        checkText.addItemListener(itemListener);
        checkXML.addItemListener(itemListener);
        JLabel label = new JLabel("Cocher les PDF à parser :");
        // Ajout d'un écouteur d'événement pour le boutton
        JButton button = new JButton("Parser");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if(parseToWhat!=null) {
	            	try {
						m.exec(s,filesToParse,parseToWhat);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
            	}
            }
        });
        
        //mise en page
        JPanel extensionPanel = new JPanel();
        extensionPanel.setBackground(Color.gray);
        JPanel selectPanel = new JPanel();
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        selectPanel.setLayout(gb);
        this.setLayout(new BorderLayout());
        this.add(extensionPanel, BorderLayout.NORTH);
        JScrollPane jsp = new JScrollPane(selectPanel);
        this.add(jsp, BorderLayout.CENTER);

        extensionPanel.add(checkText);
        extensionPanel.add(checkXML);
        gbc.gridx=0;
        gbc.gridy=0;
        selectPanel.add(label, gbc);
        gbc.gridy=1;
        selectPanel.add(btnSelectAll, gbc);
        gbc.gridy=2;
        selectPanel.add(btnDeselectAll, gbc);
        gbc.gridx=1;
        gbc.gridy=0;
        
        for(JCheckBox jcb : checkPDF) {
        	jcb.addItemListener(itemListener);
            gbc.gridy+=1;
            selectPanel.add(jcb, gbc);
        }
        this.add(button, BorderLayout.SOUTH);
        
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	dispose();
            }
        });
    }
    public int chercheInd(String name) {
    	int i =0;
    	for(String s : filesToParse) {
    		if(s.equals(name)) return i;
    		i++;
    	}
    	return -1;
    }
    // Mettre à jour le texte du JLabel pour afficher les options cochées
    private void updateLabel() {
        
        if (checkXML.isSelected() && !checkText.isSelected()) {
            parseToWhat="-x";
        }
        if (checkText.isSelected() && !checkXML.isSelected()) {
        	parseToWhat="-t";
        }
        if (checkText.isSelected() && checkXML.isSelected()) {
       	 	parseToWhat="-all";
        }
        for(JCheckBox jcb : checkPDF) {
        	if(jcb.isSelected()) {
        		if(chercheInd(jcb.getText())==-1) filesToParse.add(jcb.getText());
        	}else {
        		if(chercheInd(jcb.getText())!=-1) filesToParse.remove(chercheInd(jcb.getText()));
        	}
        	
        }     
    }

	public ArrayList<String> getFilesToParse() {
		return filesToParse;
	}

	public void setFilesToParse(ArrayList<String> filesToParse) {
		this.filesToParse = filesToParse;
	}

	public String getParseToWhat() {
		return parseToWhat;
	}

	public void setParseToWhat(String parseToWhat) {
		this.parseToWhat = parseToWhat;
	}
    public JFrame getFrame() {
    	return this;
    }
    
}
