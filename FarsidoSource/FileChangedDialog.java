/*
 * FileChangedDialog - This dialog asks the user if he or she wants to 
 * save changes to the current file, before exiting or opening a new file.
 *
 * FileChangedDialog - Æi tiu dialogo demandas al la uzanto æu li aý þi volas
 * konservi þanøojn en la nuna dosiero antaý ol eliri aý malfermi novan dokumenton.
 */


/*
 * Changes for 3.2  Þanøoj por 3.2    1999/08
 *
 * Calculate size of dialogue.
 * Kalkulu grandecon de dialogujo.
 *
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class FileChangedDialog extends JDialog implements ActionListener {

   JFrame  parent;

   private final static int d_width  = 360;   // dialog width   -  dialoga larøeco
   private final static int d_height = 180;   // dialog height  -  dialoga alteco

   String    decision = "yes";   

   private JLabel        line1;
   private JLabel        line2;
   private JButton       yes_button;
   private JButton       no_button;
   private JButton       cancel_button;

   private JPanel        p1 = new JPanel();

   public FileChangedDialog (JFrame owner, String[] labels) {
 
      super(owner, true);   // Modal dialog. Deviga dialogo.
      setSize(d_width, d_height);
      parent = owner;

      Container cp = getContentPane();
      cp.setLayout(new GridBagLayout());


      line1          = new JLabel(labels[0]);
      line2          = new JLabel(labels[1]);
      yes_button     = new JButton(labels[2]);
      no_button      = new JButton(labels[3]);
      cancel_button  = new JButton(labels[4]);

      yes_button.setActionCommand("yes");
      yes_button.addActionListener(this);

      no_button.setActionCommand("no");
      no_button.addActionListener(this);

      cancel_button.setActionCommand("cancel");
      cancel_button.addActionListener(this);

      GridBagConstraints gbc = new GridBagConstraints();
      gbc.anchor = GridBagConstraints.CENTER;
      gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3;
      gbc.insets = new Insets(5,10,5,10);
      cp.add(line1,gbc);
      gbc.gridy = 1;
      cp.add(line2,gbc);
      gbc.gridy = 2; gbc.gridwidth = 1;
      cp.add(yes_button,gbc);
      gbc.gridx = 1;
      cp.add(no_button,gbc);
      gbc.gridx = 2;
      cp.add(cancel_button,gbc);


   }  // end of constructor


   public void actionPerformed(ActionEvent e) {
      decision = e.getActionCommand();
      setVisible(false);
   }  // actionPerformed


   /*
    * show - Display this dialog.
    * show - Montru æi tiun dialogon.
    */
   public void showIt() {

      // Position the dialog in the center of the main frame.
      // Metu la dialogon en la mezon de la æefa kadro.

      int new_width = line1.getWidth() + 40;
      int new_height = yes_button.getHeight() * 5;
      if (new_width < d_width) new_width = d_width;
      if (new_height < d_height) new_height = d_height;

      Rectangle p_rect = parent.getBounds();
      Rectangle d_rect = new Rectangle(p_rect.x + Math.abs(p_rect.width - new_width)/2,
                                       p_rect.y + Math.abs(p_rect.height - new_height)/2,
                                       new_width,new_height);
      setBounds(d_rect);

      setVisible(true);
      validate();

   }  // show

   /*
    * getDecision - Indicates the user's decision: yes, no, or cancel.
    * getDecision - Indikas la decidon de la uzanto: jes, ne aý faru nenion.
    */
   public String getDecision() {
      return decision;
   }  // getDecision


}  // FileChangedDialog



