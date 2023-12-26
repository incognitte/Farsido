/*
 * UserKeyMap
 *
 * This class has methods for implementing user key maps.
 * It receives a file object from the class Farsido, reads the
 * file, and uses it to create a translation table. Keymap files have 
 * an extention of kmp, eg., Persian.kmp
 *
 * Lines that begin with a space in the keymap file are
 * ignored (treated as comments). Other lines are treated
 * as a key assignment, one per line. The translated codes 
 * can be indicated with their real unicode character or by 
 * their hex value. It is possible to translate to more than 
 * one character.
 * For example:
 *
 * QÞ
 * qþ
 * 0\u06F0
 * 1\u06F1
 * altaThis text will appear when the user types Alt-a.
 * altbThis text will appear when the user types Alt-b.
 *
 * As the above examples show, Alt-key sequences can be
 * redefined. I originally designed this class to allow
 * redefinition of Ctrl-keys also, but this is difficult
 * because many Ctrl-key have key-bindings (swing keymaps).
 * Rather than unbinding already bound ctrl-keys, I've 
 * decided not to allow redefinition of ctrl-keys.
 *
 * I've decided to add dead-key definitions:
 * 2keys^AÂ
 * 2keys^aâ
 * 2keys"AÄ
 * 2keys"aä
 * When the user types two consecutive keys, for example,
 * circumflex and the letter A, as in the above example, it will
 * be replaced with the following letter (Â).
 *
 * ------------------------------------------------------------
 *
 * Æi tiu klaso havas metodojn por funkciigi klavmapojn por 
 * uzantoj. La klaso Farsido donas dosieran objekton (File).
 * Æi tiu klaso legas la dosieron kaj kreas traduktabelon.
 * Klavmapaj dosieroj havas sufikson de kmp, ekzemple: 
 * Persian.kmp
 *
 * Linioj kiuj komenciøas per spaco en la klavmapa dosiero 
 * estas malatentataj (traktataj kiel komentoj). Aliaj linioj
 * estas traktataj kiel klavdifinoj: unu po linio. La traduk-
 * kodoj povas esti indikitaj per siaj veraj unikodaj signoj,
 * aý per deksesuma cifero. Eblas traduki klavon al pli ol unu
 * litero. Ekzemploj:
 *
 * QÞ
 * qþ
 * 0\u06F0
 * 1\u06F1
 * altaÆi tiu teksto aperos kiam la uzanto tajpas Alt-a.
 * altbÆi tiu teksto aperos kiam la uzanto tajpas Alt-b.
 *
 * Kiel la supraj ekzemploj montras, eblas redifini Alt-klavojn.
 * Mi originale desegnis æi tiun klason pro permesi la redifinon
 * de ctrl-klavoj ankaý, sed æi tio estas malfacila æar multaj
 * ctrl-klavoj estas ligitaj (vidu: Swing keymaps, bindings). 
 * Anstataý ol malligi jam ligitajn ctrl-klavojn, mi decidis ne
 * permesi redifinon de ctrl-klavojn.
 *
 *
 * Mi decidis subteni la difinon de senpaþaj klavoj, ekzemple:
 * 2keys^AÂ
 * 2keys^aâ
 * 2keys"AÄ
 * 2keys"aä
 * Kiam uzanto tajpos du sinsekvajn klavoj, ekzemple,
 * cirkumflekson kaj literon A, kiel supre, ili estos anstataýitaj
 * per la sekvanta litero, (Â).
 *
 * Cleve Lendon (Klivo)
 * indriko@yahoo.com
 * http://purl.oclc.org/net/klivo/
 * 2000/12/19
 *
 * 2001/01/11 fix for 3.31  Alas :-(
 * 2001/01/11 korekto por 3.31  Ho Ve :-(
 * Fix alt functionality (forgot else in loadUserKeyMap)
 * Korektu Alt-klav-funkcion (forgeis 'else' en loadUserKeyMap)
 *
 */

import java.awt.*;
import java.io.*;
import java.util.*;

public class UserKeyMap {

   private static boolean active;

   private static String[] key_table;   // No control keys. Sen regklavoj.
   private static String[] ctrl_table;  // control  - provizore ne uzu.
   private static String[] alt_table;   // alternative

   // To implement dead-keys, I am creating a table called twokeys,
   // which has an entry for each 'deadkey' (the first key in the
   // pair). The entries in the twokey table will be references to key tables
   // (lists of translation strings). If a key is not being used as a deadkey,
   // the corresponding entry in twokeys will be null.

   // Por funkciigi 'senpaþajn' klavojn, mi kreas tabelon nomatan twokeys,
   // kiu havas lokon por æiu 'senpaþa' klavo (la unua klavo en æiu paro).
   // La lokoj (ujoj?) en la tabelo twokeys referencas la klav_tabeloj,
   // (listoj de traduk-æenoj, kiel key_table). Se klavo ne estas difinita kiel
   // senpaþa klavo, øia loko en twokeys estas nula.

   private static String[][]  twokeys;

   private final static int table_size = ('~' - '!') + 1;

   public UserKeyMap () {
      key_table   = new String[table_size]; 
      ctrl_table  = new String[table_size];
      alt_table   = new String[table_size];
      twokeys     = new String[table_size][];
      active = false;
   }

   public static boolean isActive() {
      return active;
   }

