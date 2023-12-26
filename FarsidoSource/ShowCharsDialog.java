/*
 * ShowCharsDialog
 * This class displays all characters in the current font.
 * Æi tiu dialogo montras æiujn signojn en la aktiva tiparo.
 *
 * Cleve Lendon (Klivo)   2000/12/14
 * indriko@yahoo.com
 * http://purl.oclc.org/net/klivo/
 *
 */


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;


public class ShowCharsDialog extends JDialog implements ActionListener {

   JFrame       parent;
   Farsido     editor;

   Rectangle p_rect;    // Bounds of parent

   private final static int d_width  = 360;   // dialog width   -  dialoga larøeco
   private final static int d_height = 400;   // dialog height  -  dialoga alteco

   JLabel   top_label;
   JLabel   current_font_label;    // Por montri aktivan tiparon.

   static int      current_page = 0;      // 01 al FF

   JButton  up_button;          // supren
   JButton  fast_up_button;     // rapide supren
   JButton  down_button;        // malsupren
   JButton  fast_down_button;   // rapide malsupren
   JButton  exit_button;

   private final static int  characters_per_line = ((16*2) + 1);
   private final static int  lines_in_window = 16;
   private final static int  character_buffer_size = characters_per_line * lines_in_window;
   private char[]  the_chars = new char[character_buffer_size];
   private char[]  the_rows  = new char[lines_in_window * 5];  // La vicoj ekz: E210, E220,

   SimTextArea     character_window; // Fenestro por montri signojn.
   SimTextArea     row_window; // Show hex codes for each row.
                              // Montru deksesumajn kodojn por æiu vico.

   public ShowCharsDialog (JFrame parent, Farsido editor, String[] labels) {

      super(parent, "  ");
      this.parent = parent;
      this.editor = editor;

      p_rect = parent.getBounds();

      setSize(d_width, d_height);

      up_button   = new JButton(">");
      up_button.setActionCommand("up");
      up_button.addActionListener(this);

      fast_up_button   = new JButton(">>");
      fast_up_button.setActionCommand("fast up");
      fast_up_button.addActionListener(this);

      down_button   = new JButton("<");
      down_button.setActionCommand("down");
      down_button.addActionListener(this);

      fast_down_button   = new JButton("<<");
      fast_down_button.setActionCommand("fast down");
      fast_down_button.addActionListener(this);

      character_window  = new SimTextArea(); // Fenestro por montri signojn.
      character_window.setEditable(false);

      row_window        = new SimTextArea();
      row_window.setEditable(false);

      top_label = new JLabel(labels[0]);
      current_font_label = new JLabel("");

      exit_button = new JButton(labels[1]);
      exit_button.setActionCommand("exit");
      exit_button.addActionListener(this);

      Container cp = getContentPane();
      cp.setLayout(new GridBagLayout());

      // Arrange components. Aranøu butonojn.
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(5,5,5,5);

      gbc.fill = GridBagConstraints.NONE;
      gbc.anchor = GridBagConstraints.NORTHWEST;
      gbc.gridx = 0;  gbc.gridy = 0; gbc.gridwidth = 2;
      cp.add(top_label,gbc);
      gbc.gridx = 2;
      cp.add(current_font_label,gbc);


      gbc.gridy = 1; gbc.gridwidth = 1;
      gbc.gridx = 0; 
      cp.add(fast_down_button,gbc);
      gbc.gridx = 1;
      cp.add(down_button,gbc);
      gbc.gridx = 2;
      cp.add(up_button,gbc);
      gbc.gridx = 3;
      cp.add(fast_up_button,gbc);

      gbc.gridx = 4; gbc.gridwidth = 1;
      cp.add(exit_button,gbc);

      gbc.gridy = 2; gbc.gridx = 0; gbc.gridwidth = 1; 
      cp.add(row_window,gbc);

      gbc.gridy = 2; gbc.gridx = 1; gbc.gridwidth = 4; 
      cp.add(character_window,gbc);

      sizeIt();

   }  // end of constructor

   public void actionPerformed(ActionEvent e) {

      String the_command = e.getActionCommand();

      if (the_command.equals("exit")) {
         setVisible(false);
         return;
      }
      else
      if (the_command.equals("up")) {
         next_page();
      }
      else
      if (the_command.equals("fast up")) {
         next_16();
      } 
      else
      if (the_command.equals("down")) {
         prev_page();
      }
      else
      if (the_command.equals("fast down")) {
         prev_16();
      }
      else
      if (the_command.equals("new font")) {
         new_font();
         resizeIt();
      }

   }  // actionPerformed


   public void new_font() {
         // Display the font name. Montru la nomon de la aktiva tiparo.
         String font_name = editor.getSelectedFontName();
         Font font1 = Font.decode(font_name);
         Font font2 = font1.deriveFont(Font.PLAIN,14.0f);
         character_window.setFont(font2);
         row_window.setFont(font2);
         current_font_label.setText(font_name);
         fill_character_window(current_page);
         //current_font_label.repaint();
         repaint();
   }


