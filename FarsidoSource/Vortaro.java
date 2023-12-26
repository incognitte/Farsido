// Vortaro - Æi tiu klaso havas metodojn kiuj traktas la vortaron 
// por la programo Simredo. Øi havas metodojn por legi kaj seræi la 
// vortaron. Øi estas adaptitaj de mia programo KL por DOS.
// versio 1.0, junio 1998
// februaro 1999 - Forigu Latinon 3 por Simredo 3.
// marto 1999 - vortaro.dat estas en la dosierujo class_path
// Klivo

////////////////////////////////////////////////////////////////////////
// Pri la vortaro 
// Estas du paþoj por krei vortaron. Oni komencas per la teksta dosiero 
// vortaro.txt . Øi enhavas informojn pri radikoj kaj vortoj en alfabeta 
// ordo. Ekzemple:
// melopsit NOMO BIRDO N N KF NLM 3 R
// (La informoj estas: radiko, kategorio, signifo, transitiveco, 
// sen_fina¼o, kun_fina¼o, kunmeta¼o, rareco, flago.) La unua paþo por 
// krei vortaron estas kompaktigi la informojn per la programo "Kompaktigu".
// "Kompaktigu" skribas la kompatigitajn informojn al dosiero kiu nomiøas 
// vortaro.kom . 
// En vortaro.txt, radiko povas havi inter 2 kaj 18 literoj. (La plej longa
// radiko estas otorinolaringologi.) Post kompaktigo, la longeco estas 
// inter 1 kaj 9. La radikinformoj kompaktiøas al du bajtoj. Pro tio, 
// vortaro.kom enhavas liniojn de inter 3 kaj 11 bajtoj. La dua paþo en 
// kreado de vortaro estas ordigi la informojn de vortaro.kom en tabelojn. 
// La programo "Ordigu" faras tion, kreante dosieron vortaro.dat . Estas naý 
// tabeloj en vortaro.dat, laý la longeco de la kompaktigitaj radikoj. 
// Æiuj informoj por radikoj de longeco 1 (post kompaktigo) estas kunaj. 
// Simile estas pri radikoj de longeco 2, 3, ktp. Ene de æiu tabelo, la 
// bajtoj estas ordigitaj de malgrandaj numeroj al grandaj. Tio ebligas 
// uzadon de duuma seræalgoritmo ene de tabelo. Vortaro.dat devas indiki 
// la bazan indekson, kaj nombron da elementoj en æiu tabelo. Tiu informo 
// estas æe la komenco de la dosiero. La unua entjero estas indekso al la 
// unua tabelo.
// Jen formato de vortaro.dat .
//
// baza indekso por tabelo 1
// baza indekso por tabelo 2
// ktp, øis tabelo 9
// nombro da rikordoj en tabelo 1
// nombro da rikordoj en tabelo 2
// ktp, øis tabelo 9
// tabelo 1 ...
// tabelo 2 ...
// :
// :
// tabelo 9 ...
//////////////////////////////////////////////////////////////////////////

import java.io.*;
import java.net.*;

class Vortaro extends Vkomuna {

   public boolean vortaro_shargita = false;
   private static byte[]  bufro;   // Entenas datenoj de vortaro.dat .

   File    vortarodosiero;
   int     grandeco_de_vortaro;

   int[]   tabelo_indekso      = new int[10];   // indeksoj al tabeloj en vortaro.dat
   int[]   nombro_da_rikordoj  = new int[10];   // nombro en æiu tabelo de vortaro.dat
   
   public Vortaro () {
      shargu_vortaron(null,"vortaro.dat");
   }

   public Vortaro (String nomo) {
      shargu_vortaron(null, nomo);
   }

   public Vortaro (URL urlo, String nomo) {
      shargu_vortaron(urlo, nomo);
   }

   /////////////////////////////////
   // shargu vortaron.

   public void shargu_vortaron(URL urlo, String vortaronomo) {

      grandeco_de_vortaro = 60000;
      String class_path, temp;

      InputStream    enstrio;

      try {
         if (urlo == null) {   // Ne estas en la reto.

            // Get the class path. We need this to find vortaro.dat .
            // Trovu la klasdosierujon. Ni bezonas øin por trovi vortaro.dat .
            class_path = System.getProperty("java.class.path");
            try {
               File thedir = new File(class_path);
               if (!thedir.isDirectory()) {
                  class_path = thedir.getParent();
               }
            } catch (NullPointerException npe) {
               class_path = ".";
            }
            if (class_path == null) class_path = ".";

            vortarodosiero = new File(class_path, vortaronomo);
            if (vortarodosiero.exists()) {
               grandeco_de_vortaro = (int)vortarodosiero.length();
            }
            else {
               System.err.println("Ne povas trovi vortaron (vortaro.dat) .");
               vortaro_shargita = false;
               return;
            }
            enstrio = new FileInputStream(vortarodosiero);
         }
         else {  // Por retprogramoj
            enstrio = urlo.openStream();
         }

         bufro = new byte[grandeco_de_vortaro];
         enstrio.read(bufro);

         int j = 0;
         for (int i = 0; i < MAKS_LONG_KOMP_RAD; i++) {
            tabelo_indekso[i] = kreu_entjeron(j*4);
            j++;
         }
         for (int i = 0; i < MAKS_LONG_KOMP_RAD; i++) {
            nombro_da_rikordoj[i] = kreu_entjeron(j*4);
            j++;
         }
         enstrio.close();
      }
      catch ( IOException e )
      {
         System.err.println("Problemo okazis dum legado de vortaro. " + e.getMessage());
         vortaro_shargita = false;
         return;
      }
      catch (NullPointerException e) {
         System.err.println("Nomo de vortaro estas nevalida. ");
         vortaro_shargita = false;
         return;
      }


      // Kontrolu, æu la vortaro estas øusta.
      // La lasta bajto de la naýa tabelo devas esti la 
      // lasta bajto de la dosiero.
      //int fino_de_vortaro = tabelo_indekso[MAKS_LONG_KOMP_RAD - 1] +
      //       (nombro_da_rikordoj[MAKS_LONG_KOMP_RAD - 1] * 
      //       (MAKS_LONG_KOMP_RAD + 2));
      //if (fino_de_vortaro != grandeco_de_vortaro) {
      //   System.err.println("Vortaro estas difektita. 2 ");
      //   vortaro_shargita = false;
      //   return;
      //}

      vortaro_shargita = true;

   }  // fino de "shargu_vortaron"



