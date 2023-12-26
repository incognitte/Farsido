/*
 * SavedMessage - This class briefly display a message saying the
 * a file was saved. A timer is used to hide the message.
 *
 * SavedMessage - Æi tiu klaso mallonge montras mesaøon kiu informas
 * ke dosiero estis konservita. Horloøo estas uzata por kaþi la 
 * mesaøon.
 *
 * Cleve Lendon (Klivo)     2000/12/25 
 * indriko@yahoo.com
 * http://purl.oclc.org/net/klivo/
 *
 */


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SavedMessage extends JDialog implements ActionListener {

   JFrame  parent;

   private final static int d_width  = 180;   // dialog width   -  dialoga larøeco
   private final static int d_height = 80;   // dialog height  -  dialoga alteco

   private Timer the_timer;

   private JLabel         line1;

   public SavedMessage (JFrame owner, String[] labels) {

      super(owner, true);   // Modal dialogue. Deviga dialogo.
      setSize(d_width, d_height);
      parent = owner;

      Container cp = getContentPane();
      cp.setLayout(new GridBagLayout());

      line1      = new JLabel(labels[0]);

      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(5,10,5,10);
      //gbc.anchor = GridBagConstraints.NORTH;
      gbc.gridx = 0; gbc.gridy = 0;
      cp.add(line1,gbc);

   }  // end of constructor


   public void actionPerformed(ActionEvent e) {
      // Must be timer. Devas esti horloøo.
      the_timer.stop();
      the_timer = null;
      setVisible(false);
   }


   /*
    * showIt - Display this message. 
    * showIt - Montru æi tiun mesaøon. 
    */
   public void showIt() {

      // Display this message for only 1.5 seconds.
      // Montru æi tiun dialogon nur 1.5 sekuncojn.
      the_timer = new Timer(1500,this);
      the_timer.start();   // komencu

      // Position the message box in the center of the main frame.
      // Metu mesaø-kvadraton en la mezon de la æefa kadro.

      int new_width = line1.getWidth() + 40;
      int new_height = line1.getHeight() * 2 + 40;
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


}  // SavedMessage


