package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileEditor extends JDialog {
    /** 
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
    private JButton buttonTopNew;
    private JButton buttonClose;
    private JButton buttonSave;
    private JButton buttonBottomNew;
    private JButton buttonOpenTop;
    private JButton buttonOpenBottom;
    private JTextArea textAreaTop;
    private JTextArea textAreaBottom;
    private JTextArea textAreaNew;
    String directory; // The default directory to display in the FileDialog
    String selection;
    public FileEditor() {
    	contentPane = new JPanel();
    	contentPane.setBackground(new Color(51, 51, 51));
   
    	buttonTopNew = new JButton();
    	buttonTopNew.setText("Top -> New");
    	buttonTopNew.setBounds(281, 79, 117, 29);
        contentPane.add(buttonTopNew);
    	
        buttonSave = new JButton();
        buttonClose =  new JButton();
        
        directory = "C:\\Users\\Nena\\Desktop\\Test";
    
        
        textAreaTop = new JTextArea();
        textAreaTop.setBackground(new Color(112, 114, 114));
        textAreaTop.setBounds(17, 6, 260, 168);
        contentPane.add(textAreaTop);
        
        textAreaBottom = new JTextArea();
        textAreaBottom.setBackground(new Color(111, 112, 113));
        textAreaBottom.setBounds(17, 186, 260, 161);
        contentPane.add(textAreaBottom);
        
        buttonBottomNew = new JButton("Bottom -> New");
        buttonBottomNew.setBounds(281, 252, 127, 29);
        contentPane.add(buttonBottomNew);
        
        textAreaNew = new JTextArea();
        textAreaNew.setBackground(new Color(110, 111, 112));
        textAreaNew.setBounds(410, 6, 276, 345);
        contentPane.add(textAreaNew);
        
        buttonOpenTop = new JButton("Open Top");
        buttonOpenTop.setBounds(6, 363, 92, 29);
        contentPane.add(buttonOpenTop);
        
        buttonOpenBottom = new JButton("Open Bottom");
        buttonOpenBottom.setBounds(92, 363, 117, 29);
        contentPane.add(buttonOpenBottom);
        
        buttonSave = new JButton("Save New");
        buttonSave.setBounds(207, 363, 85, 29);
        contentPane.add(buttonSave);
        
        buttonClose = new JButton("Close");
        buttonClose.setBounds(289, 363, 109, 29);
        contentPane.add(buttonClose);
        
        setModal(true);
        contentPane.setLayout(null);
  
        setContentPane(contentPane);
        
        buttonOpenTop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openFile(textAreaTop, directory);
            }
        });
        
        buttonOpenBottom.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		openFile(textAreaBottom, directory);
        	}
        });
        
    	buttonTopNew.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			getSelectedTextAndCopy(textAreaTop, textAreaNew);
    		}
    	});
        
        buttonBottomNew.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		getSelectedTextAndCopy(textAreaBottom, textAreaNew);
        	}
        });
    
        buttonSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	saveNewFile(textAreaNew);
            }
        });
        buttonClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	close();
            }
        });
     

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
            close();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	close();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public void saveFile(String directory, String filename, JTextArea textArea) {
        if ((filename == null) || (filename.length() == 0))
            return;
        File file;
        FileWriter out = null;
        try {
            file = new File(directory, filename); // Create a file object
            out = new FileWriter(file); // And a char stream to write it
            textArea.getLineCount(); // Get text from the text area
            String s = textArea.getText();
            out.write(s);
        }
        // Display messages if something goes wrong
        catch (IOException e) {
        	textArea.setText(e.getClass().getName() + ": " + e.getMessage());
            this.setTitle("FileViewer: " + filename + ": I/O Exception");
        }
        // Always be sure to close the input stream!
        finally {
            try {
                if (out != null)
                    out.close();
            }
            catch (IOException e) {
            }
        }
    }

    public void loadAndDisplayFile(JTextArea textArea, String directory, String filename) {
        if ((filename == null) || (filename.length() == 0))
            return;
        File file;
        FileReader in = null;  
        // Read and display the file contents. Since we're reading text, we
        // use a FileReader instead of a FileInputStream.
        try {
            file = new File(directory, filename); // Create a file object
             in = new FileReader(file); // And a char stream to read it
            char[] buffer = new char[4096]; // Read 4K characters at a time
            int len; // How many chars read each time
            textArea.setText(""); // Clear the text area
            while ((len = in.read(buffer)) != -1) { // Read a batch of chars
                String s = new String(buffer, 0, len); // Convert to a string 
                textArea.append(s); // And display them
            }
            this.setTitle("FileViewer: " + filename); // Set the window title
            textArea.setCaretPosition(0); // Go to start of file
        }
        // Display messages if something goes wrong
        catch (IOException e) {
        	textArea.setText(e.getClass().getName() + ": " + e.getMessage());
            this.setTitle("FileViewer: " + filename + ": I/O Exception");
        }
        // Always be sure to close the input stream!
        finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
            }
        }
    }

    private void openFile(JTextArea textArea, String directory) {
        // Create a file dialog box to prompt for a new file to display
        FileDialog f = new FileDialog(this, "Otvori fajl", FileDialog.LOAD);
        f.setDirectory(directory); // Set the default directory
        // Display the dialog and wait for the user's response
        f.setVisible(true);
        loadAndDisplayFile(textArea, f.getDirectory(), f.getFile()); // Load and display selection
        f.dispose(); // Get rid of the dialog box
    }

    private void saveNewFile(JTextArea textArea) {
        // Create a file dialog box to prompt for a new file to display
        FileDialog f = new FileDialog(this, "Otvori fajl", FileDialog.SAVE);
        f.setDirectory(directory); // Set the default directory
        // Display the dialog and wait for the user's response
        f.setVisible(true);
        directory = f.getDirectory(); // Remember new default directory
        saveFile(directory, f.getFile(), textArea); // Load and display selection
        f.dispose(); // Get rid of the dialog box
    }

    private void close() {
        // add your code here if necessary
        dispose();
    }

    private void getSelectedTextAndCopy(JTextArea textAreaFrom, JTextArea textAreaTo)
    {
    	selection = textAreaFrom.getSelectedText();
    	if(textAreaTo.getText() == null) {
    	    textAreaTo.setText(selection);
    	} else {
    		textAreaTo.append(selection);
    	}
    }

    public static void main(String[] args) {
        FileEditor dialog = new FileEditor();
        dialog.pack();
        dialog.setTitle("File Editor");
        dialog.setSize(new Dimension(700, 440));
        dialog.setResizable(true);
        dialog.setVisible(true);
        System.exit(0);
    }

    private void createUIComponents()
    {
        // TODO: place custom component creation code here
    }
}
