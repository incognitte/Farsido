/*
 * This class is similar to SimTextPane. I am subclassing JTextField so 
 * that I can overwrite processKeyEvent, and implement a dead key for
 * typing accents. Unlike SimTextPane, this class has no print functions.
 * Cleve 1999/01/12
 *
 * Æi tiu klaso similas al SimTextPane. Mi subklasigas JTextField por 
 * superskribi la metodon processKeyEvent, kaj tiel krei senpaþan klavon
 * por tajpado de supersignoj. Malsimile al SimTextPane, æi tiu klaso ne
 * havas metodojn por presi.
 * Klivo 1999/01/12
 *
 * For 3.3 - changes to deal with key maps.  2000/12/23
 * Por 3.3 - þanøoj por trakti klavmapojn.   2000/12/23
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;

public class SimTextField extends JTextField {

   private static char    typed_key = 0;          // tajpita klavo (signo)
   private static char    previously_typed = 0;   // antaýe tajpita signo

   public SimTextField () {
      super();
      fontSize16();
      unbind();
   }

   public SimTextField (int width) {
      super(width);
      fontSize16();
      unbind();
   }

   public SimTextField (String first_text) {
      super(first_text);
      fontSize16();
      unbind();
   }

   public SimTextField (String first_text, int width) {
      super(first_text, width);
      fontSize16();
      unbind();
   }



   /*
    * fontSize16 - Set the font size to 16.
    * fontSize16 - Þanøu la tiparan grandecon al 16.
    */
    private void fontSize16 () {
       Font font1 = getFont();
       setFont(font1.deriveFont(Font.PLAIN,16.0f));
    }


   /*
    * unbind - Unbind the enter and tab keys in the key map. I want to put
    * the codes into the text field. This doesn't seem to work.
    * malligu - Malligu la novlinian kaj saltan klavojn en la klavara mapo.
    * Mi volas meti la kodojn en la tekst-kampon. ?ajne ne funkcias.
    */
   private void unbind() {
      KeyStroke enter_key = KeyStroke.getKeyStroke((char)KeyEvent.VK_ENTER); 
      KeyStroke tab_key   = KeyStroke.getKeyStroke((char)KeyEvent.VK_TAB);
      Keymap map = getKeymap();
      map.removeKeyStrokeBinding(enter_key); 
      map.removeKeyStrokeBinding(tab_key); 
      setKeymap(map);
   }


   /* 
    * processKeyEvent - Capture key events and check whether 
    * key map conversion is necessary.
    * processKeyEvent - Kaptu klaveventojn kaj kontrolu æu 
    * klavmapo estas aktiva.
    */
   protected void processKeyEvent(KeyEvent e)  {

      int     pos;    // Position of caret. Pozicio de tekstomontrilo.
      String  previous_chars;
      char    accented_character;
      char    deadkey;
      String  replacement;

      int  the_key_code = e.getKeyCode();
      int  the_key_id     = e.getID();

      char[] typed_key_array = {0};
      typed_key_array[0] = e.getKeyChar();
      typed_key               = e.getKeyChar();

      if (the_key_code == KeyEvent.VK_ENTER && 
          the_key_id == KeyEvent.KEY_RELEASED) {
         replaceSelection("\\n");
         return;
      } else

      if (the_key_code == KeyEvent.VK_TAB) {
         if (the_key_id == KeyEvent.KEY_PRESSED) replaceSelection("\\t");
         e.consume();
         requestFocus();  // Come back here! Revenu!
         return;
      } else


      if (UserKeyMap.isActive()) {   
                // Key maps are such a bother...

         if (typed_key < '!' || typed_key > '~') {
            super.processKeyEvent(e);
            if (the_key_code != KeyEvent.VK_SHIFT && 
                the_key_code != KeyEvent.VK_ALT) {
               previously_typed = ' ';
            }
            return;
         }

         if (the_key_id == KeyEvent.KEY_PRESSED || 
             the_key_id == KeyEvent.KEY_RELEASED) return;


         // Try to translate deadkey. Provu traduki senpaþan klavon.
         replacement = UserKeyMap.translate(previously_typed, typed_key);
         if (replacement != null) {
            //if (the_key_id == KeyEvent.KEY_TYPED) {
               pos = getCaretPosition();
               select(pos-1, pos);
               replaceSelection(replacement);
               // Must prevent next key press from causing a translation.
               // Devas malebligi tradukon de sekvanta klavpremo.
               previously_typed = ' ';
            //}
            return;
         }

         replacement = UserKeyMap.translate(typed_key, e.isControlDown(), e.isAltDown());
         if (replacement != null) {
            replaceSelection(replacement);
            if (typed_key >= '!' || typed_key <= '~') previously_typed = typed_key;
            return;
         }
         super.processKeyEvent(e);
            if (typed_key >= '!' || typed_key <= '~') previously_typed = typed_key;
      }
      else {   // user key map not aktive  /  Klavmapo neaktiva.
         super.processKeyEvent(e);
         if (the_key_id == KeyEvent.KEY_RELEASED) {
            if (typed_key >= '!' || typed_key <= '~') previously_typed = typed_key;
         }
      }

   }  // processKeyEvent

}  // SimTextField

