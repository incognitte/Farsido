/*
 * EspConvertDialog - This dialogue has text conversion routines which are useful to Esperantists
 * The user can convert between cx, ^c, Latin 3 and Unicode.
 * EspConvertDialog - Æi tiu dialogo enhavas tekst-konvertajn metodojn kiuj estas utilaj al
 * Esperantistoj. La uzanto povas konverti inter cx, ^c, Latino 3 kaj Unicode.
 *
 * Cleve Lendon (Klivo)
 * indriko@yahoo.com
 * http://purl.oclc.org/net/klivo/
 *
 */


/*
 * Changes for 3.2  Þanøoj por 3.2    1999/08
 *
 * Calculate size of dialogue from largest string.
 * Kalkulu grandecon de dialogujo de plej larøa litervico.
 *
 */

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;

public class EspConvertDialog extends JDialog implements ActionListener {

   JFrame           parent;

   private final static int d_width  = 290;   // dialog width   -  dialoga larøeco
   private final static int d_height = 290;   // dialog height  -  dialoga alteco

   SimTextPane  text_pane;

   JButton   exit_button;
   JButton   convert_button;

   JCheckBox  from_x;      // x method
   JCheckBox  from_h;      // Zamenhof method
   JCheckBox  from_cir;    // circumflex method
   JCheckBox  from_latin3;
   JCheckBox  from_unicode;

   JRadioButton  to_x;
   JRadioButton  to_h;
   JRadioButton  to_cir;
   JRadioButton  to_latin3;
   JRadioButton  to_unicode;
   ButtonGroup   radio_group;

   JLabel      from_label;
   JLabel      to_label;


   public EspConvertDialog (JFrame parent, String[] labels) {

      super(parent, "  " + labels[0]);
      //super(parent, "  " );
      this.parent = parent;
      setSize(d_width, d_height);
      Container cp = getContentPane();
      cp.setLayout(new GridBagLayout());

      convert_button = new JButton(labels[1]);
      convert_button.setActionCommand("convert");
      convert_button.addActionListener(this);

      exit_button = new JButton(labels[2]);
      exit_button.setActionCommand("exit");
      exit_button.addActionListener(this);

      from_x = new JCheckBox(labels[3]);
      from_h = new JCheckBox(labels[7]);
      from_cir = new JCheckBox(labels[4]);
      from_latin3 = new JCheckBox(labels[5]);
      from_unicode = new JCheckBox(labels[6]);

      to_x = new JRadioButton(labels[3]);
      to_x.setActionCommand("to_x");
      to_x.addActionListener(this);

      to_h = new JRadioButton(labels[7]);
      to_h.setActionCommand("to_h");
      to_h.addActionListener(this);

      to_cir = new JRadioButton(labels[4]);
      to_cir.setActionCommand("to_cir");
      to_cir.addActionListener(this);

      to_latin3 = new JRadioButton(labels[5]);
      to_latin3.setActionCommand("to_latin3");
      to_latin3.addActionListener(this);

      to_unicode = new JRadioButton(labels[6]);
      to_unicode.setActionCommand("to_unicode");
      to_unicode.addActionListener(this);

      radio_group = new ButtonGroup();
      radio_group.add(to_x);
      radio_group.add(to_h);
      radio_group.add(to_cir);
      radio_group.add(to_latin3);
      radio_group.add(to_unicode);

      from_label = new JLabel(labels[8]);
      to_label = new JLabel(labels[9]);

      from_x.setSelected(true);
      to_unicode.setSelected(true);
      // If the target is unicode, the source cannot be unicode.
      // Se la celo estas unikodo, la fonto ne rajtas esti unikodo.
      from_unicode.setSelected(false);
      from_unicode.setEnabled(false);

      // Arrange components. Aranøu butonojn.
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.anchor = GridBagConstraints.NORTH;
      gbc.insets = new Insets(5,10,5,10);
      gbc.gridx = 0;  gbc.gridy = 0;
      cp.add(convert_button,gbc);
      gbc.gridx = 1;
      cp.add(exit_button,gbc);
      gbc.gridx = 0;  gbc.gridy = 1;
      cp.add(from_label,gbc);
      gbc.gridx = 1;  gbc.gridy = 1;
      cp.add(to_label,gbc);
      gbc.anchor = GridBagConstraints.NORTHWEST;
      gbc.gridx = 0;  gbc.gridy = 2;
      cp.add(from_x,gbc);
      gbc.gridx = 1;  gbc.gridy = 2;
      cp.add(to_x,gbc);
      gbc.gridx = 0;  gbc.gridy = 3;
      cp.add(from_h,gbc);
      gbc.gridx = 1;  gbc.gridy = 3;
      cp.add(to_h,gbc);
      gbc.gridx = 0;  gbc.gridy = 4;
      cp.add(from_cir,gbc);
      gbc.gridx = 1;  gbc.gridy = 4;
      cp.add(to_cir,gbc);
      gbc.gridx = 0;  gbc.gridy = 5;
      cp.add(from_latin3,gbc);
      gbc.gridx = 1;  gbc.gridy = 5;
      cp.add(to_latin3,gbc);
      gbc.gridx = 0;  gbc.gridy = 6;
      cp.add(from_unicode,gbc);
      gbc.gridx = 1;  gbc.gridy = 6;
      cp.add(to_unicode,gbc);

   }  // end of constructor


