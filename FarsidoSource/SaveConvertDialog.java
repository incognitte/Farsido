/*
 * SaveConvertDialog - This dialogue gives the user a list of file formats 
 * to choose from when saving a file to disk.
 *
 * KonservoKonvertoDialogo - Æi tiu dialogo montras al la uzanto liston de 
 * dosiero-formatoj kiujn li povas elekti por konservi la dokumenton sur disko.
 *
 * Cleve Lendon (Klivo)     1999/02/22
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

public class SaveConvertDialog extends JDialog implements ActionListener {


   JFrame  parent;

   private final static int d_width  = 310;   // dialog width   -  dialoga larøeco
   private final static int d_height = 330;   // dialog height  -  dialoga alteco


   String[]     encoding_names;          // Names of the encodings, eg. SJIS, UTF8
                                         // Nomoj de la kodaroj, ekz. SJIS, UTF8

   String[]     encoding_descriptions;   // Descriptions on the encodings, eg. Shift-JIS Japanese.
                                         // Priskriboj de la kodaroj, ekz. Shift-JIS Japanese.
   int          number_of_encodings;

   Hashtable    long_to_short;           // Associate long description to short internal name.
                                         // Asociigu longan priskribon al mallonga interna nomo.

   Hashtable    short_to_long;           // Associate short internal name to long description.
                                         // Asociigu mallongan internan nomon al longa priskribo.

   private JLabel         line1;
   private JButton        convert_button;
   private JButton        dont_button;
   private JComboBox      format;          // Listo de dosieroformatoj.
   private JPanel         space;           // Make space for the combobox to open into.
                                           // Faru spacon por malfermita selektolisto.

   String decision = "ISO8859_1";    // ISO8859_1 - Latin 1. Same as no conversion.
                                     // Egalas al neniu konverto.

   Color yellow = new Color(0xffff88);   // flava koloro
   private JPanel  background;

   public SaveConvertDialog (JFrame owner, String[] labels, String encodings) {

      super(owner, true);   // Modal dialogue. Deviga dialogo.
      setSize(d_width, d_height);
      parent = owner;

      Container cp = getContentPane();

      background = new JPanel(new GridBagLayout());
      background.setBackground(yellow);

      //cp.setLayout(new GridBagLayout());

      String[] tokens       = SimCon.tokenize(encodings);
      number_of_encodings   = tokens.length / 2; 
      encoding_names        = new String[number_of_encodings];
      encoding_descriptions = new String[number_of_encodings];

      int i,j;
      for (i = 0, j = 0; j < tokens.length; i++, j = j + 2) {
         encoding_names[i]        = tokens[j];
         encoding_descriptions[i] = tokens[j+1];
      }

      long_to_short = new Hashtable();
      for (i = 0; i < number_of_encodings; i++) {
         long_to_short.put(encoding_descriptions[i], encoding_names[i]);
      }

      short_to_long = new Hashtable();
      for (i = 0; i < number_of_encodings; i++) {
         short_to_long.put(encoding_names[i], encoding_descriptions[i]);
      }

      // Create and fill the format list.  Kreu kaj plenigu la formatliston.
      format = new JComboBox();
      for (i = 0; i < number_of_encodings; i++) {
         format.addItem(encoding_descriptions[i]);
      }

      setBackground(yellow);

      line1           = new JLabel(labels[0]);
      convert_button  = new JButton(labels[1]);
      dont_button     = new JButton(labels[2]);
      space           = new JPanel();
      space.setBackground(yellow);

      convert_button.setActionCommand("convert");
      convert_button.addActionListener(this);

      dont_button.setActionCommand("dont");
      dont_button.addActionListener(this);

      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(5,10,5,10);
      gbc.anchor = GridBagConstraints.NORTH;
      gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
      background.add(line1,gbc);
      gbc.gridy = 1; gbc.gridwidth = 1;
      background.add(convert_button,gbc);
      gbc.gridx = 1;
      background.add(dont_button,gbc);
      gbc.gridx = 0; gbc.gridy = 2;
      gbc.gridwidth = 2; gbc.fill = GridBagConstraints.BOTH;
      background.add(format,gbc);
      gbc.gridx = 0; gbc.gridy = 3; gbc.gridheight = 7;
      gbc.weighty = 0.7;
      background.add(space,gbc);
      cp.add(background);

   }  // end of constructor


   public void actionPerformed(ActionEvent e) {
      String command = e.getActionCommand();
      if (command.equals("convert")) {
         decision = (String)long_to_short.get((String)format.getSelectedItem());
         setVisible(false);
         return;
      }
      else if (command.equals("dont")) {
         decision = "ISO8859_1";   // Latin1 -> Unicode - same as no conversion 
                                   // egalas al neniu konverto.
         setVisible(false);
         return;
      }
   }  // actionPerformed


   /*
    * getDecision - Get the encoding name to convert from.
    * getDecision - Akiru la kodaran nomo por elkonverto.
    */
   public String getDecision() {
      return decision;
   }  // getDecision

   /*
    * showIt - Display this dialog.
    * showIt - Montru æi tiun dialogon.
    */
   public void showIt() {
      showIt(null);
   }


   /*
    * showIt - Display this dialog. Set format box to the default value.
    * showIt - Montru æi tiun dialogon. Metu defaýltan formaton en la selektolisto.
    */
   public void showIt(String default_format) {

      // Position the dialog in the center of the main frame.
      // Metu la dialogon en la mezon de la æefa kadro.

      int new_width = format.getWidth() + 40;
      if (new_width < d_width) new_width = d_width;
      int new_height = new_width  + 100;
      if (new_height < d_height) new_height = d_height;

      Rectangle p_rect = parent.getBounds();
      Rectangle d_rect = new Rectangle(p_rect.x + Math.abs(p_rect.width - new_width)/2,
                                       p_rect.y + Math.abs(p_rect.height - new_height)/2,
                                       new_width,new_height);
      setBounds(d_rect);
      if (default_format != null) {
         format.setSelectedItem(short_to_long.get(default_format));
      }

      setVisible(true);
      validate();

   }  // showIt

}  // SaveConvertDialog