   /*
    * showIt - Display this dialog.
    * showIt - Montru æi tiun dialogon.
    */
   public void showIt() {

      setVisible(true);
      new_font();
      p_rect = parent.getBounds();
      sizeIt();
      validate();

   }  // showIt



   /*
    * sizeIt - Adjust size.
    * sizeIt - Øustigu grandecon.
    */
   public void sizeIt() {

      p_rect = parent.getBounds();
      int width  = width_of_char_window() + width_of_row_window() + 40;
      int height = height_of_char_window() + (up_button.getHeight() * 4) + 20; 
      Rectangle d_rect = new Rectangle(p_rect.x + 400, p_rect.y + 90, width, height);
      setBounds(d_rect);

   }


   /*
    * resizeIt - Readjust size.
    * resizeIt - Reøustigu grandecon.
    */
   public void resizeIt() {

      Rectangle old_rect     = getBounds();
      int new_width  = width_of_char_window() + width_of_row_window() + 40;
      int new_height = height_of_char_window() + (up_button.getHeight() * 4) + 20; 

      //System.err.println("x " + old_rect.x + " y " + old_rect.y +
      //                   " w " + old_rect.width + " h " + old_rect.height + "\n");
      //System.err.println("width " + new_width + " height " + new_height + "\n");

      if (new_width < d_width) new_width = d_width;
      if (new_height < d_height) new_height = d_height;

      Rectangle d_rect = new Rectangle(old_rect.x,old_rect.y,new_width,new_height);
      setBounds(d_rect);
      validate();
   }

   // Larøeco de la signofenestro
   private int width_of_char_window() {
      //char[] a_line = {'@',' ','A',' ','B',' ','C',' ','D',' ','E',' ',
      //                  'F',' ','G',' ','H',' ','I',' ','J',' ','K',' ',
      //                  'L',' ','M',' ','N',' ','O','2','2','2','2','2','2','2','2','2'};
      //return character_window.widthOfChars(a_line) + 20;
      int widest = 30;
      int width  = 0;
      for (int i = 0; i < lines_in_window; i++) {
         width = character_window.widthOfChars(the_chars,
                 (i*characters_per_line),characters_per_line);
         if (width > widest) widest = width;
      } 
      return widest;
   }

   // Larøeco de la vicofenestro
   private int width_of_row_window() {
      char[] a_row = {'0','0','0','0'};
      return row_window.widthOfChars(a_row) + 20;
   }



   // Alteco de la signofenestro
   private int height_of_char_window() {
      return character_window.heightOfChar() * 16;
   }


   /*
    * fill_character_window - Writes 256 characters to the character window. 
    * fill_character_window - Skribas 256 signojn al la signofenestro.
    */
   protected void fill_character_window (int page) {

      int j;
      int za_page = page * 256;

      j = 0;
      for (int row=0; row < 16; row++) {

         the_rows[(row*5) + 0] = hexdigit(page/16);
         the_rows[(row*5) + 1] = hexdigit(page & 0x0F);
         the_rows[(row*5) + 2] = hexdigit(row);
         the_rows[(row*5) + 3] = '0';
         the_rows[(row*5) + 4] = '\n';

         for (int col= 0; col < 16; col++) {
            // I don't know why I can't display the first three rows of page 
            // 32, but it causes nasty errors.
            // Mi ne scias kial mi ni povas montri la unuajn tri vicojn de
            // paøo 32, sed tio kaýzas erarojn.
            if ((row < 2 && page == 0) || (row < 3 && page == 32)) {
               the_chars[j] = ' '; j++;
               the_chars[j] = ' '; j++;
            }
            else {
               the_chars[j] = (char)(za_page + (row * 16) + col); j++;
               the_chars[j] = ' '; j++;
            }
         }
         the_chars[j] = '\n'; j++;
      }

      // Don't need the last carriage returns.
      // Ne bezonas la lastajn novliniajn kodojn.
      the_chars[the_chars.length-1] = ' ';
      the_rows[the_rows.length-1] = ' ';

      character_window.setText(new String(the_chars));
      row_window.setText(new String(the_rows));
      character_window.repaint();

   }  // fill_character_window


   private void next_page() {
      if (current_page < 0xFF) current_page++;
      fill_character_window(current_page);
      validate();
   }

   private void next_16() {
      if (current_page + 16 < 0xFF) current_page = current_page + 16;
      else current_page = 0xFF;
      fill_character_window(current_page);
      validate();
   }

   private void prev_page() {
      if (current_page > 0) current_page--;
      fill_character_window(current_page);
      validate();
   }

   private void prev_16() {
      if (current_page - 16 > 0) current_page = current_page - 16;
      else current_page = 0;
      fill_character_window(current_page);
      validate();
   }

   // Faru duciferan deksesuman liter-æenon.
   private String make_hexadecimal(int the_number) {
      if (the_number < 16) {
         return "0" + Integer.toHexString(the_number).toUpperCase();
      }
      return Integer.toHexString(the_number).toUpperCase();
   }

   // Faru deksesuman ciferon
   private char hexdigit(int the_number) {
      if (the_number < 10) return (char)(the_number + (int)'0');
      else return (char)(the_number - 10 + (int)'A');
   }

}  // ShowCharsDialog



