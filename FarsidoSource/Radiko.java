// Radiko - Æi tiu klaso entenas informojn pri radiko el vortaro.
// Klivo   junio 1998

class Radiko extends Vkonstantoj {

   public boolean  trovita;    // Indikas æu la literoj estis trovataj
                               // en la vortaro.

   public char[]  literoj;    // literoj de la radiko
   public int     indekso;    // indekso al komenco de radiko en "literoj"
   public int     longeco;    // longeco de radiko en "literoj"

   public int[]   kompakta_radiko;  // radiko post kompaktigo
   public int     kr_longeco;

   public int kategorio;       // NOMO, VERBO ktp.
   public int signifo;         // PERSONO, BIRDO ktp.
   public int transitiveco;    // TRANSITIVA aý NETRANSITIVA
   public int sen_finajxo;     // radiko povas stari sen fina¼o, ekz.  antaý
   public int kun_finajxo;     // radiko povas havi fina¼on, ekz.      antaýa
   public int kunmetajxo;      // radiko povas esti en kunemta¼o, ekz  malantaý
   public int rareco;          // rareco de la radiko 0 - 4. 4 estas rara.

   public    Radiko (char[] lit) {
      literoj =  lit;
      indekso =  0;
      longeco =  0;
      trovita    = false;
      kompakta_radiko    = new int[MAKS_LONG_KOMP_RAD*2];
      // Radiko estas maksimume 18 karaktrojn longa. Kompakta radiko
      // estas maksimume 9. Sed la vektoro "kompakta_radiko" devas esti 
      // pli granda ol 9, æar oni ne necese povas kompaktigi 18 karaktran
      // radikon en  9 bajtajn. Do, mi aldonas kroman spacon (9*2). 
   }

}  // fino de "Radiko"


