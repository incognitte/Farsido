/*
 * OpenConvertDialog - This dialogue gives the user a list of file formats when
 * he or she selects a file to open. Simredo tries to detect the format of
 * the file to open. If Simredo detects UTF8 codes, it will make the listbox 
 * default to UTF8. The user may select another format for opening.
 *
 * MalfermoKonvertoDialogo - Æi tiu dialogo montras al la uzanto liston de 
 * dosiero-formatoj kiam li aý þi selektas dosieron por malfermi. Simredo 
 * provas determini la formaton de la malfermota dosiero. Se Simredo vidas 
 * UTF8-kodojn, øi defaýltigos la selektoliston al UTF8. La uzanto povas 
 * selekti alian formaton por malfermado. 
 *
 * Cleve Lendon (Klivo)     1999/02/19
 * klivo@infotrans.or.jp
 * http://www.infotrans.or.jp/~klivo/
 *
 * Changes / Þanøoj
 * Don't put "Encrypted" into the list box.
 * Ne metu "Kriptigita" en la selektoskatolon.
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

public class OpenConvertDialog extends JDialog implements ActionListener {

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

   public OpenConvertDialog (JFrame owner, String[] labels, String encodings) {

      super(owner, true);   // Modal dialogue. Deviga dialogo.
      setSize(d_width, d_height);
      parent = owner;

      Container cp = getContentPane();
      cp.setLayout(new GridBagLayout());

      String[] tokens       = SimCon.tokenize(encodings);
      number_of_encodings   = (tokens.length / 2) - 3;   // Skip the first 3 (big,little,encrypted).
                                    // Ne inkluzivu la unuajn tre (pezkomenca,pezfina,kriptigita).
      encoding_names        = new String[number_of_encodings];
      encoding_descriptions = new String[number_of_encodings];

      int i,j;
      // j = 6, Skip the first 3 (UTF16 big and little, encrypted).
      // j = 6, Ne inkluzivu la unuajn tri (UTF16 pezkomenca kaj pezfina, kriptigita).
      for (i = 0, j = 6; j < tokens.length; i++, j = j + 2) {
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

      line1           = new JLabel(labels[0]);
      convert_button  = new JButton(labels[1]);
      dont_button     = new JButton(labels[2]);
      space           = new JPanel();

      convert_button.setActionCommand("convert");
      convert_button.addActionListener(this);

      dont_button.setActionCommand("dont");
      dont_button.addActionListener(this);

      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(5,10,5,10);
      gbc.anchor = GridBagConstraints.NORTH;
      gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
      cp.add(line1,gbc);
      gbc.gridy = 1; gbc.gridwidth = 1;
      cp.add(convert_button,gbc);
      gbc.gridx = 1;
      cp.add(dont_button,gbc);
      gbc.gridx = 0; gbc.gridy = 2; 
      gbc.gridwidth = 2; gbc.fill = GridBagConstraints.BOTH;
      cp.add(format,gbc);
      gbc.gridx = 0; gbc.gridy = 3; gbc.gridheight = 7;
      gbc.weighty = 0.7;
      cp.add(space,gbc);

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


}  // OpenConvertDialog