   public static void loadUserKeyMap(File keymap_file) {

      // Clear out the translation tables.
      // Viþu la traduktabelojn.
      key_table   = new String[table_size]; 
      ctrl_table  = new String[table_size];
      alt_table   = new String[table_size];
      twokeys     = new String[table_size][];
      String[]    table;

      // If the keymap file was null, just exit.
      // Se la klavmapa dosiero estis nula, nur æesu.
      active = false;
      if (keymap_file == null) return;
 
      table = key_table;
      int   table_index;

      String   the_line;      // Linio de la klavmapa dosiero.
      char     the_char;      // key to convert   /  konvertota klavo
      char     second_char;   // for deadkeys  / por senpaþaj klavoj (twokeys)
      String   rest_of_line;  // resta¼o de la linio

      if (keymap_file != null) {
         try {
            FileInputStream   fis   = new FileInputStream(keymap_file);
            InputStreamReader isr   = new InputStreamReader(fis,"UnicodeBig");
            BufferedReader    in    = new BufferedReader(isr);
            while (in.ready()) {
               the_line = in.readLine();
               if (the_line.length() > 1 && the_line.charAt(0) != ' ') {
                  rest_of_line = "";
                  if (the_line.toLowerCase().startsWith("alt")) {
                     if (the_line.length() >= 5) {
                        table = alt_table;
                        the_char = the_line.charAt(3);
                        rest_of_line = the_line.substring(4);
                     }
                     else {
                        the_char = ' ';   // don't translate  /  ne traduku
                     }
                  } else  
                  if (the_line.toLowerCase().startsWith("2keys")) {
                     if (the_line.length() >= 8) {
                        the_char = the_line.charAt(5);
                        second_char = the_line.charAt(6);
                        if (the_char >= '!' && the_char <= '~' &&
                            second_char >= '!' && second_char <= '~') {
                           rest_of_line = the_line.substring(7);
                           table_index = (int)(the_char - '!');
                           if (twokeys[table_index] == null) {
                              twokeys[table_index]  = new String[table_size];
                           }
                           table = twokeys[table_index];
                           the_char = second_char;
                        }
                        else the_char = ' ';
                     }
                     else {
                        the_char = ' '; // don't translate  /  ne traduku
                     }
                  }
                  else {
                     table = key_table;
                     the_char = the_line.charAt(0);
                     rest_of_line = the_line.substring(1);
                  }

                  if (the_char >= '!' && the_char <= '~' && 
                      rest_of_line.length() > 0) {
                     table_index = (int)(the_char - '!');
                     table[table_index] = convert_slash_u(rest_of_line);
                  }
               }
            }  // while

         } catch (IOException ioe) {
            System.err.println("Couldn't read keymap. Ne povis legi klavmapon.");
         }
         active = true;
      }
   }


   // Translate dead key. Traduku senpaþajn klavojn.
   public static String translate(char prev_key, char the_key) {
      String[] table;
      int index;
      //System.out.println(">> " + prev_key + " " + the_key);

      if (the_key < '!' || the_key > '~' ||
          prev_key < '!' || prev_key > '~') return null;
      index = the_key - '!';

      if (isDeadKey(prev_key)) {
         table = twokeys[(prev_key - '!')];
         return table[index];
      }
      return null;

   }  // translate



   // Translations for single key presses. 
   // Tradukoj pro unuopaj klav-premoj.
   public static String translate(char the_key, boolean ctrl_down, boolean alt_down) {
      String[] table;
      int index;
      //System.out.println(">> " + the_key + " " + ctrl_down);

      if (the_key < '!' || the_key > '~') return null;
      index = the_key - '!';

      if (alt_down) {
         return alt_table[index];
      }
      else {
         return key_table[index];
      }

   }  // translate



   // This method converts unicode escape sequences (\u06F0) in a 
   // string to their equivalent codex.
   // Æi tiu metodo convertas unikodajn eskap-tekstojn (\u06F0) en 
   // signoæeno al øiaj ekvivalentaj kodoj.
   private static String convert_slash_u(String to_convert) {

      String  converting = to_convert;
      String  front, uni, end;
      int     the_index;

      the_index = converting.indexOf("\\u");

      while (the_index != -1 && (the_index + 6 <= converting.length())) {
         front = converting.substring(0,the_index);
         uni   = convert_uni_escape(converting.substring(the_index+2,the_index+6));
         if (the_index+6 < converting.length()) end  = converting.substring(the_index+6);
         else end = "";
         converting = front + uni + end;
         the_index = converting.indexOf("\\u");
      }  // while

      the_index = converting.indexOf("\\U");

      while (the_index != -1 && (the_index + 6 <= converting.length())) {
         front = converting.substring(0,the_index);
         uni   = convert_uni_escape(converting.substring(the_index+2,the_index+6));
         if (the_index+6 < converting.length()) end   = converting.substring(the_index+6);
         else end = "";
         converting = front + uni + end;
         the_index = converting.indexOf("\\U");
      }  // while
      return converting;

   }  // convert_slash_u



   private static String convert_uni_escape(String uni) {
      char[] the_unicode_char = new char[1];
      try {
         the_unicode_char[0] = (char)Integer.parseInt(uni,16);
      } catch (NumberFormatException nfe) {
         return uni;
      }
      return new String(the_unicode_char);
   }

   public static boolean isDeadKey(char the_key) {
      if (the_key > '~' || the_key < '!') return false;
      if (twokeys[(the_key - '!')] != null) return true;
      return false;
   }

}  // UserKeyMap