   /////////////////////////////////////////////////////////////
   // seræu - La metodo ricevas radiko-objekton kiu enhavas nur
   // literojn kaj longecon de seræota radiko. Øi poste seræas 
   // en la vortaron por la radiko. Se øi trovas øin, la metodo
   // plenigas la aliajn kampojn de  "radiko". Se ne, øi markas 
   // la radikon netrovita. Æi tiu metodo ne kreas radiko-objektojn. 
   // Kreado de objektoj estas tro temporaba.
   // Notu, ke la literoj en la radiko-objekto estas normigitaj.

   public boolean seræu(Radiko radiko) {

      if (radiko.longeco > MAKS_RADIKLONGECO) return false;

      kompaktigu(radiko);

      int kompakta_longeco = radiko.kr_longeco;

      if (kompakta_longeco > MAKS_LONG_KOMP_RAD) return false;

      seræu2(radiko, 
                tabelo_indekso[kompakta_longeco - 1],
                   nombro_da_rikordoj[kompakta_longeco - 1]);

      return radiko.trovita;

   }  // fino de "seræu".

   /////////////////////////////////////////////////////////////////////
   // seræu2 - æi tiu metodo estas duuma seræalgoritmo por trovi radikon
   // en tabelo de la vortaro. Bazo enhavas indekson al la bazo de la 
   // tabelo en "bufro". Nombro estas la nombro da rikordoj en tiu tabelo.

   private final void seræu2(Radiko radiko, int bazo, int nombro)
   {

      int  malsupro,supro,mezo;
      int  komparrezulto;
      int  vortaroindekso;
      int  datenlongeco;      // longeco de kompakta radiko plus radikinformoj
      int  komp_longeco;      // longeco de kompakta radiko aý vorto
      int  i;
      byte radikinformo1, radikinformo2;

      komp_longeco = radiko.kr_longeco;

      malsupro = 0;
      supro = nombro - 1;
      datenlongeco = komp_longeco + 2;

      while (malsupro <= supro) {
         mezo = (supro + malsupro)/2;
         vortaroindekso = bazo+(mezo*datenlongeco);
         for (i = 0, komparrezulto = 0; 
             (i < komp_longeco) && (komparrezulto == 0); i++)
             komparrezulto = (radiko.kompakta_radiko[i] - 
                              (bufro[vortaroindekso + i] & 0xFF));

         if (komparrezulto < 0)
            supro = mezo-1;
         else if (komparrezulto > 0)
            malsupro = mezo+1;
         else {
            // La algoritmo trovis la radikon.

            radikinformo1 = bufro[vortaroindekso + komp_longeco];
            radikinformo2 = bufro[vortaroindekso + komp_longeco + 1];

            radiko.kategorio =    (radikinformo1 >> 4) & 0xF;
            radiko.signifo =      (radikinformo1 & 0xF);

            radiko.transitiveco =  (radikinformo2 >> 7) & 1;
            radiko.sen_finajxo =   (radikinformo2 >> 6) & 1;
            radiko.kun_finajxo =   (radikinformo2 >> 5) & 1;
            radiko.kunmetajxo  =   (radikinformo2 >> 2) & 7;
            radiko.rareco =        (radikinformo2 & 3);

            radiko.trovita = true;
            return;
         }

      }  // fino de "while"

      radiko.trovita = false;
      return;

   }  // fino de "seræu2"

   // Kreu entjeron el 4 bajtoj de la bufro.
   int kreu_entjeron(int indekso) {
      int ent1, ent2, ent3, ent4;
      ent1 = bufro[indekso] & 0xFF;
      ent2 = bufro[indekso + 1] & 0xFF;
      ent3 = bufro[indekso + 2] & 0xFF;
      ent4 = bufro[indekso + 3] & 0xFF;
      return (ent1 * 0x1000000) + (ent2 * 0x10000) + (ent3 * 0x100) + ent4;
   }

   public static void shargu_klason() {
   }

} // fino de "Vortaro"





