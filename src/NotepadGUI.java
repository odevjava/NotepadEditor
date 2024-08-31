import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class NotepadGUI extends JFrame {
    // file explorer
    private JFileChooser fileChooser;

    private JTextArea textArea;
    private File currentFile;

    // swing's built in library to manage undo and redo functionalities
    private UndoManager undoManager;

    public NotepadGUI() {

        super("Notepad GUI");
        setSize(400, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // file chooser setup
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(  "src/assets"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));

        addGuiComponents();
    }

    private void addGuiComponents() {
        addToolbar();

        // area to type text
        textArea = new JTextArea();
        add(textArea, BorderLayout.CENTER);

    }

    private void addToolbar() {
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);

        // menu bar
        JMenuBar menuBar = new JMenuBar();
        toolbar.add(menuBar);

        // add menus
        menuBar.add(addFileMenu());
        menuBar.add(addEditMenu());


        add(toolbar, BorderLayout.NORTH);
    }

    private JMenu addFileMenu() {
        JMenu fileMenu = new JMenu("File");

        // "new" functionality - resets everything
        JMenuItem newMenuItem = new JMenuItem("New");
        newMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // reset title header
                setTitle("Notepad");

                // reset text area
                textArea.setText("");

                // reset current file
                currentFile = null;
            }
        });
        fileMenu.add(newMenuItem);

        // "open" functionality - open a text file
        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // open file explorer
                int result = fileChooser.showOpenDialog(NotepadGUI.this);

                if (result != JFileChooser.APPROVE_OPTION) return;

                try{
                    // reset notepad
                    newMenuItem.doClick();

                    // get the selected file
                    File selectedFile = fileChooser.getSelectedFile();

                    // update current file
                    currentFile = selectedFile;

                    // update title header
                    setTitle(selectedFile.getName());

                    // read the file
                    FileReader fileReader = new FileReader(selectedFile);
                    BufferedReader bufferedReader = new BufferedReader(fileReader);

                    // store the text
                    StringBuilder fileText = new StringBuilder();
                    String readText;
                    while ((readText = bufferedReader.readLine()) != null) {
                        fileText.append(readText);
                    }

                    // update text area gui
                    textArea.setText(fileText.toString());

                }catch (Exception e1){

                }
            }
        });
        fileMenu.add(openMenuItem);

        // "save as" functionality - creates a new text file and saves user text
        JMenuItem saveAsMenuItem = new JMenuItem("Save As");
        saveAsMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // open save dialog
                int result = fileChooser.showSaveDialog(NotepadGUI.this);

                // continue to execute code only if the user pressed the save button
                if (result != JFileChooser.APPROVE_OPTION) return;
                try{
                    File selectedFile = fileChooser.getSelectedFile();

                    String fileName = selectedFile.getName();
                    if (!fileName.substring(fileName.length() -4).equalsIgnoreCase(".txt")){
                        selectedFile = new File(selectedFile.getAbsoluteFile() + ".txt");

                    }

                    // create a new file
                    selectedFile.createNewFile();

                    FileWriter fileWriter = new FileWriter(selectedFile);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                    bufferedWriter.write(textArea.getText());
                    bufferedWriter.close();
                    fileWriter.close();

                    // update the title header of gui to the save text file
                    setTitle(fileName);

                    //update current file
                    currentFile = selectedFile;

                    // show display dialog
                    JOptionPane.showMessageDialog(NotepadGUI.this, "File Saved!");

                }catch (Exception e1){
                    e1.printStackTrace();
                }

            }
        });
        fileMenu.add(saveAsMenuItem);

        // "save" functionality - saves text into current text file
        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                // if the current file is null then we have to perform save as functionality
                if (currentFile == null) saveAsMenuItem.doClick();

                if (currentFile == null) return;


                try{
                    // write current file
                    FileWriter fileWriter = new FileWriter(currentFile);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                    bufferedWriter.write(textArea.getText());
                    bufferedWriter.close();
                    fileWriter.close();
                }catch (Exception e1){
                    e1.printStackTrace();
                }
            }
        });
        fileMenu.add(saveMenuItem);

        // "exit" functionality - ends program process
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
           @Override
            public void actionPerformed(ActionEvent e) {
               // dispose of this gui
               NotepadGUI.this.dispose();
           }
        });
        fileMenu.add(exitMenuItem);



        return fileMenu;

    }

    private JMenu addEditMenu(){
        JMenu editMenu = new JMenu("Edit");

        JMenuItem undoMenuItem = new JMenuItem("Undo");
        editMenu.add(undoMenuItem);

        JMenuItem redoMenuItem = new JMenuItem("Redo");
        


        return editMenu;
    }
}
