/*
 * Encrypt - This class contains methods to encrypt and decrypt text.
 * The encryption algorithm is fairly simple. It would probably take
 * a snoopy acquaintance several years to decrypt your files. It would
 * probably take the CIA several minutes.
 *
 * Æi tiu klaso enhavas metodojn por kriptigi kaj malkriptigi tekston.
 * La kriptiga algoritmo estas iom simpla. Scivolema konato bezonus 
 * plurajn jarojn por malkriptigi viajn dosierojn. Centra Inteligento-
 * Agentejo bezonus plurajn minutojn.
 * 1999/06/17
 *
 * For version 3.3  /  Por versio 3.3
 * Handle IO exceptions at a higher level.
 * Traktu en/el-eraroj æe pli alta nivelo.
 *
 * Cleve Lendon (Klivo)
 * indriko@yahoo.com
 * http://purl.oclc.org/net/klivo/
 */


import java.io.*;
import javax.swing.text.*;

class Encrypt  {

   public static int MIN_KEY_SIZE = 8;


/*
    The encryption algorithm is based on Columnar Encryption. Imagine
    that the file contains the text "This is my text." and that the 
    encryption key is "my key". The first step is to arrange the 
    information in columns under the key. 

    my key
    ------
    This i
    s my t
    ext.

    The last row must be padded to complete it. I use new-line characters
    for padding.

    The next step is to sort the key. "my key" becomes " ekmyy". The columns
    under the key are also sorted in the same order (using the transformation 
    buffer).

     ekmyy
    ------
    i sThi
    m ys t
    t .ex

    The columns are now read out as rows. The encrypted text becomes:
    "imt   sy.Tseh xit "

    Of course, this is not good enough. For a short message such as above,
    a clever snoop might be able figure out the original text, and then 
    work out your key. In order to prevent that I've added an extra step.
    The program inverts some of the bits in each character using xor, to
    change the original characters.

----------------------------------------

    La kriptiga algoritmo baziøas sur Kolumna Kriptigo. Imagu ke 
    dosiero enhavas la tekston "Æi tio estas mia teksto." kaj ke la
    kriptiga þlosilo estas "poefago". La unua paþo estas aranøi la 
    tekston laý kolumnoj sub la þlosilo.

    poefago
    -------
    Æi tio 
    estas m
    ia teks
    to.    


    La lasta vico devas esti "faræita". Mi uzas novliniajn kodojn por faræi.

    La sekvanta paþo estas ordigi la þlosilon. "poefago" fariøas "aefgoop".
    Oni ordigas la kolumnojn sub la þlosilo laý la sama ordo.

    aefgoop
    -------
    i toi Æ
    sta sme
    e tkasi
     .  o t

    La kolumnoj nun elskribiøas kiel vicoj. La kriptigita teksto fariøas:
    "ise  t .tat o k isao ms Æeit" .

    Kompreneble, æi tio ne sufiæas. Por mallonga mesaøo kiel la supra, 
    lerta scivolemulo eble povus diveni la originalan tekston, kaj poste
    la þlosilon. Por malhelpi tion mi aldonis kroman paþon. La programo
    renversas iujn bitojn en æiu kodo uzante ekskluzivaý-on (xor), por
    þanøi la originalajn signojn.

*/



