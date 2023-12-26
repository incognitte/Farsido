/*
 * ChangeDialog
 * This class implements a search and change dialogue. It's based on SearchDialog.
 * It must listen for "new font" events from the font_list.
 * Æi tiu klaso kreas seræ-kaj-þanø-dialogon. Øi baziøas sur SearchDialog.
 * Øi devas aýskulti "new font" (nova tiparo) eventojn de la tipara listo.
 *
 * Cleve Lendon (Klivo)
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
import javax.swing.*;
import javax.swing.text.*;


public class ChangeDialog extends JDialog implements ActionListener {

   JFrame       parent;
   Farsido     editor;

   private final static int d_width  = 380;   // dialog width   -  dialoga larøeco
   private final static int d_height = 230;   // dialog height  -  dialoga alteco

   JLabel   old_label;
   JLabel   new_label;
   JLabel   number_changed_label;
   String   number_changed_text;

   JButton  next_button;
   JButton  change_one_button;
   JButton  change_all_button;
   JButton  exit_button;

   JCheckBox   distinguish_box;   // Distinguish between upper and lower case.
                                  // Distingu inter majuskloj kaj minuskloj.

   SimTextField   old_text;    // seræota teksto (malnova teksto)
   SimTextField   new_text;    // nova teksto

   SimTextPane  text_pane;

   Segment      the_text = new Segment();
   Document     doc;

   public ChangeDialog (JFrame parent, Farsido editor, String[] labels) {

      //super(parent, "  " + labels[0]); // Ne povas montri Unikodon en kadro.
      super(parent, "  ");
      this.parent = parent;
      this.editor = editor;

      setSize(d_width, d_height);

      next_button   = new JButton(labels[1]);
      next_button.setActionCommand("next");
      next_button.addActionListener(this);

      change_one_button = new JButton(labels[2]);
      change_one_button.setActionCommand("change one");
      change_one_button.addActionListener(this);

      distinguish_box = new JCheckBox(labels[3]);
      distinguish_box.setActionCommand("distinguish");
      distinguish_box.addActionListener(this);

      old_label = new JLabel(labels[4]);
      old_text  = new SimTextField("",30);
      new_label = new JLabel(labels[5]);
      new_text  = new SimTextField("",30);

      change_all_button = new JButton(labels[6]);
      change_all_button.setActionCommand("change all");
      change_all_button.addActionListener(this);

      exit_button = new JButton(labels[7]);
      exit_button.setActionCommand("exit");
      exit_button.addActionListener(this);

      number_changed_label = new JLabel("");
      number_changed_text  = labels[8];

      Container cp = getContentPane();
      cp.setLayout(new GridBagLayout());

      // Arrange components. Aranøu butonojn.
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(5,10,5,10);

      gbc.fill = GridBagConstraints.NONE;
      gbc.anchor = GridBagConstraints.WEST;
      gbc.gridx = 0;  gbc.gridy = 0; gbc.gridwidth = 2;
      cp.add(next_button,gbc);
      gbc.gridx = 2;
      cp.add(change_one_button,gbc);
      gbc.gridx = 5;
      gbc.anchor = GridBagConstraints.EAST;
      cp.add(exit_button,gbc);
      gbc.anchor = GridBagConstraints.CENTER;
      gbc.gridx = 0;  gbc.gridy = 1; gbc.gridwidth = GridBagConstraints.REMAINDER;
      cp.add(distinguish_box,gbc);

      gbc.anchor = GridBagConstraints.NORTHWEST;
      gbc.gridx = 0;  gbc.gridy = 2; gbc.gridwidth = 1;
       cp.add(old_label,gbc);
      gbc.gridx = 1;  gbc.gridy = 2; gbc.gridwidth = GridBagConstraints.REMAINDER;
      gbc.fill = GridBagConstraints.BOTH;
      cp.add(old_text,gbc);
      gbc.fill = GridBagConstraints.NONE;
      gbc.gridx = 0;  gbc.gridy = 3; gbc.gridwidth = 1;
      cp.add(new_label,gbc);
      gbc.gridx = 1;  gbc.gridy = 3; gbc.gridwidth = GridBagConstraints.REMAINDER;
      gbc.fill = GridBagConstraints.BOTH;
      cp.add(new_text,gbc);
      gbc.fill = GridBagConstraints.NONE;

      gbc.gridx = 0;  gbc.gridy = 4; gbc.gridwidth = 3;
      cp.add(change_all_button,gbc);
      gbc.anchor = GridBagConstraints.CENTER;
      gbc.gridx = 2; gbc.gridwidth = GridBagConstraints.REMAINDER;
      cp.add(number_changed_label,gbc);

   }  // end of constructor


   public void actionPerformed(ActionEvent e) {

      String the_command = e.getActionCommand();
      boolean  distinguish = distinguish_box.isSelected();

      String replacement_text = SimCon.convertBackslash(new_text.getText());

      number_changed_label.setText("");
      number_changed_label.repaint();

      if (the_command.equals("exit")) {
         setVisible(false);
         return;
      }
      else
      if (the_command.equals("next")) {   // Trovu sekvantan lokon.
         doc = text_pane.getDocument();
         find_next(get_text_to_find(), distinguish);
         parent.requestFocus();
      }  // if next (sekvanta)
      else
      if (the_command.equals("change one")) {    // Þanøu unu.
         if (text_pane.getSelectionStart() != text_pane.getSelectionEnd()) {
            text_pane.replaceSelection(replacement_text);
         }
         doc = text_pane.getDocument();
         find_next(get_text_to_find(), distinguish);  // al la sekvanta
         parent.requestFocus();
      }
      else
      if (the_command.equals("change all")) {    // Þanøu æiujn.
         String  find_this = get_text_to_find();
         String  to_insert = replacement_text;
         text_pane.setCaretPosition(0);  // Go to the start. Iru al la komenco.
         // Search and change. Seræu kaj þanøu.
         int count = 0;     // number of changes   nombro da þanøoj
         doc = text_pane.getDocument();
         while (find_next(find_this, distinguish)) {
            text_pane.replaceSelection(to_insert);
            count++;
         }
         // Display the number of changes.  Montru la nombron da þanøoj.
         number_changed_label.setText(number_changed_text + "  " + String.valueOf(count));
         number_changed_label.repaint();
         parent.requestFocus();
      }
      else
      if (the_command.equals("new font")) {
         new_font();
      }

   }  // actionPerformed


   public void new_font() {
         // Change the font.  Þanøu la tiparon.
         String font_name = editor.getSelectedFontName();
         Font font1 = Font.decode(font_name);
         Font font2 = font1.deriveFont(Font.PLAIN,16.0f);
         old_text.setFont(font2);
         old_text.repaint();
         new_text.setFont(font2);
         new_text.repaint();
         validate();
   }


   /*
    * find_next - Find the next string. This method searches down from the 
    * current position. If the text is found, it is selected.
    * find_next - Trovu la sekvantan literæenon. Æi tiu metodo seræas malsupren de
    * la nuna pozicio. Se øi trovas la tekston, øi selektas øin.
    */
   private boolean find_next(String to_find, boolean distinguish) {

      int number_to_compare = to_find.length();  // nombro por kompari

      int start_position = text_pane.getCaretPosition() + 1;   // komenca pozicio
 
      Segment the_text = new Segment();        // akiru la tekston
      int length_of_document = doc.getLength();
      if (number_to_compare >= length_of_document) return false;
      try {
         doc.getText(0,length_of_document,the_text);
      } catch (BadLocationException blx) {
         System.err.println("Failure in Change Dialog. Fusho en Shanghodialogo.\n" 
             + blx.toString());
         return false;
      }

      int end = length_of_document - number_to_compare + 1;

      if (distinguish) {  // if case matters / se majuskleco gravas

         int i,j;
         for (i = start_position; i < end; i++) {
            for (j = 0; j < number_to_compare; j++) {
               if (to_find.charAt(j) != the_text.array[i+j]) break; 
            }
            if (j == number_to_compare) {   // if found / se trovita
               try {
                  text_pane.setCaretPosition(i+number_to_compare);
                  text_pane.moveCaretPosition(i);
               } catch (NullPointerException npx) {
                  // I don't know why this happens, but I'll report it.
                  // Mi ne scias kial æi tio okazas, sed mi raportos øin.
                  System.err.println("Null pointer. Nula montrilo. 2\n" + npx.toString());
               }
               return true;
            }
         }  // for
         return false;
      }
      else {   // if case doesn't matter / se majuskleco ne gravas

         int i,j;
         for (i = start_position; i < end; i++) {
            for (j = 0; j < number_to_compare; j++) {
               if (to_find.charAt(j) != SimCon.toLower(the_text.array[i+j])) break; 
            }
            if (j == number_to_compare) {   // if found / se trovita
               try {
                  text_pane.setCaretPosition(i+number_to_compare);
                  text_pane.moveCaretPosition(i);
               } catch (NullPointerException npx) {
                  // I don't know why this happens, but I'll report it.
                  // Mi ne scias kial æi tio okazas, sed mi raportos øin.
                  System.err.println("Null pointer. Nula montrilo. 3\n" + npx.toString());
               }
               return true;
            }
         }  // for
         return false;
      }  // else

   }  // find_next


   /*
    * showIt - Display this dialog.
    * showIt - Montru æi tiun dialogon.
    */
   public void showIt(SimTextPane text_pane) {

      setVisible(true);

      // Position the dialog in the center of the main frame.
      // Metu la dialogon en la mezon de la æefa kadro.

      int new_width = old_label.getWidth() + old_text.getWidth() + 60;
      int new_height = old_text.getHeight() * 7 + 50;
      if (new_width < d_width) new_width = d_width;
      if (new_height < d_height) new_height = d_height;

      Rectangle p_rect = parent.getBounds();
      Rectangle d_rect = new Rectangle(p_rect.x + Math.abs(p_rect.width - new_width)/2,
                                       p_rect.y + Math.abs(p_rect.height - new_height)/2,
                                       new_width,new_height);
      setBounds(d_rect);

      this.text_pane = text_pane;

      parent.requestFocus();
      validate();

   }  // showIt

   /*
    * get_text_to_find - Gets the text to find from the text field and 
    * converts to lower case if necesary.
    * get_text_to_fine - Akiras seræotan tekston de la tekst-kampo kaj
    * konvertas øin al minuskloj, se necese.
    */
   private String get_text_to_find() {
      String  from_field = old_text.getText();

      // Convert \n and \t to new line and tab.
      // Konvertu \n kaj \t al novlinio kaj horizontala salto.
      String converted_string = SimCon.convertBackslash(from_field);

      String  temp1,temp2;
      if (distinguish_box.isSelected()) {
         return converted_string;
      }
      else {
         // Must convert to lower case. Devas minuskligi.
         return SimCon.toLower(converted_string);
      }

   }  // get_text_to_find


}  // ChangeDialog



