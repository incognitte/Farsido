/*
 * ErrorDialog - This dialogue displays an error message, such
 * as "Unable to save file".
 *
 * ErrorDialog (EraroDialogo) Æi tiu dialogo montras erarmesaøon,
 * ekzemple "Ne povas konservi dosieron."
 *
 * Cleve Lendon (Klivo)     1999/06/30
 * indriko@yahoo.com
 * http://purl.oclc.org/net/klivo/
 *
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
import java.util.*;
import javax.swing.*;

public class ErrorDialog extends JDialog implements ActionListener {

   JFrame  parent;

   private final static int d_width  = 240;   // dialog width   -  dialoga larøeco
   private final static int d_height = 110;   // dialog height  -  dialoga alteco


   private JLabel         line1;
   private JButton       ok_button;


   public ErrorDialog (JFrame owner, String[] labels) {

      super(owner, true);   // Modal dialogue. Deviga dialogo.
      setSize(d_width, d_height);
      parent = owner;

      Container cp = getContentPane();
      cp.setLayout(new GridBagLayout());

      line1      = new JLabel("");
      ok_button  = new JButton(labels[0]);

      ok_button.setActionCommand("ok");
      ok_button.addActionListener(this);

      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(5,10,5,10);
      //gbc.anchor = GridBagConstraints.NORTH;
      gbc.gridx = 0; gbc.gridy = 0;
      cp.add(line1,gbc);
      gbc.gridy = 1;
      cp.add(ok_button,gbc);

   }  // end of constructor


   public void actionPerformed(ActionEvent e) {

      String command = e.getActionCommand();
      if (command.equals("ok")) {
         setVisible(false);
         return;
      }

   }  // actionPerformed


   /*
    * showIt - Display this dialog. 
    * showIt - Montru æi tiun dialogon. 
    */
   public void showIt(String error_message) {

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
      line1.setText(error_message);
      line1.repaint();

      setVisible(true);
      validate();

   }  // showIt


}  // ErrorDialog


