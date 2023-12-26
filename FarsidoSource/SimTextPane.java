/*
 * I am subclassing JTextPane so that I can overwrite processKeyEvent. The 
 * main reason is that I want to implement a dead key (The user types two 
 * keys, eg. ^c, and a single accented letter appears). I previously tried 
 * to do this with a key map. I was able to cut out the dead key character 
 * and put in an accented letter, but the second letter always followed 
 * it (^ee). I tried to consume the second key event without success. 
 * Cleve 1999/01/12
 * I've changed the BasicTextUI, so that I can implement an Esperanto 
 * Spell-Checker.
 * Cleve 1999/02/10
 * I'm adding a timer to repaint the screen every 3 seconds, because Swing
 * doesn't always repaint correctly.
 * Cleve 1999/02/15
 * Correct linesInDocument(). Base calculation on size of text pane.
 * Cleve 1999/07/26
 * Add support for Persian (reverse text) and key maps.
 * Cleve 2000/12/05
 *
 * Mi subklasigas JTextPane por superskribi la metodon processKeyEvent. La 
 * æefa kialo estas ke mi volas krei senpaþan klavon. (La uzanto tajpas du 
 * klavojn, ekzemple ^c, kaj unu supersignita litero aperas.) Antaýe mi 
 * provis fari æi tion per klavmapo. Mi sukcesis eltondi la senpaþan signon 
 * kaj enmeti supersignitan literon, sed la dua litero æiam sekvis øin (^cc). 
 * Mi provis "manøi" la duan klaveventon sen sukceso.
 * Klivo 1999/01/12
 * Mi þanøis la tekstan interfacon (BasicTextUI), por enmeti Esperantan
 * kontrolilon de literumado.
 * Klivo 1999/02/10
 * Mi enmetas horloøon por repentri la ekranon æiun trian sekundon, æar "Sving"
 * ne æiam repentras øuste.
 * Korektu linesInDocument(). Kalkulu laý la grandeco de la tekstujo.
 * Klivo 1999/07/26
 * Subtenu Persan lingvon (inversigu tekston) kaj klavmapojn.
 * Klivo 2000/12/05
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;

public class SimTextPane extends JTextPane implements Printable, ActionListener {

   // This is the view port which the text pane has been installed into.
   // Æi tio estas la montrofenestro en kiu æu tiu teksta kampo estas instalita.
   private JViewport      view_port;   

   private static char    typed_key = 0;          // tajpita klavo (signo)
   private static char    previously_typed = 0;   // antaýe tajpita signo

   private static Color   not_so_bright = new Color(235,235,235);

   // first_action is used to request focus.
   // first_action (unua agevento) estas uzita por akiri fokuson.
   private boolean   first_action = true;
   private Timer the_timer;


   static boolean  is_gray = false;   // Background colour. Koloro de fono.


   public SimTextPane () {
      super();
      startup();
   }

   public SimTextPane (StyledDocument sd) {
      super(sd);
      startup();
   }

   private void startup() {
      setUI(new SimTextUI());
      fontSize16();
//      setTabs();
      the_timer = new Timer(3000,this);  // three seconds  / tri sekundoj
      the_timer.start();   // komencu
      load_esperanto_dictionary();
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
    * setTabs - Sets the tabs. This doesn't work.
    * - difinas tabpoziciojn. Æi tio ne funkcias.
    */
   private void setTabs() {
      final int number_of_stops = 20;   // Nombro da taboj.
      TabStop[]  tabstops = new TabStop[number_of_stops];
      for (int i = 0; i < number_of_stops; i++) {
         tabstops[i] = new TabStop(40.0f * (i+1),
                     TabStop.ALIGN_LEFT,TabStop.LEAD_NONE);
      }
      TabSet tabset = new TabSet(tabstops);
      SimpleAttributeSet new_attribute = new SimpleAttributeSet();
      StyleConstants.setTabSet(new_attribute, tabset);
      StyleConstants.setLeftIndent(new_attribute, 10.0f);
      setParagraphAttributes(new_attribute,false);
      setCharacterAttributes(new_attribute,false);
   }  // setTabs



   public void actionPerformed(ActionEvent e) {
      // Must be timer. Devas esti horloøo.
      if (first_action) {
         requestFocus();
         first_action = false;
      }
      repaint();
   }


   /* 
    * processKeyEvent - Capture key events and check whether dead 
    * key conversion is necessary.
    * processKeyEvent - Kaptu klaveventojn kaj kontrolu æu konverto 
    * de senpaþa klavo necesas.
    */
   protected void processKeyEvent(KeyEvent e)  {

      int     pos;    // Position of caret. Pozicio de tekstomontrilo.
      String  previous_chars;
      char    accented_character;
      String  replacement;

      int  the_key_code   = e.getKeyCode();
      int  the_key_id     = e.getID();

      char[] typed_key_array = {0};
      typed_key_array[0] = e.getKeyChar();
      typed_key          = e.getKeyChar();

      //System.err.println("K " + typed_key + " " + (int)typed_key + 
      //                   " " + the_key_code + " " + the_key_id + "\n");


      // For debugging purposes. Por testaj celoj.
      if (the_key_code == KeyEvent.VK_F1 && the_key_id == KeyEvent.KEY_RELEASED) {
         // Get the text. Akiru la tekston.
         Document doc = getDocument();
         int length = doc.getLength();
         Segment text_segment = new Segment();
         try {
            doc.getText(0,length,text_segment);
         } catch (BadLocationException blx) { }
         int max = text_segment.count;
         if (max > 20) max = 20;
         System.out.println("number " + max);
         System.out.println("caret position " + getCaretPosition());
         for (int i = 0; i < max; i++) {
            System.out.println("> " + text_segment.array[i] + " " +
                                    (int)text_segment.array[i] +
                 " " + Integer.toHexString((int)text_segment.array[i]));
         }
      } else


      // For debugging purposes. Por testaj celoj.
      if (the_key_code == KeyEvent.VK_F2 && the_key_id == KeyEvent.KEY_RELEASED) {
         // Get the text. Akiru la tekston.
         //Document doc = getDocument();
         //String  the_model = ModelDisplay.showModel(doc);
         //System.out.println(the_model);
         Dimension dmn = new Dimension();
         dmn = getSize(dmn);
         System.out.println("Size w=" + dmn.width + " h=" + dmn.height);
      } else


      // Make background gray. Grizigu fonon.
      if (the_key_code == KeyEvent.VK_F3 && the_key_id == KeyEvent.KEY_RELEASED) {
         if (is_gray) {
            setBackground(Color.white);
            is_gray = false;
         }
         else {
            setBackground(not_so_bright);
            is_gray = true;
         }

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


   public void setViewport(JViewport view_port) {
      this.view_port = view_port;
   }

   Point      upper_left, bottom_right;  // coordinates of viewport  
                                         // koordinatoj de montrofenestro
   Dimension  port_size;                 // grandeco de montrofenestro
   Segment    text_segment = new Segment();
   Document   doc;                       // dokumento

   /* 
    * check_spelling  - Check the spelling of Esperanto words and draws a red 
    * underline under misspelled words.
    * - Kontrola literumadon de Esperantaj vortoj kaj desegnas ruøan linion sub
    * misliterumitaj vortoj.
    */
   public void check_spelling (Graphics g) {

      int start_index, end_index;
      int y;  // For drawing underline. Por desegno de substreko.

      if (view_port == null) return;
      if (analizo == null) return;

      if (!analizo.vortaro_shargita) return;
      try {

         // Get upper left and lower right of view port.
         // Akiru supran maldekstron kaj malsupran dekstron de montrofenestro.
         upper_left = view_port.getViewPosition();
         port_size = view_port.getExtentSize();
         bottom_right  = new Point(port_size.width, upper_left.y + port_size.height);

         // Now get start and end indices from the interface.
         // Nun akiru komenco-kaj-fino-indeksojn de la interfaco.
         TextUI user_interface = getUI();
         start_index = user_interface.viewToModel(this, upper_left);
         end_index   = user_interface.viewToModel(this, bottom_right);

         // Get the text. Akiru la tekston.
         doc = getDocument();
         int length = doc.getLength();
         doc.getText(0,length,text_segment);

         // Red underline for errors.   Ruøa substreko por eraroj.
         g.setColor(Color.red);

         for (int i = start_index; i < end_index; ) {
            if (SimCon.isALetter(text_segment.array[i])) {
               int word_start = i;
               while (i < length && SimCon.isALetter(text_segment.array[i])) i++;
               int word_end = i;
               if (word_end - word_start > 1) {
                  analizo.normigu_vorton(text_segment.array, word_start, word_end);
                  if (!analizo.kontrolu_vorton()) {
                     // draw a line  /  desegnu linion
                     Rectangle line_start = user_interface.modelToView(this, word_start);
                     Rectangle line_end   = user_interface.modelToView(this, word_end);
                     y = line_start.y + line_start.height - 3;
                     g.drawLine(line_start.x, y, line_end.x, y);
                  }
               }
            }
            else i++;  // Skip nonletters. Preteriru neliteroj.
         } // for

         g.setColor(Color.black);
      } catch (BadLocationException ble) {
         System.err.println("Error in check_spelling. Eraro en kontrolo de literumado.");
         return;
      }

   }  // check_spelling


   /*
    * print is part of the Printable interface.
    * print estas parto de la "Printable" interfaco.
    */
   public int print (Graphics g, PageFormat pf, int index) {
      // Remove caret while printing.
      // Forigu tekstomontrilon dum presado.
      Caret caret = getCaret();
      caret.setVisible(false);
      // Disable Spellcheck temporarily.
      // Malaktivigu Kontroladon portempe.
      boolean save_spellcheck = spellcheck_enabled;
      setSpellCheck(false);

      double left_x = pf.getImageableX();
      double top_y  = pf.getImageableY() - (pf.getImageableHeight() * index) - 3.0;
      // Why -3? The pages were not dividing properly. -3 was decided by experiment.
      // Kial -3? La paøoj ne dividiøis øuste. -3 estis fiksita per eksperimento.
      g.translate((int)left_x, (int)top_y);
      printAll(g);
      caret.setVisible(true);
      setSpellCheck(save_spellcheck);
      return(Printable.PAGE_EXISTS);
   }  // print

   /* 
    * linesInDocument - Calculate the number of lines in the document.
    * linesInDocument - Kalkulu la nombron da linioj en la dokumento.
    */
   public int linesInDocument() {
      //return getDocument().getDefaultRootElement().getElementCount();
      // Korektita 1999/07/27
      Dimension dmn = new Dimension();
      dmn = getSize(dmn);
      Double fh;
      fh = new Double(heightOfFont());
      return (dmn.height / fh.intValue());
   }


   /* 
    * heightOfFont - Calculate the height of the current font from FontMetrics.
    * heightOfFont - Kalkulu la altecon de la nuna tiparo per FontMetrics.
    */
   public double heightOfFont() {
      // Getting the font size from the font object does not work.
      // For reasons which I don't understand, a font object's point size
      // does not reflect the actual size it will have on the screen
      // or page. The correct size must be acquired from FontMetrics.
      // Tipara grandeco akirita de la tipara objekto estas maløusta.
      // Pro kialoj kiujn mi ne komprenas, la "punkto"-grandeco de tiparo
      // ne spegulas la efektivan grandecon kiun øi havos sur ekrano aý
      // paøo. La øusta grandeco devas esti akirita de FontMetrics.
      // font_size = text_pane.getFont().getSize();
      return getGraphics().getFontMetrics().getHeight();
   }

   /* 
    * linesInPage - Calculate the number of lines in a page.
    * linesInPage - Kalkulu la nombron da linioj en paøo.
    */
   public int linesInPage(PageFormat pf) {
      return (int)(pf.getImageableHeight() / heightOfFont());
   }

   /* 
    * numberOfPages - Calculate the number of pages in the current document.
    * numberOfPages - Kalkulu la nombron da paøoj en la nuna dokumento.
    */
   public int numberOfPages(PageFormat pf) {
      int lines_in_doc  = linesInDocument();
      int lines_in_page = linesInPage(pf);
      int number_of_pages = (lines_in_doc / lines_in_page);
      if ((lines_in_doc % lines_in_page) > 0) number_of_pages++;
      return number_of_pages;
   }  // numberOfPages


   // SimTextUI - I've extended the user interface (view) in order to
   // overwrite the update method. Check_spelling needs to draw a red 
   // underline under misspelled words.
   // Mi etendis la uzantan interfacon por superskribi la 
   // "update"-metodon. Check_spelling (kontrolu literumadon) devas
   // desegni ruøan linion sub misliterumitaj vortoj.  - Klivo
   public class SimTextUI extends BasicTextPaneUI {
      public SimTextUI() {
         super();  
      }
      public void update (Graphics g, JComponent c) {
         paint(g,c);
         if (spellcheck_enabled) check_spelling(g);
      }
   } // SimTextUI


   /*
    * load_esperanto_dictionary 
    * Þargu Esperantan Vortaron
    */ 
   Analizo  analizo;
   private void load_esperanto_dictionary () {
      analizo = new Analizo("vortaro.dat");
   }  // load_esperanto_dictionary


   private boolean spellcheck_enabled = false;  // Check Esperanto Spelling. 
                                               // Kontrolu Esperantan literumadon.

   public void setSpellCheck(boolean check) {
      spellcheck_enabled = check;
      repaint();
   }

   public void toggleSpellCheck() {
      if (spellcheck_enabled) spellcheck_enabled = false;
      else spellcheck_enabled = true;
      repaint();
   }

   // Reverse text. (For Persian)
   // Inversigu tekston (por la persa).
   public void reverseText() {
         // Get the text. Akiru la tekston.
         Document doc = getDocument();
         int length = doc.getLength();
         Segment text_segment = new Segment();
         try {
            doc.getText(0,length,text_segment);
         } catch (BadLocationException blx) { }

         char[] reversing_buffer = new char[200];
         int  start = 0;
         int  end;
         int  number_in_line;
         for (int i = 1; i <= length; i++) {
            if (i == length || text_segment.array[i] == '\n') {
               end = i;
               number_in_line = end - start;
               if (number_in_line < 200 && number_in_line > 1) {
                  int j = start;
                  int k = 0;   
                  while (j < end) {
                      reversing_buffer[number_in_line - 1 -k] = text_segment.array[j];
                      j++; k++;
                  }  // while

                  try {
                     setCaretPosition(start);
                     moveCaretPosition(end);
                     replaceSelection(new String(reversing_buffer,0,number_in_line));
                  } catch (NullPointerException npx) {
                     System.err.println("SimTextPane: Null pointer. Nula montrilo. 10\n"  + npx.toString());
                  }
               } // if
               while (i < length && text_segment.array[i] == '\n') i++;
               start = i;
            }  // if
         }  // for
    }


}  // SimTextPane