   public void actionPerformed(ActionEvent e) {
      String command = e.getActionCommand();
      if (command.equals("exit")) {
         setVisible(false);
      }
      else
      if (command.equals("convert")) {
         convertAll();
      }
      else {

         // The command must be from one of the target buttons. Set all source
         // buttons to enabled, and then disable one of them.
         // La ordono nepre venis de celbutono. Aktivigu æiujn fontbutonojn, kaj
         // poste malaktivigu unu.

         from_x.setEnabled(true);
         from_h.setEnabled(true);
         from_cir.setEnabled(true);
         from_latin3.setEnabled(true);
         from_unicode.setEnabled(true);

         if (command.equals("to_x")) {
            from_x.setEnabled(false);
            from_x.setSelected(false);
         }
         else
         if (command.equals("to_h")) {
            from_h.setEnabled(false);
            from_h.setSelected(false);
         }
         else
         if (command.equals("to_cir")) {
            from_cir.setEnabled(false);
            from_cir.setSelected(false);
         }
         else
         if (command.equals("to_latin3")) {
            from_latin3.setEnabled(false);
            from_latin3.setSelected(false);
         }
         else
         if (command.equals("to_unicode")) {
            from_unicode.setEnabled(false);
            from_unicode.setSelected(false);
         }
      }
   }  // actionPerformed


   /*
    * showIt - Display this dialog.
    * showIt - Montru æi tiun dialogon.
    */
   public void showIt(SimTextPane text_pane) {

      setVisible(true);
      validate();

      // Position the dialog in the center of the main frame.
      // Metu la dialogon en la mezon de la æefa kadro.

      int new_width = (to_cir.getWidth() * 2) + 60;
      if (new_width < d_width) new_width = d_width;
      int new_height = new_width;

      Rectangle p_rect = parent.getBounds();
      Rectangle d_rect = new Rectangle(p_rect.x + Math.abs(p_rect.width - new_width)/2,
                                       p_rect.y + Math.abs(p_rect.height - new_height)/2 + 20,
                                       new_width,new_height);
      setBounds(d_rect);

      this.text_pane = text_pane;

      validate();

   }  // showIt



   /*
    * ConvertAll - Converts all accents from the source encoding to the target encoding.
    * Konvertu æion. - Konvertas æiujn supersignojn de la fontkodaro al la celkodaro.
    */
   private char character_to_convert;  // konvertota signo
   private int replacement_index;      // anstataýa indekso
   private Document doc;

