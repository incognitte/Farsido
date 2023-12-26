/*
 * ExistsDialog - This class informs the user that a file which
 * is about to be saved already exists, and asks it the user wants
 * to overwrite it.
 *
 * Æi tiu klaso informas la uzanton ke konservota dosiero jam 
 * ekzistas, kaj demandas æu li/þi volas surskribi øin.
 *
 * Cleve Lendon (Klivo)     2000/12/25 
 * indriko@yahoo.com
 * http://purl.oclc.org/net/klivo/
 *
 */


import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class ExistsDialog extends JDialog implements ActionListener {

   JFrame  parent;

   private final static int d_width  = 240;   // dialog width   -  dialoga larøeco
   private final static int d_height = 110;   // dialog height  -  dialoga alteco

   private String    decision;   // yes or no  / jes aý ne

   private JLabel    line1;
   private JButton   yes_button;
   private JButton   no_button;

   public ExistsDialog (JFrame owner, String[] labels) {

      super(owner, true);   // Modal dialogue. Deviga dialogo.
      setSize(d_width, d_height);
      parent = owner;

      Container cp = getContentPane();
      cp.setLayout(new GridBagLayout());

      line1       = new JLabel(labels[0]);
      yes_button  = new JButton(labels[1]);
      no_button   = new JButton(labels[2]);

      yes_button.setActionCommand("yes");
      yes_button.addActionListener(this);

      no_button.setActionCommand("no");
      no_button.addActionListener(this);

      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(5,10,5,10);
      //gbc.anchor = GridBagConstraints.NORTH;
      gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
      cp.add(line1,gbc);
      gbc.gridwidth = 1;
      gbc.gridy = 1;
      cp.add(yes_button,gbc);
      gbc.gridx = 1;
      cp.add(no_button,gbc);

   }  // end of constructor


   /*
    * showIt - Display this dialog. 
    * showIt - Montru æi tiun dialogon. 
    */
   public void showIt() {

      // Position the dialog in the center of the main frame.
      // Metu la dialogon en la mezon de la æefa kadro.

      int new_width = line1.getWidth() + 40;
      int new_height = line1.getHeight() * 3 + 40;
      if (new_width < d_width) new_width = d_width;
      if (new_height < d_height) new_height = d_height;

      Rectangle p_rect = parent.getBounds();
      Rectangle d_rect = new Rectangle(p_rect.x + Math.abs(p_rect.width - new_width)/2,
                                       p_rect.y + Math.abs(p_rect.height - new_height)/2,
                                       new_width,new_height);

      setBounds(d_rect);
      setVisible(true);
      validate();

   }  // showIt



   public void actionPerformed(ActionEvent e) {
      decision = e.getActionCommand();
      setVisible(false);
   }  // actionPerformed


   /*
    * getDecision - Indicates the user's decision: yes or no.
    * getDecision - Indikas la decidon de la uzanto: jes aý ne.
    */
   public String getDecision() {
      return decision;
   }  // getDecision


}  // ExistsDialog


