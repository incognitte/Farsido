/*
 * SimTextArea - I am subclassing JTextArea to try to get around 
 * a bug concerning key bindings. Ctrl-x, ctrl-c, kaj ctrl-p don't
 * work in the text area of ShowCharsDialog. 
 * SimTextArea - Mi kreas subklason pro provi eviti problemon en 
 * JTextArea pri klavo-ligoj. Ctrl-x, ctrl-c kaj ctrl-p ne 
 * funkcias en la teksta kampo de ShowCharsDialog.
 * Cleve Lendon (Klivo)   2000/12/15
 * indriko@yahoo.com
 * http://purl.oclc.org/net/klivo/
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;


public class SimTextArea extends JTextArea {

   Action sim_cut     = new SimCut("ta-cut");
   Action sim_copy    = new SimCopy("ta-copy");
   Action sim_paste   = new SimPaste("ta-paste");

   public SimTextArea () {
      super();
      set_key_bindings();
   }

   public SimTextArea (int width, int height) {
      super(width, height);
      set_key_bindings();
   }

   public SimTextArea (String first_text) {
      super(first_text);
      set_key_bindings();
   }

   public int getRowHeight() {
      return super.getRowHeight();
   }

   public int getColumnWidth() {
      return super.getColumnWidth();
   }


   /* 
    * heightOfChar - Calculate the height of a character 
    * in the current font from FontMetrics.
    * heightOfChar - Kalkulu la altecon de signo en la nuna 
    * tiparo per FontMetrics.
    */
   public int heightOfChar() {
      // Getting the font size from the font object does not work.
      // For reasons which I don't understand, a font object's point size
      // does not reflect the actual size it will have on the screen
      // or page. The correct size must be acquired from FontMetrics.
      // Tipara grandeco akirita de la tipara objekto estas maløusta.
      // Pro kialoj kiujn mi ne komprenas, la "punkto"-grandeco de tiparo
      // ne spegulas la efektivan grandecon kiun øi havos sur ekrano aý
      // paøo. La øusta grandeco devas esti akirita de FontMetrics.
      // font_size = text_pane.getFont().getSize();
      Graphics gf = getGraphics();
      if (gf == null) return 10;
      FontMetrics fm = gf.getFontMetrics();
      if (fm != null) return fm.getHeight();
      return 10;
   }

   public int widthOfChar() {
      Graphics gf = getGraphics();
      if (gf == null) return 10;
      FontMetrics fm = gf.getFontMetrics();
      if (fm != null) return fm.charWidth('M');
      return 10;
   }

   public int widthOfChars(char[] the_chars) {
      Graphics gf = getGraphics();
      if (gf == null) return 10;
      FontMetrics fm = gf.getFontMetrics();
      if (fm != null) return fm.charsWidth(the_chars,0,the_chars.length);
      return 10;
   }

   public int widthOfChars(char[] the_chars, int start, int num) {
      Graphics gf = getGraphics();
      if (gf == null) return 10;
      FontMetrics fm = gf.getFontMetrics();
      if (fm != null) return fm.charsWidth(the_chars,start,num);
      return 10;
   }


   //////////////////////////////////////////////////
   // Set key bindings.  Pretigu klav-ligojn.

   private void set_key_bindings () {

      JTextComponent.KeyBinding[] bindings = {

         new JTextComponent.KeyBinding(KeyStroke.getKeyStroke(
                        KeyEvent.VK_X, 
                        InputEvent.CTRL_MASK),
                        "ta-cut"),
         new JTextComponent.KeyBinding(KeyStroke.getKeyStroke(
                        KeyEvent.VK_C, 
                        InputEvent.CTRL_MASK),
                        "ta-copy"),
         new JTextComponent.KeyBinding(KeyStroke.getKeyStroke(
                        KeyEvent.VK_V, 
                        InputEvent.CTRL_MASK),
                        "ta-paste"),
      };
      Keymap parentmap = getKeymap();
      Keymap takeymap = JTextComponent.addKeymap("takeymap", parentmap);
      Action[] the_action_list = { sim_cut, sim_copy, sim_paste };
      JTextComponent.loadKeymap(takeymap, bindings, the_action_list);
      setKeymap(takeymap);

   }


   /**
    * SimCopy - Action class to copy text.
    * SimCopy - Agklaso por kopii tekston.
    */
   class SimCopy extends AbstractAction {

      SimCopy(String default_label) {
         super(default_label);
      }

      public void actionPerformed(ActionEvent e) {
         copy();
         requestFocus();
      }  // actionPerformed

   }  // SimCopy

   /**
    * SimCut - Action class to cut text.
    * SimCut - Agklaso por eltondi tekston.
    */
   class SimCut extends AbstractAction {

      SimCut(String default_label) {
         super(default_label);
      }

      public void actionPerformed(ActionEvent e) {
         cut();
         requestFocus();
      }  // actionPerformed

   }  // SimCut

   /**
    * SimPaste - Action class to paste text.
    * SimCopy - Agklaso por interglui tekston.
    */
   class SimPaste extends AbstractAction {

      SimPaste(String default_label) {
         super(default_label);
      }

      public void actionPerformed(ActionEvent e) {
         paste();
         requestFocus();
      }  // actionPerformed

   }  // SimPaste



}  // SimTextArea