   private void convertAll () {

      int count;
      String[] target_encoding;       // celkodaro
      String replacement_string;    // anstataýa literæeno

      doc = text_pane.getDocument();
      count = 0;     // number of changes   nombro da þanøoj

      // Get the target encoding. Akiru la celkodaron.
      if (to_x.isSelected()) {
         target_encoding = Xstrings;
      }
      else
      if (to_h.isSelected()) {
         target_encoding = Hstrings;
      }
      else
      if (to_cir.isSelected()) {
         target_encoding = CIRstrings;
      }
      else 
      if (to_latin3.isSelected()) {
         target_encoding = LATIN3strings;
      }
      else {
         target_encoding = UNIstrings;
      }

      if (from_x.isSelected()) {
         text_pane.setCaretPosition(0);  // Go to the start. Iru al la komenco.
         while (findAndSelectCX()) {
            replacement_index = getIndex(SENSUPERSIGNO,character_to_convert);
            if (replacement_index >= 0) {
               replacement_string = target_encoding[replacement_index];
               if (replacement_string != null) {
                  text_pane.replaceSelection(replacement_string);
               }
            }
            count++;
         }  // while
      } // X

      if (from_h.isSelected()) {
         text_pane.setCaretPosition(0);  // Go to the start. Iru al la komenco.
         while (findAndSelectCH()) {
            replacement_index = getIndex(SENSUPERSIGNO,character_to_convert);
            if (replacement_index >= 0) {
               replacement_string = target_encoding[replacement_index];
               if (replacement_string != null) {
                  text_pane.replaceSelection(replacement_string);
               }
            }
            count++;
         }  // while
      } // H

      if (from_cir.isSelected()) {
         text_pane.setCaretPosition(0);  // Go to the start. Iru al la komenco.
         while (findAndSelectCir()) {
            replacement_index = getIndex(SENSUPERSIGNO,character_to_convert);
            if (replacement_index >= 0) {
               replacement_string = target_encoding[replacement_index];
               if (replacement_string != null) {
                  text_pane.replaceSelection(replacement_string);
               }
            }
            count++;
         }  // while
      } // circumflex  / cirkumflekso

      if (from_latin3.isSelected()) {
         text_pane.setCaretPosition(0);  // Go to the start. Iru al la komenco.
         while (findAndSelect(LATIN3)) {
            // findAndSelect writes to replacement_index. findAndSelect skribas al replacement_index.
            if (replacement_index >= 0) {
               replacement_string = target_encoding[replacement_index];
               if (replacement_string != null) {
                  text_pane.replaceSelection(replacement_string);
               }
            }
            count++;
         }  // while
      } // Latin 3


      if (from_unicode.isSelected()) {
         text_pane.setCaretPosition(0);  // Go to the start. Iru al la komenco.
         while (findAndSelect(UNIKODO)) {
            // findAndSelect writes to replacement_index. findAndSelect skribas al replacement_index.
            if (replacement_index >= 0) {
               replacement_string = target_encoding[replacement_index];
               if (replacement_string != null) {
                  text_pane.replaceSelection(replacement_string);
               }
            }
            count++;
         }  // while
      } // Latin 3



   }  // convertAll


   /* 
    * findAndSelectCX - Find the next occurrence of cx, gx, hx etc, and select it. 
    * findAndSelectCX - Trovu la pozicion de la sekvanta cx, gx, hx ktp, kaj selektu øin.
    */
   private boolean findAndSelectCX() {

      char  char1, char2;

      int start_position = text_pane.getCaretPosition() + 1;   // komenca pozicio
      int length_of_document = doc.getLength();

      Segment the_text = new Segment();        // akiru la tekston
      if (start_position > length_of_document - 1) return false;

      try {  // Get the text. Akiru la tekston.
         doc.getText(0,length_of_document,the_text);
      } catch (BadLocationException blx) {
         System.err.println("EspConvertDialog - failure / fusho  1.\n" 
             + blx.toString());
         return false;
      }

      for (int i = start_position; i < length_of_document; i++) {
         char1 = the_text.array[i];
         if (char1 == 'x' || char1 == 'X') {
            char2 = the_text.array[i-1];
            if (canHaveAccent(char2)) {
               try {
                  text_pane.setCaretPosition(i - 1);
                  text_pane.moveCaretPosition(i + 1);
               } catch (NullPointerException npx) {
                  System.err.println("EspConvertDialog: Null pointer. Nula montrilo. 1\n" 
                                      + npx.toString());
               }
               character_to_convert = char2;
               return true;
            }  // if          
         } // if
      }  // for

      return false;

   }  // findAndSelectCX



