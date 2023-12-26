/*
 * SaveKeyDialog - This dialogue asks the user to enter an encryption key,
 * in order to save a file in encrypted format.
 *
 * Konservo-�losilo-Dialogo - �i tiu dialogo petas kriptigan �losilon, por
 * konservi dosieron en kriptigita formo.
 *
 * Cleve Lendon (Klivo)     1999/06/17
 * klivo@infotrans.or.jp
 * http://www.infotrans.or.jp/~klivo/
 *
 */


/*
 * Changes for 3.2  �an�oj por 3.2    1999/08
 *
 * Calculate size of dialogue.
 * Kalkulu grandecon de dialogujo.
 *
 */

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class SaveKeyDialog extends JDialog implements ActionListener {


   JFrame  parent;

   private final static int d_width  = 320;   // dialog width   -  dialoga lar�eco
   private final static int d_height = 240;   // dialog height  -  dialoga alteco

   private JLabel           line1;
   private JLabel           line2;
   private JButton          ok_button;
   private JButton          cancel_button;
   private JPasswordField   key_field;
   private JPasswordField   confirm_field;

   char[] the_key = new char[30];     // la �losilo

   public SaveKeyDialog (JFrame owner, String[] labels) {

      super(owner, true);   // Modal dialogue. Deviga dialogo.
      setSize(d_width, d_height);
      parent = owner;

      Container cp = getContentPane();

      cp.setLayout(new GridBagLayout());

      line1           = new JLabel(labels[0]);
      line2           = new JLabel(labels[1]);
      ok_button       = new JButton(labels[2]);
      cancel_button   = new JButton(labels[3]);

      ok_button.setActionCommand("ok");
      ok_button.addActionListener(this);

      cancel_button.setActionCommand("cancel");
      cancel_button.addActionListener(this);

      key_field     = new JPasswordField("",30);
      confirm_field = new JPasswordField("",30);

      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(5,10,5,10);
      gbc.anchor = GridBagConstraints.CENTER;
      gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
      cp.add(line1,gbc);
      gbc.gridy = 1; gbc.gridwidth = 2;
      cp.add(line2,gbc);
      gbc.gridy = 2; gbc.gridwidth = 2;
      gbc.fill = GridBagConstraints.BOTH;
      cp.add(key_field,gbc);
      gbc.gridy = 3;
      cp.add(confirm_field,gbc);     
      gbc.fill = GridBagConstraints.NONE;
      gbc.gridy = 4; gbc.gridwidth = 1;
      cp.add(ok_button,gbc);
      gbc.gridx = 1;
      cp.add(cancel_button,gbc);

   }  // end of constructor


   public void actionPerformed(ActionEvent e) {
      String command = e.getActionCommand();
      if (command.equals("ok")) {
         the_key = null;
         // The key field and the confirmation field must be 
         // the same and greater than 7 chars in length.
         // La �losila teksto kaj la konfirma teksto devas esti
         // samaj kaj pli ol 7 literojn longaj.
         char[] key = key_field.getPassword();
         int kl = key.length;
         char[] confirm = confirm_field.getPassword();
         int cl = confirm.length;
         if ((cl != kl) || kl < Encrypt.MIN_KEY_SIZE) { 
            return;
         }
         for (int i = 0; i < kl; i++) 
            if (key[i] != confirm[i]) {
               return;
            }
         the_key = key;      
         setVisible(false);
         return;
      }
      else if (command.equals("cancel")) {
         the_key = null;
         setVisible(false);
         return;
      }
   }  // actionPerformed


   /*
    * getKey - Get the encrytion key.
    * getKey - Akiru la kriptigan �losilon.
    */
   public char[] getKey() {
      return the_key;
   }  // getKey

   /*
    * showIt - Display this dialog.
    * showIt - Montru �i tiun dialogon.
    */
   public void showIt() {
      showIt(null);
   }


   /*
    * showIt - Display this dialog. Set key fields to previous key, if there was one.
    * showIt - Montru �i tiun dialogon. Plenigu �losilaj tekstujoj per anta�a �losilo, se estis.
    */
   public void showIt(char[] previous_key) {

      // Position the dialog in the center of the main frame.
      // Metu la dialogon en la mezon de la �efa kadro.

      int new_width = key_field.getWidth() + 50;
      int new_height = ok_button.getHeight() * 4 + 60;
      if (new_width < d_width) new_width = d_width;
      if (new_height < d_height) new_height = d_height;

      Rectangle p_rect = parent.getBounds();
      Rectangle d_rect = new Rectangle(p_rect.x + Math.abs(p_rect.width - new_width)/2,
                                       p_rect.y + Math.abs(p_rect.height - new_height)/2,
                                       new_width,new_height);
      setBounds(d_rect);
      if (previous_key != null) {
         key_field.setText(new String(previous_key));
         confirm_field.setText(new String(previous_key));
      }
      else {
         key_field.setText("");
         confirm_field.setText("");
      }

      setVisible(true);
      validate();

   }  // showIt

}  // SaveKeyDialog






