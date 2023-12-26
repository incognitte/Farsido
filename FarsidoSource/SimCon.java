/*
 * SimCon - This class contains various methods for converting characters and strings.
 * SimCon - Æi tiu klaso enhavas diversajn metodojn por konverti signojn kaj signoæenojn.
 *
 * Cleve Lendon (Klivo)
 * indriko@yahoo.com
 * http://purl.oclc.org/net/klivo/
 */


import java.awt.*;
import java.util.*;
import java.io.*;

public class SimCon {


   /*
    * isALetter - returns true if the character is a letter between A to Z,
    * a to z, or above 127.
    * isALetter - (estasLitero) - redonas "vera" se la signo estas inter A kaj Z, 
    * a kaj z, aý super 127.
    */
   public final static boolean isALetter(char char1) {
      if (char1 >= 'a' && char1 <= 'z') return true;
      if (char1 >= 'A' && char1 <= 'Z') return true;
      if (char1 > 127) return true;
      return false;
   }



   /*
    * toLower - The toLowerCase methods of the String and Character classes 
    * convert unicode characters to lower case (and therefore convert Latin 1
    * to lower case). But they do not convert the Latin 3 characters for ^H and
    * ^J. This routine corrects the problem.
    * toLower - (al minuskloj) La "toLowerCase" metodoj de la æeno-kaj-signo-
    * klasoj konvertas unikodajn signojn al minuskloj (kaj tial ili ankaý konvertas
    * Latin1-kodojn). Sed ili ne konvertas la Latin-3-kodojn por ^H kaj ^J. Æi 
    * tiu metodo korektas la problemon.
    */
    public static char toLower(char to_convert) {
       char char1 = Character.toLowerCase(to_convert);
       if (char1 == 0xac || char1 == 0xa6) {
          char1 = (char)(char1 + 16);
       }
       return char1;
    }

   /*
    * toLower - This method converts a string to lower case. It handles two
    * Latin-3 codes which are not converted by toLower (^H and ^J).
    * toLower - Æi tiu metodo minuskligas signoæenon. Øi traktas du
    * Latin-3-kodojn kiuj ne estas konvertataj de "toLower" (¦ kaj ¬).
    */
   public static String toLower(String to_convert) {
      String  temp1 = to_convert.toLowerCase();
      // Convert 2 latin 3 characters (^H and ^J). 
      // Konvertu 2 latin-3-signojn (¦ kaj ¬).
      String temp2 = temp1.replace((char)0xa6,(char)0xb6); 
      return temp2.replace((char)0xac,(char)0xbc);
   }


   /* 
    * convertBackslash - This method converts \n,\t etc to new line, tab etc.
    * convertBackslash - Æi tiu metodo konvertas \n, \t ktp al novlinio, horizontala salto ktp.
    */
   public static String convertBackslash(String to_convert) {

      int  length = to_convert.length();
      char[]  char_buffer = new char[length];

      int i = 0; int j = 0;
      char char1 = 0;
      char last_char = 0;
      for (; i < length; i++, j++) {
         char1 = to_convert.charAt(i);
         if (last_char == '\\') {
            if (char1 == 'n' || char1 == 'N') {
               j--;
               char_buffer[j] = (char)10;
            }
            else
            if (char1 == 't' || char1 == 'T') {
               j--;
               char_buffer[j] = (char)9;
            }
            else
            if (char1 == 'r' || char1 == 'R') {
               j--;
               char_buffer[j] = (char)13;
            }
            else
            if (char1 == '\\') {
               j--;
               char_buffer[j] = '\\';
            }
            else {
               char_buffer[j] = char1;
            }
            last_char = 'a';
         }
         else {
            char_buffer[j] = char1;
            last_char = char1;
         }
      }  // for

      return new String(char_buffer, 0, j);

   }  // convertBackslash