   /* 
    * findAndSelectCH - Find the next occurrence of ch, gh, hh etc, and select it. 
    * findAndSelectCH - Trovu la pozicion de la sekvanta ch, gh, hh ktp, kaj selektu øin.
    */
   private boolean findAndSelectCH() {

      char  char1, char2;

      int start_position = text_pane.getCaretPosition() + 1;   // komenca pozicio
      int length_of_document = doc.getLength();

      Segment the_text = new Segment();        // akiru la tekston
      if (start_position > length_of_document - 1) return false;

      try {  // Get the text. Akiru la tekston.
         doc.getText(0,length_of_document,the_text);
      } catch (BadLocationException blx) {
         System.err.println("EspConvertDialog - failure / fusho  2.\n" 
             + blx.toString());
         return false;
      }

      for (int i = start_position; i < length_of_document; i++) {
         char1 = the_text.array[i];
         if (char1 == 'h' || char1 == 'H') {   // Don't convert uh. Ne konvertu uh.
            char2 = the_text.array[i-1];
            if (canHaveAccent(char2) && char2 != 'u' && char2 != 'U') {
               try {
                  text_pane.setCaretPosition(i - 1);
                  text_pane.moveCaretPosition(i + 1);
               } catch (NullPointerException npx) {
                  System.err.println("EspConvertDialog: Null pointer. Nula montrilo. 2a\n" 
                                      + npx.toString());
               }
               character_to_convert = char2;
               return true;
            }  // if
         } // if
         else
         if (char1 == 'u' || char1 == 'U') {   // Now deal with au and eu. Nun traktu au kaj eu.
            char2 = the_text.array[i-1];
            if (char2 == 'a' || char2 == 'A' || char2 == 'e' || char2 == 'E') {
               try {
                  text_pane.setCaretPosition(i);
                  text_pane.moveCaretPosition(i + 1);
               } catch (NullPointerException npx) {
                  System.err.println("EspConvertDialog: Null pointer. Nula montrilo. 2b\n" 
                                      + npx.toString());
               }
               character_to_convert = char1;
               return true;
            }  // if
         }
      }  // for

      return false;

   }  // findAndSelectCH



   /* 
    * findAndSelectCir - Find the next occurrence of ^c, ^g, ^h etc, and select it. 
    * findAndSelectCir - Trovu la pozicion de la sekvanta ^c, ^g, ^h ktp, kaj selektu øin.
    */
   private boolean findAndSelectCir() {

      char  char1, char2;

      int start_position = text_pane.getCaretPosition();   // komenca pozicio
      int length_of_document = doc.getLength();

      Segment the_text = new Segment();        // akiru la tekston
      if (start_position > length_of_document - 2) return false;

      try {  // Get the text. Akiru la tekston.
         doc.getText(0,length_of_document,the_text);
      } catch (BadLocationException blx) {
         System.err.println("EspConvertDialog - failure / fusho  3.\n" 
             + blx.toString());
         return false;
      }

      for (int i = start_position; i < length_of_document - 1; i++) {
         char1 = the_text.array[i];
         if (char1 == '^') {
            char2 = the_text.array[i+1];
            if (canHaveAccent(char2)) {
               try {
                  text_pane.setCaretPosition(i);
                  text_pane.moveCaretPosition(i + 2);
               } catch (NullPointerException npx) {
                  System.err.println("EspConvertDialog: Null pointer. Nula montrilo. 3\n" + npx.toString());
               }
               character_to_convert = char2;
               return true;
            }  // if          
         } // if
      }  // for

      return false;

   }  // findAndSelectCir


   /* 
    * findAndSelect - Find the next occurrence of a code, and select it.
    * The parameter is a list of codes to find.
    * findAndSelect - Trovu la sekvantan pozicion de kodo kaj selektu øin.
    * La parametro estas listo de trovindaj kodoj.
    */
   private boolean findAndSelect(char[] codes_to_find) {

      char  char1;
      int   code_index; 

      int start_position = text_pane.getCaretPosition();   // komenca pozicio
      int length_of_document = doc.getLength();

      Segment the_text = new Segment();        // akiru la tekston
      if (start_position == length_of_document) return false;

      try {  // Get the text. Akiru la tekston.
         doc.getText(0,length_of_document,the_text);
      } catch (BadLocationException blx) {
         System.err.println("EspConvertDialog - failure / fusho  4.\n" 
             + blx.toString());
         return false;
      }

      for (int i = start_position; i < length_of_document; i++) {
         char1 = the_text.array[i];
         code_index = getIndex(codes_to_find, char1);
         if (code_index >= 0) {    // If found...    Se trovita...
            try {
               text_pane.setCaretPosition(i);
               text_pane.moveCaretPosition(i + 1);
            } catch (NullPointerException npx) {
               System.err.println("EspConvertDialog: Null pointer. Nula montrilo. 4\n" 
                                   + npx.toString());
            }
            replacement_index = code_index;
            return true;
         } // if
      }  // for

      return false;

   }  // findAndSelect