   public static char[] encrypt(char[] to_encrypt, char[] key) {
   
      char[]  the_key;       // la þlosilo
      int     key_size;      // ties longeco

      char[]  sorted_key;       // used for sorting / uzata por ordigo
      int[]   transformation;   // to keep track of the new positions of letters.
                                // por registri la novajn poziciojn de literoj.

      char[]  xor_buffer;     // Used to obfuscate individual characters.
                              // Por kaþi individuajn signojn.

      int     padding;        // Must pad the last row, if not complete.
                              // Necesas faræi la lastan vicon, se ne kompleta
      int     complete_rows;  // Number of complete (non-padded) rows in original text.
                              // Nombro da plenaj (nelongigitaj) vicoj en originala teksto.
      int i,j;

      char[]  encryption_buffer;
      char[]  encryption_buffer2;
      int     encryption_buffer_size;

      the_key = key;
      key_size = key.length;
      sorted_key     = new char[key_size];
      transformation = new int[key_size];

      complete_rows = to_encrypt.length / key_size;
      padding = key_size - (to_encrypt.length % key_size);
      if (padding == key_size) padding = 0;

      encryption_buffer_size  = to_encrypt.length + padding;
      encryption_buffer   = new char[encryption_buffer_size];
      encryption_buffer2  = new char[encryption_buffer_size];

      key_sort(the_key, sorted_key, transformation);

      // Now, sort the characters in the original text according to 
      // the order of the sorted key.
      // Nun, ordigu la kodojn de la originala teksto laý la ordo
      // de la ordigita þlosilo.

      int base = 0;
      for (j = 0; j < complete_rows; j++) {
         for (i = 0; i < key_size; i++) {
            encryption_buffer[base + i] = to_encrypt[base + transformation[i]];
         }
         base = base + key_size;
      }

      // Transform and pad last row.
      if (padding != 0) {
         char[] last_row = new char[key_size];
         i = 0;
         for (i = 0; i < key_size - padding; i++) {
            last_row[i] = to_encrypt[base + i];
         }
         for (; i < key_size; i++) {
            last_row[i] = '\n';
         }
         for (i = 0; i < key_size; i++) {
            encryption_buffer[base + i] = last_row[transformation[i]];
         }
      }

      // Copy columns of encryption_buffer to encryption_buffer2.
      // Kopiu kolumnojn de kriptobufro al kriptobufro2.

      j = 0;
      for (base = 0; base < key_size; base++) {
         for (i = 0; i < encryption_buffer_size; i = i + key_size) {
            encryption_buffer2[j] = encryption_buffer[base + i];
            j++;
         }
      }

      // Now xor everything in the encryption buffer.
      // Nun ekskluzivaýigi æion en la kriptigobufro.

      xor_buffer = new char[key_size];
      initialize_xor(the_key, xor_buffer);

      for (base = 0; base < encryption_buffer2.length; base = base + key_size) {
         for (int m = 0; m < key_size; m++) {
            encryption_buffer2[base + m] = (char)(encryption_buffer2[base + m] ^ xor_buffer[m]);
         }
      }

      return encryption_buffer2;

   }  // end of encrypt



   public static char[] decrypt(char[] encrypted_text, char[] key) {

      char[]  the_key;       // la þlosilo
      int     key_size;      // ties longeco

      char[]  sorted_key;       // used for sorting / uzata por ordigo
      int[]   transformation;   // to keep track of the new positions of letters.
                                // por registri la novajn poziciojn de literoj.

      int  number_of_rows, number_of_columns;  // nombroj da vicoj kaj kolumnoj

      int i,j,k;

      int     decryption_buffer_size = encrypted_text.length;
      char[]  decryption_buffer = new char[decryption_buffer_size];
      char[]  decryption_buffer2 = new char[decryption_buffer_size];

      the_key = key;
      key_size = key.length;
      number_of_rows = key_size;
      number_of_columns = encrypted_text.length / number_of_rows;

      sorted_key     = new char[key_size];
      transformation = new int[key_size];



      // First xor everything in the decryption buffer.
      // Unue ekskluzivaýigi æion en la malkriptigobufro.

      char[] xor_buffer = new char[key_size];
      initialize_xor(the_key, xor_buffer);

      for (int base = 0; base < decryption_buffer_size; base = base + key_size) {
         for (int m = 0; m < key_size; m++) {
            decryption_buffer2[base + m] = (char)(encrypted_text[base + m] ^ xor_buffer[m]);
         }
      }


      // Sort the encryption key. Ordigu la kriptigan bufron.
      key_sort(the_key, sorted_key, transformation);

      // Unsort the decryption buffer. Malordigu la malkriptigan bufron.
      int base, base2;
      for (i = 0; i < number_of_rows; i++) {
         base2 = i * number_of_columns;
         base  = transformation[i] * number_of_columns;
         for (j = 0; j < number_of_columns; j++) {
            decryption_buffer[base + j] = decryption_buffer2[base2 + j];
         }
      }


      // Now turn columns into rows. Nun þangý kolumnojn al vicoj.
      k = 0;
      for (i = 0; i < number_of_columns; i++) {
         for (j = i; j < encrypted_text.length; j = j + number_of_columns) {
            decryption_buffer2[k] = decryption_buffer[j];
            k++;
         }
      }

      return decryption_buffer2;

   }  // end of decrypt



