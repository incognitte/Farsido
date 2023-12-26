/*
 * SearchDialog
 * This class implements a search dialogue. It must listen for "new font" 
 * events from the font_list.
 * Æi tiu klaso kreas seræ-dialogon. Øi devas aýskulti "new font" (nova tiparo) 
 * eventojn de la tipara listo.
 *
 * Cleve Lendon (Klivo)
 * indriko@yahoo.com
 * http://purl.oclc.org/net/klivo/
 *
 * Versio 3.1  1999/06/24
 * - showIt - Don't position in center. Move to top right
 *            Ne montru en mezo. Montre supre maldekstre.
 * - Don't display up, down and close buttons. Up and down are
 *   now on the tool bar. Make search dialogue smaller.
 *   Ne montru "supren", "malsupren" kaj "fermu" butonojn. "Supren"
 *   kaj "malsupren" estas sur la il-trabo. Malpligrandigu la 
 *   seræodialogon.
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


public class SearchDialog extends JDialog implements ActionListener {

   JFrame       parent;
   Farsido     editor;

   private final static int d_width  = 360;   // dialog width   -  dialoga larøeco
   private final static int d_height = 105;   // dialog height  -  dialoga alteco

   JButton  up_button;
   JButton  down_button;
   JButton  exit_button;

   JCheckBox   distinguish_box;   // Distinguish between upper and lower case.
                                  // Distingu inter majuskloj kaj minuskloj.

   SimTextField   search_text;    // seræota teksto

   SimTextPane  text_pane;
   Segment      the_text = new Segment();
   Document     doc;

   public SearchDialog (JFrame parent, Farsido editor, String[] labels) {

      //super(parent, "  " + labels[0]);  // Ne povas montri Unikodon en kadro.
      super(parent, "  ");  
      this.parent = parent;
      this.editor = editor;
      this.text_pane = editor.text_pane;

      setSize(d_width, d_height);

      up_button   = new JButton(labels[2]);
      up_button.setActionCommand("up");
      up_button.addActionListener(this);

      down_button = new JButton(labels[3]);
      down_button.setActionCommand("down");
      down_button.addActionListener(this);

      exit_button = new JButton(labels[4]);
      exit_button.setActionCommand("exit");
      exit_button.addActionListener(this);

      distinguish_box = new JCheckBox(labels[1]);
      distinguish_box.setActionCommand("distinguish");
      distinguish_box.addActionListener(this);

      search_text     = new SimTextField("",30);

      Container cp = getContentPane();
      cp.setLayout(new GridBagLayout());

      // Arrange components. Aranøu butonojn.
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.anchor = GridBagConstraints.NORTH;
      gbc.insets = new Insets(5,5,5,5);

      gbc.gridwidth = 3;
      gbc.gridx = 0; gbc.gridy = 0;
      gbc.gridwidth = GridBagConstraints.REMAINDER;
      gbc.fill = GridBagConstraints.BOTH;
      cp.add(search_text,gbc);

      //gbc.fill = GridBagConstraints.NONE;
      //gbc.gridx = 0;  gbc.gridy = 1;
      //gbc.gridwidth = 1;
      //cp.add(up_button,gbc);

      //gbc.gridx = 1;  gbc.gridy = 1;
      //cp.add(down_button,gbc);

      //gbc.gridx = 2;  gbc.gridy = 1;
      //gbc.anchor = GridBagConstraints.EAST;
      //cp.add(exit_button,gbc);

      gbc.gridx = 0;  gbc.gridy = 1;
      gbc.gridwidth = 3;
      gbc.anchor = GridBagConstraints.NORTH;
      cp.add(distinguish_box,gbc);


      //////////////////////////////////////////////////
      // Set key bindings.  Pretigu klav-ligojn.
      // ctrl-n >> Search down / Seræu malsupren
      // ctrl-b >> Search up   / Seræu supren

      Action search_down   = new SearchDown("search-down");
      Action search_up     = new SearchUp("search-up");

      JTextComponent.KeyBinding[] bindings = {

         new JTextComponent.KeyBinding(KeyStroke.getKeyStroke(
                        KeyEvent.VK_N, 
                        InputEvent.CTRL_MASK),
                        "search-down"),
         new JTextComponent.KeyBinding(KeyStroke.getKeyStroke(
                        KeyEvent.VK_B, 
                        InputEvent.CTRL_MASK),
                        "search-up"),
      };
      Keymap parentmap = search_text.getKeymap();
      Keymap searchkeymap = JTextComponent.addKeymap("searchkeymap", parentmap);
      Action[] short_action_list = {  search_down, search_up };
      JTextComponent.loadKeymap(searchkeymap, bindings, short_action_list);
      search_text.setKeymap(searchkeymap);

   }  // end of constructor


   public void actionPerformed(ActionEvent e) {

      String the_command = e.getActionCommand();

      if (the_command.equals("exit")) {
         setVisible(false);
         return;
      }
      else
      if (the_command.equals("up")) {
         search_up();
      } 

      else
      if (the_command.equals("down")) {
         search_down();
      }  

      else
      if (the_command.equals("new font")) {
          new_font();
      }

   }  // actionPerformed


   /*
    * showIt - Display this dialog.
    * showIt - Montru æi tiun dialogon.
    */
   public void showIt(SimTextPane text_pane) {

      setVisible(false);

      // Position the dialog in at bottom right.
      // Metu la dialogon æe malsupra dektro.

      int new_width = search_text.getWidth() + 30;
      int new_height = search_text.getHeight() * 2 + 40;
      if (new_width < d_width) new_width = d_width;
      if (new_height < d_height) new_height = d_height;

      Rectangle p_rect = parent.getBounds();
      Rectangle d_rect = new Rectangle(p_rect.x + 450, p_rect.y,new_width,new_height);
      setBounds(d_rect);

      this.text_pane = text_pane;
      setVisible(true);
      search_text.requestFocus();
      validate();

   }  // showIt

   /*
    * get_text_to_find - Gets the text to find from the text field and 
    * converts to lower case if necesary.
    * get_text_to_fine - Akiras seræotan tekston de la tekst-kampo kaj
    * konvertas øin al minuskloj, se necese.
    */
   private String get_text_to_find() {

      String  from_field = search_text.getText();

      if (from_field == null) return "";

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




   /*
    * search_down  
    * seræu malsupren
    */
    public void search_down () {

         String to_find = get_text_to_find();       // seræota teksto
         if (to_find == null) return;
         int number_to_compare = to_find.length();  // nombro por kompari
         int start_position = text_pane.getCaretPosition() + 1;   // komenca pozicio
         Segment the_text = new Segment();        // akiru la tekston
         doc = text_pane.getDocument();
         int length_of_document = doc.getLength();
         if (number_to_compare >= length_of_document) return;
         try {
            doc.getText(0,length_of_document,the_text);
         } catch (BadLocationException blx) {
            System.err.println("Failure in Search Dialog. Fusho en Serchodialogo.\n" 
                + blx.toString());
         }

         int end = length_of_document - number_to_compare + 1;

         if (distinguish_box.isSelected()) {  // if case matters / se majuskleco gravas

            int i,j;
            for (i = start_position; i < end; i++) {
               for (j = 0; j < number_to_compare; j++) {
                  if (to_find.charAt(j) != the_text.array[i+j]) break; 
               }
               if (j == number_to_compare) {   // if found / se trovita
                  text_pane.setCaretPosition(i+number_to_compare);
                  text_pane.moveCaretPosition(i);
                  editor.text_pane.requestFocus();
                  return;
               }
            }  // for
            editor.text_pane.requestFocus();

         }
         else {   // if case doesn't matter / se majuskleco ne gravas

            int i,j;
            for (i = start_position; i < end; i++) {
               for (j = 0; j < number_to_compare; j++) {
                  if (to_find.charAt(j) != SimCon.toLower(the_text.array[i+j])) break; 
               }
               if (j == number_to_compare) {   // if found / se trovita
                  text_pane.setCaretPosition(i+number_to_compare);
                  text_pane.moveCaretPosition(i);
                  editor.text_pane.requestFocus();
                  return;
               }
            }  // for
            editor.text_pane.requestFocus();

         }  // else

    }  // end of search_down


   /*
    * search_up  
    * seræu supren
    */
    public void search_up () {

         String to_find = get_text_to_find();       // seræota teksto
         if (to_find == null) return;

         int number_to_compare = to_find.length();  // nombro por kompari
         int start_position = text_pane.getCaretPosition() - 1;   // komenca pozicio

         Segment the_text = new Segment();        // akiru la tekston
         doc = text_pane.getDocument();
         int length_of_document = doc.getLength();
         if (number_to_compare >= length_of_document) return;
         try {
            doc.getText(0,length_of_document,the_text);
         } catch (BadLocationException blx) {
            System.err.println("Failure in Search Dialog. Fusho en Serchodialogo.\n" 
                      + blx.toString());
         }

         if (distinguish_box.isSelected()) {  // if case matters / se majuskleco gravas

            int i,j;

            for (i = start_position; i >= 0; i--) {
               for (j = 0; j < number_to_compare; j++) {
                  if (to_find.charAt(j) != the_text.array[i+j]) break; 
               }
               if (j == number_to_compare) {   // if found / se trovita
                  text_pane.setCaretPosition(i+number_to_compare);
                  text_pane.moveCaretPosition(i);
                  editor.text_pane.requestFocus();
                  return;
               }
            }  // for
            editor.text_pane.requestFocus();

         }
         else {   // if case doesn't matter / se majuskleco ne gravas

            int i,j;

            for (i = start_position; i >= 0; i--) {
               for (j = 0; j < number_to_compare; j++) {
                  if (to_find.charAt(j) !=  SimCon.toLower(the_text.array[i+j])) break; 
               }
               if (j == number_to_compare) {   // if found / se trovita
                  text_pane.setCaretPosition(i+number_to_compare);
                  text_pane.moveCaretPosition(i);
                  editor.text_pane.requestFocus();
                  return;
               }
            }  // for
            editor.text_pane.requestFocus();

         }  // else
    }

   public void new_font() {
         // Change the font.  Þanøu la tiparon.
         String font_name = editor.getSelectedFontName();
         Font font1 = Font.decode(font_name);
         Font font2 = font1.deriveFont(Font.PLAIN,16.0f);
         search_text.setFont(font2);
         search_text.repaint();
         validate();
   }

   /**
    * SearchUp - Action class to search upward.
    * SearchUp - Agoklaso por seræi supren.
    */
   public class SearchUp extends AbstractAction {

      SearchUp(String default_label) {
         super(default_label);
      }

      public void actionPerformed(ActionEvent e) {
         search_up();
         editor.text_pane.requestFocus();
      }  // actionPerformed

   }  // SearchUp



   /**
    * SearchDown - Action class to search downward.
    * SearchDown - Agoklaso por seræi malsupren.
    */
   public class SearchDown extends AbstractAction {

      SearchDown(String default_label) {
         super(default_label);
      }

      public void actionPerformed(ActionEvent e) {
         search_down();
         editor.text_pane.requestFocus();
      }  // actionPerformed

   }  // SearchDown


}  // SearchDialog