   /* 
    * getIndex - Find the index of the given character in the character array. 
    * Returns -1 if not found.
    * getIndex - trovuIndekson - Trovu la indekson de signo en signomatrico.
    * Redonas -1 se netrovita.
    */
   private int getIndex (char[] the_array, char the_char) {
      char from_array;
      for (int i = 0; i < the_array.length; i++) {
         from_array = the_array[i];
         if (from_array == the_char) return i;
         if (from_array == 0) return -1;
      }
      return -1;
   } // getIndex



   /* 
    * canHaveAccent - This method indicates whether the letter can take an accent in Esperanto.
    * supersignebla - Æi tiu metodo indikas æu la litero povas akcepti supersignon.
    */
   public static boolean canHaveAccent (char litero) {

         if (litero == 'c') return true;
         if (litero == 'g') return true;
         if (litero == 's') return true;
         if (litero == 'u') return true;
         if (litero == 'h') return true;
         if (litero == 'j') return true;
         if (litero == 'C') return true;
         if (litero == 'G') return true;
         if (litero == 'S') return true;
         if (litero == 'U') return true;
         if (litero == 'H') return true;
         if (litero == 'J') return true;
         return false;

   }   // canHaveAccent


   ///////////////////////////////////////////////////////////////////////////////////
   // Strings for converting between the x-method, h-method, the circumflex method, 
   // Latin 3 and Unicode.
   // Literæenoj por konverti inter la x-metodo, h-metodo, cirkumfleks-metodo,
   // Latino 3 kaj Unikodo.

   public final static String[]  Xstrings
   = {"cx","gx","sx","ux","jx","hx","Cx","Gx","Sx","Ux","Jx","Hx",
     null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null
   };

   public final static String[]  Hstrings
   = {"ch","gh","sh","u","jh","hh","Ch","Gh","Sh","U","Jh","Hh",
     null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null
   };

   public final static String[]  CIRstrings
   = {"^c","^g","^s","^u","^j","^h","^C","^G","^S","^U","^J","^H",
     null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null
   };

   public final static String[]  LATIN3strings 
   = {"\u00e6","\u00f8","\u00fe","\u00fd","\u00bc","\u00b6",  
      "\u00c6","\u00d8","\u00de","\u00dd","\u00ac","\u00a6",
      "\u00a1","\u00b1","\u00a2","\u00a9","\u00b9","\u00aa",
      "\u00ba","\u00ab","\u00bb","\u00af","\u00bf","\u00c5",
      "\u00e5","\u00d5","\u00f5","\u00ff"};


   public final static String[]   UNIstrings
   = {"\u0109","\u011d","\u015d","\u016d","\u0135","\u0125", 
      "\u0108","\u011c","\u015c","\u016c","\u0134","\u0124",
      "\u0126","\u0127","\u02d8","\u0130","\u0131","\u015e",
      "\u015f","\u011e","\u011f","\u017b","\u017c","\u010a",
      "\u010b","\u0120","\u0121","\u02d9"};


   public final static char[]   SENSUPERSIGNO
   = {'c','g','s','u','j','h','C','G','S','U','J','H',
       0,0,0,
       0,0,
       0,0,
       0,0,
       0,0,
       0,0,
       0,0,
       0,};


   public final static char[]   LATIN3 
   = {0xe6,0xf8,0xfe,0xfd,0xbc,0xb6,  0xc6,0xd8,0xde,0xdd,0xac,0xa6,
      0xa1,0xb1,0xa2,
      0xa9,0xb9,
      0xaa,0xba,
      0xab,0xbb,
      0xaf,0xbf,
      0xc5,0xe5,
      0xd5,0xf5,
      0xff,};

   public final static char[]   UNIKODO
   = {0x109,0x11d,0x15d,0x16d,0x135,0x125, 0x108,0x11c,0x15c,0x16c,0x134,0x124,
      0x126,0x127,0x2d8,
      0x130,0x131,
      0x15e,0x15f,
      0x11e,0x11f,
      0x17b,0x17c,
      0x10a,0x10b,
      0x120,0x121,
      0x2d9,};


}  // EspConvertDialog