   /* 
    * writeEncrypted - Encrypt the text in the segment using the key, and
    * write it out to the FileOutputStream.
    * writeEncrypted - Kriptigu la tekston en "segment" uzante "key", kaj
    * elskribu al dosiero.
    */

   public static void writeEncrypted(FileOutputStream fout, 
                                     Segment the_segment, 
                                     int seg_length, char[] key) 
                                     throws IOException {

      int   checksum;      // to check if la key is valid
                           // por kontroli æu la þlosilo validas

      char[] to_encrypt, encrypted_buffer;
      int lo, hi;

      to_encrypt = new char[seg_length];
      for (int i = 0; i < seg_length; i++) {
         to_encrypt[i] = (char)the_segment.array[i];
      }

      encrypted_buffer = encrypt(to_encrypt, key);
      int length = encrypted_buffer.length;

      checksum = calculate_checksum(key);

      //try {

         // Write our marker. Elskribu markilon.
         for (int i = 0; i < Detect.ENCRYPTION_MARKER.length; i++) {
            fout.write(Detect.ENCRYPTION_MARKER[i]);
         }

         // Write out key's checksum. Elskribu kontrolkodon por þlosilo.
         lo = checksum & 0xFF;
         hi = checksum / 0x100;
         fout.write(lo);
         fout.write(hi);
         //System.err.println("Checksum "  + calculate_checksum(key));

         // Write out the number of padded character. Use xor to disguise it.
         // Elskribu la nombron da "faræaj" signoj. Uzu eksluzivaý por maskigi øin.
         int padding =  encrypted_buffer.length - to_encrypt.length;
         padding = padding ^ key[0];
         fout.write(padding);

         for (int i = 0; i < length; i++) {
            lo = (int)encrypted_buffer[i] & 0xFF;         
            hi =  (int)encrypted_buffer[i] / 0x100;         
            fout.write(lo);
            fout.write(hi);
         }  // for

      //} catch ( IOException e ) {
      //   System.err.println
      //       ("Could not save file. Ne povis konservi dosieron. -G- " + 
      //       e.getMessage());
      //}  // try/catch

   }  // end of writeEncrypted




   /* 
    * decrypt2 - decrypts text in a byte buffer and loads it into a char buffer.
    * decrypt2 - Elkriptigas tekston en "byte" bufro kaj metas øin en "char" bufron.
    */

   public static char[] decrypt2(byte[] byte_buffer, char[] key) {

      char[] decryption_buffer;
      char[] final_buffer;

      int   lo, hi, padding;
      int   checksum;      // to check if la key is valid
                           // por kontroli æu la þlosilo validas

      int   checksum_from_file;   // kontrolkodo de dosiero.

      if (key == null) return null;
      if (byte_buffer.length < Detect.ENCRYPTION_MARKER.length + 3) return null;

      int   start_of_info = Detect.ENCRYPTION_MARKER.length + 3;
            // 2 for checksum. 1 for number of padding character.
            // 2 por kontrolkodon. 1 por la nombro da faræaj signoj.

      int   size_of_decryption_buffer;

      // Get key checksum from file.
      // Akiru kontrolkodon de dosiero.
      lo = (int)byte_buffer[Detect.ENCRYPTION_MARKER.length] & 0xFF;
      hi = (int)byte_buffer[Detect.ENCRYPTION_MARKER.length + 1] & 0xFF;
      checksum_from_file = hi * 0x100 + lo;

      // Get the amount of padding.
      // Akiru la nombron da faræaj signoj.
      padding = (int)byte_buffer[Detect.ENCRYPTION_MARKER.length + 2] & 0xFF;
      padding = padding ^ key[0];

      // If the checksum from the file doesn't match to checksum of the 
      // key, do not try to open the file.
      // Se la kontrolkodo de la dosiero ne kongruas kun la kontrolkodo
      // de la þlosilo, ne provu malfermi la dosieron.

      //System.err.println("Checksum " + checksum_from_file + "  " 
      //                    + calculate_checksum(key));
      if (checksum_from_file != calculate_checksum(key)) {
         System.err.println("Cannot decrypt file. Ne povas malkriptigi dosieron.");
         return null;
      }

      // Create a buffer for decryption. Kreu bufron por elkriptigo.
      size_of_decryption_buffer = (byte_buffer.length - start_of_info) / 2;
      decryption_buffer = new char[size_of_decryption_buffer];

      // Fill up the decryption buffer. Plenigu la malkriptan bufron.

      int i,j;
      for (i = start_of_info, j = 0; i < byte_buffer.length; i = i + 2, j++) {
         lo = (int)(byte_buffer[i] & 0x00FF);
         hi = (int)(byte_buffer[i+1] & 0x00FF);
         decryption_buffer[j] = (char)((hi * 0x100) + lo);
      }

      // Now, decrypt. Nun malkriptigu.
      decryption_buffer = decrypt(decryption_buffer, key);

      final_buffer = new char[size_of_decryption_buffer - padding];
      for (int m = 0; m < final_buffer.length; m++ ) {
         final_buffer[m] = decryption_buffer[m];
      }

      return final_buffer;

   }  // end of decrypt2





