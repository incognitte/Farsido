/*
 * Detect - This class holds methods which detect the encoding of data in
 * and input stream or buffer: unicode, utf8, etc.
 * (Determinu) - Æi tiu klaso entenas metodojn por determini la enkodigon 
 * de datumo en enstrio aý bufro: unikodo, utf8, ktp.
 *
 * Cleve Lendon (Klivo)     1999/02/19
 * klivo@infotrans.or.jp
 * http://www.infotrans.or.jp/~klivo/
 */

import java.awt.*;
import java.io.*;

public class Detect {

   public static final int UNKNOWN            = 0;
   public static final int UNI_BIG_ENDIAN     = 1;     // Unicode big endian. Unikodo  ega-eta
   public static final int UNI_LITTLE_ENDIAN  = 2;     // One little two little three little endians.
   public static final int UTF8               = 3;
   public static final int ENCRYPTED          = 4;


   public static final int MAX                = 3000;  // Maksiuma nombro por kontroli

   // Marker to identify encrypted files. The codes are random.
   // Markilo por identigi kriptaj dosieroj. La kodoj estas hazardaj.

   public static final byte[] ENCRYPTION_MARKER  
                      = {(byte)249, (byte)162, (byte)48, (byte)93, (byte)11, (byte)129}; 

   public Detect () {}

   public static int whatIsIt(File file_to_test) {

      int    first_code = 0;
      int    second_code = 0;
      byte[] byte_buffer = new byte[MAX];

      if (file_to_test.exists()) {       // Se la dosiero ekzistas...

         try {
            // Read bytes to determine the file encoding.
            // Legu bajtojn por determini la enkodigon de la dosiero.
            FileInputStream fin = new FileInputStream(file_to_test);
            int number_read = fin.read(byte_buffer, 0, MAX);
            fin.close();

            // Check for double byte standards (UTF-16, big or little endian)
            // Kontrolu dubajtajn normojn (UTF-16, pezkomenca aý pezfina)
            if (number_read > 2) {
               first_code = (int)(byte_buffer[0]) & 0xFF;
               second_code = (int)(byte_buffer[1]) & 0xFF;
            }
            else return UNKNOWN;

            if (first_code == 255 && second_code == 254) {
               // this is a little endian unicode file
               // æi tio estas pezfina unikodo
               return UNI_LITTLE_ENDIAN;
            }
            else
            if (first_code == 254 && second_code == 255) {
               // this is a big endian unicode file
               // æi tio estas pezkomenca unikodo
               return UNI_BIG_ENDIAN;
            }
            else
            if (isEncryptedFile(byte_buffer)) return ENCRYPTED;
            else
            if (isUTF8(byte_buffer)) return UTF8;
            else return UNKNOWN;

         } catch (IOException io) {
            System.err.println("I/O error in Detect. En/El-eraro en Detect. " + io.getMessage());
            return UNKNOWN;
         } 

      } // if

      return UNKNOWN;

   }  // whatIsIt



   public static int whatIsIt(byte[] byte_buffer) {

      int    first_code = 0;
      int    second_code = 0;
      int    number_read = byte_buffer.length;

      // Check for double byte formats (UTF-16, big or little endian)
      // Kontrolu dubajtajn formatojn (UTF-16, pezkomenca aý pezfina)
      if (number_read > 2) {
         first_code = (int)(byte_buffer[0]) & 0xFF;
         second_code = (int)(byte_buffer[1]) & 0xFF;
      } else return UNKNOWN;

      if (first_code == 255 && second_code == 254) {
         // this is a little endian unicode file
         // æi tio estas pezfina unikodo
         return UNI_LITTLE_ENDIAN;
      }
      else
      if (first_code == 254 && second_code == 255) {
         // this is a big endian unicode file
         // æi tio estas pezkomenca unikodo
         return UNI_BIG_ENDIAN;
      }
      else
      if (isEncryptedFile(byte_buffer)) return ENCRYPTED;
      else
      if (isUTF8(byte_buffer)) return UTF8;
      else return UNKNOWN;

   }  // whatIsIt



// Æi tiu tabelo montras la rilaton inter Unikodo kaj UTF8.
//
// This table shows the relationship between Unicode and UTF8. 
//--------------------------------------------------------
//    Unikodo                 UTF8
//--------------------------------------------------------
//    00000000 0xxxxxxx       0xxxxxxx         
//    00000xxx xxyyyyyy       110xxxxx 10yyyyyy
//    xxxxyyyy yyzzzzzz       1110xxxx 10yyyyyy 10zzzzzz
//--------------------------------------------------------
//
// De la supra tabelo, ni povas determini la sekvantajn regulojn.
// (komprenu ke æiuj intervaloj estas inkluzivaj):
// - Kodoj inter 0 kaj 127 estas validaj en UTF8.
// - se kodo estas super 127, øi devas esti inter 192 kaj 239.
// - se la kodo estas inter 192 kaj 223, la sekvanta kodo devas
//   estis inter 128 kaj 191.
// - se la kodo estas inter 224 kaj 239, la sekvanta kodo devas 
//   esti inter 128 kaj 191, kaj la sekvanta ankaý devas esti
//   inter 128 kaj 191.
// 
// From the above table, we can determine the following (assume 
// all ranges are inclusive):
// - Codes from 0 to 127 are valid in UTF8.
// - if a code is above 127, it must be between 192 and 239
// - if the code is between 192 and 223, the next code must be
//   between 128 and 191.
// - if the code is between 224 and 239, the next code must be 
//   between 128 and 191, and the next one must also be between 
//   128 and 191.

   /*
    * isUTF8 - Determine if the text is in UTF8 format. True means
    * that it could be. False means that it is definately not.
    * isUTF8 - Determinu æu la teksto estas en UTF8-formato. Vera 
    * signifas ke øi eble estas. Falsa signifas ke øi certe ne estas.
    */
   public static boolean isUTF8(byte[] the_text) {

      boolean all_7_bit_codes = true;      // æiuj kodoj estas 7-bitaj
      int  the_char; 
      int length = the_text.length;

      if (length > MAX) length = MAX;

      for (int i = 0; i < length; i++) {
         the_char = ((int)the_text[i] & 0xFF);
         if (the_char > 127) {
            all_7_bit_codes = false;
            if (the_char >= 192 && the_char <= 223) {
               // Get next code. Akiru sekvantan kodon.
               i++;
               the_char = ((int)the_text[i] & 0xFF);
               if (the_char < 128 || the_char > 191) {
                  return false;
               }
            }
            else if (the_char >= 224 && the_char <= 239) {
               // Get next code. Akiru sekvantan kodon.
               i++;
               the_char = ((int)the_text[i] & 0xFF);
               if (the_char < 128 || the_char > 191) {
                  return false;
               }
               // Get next code. Akiru sekvantan kodon.
               i++;
               the_char = ((int)the_text[i] & 0xFF);
               if (the_char < 128 || the_char > 191) {
                  return false;
               }
            }
            else {
               return false;
            }
         }  // if (the_char > 127)
      }  // for
      if (all_7_bit_codes) return false;
      return true;
   }  // isUTF8



   /*
    * isEncrypted - Determine if the text is and encrypted file.
    * isUTF8 - Determinu æu la teksto estas en kripta.
    */
   public static boolean isEncryptedFile(byte[] the_text) {

      int length = ENCRYPTION_MARKER.length;
      if (the_text.length < length) return false;
      for (int i = 0; i < length; i++) {
         if (ENCRYPTION_MARKER[i] != the_text[i]) return false;
      }  // for

      return true;

   }  // isEncrypted


}  // Detect