   /* 
    * convertToUnicode - Convert a non unicode byte array to a unicode string.
    * convertToUnicode - convert neunikodan bajtmatricon al unikoda æeno.
    */
   public static String convertToUnicode(byte[] byte_array, String encoding) {
      String unicode_string;
      if (encoding.startsWith("Iran")) {    // Convert Iran System to Unicode
         char[] unicode_chars = new char[byte_array.length];
         for (int i = 0; i < byte_array.length; i++) {
            unicode_chars[i] = (char)SimIran.convertIranUni(byte_array[i]);
         }
         unicode_string = new String(unicode_chars);
      }
	  else if (encoding.startsWith("Vazhe")) {    // Convert Iran System to Unicode
         char[] unicode_chars = new char[byte_array.length];
         for (int i = 0; i < byte_array.length; i++) {
            unicode_chars[i] = (char)SimIran.convertVazheUni(byte_array[i]);
         }
         unicode_string = new String(unicode_chars);
      }
      else {
         try {
            // Convert the bytes to a unicode string.
            // convert la bajtojn al unikoda æeno.
            unicode_string = new String(byte_array, encoding);
         } catch (UnsupportedEncodingException uex) {
            System.err.println("convert-to-unicode  Conversion failed. Konvertado malsukcesis.");
            return null;
         }
      }
      return unicode_string;
   }  // convertToUnicode



   /*
    * convertUTF8Uni - This method converts a utf8 byte array to 
    * an array of unicode characters. It returns the number of characters
    * in the character array.
    * convertUTF8Uni - Æi tiu metodo konvertas UTF8-bajtmatricon al
    * matrico de unikodaj signoj. Øi redonas la nombron da signoj en la 
    * signomatrico.
    */ 

   public static char[] convertUTF8Uni(byte[] byte_array) {

      char char1, char2, char3, charx, chary, charz;

      int  byte_array_length = byte_array.length;  // Nombro da karaktroj en tekst-bufro.
      char[]   char_array = new char[byte_array_length];

      int byte_index = 0;   
      int char_index   = 0; 

      while (byte_index < byte_array_length) {

        char1 = (char)(byte_array[byte_index] & 0xFF); 

         if (char1 >= 192 && char1 < 224 && (byte_index + 1 < byte_array_length)) {
            char2 = (char)(byte_array[byte_index+1] & 0xFF); 
            if (char2 >= 128 && char2 < 192) {
               char_array[char_index] = (char)(((char1 - 192) * 64) + char2 - 128);
               char_index++;
               byte_index = byte_index + 2;
            }
            else {
               char_array[char_index] = char1;
               char_index++;
               byte_index++;
            }
         }
         else 
         if (char1 >= 224 && char1 < 240 && (byte_index + 2 < byte_array_length)) {
            char2 = (char)(byte_array[byte_index+1] & 0xFF); 
            char3 = (char)(byte_array[byte_index+2] & 0xFF); 
            if (char2 >= 128 && char2 < 192 && char3 >= 128 && char3 < 192) {
               char_array[char_index] = 
                       (char)(((char1 - 224) * 4096) + ((char2 - 128) * 64) + char3 - 128);
               char_index++;
               byte_index = byte_index + 3;
            }
            else {
               char_array[char_index] = char1;
               char_index++;
               byte_index++;
            }
         }
         else {
            char_array[char_index] = char1;
            char_index++;
            byte_index++;
         }
      }  // while

      // The array we return must be the right length.
      // La redonita matrico devas havi øustan longecon.
      char[] return_array = new char[char_index];
      System.arraycopy(char_array,0,return_array,0,char_index);
      return return_array;

   }  // convertUTF8Uni



   /**
    * tokenize
    * Get tokens from a string. Tokens are separated by a vertical line |.
    * Use this to get codes and labels from the resource bundle.
    *
    * Eltondu simbolojn (vortojn) de signoæenoj. Simboloj estas apartigitaj 
    * per vertikala linio | . Uzu æi tion por akiri kodojn kaj etikedojn de 
    * la "rimeda" dosiero.
    */
   public static String[] tokenize(String input) {
      StringTokenizer t = new StringTokenizer(input,"|");
      int number = t.countTokens();
      String[]  the_tokens = new String[number];
      for (int i = 0; i < number; i++) {
         the_tokens[i] = t.nextToken();
      }
      return the_tokens;
   }  // tokenize


}  // SimCon