   /* 
    * calculate_checksum - Checksum for encryption key. 
    * calculate_checksum - Kontrolkodo por kriptoþlosilo.
    */

   private static int calculate_checksum(char[] key) {
      int checksum = 0;
      for (int k = 0; k < key.length; k++) {
         checksum = checksum + (key[k] & 0xFF);
      }
      checksum = checksum % 10000;
      return checksum;
   }



   /* 
    * key_sort - This method sorts the letters of the encryption key.
    * It also keeps track of the new letter positions by sorting the
    * buffer "transformation" in the same order.
    * key_sort - Æi tiu metodo ordigas la literojn de la kriptiga þlosilo.
    * Øi ankaý registras la novajn poziciojn de la literoj, ordigante la
    * bufron "transformation" laý la sama ordo.
    */

   private static void key_sort(char[] the_key, char[] sorted_key, int[] transformation) {

      int key_size = the_key.length;

      char temporary;
      int  temporary2;

      int difference;
      int index1, index2;
      int i,j,k;

      for (i = 0; i < key_size; i++) sorted_key[i] = the_key[i];

      for (i = 0; i < key_size; i++) transformation[i] = i;

      //System.err.println("before " + new String(sorted_key));
      // Sort the letters Ordigu la literojn.
      for (i = 0; i < key_size; i++) {

         for (j = 0; j < (key_size - i - 1); j++) {

            index1 = j;  
            index2 = index1 + 1;

            difference = sorted_key[index1] - sorted_key[index2];

            if (difference > 0) {
               // Exchange letters. Interþanøu literojn.
                temporary = sorted_key[index1];
                sorted_key[index1] = sorted_key[index2];
                sorted_key[index2] = temporary;

                temporary2 = transformation[index1];
                transformation[index1] = transformation[index2];
                transformation[index2] = temporary2;
            }
         }

      }  // end of for (i = 0 ...

      //System.err.println("after " + new String(sorted_key));

   }  // end of key_sort
 



   /* 
    * initialize_xor - Method to initialize the xor buffer from the encryption key.
    * initialize_xor - Metodo por pretigi la xor (ekskluzivaý ?) bufron de la þlosilo.
    */

   private static void initialize_xor(char[] the_key, char[] xor_buffer) {

      int[]  mask_number; // There will be 8 different xor masks.
                          // Estos 8 malsamaj ekskluzivaý-maskoj.

      int[]  xor_masks = {0x01F0, 0x010F, 0x0033, 0x0087,  // random  hazardaj
                          0x01A2, 0X00C6, 0x00A6, 0x0171};

      int key_size = the_key.length;
      mask_number = new int[key_size];

      for (int i = 0; i < key_size; i++) {
         mask_number[i] = the_key[i] & 0x0007;
      }

      for (int i = 0; i < key_size; i++) {
         xor_buffer[i] = (char)xor_masks[mask_number[i]];
      }

   }  // end of initialize_xor



}  // end of Encrypt

