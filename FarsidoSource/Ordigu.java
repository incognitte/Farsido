// Ordigu - Ordigu Vortaron. Estas du paþoj por krei uzeblan 
// vortaron por la programo Simredo. Oni komencas per la teksta 
// dosiero vortaro.txt . Øi enhavas informojn pri radikoj kaj vortoj 
// en alfabeta ordo. Ekzemple:
// melopsit NOMO BIRDO N N KF NLM 3 R
// (La informoj estas: radiko, kategorio, signifo, transitiveco, 
// sen_fina¼o, kun_fina¼o, kunmeta¼o, rareco, flago.) La unua paþo por 
// krei vortaron estas kompaktigi la informojn en vortaro.txt . 
// La programo "Kompaktigu" kompaktigas la literojn de la radiko, kaj
// konvertas la radik-informojn al du-bajta entjero. Poste øi skribas 
// la rezulton al dosiero kiu nomiøas vortaro.kom . La dua paþo en kreado
// de vortaro, estas ordigi la informojn de vortaro.kom en tabelojn (laý
// longeco de la radikoj). La programo "Ordigu" faras tion. "Ordigu"
// elskribas la tabelojn, kaj indeksojn al la tabeloj al la dosiero 
// vortaro.dat, kiu estas uzata per Simredo. 
// Æi tiu klaso baziøas sur la kodo de mia antaýa kontrolilo de 
// literumado KL3.
// Klivo     julio 1998
// 1998/marto  - Forigu Latin-3 supersignojn. La øava tradukilo ne þatas ilin.
//             - StreamTokenizer nun bezonas "Reader"

import java.io.*;

class Ordigu extends Vkomuna {

   static InputStream       enstrio;
   static StreamTokenizer   simbolstrio;
   static RandomAccessFile  elstrio;

   static int  longeco;       // Longeco de radiko.

   static int[]    tabelo_indekso     = new int[MAKS_LONG_KOMP_RAD];
   static int[]    nombro_da_rikordoj = new int[MAKS_LONG_KOMP_RAD];
   static Bufro[]  la_tabeloj         = new Bufro[MAKS_LONG_KOMP_RAD];

   public Ordigu() {
   }

   public static void main (String args[]) {

      System.out.println("Ordigu - Programo por ordigi vortaron (por Simredo).");
      System.out.println("Uzu \"Kompaktigu\" unue.");
      System.out.println("\"Ordigu\" kreas dosieron \"vortaro.dat\".");
      System.out.println("Klivo 1998");

      if (!kreu_ord()) return;     // Kreu ordigitan vortaron.

      // Kreu bufrojn por la tabeloj. Estas bufro por æiu radiklongeco 
      // de 1 øis la maksimuma kompakta radiklongeco.
      // Bufro kapablas kreski.
      la_tabeloj[0] = new Bufro(new char[600]);  
      la_tabeloj[1] = new Bufro(new char[12000]);  
      la_tabeloj[2] = new Bufro(new char[26000]);  
      la_tabeloj[3] = new Bufro(new char[21000]);  
      la_tabeloj[4] = new Bufro(new char[11000]);  
      la_tabeloj[5] = new Bufro(new char[6000]);  
      la_tabeloj[6] = new Bufro(new char[2000]);  
      la_tabeloj[7] = new Bufro(new char[600]);  
      la_tabeloj[8] = new Bufro(new char[300]);  

      for (int i = 0; i < MAKS_LONG_KOMP_RAD; i++) {
         longeco = i + 1;
         System.out.println("Tabelo " + i );
         plenigu_tabelon(la_tabeloj[i], longeco);
         ordigu_tabelon(la_tabeloj[i], nombro_da_rikordoj[i], longeco);
      }

      // Kalkulu indeksojn por la dosiero "vortaro.dat" .
      // En la komenco de la dosiero estas tabelo kiu enhavas indeksojn
      // al æiu tabelo de radikoj (1 øis 9), kaj la nombron de rikordoj 
      // en æiu tabelo.
      // Estas 4 bajtoj en æiu entjero.
      tabelo_indekso[0] = (MAKS_LONG_KOMP_RAD * 4) * 2; // indeksoj kaj nombroj
      int antaua_bazo, antaua_nombro, antaua_grandeco;
      for (int i = 1; i < MAKS_LONG_KOMP_RAD; i++) {
         antaua_bazo = tabelo_indekso[i-1];
         antaua_nombro = nombro_da_rikordoj[i-1];
         antaua_grandeco = i + 2;
         tabelo_indekso[i] = antaua_bazo + (antaua_nombro * antaua_grandeco); 
      }

      try {
         for (int i = 0; i < MAKS_LONG_KOMP_RAD; i++) {
            elstrio.writeInt(tabelo_indekso[i]);
         }
         for (int i = 0; i < MAKS_LONG_KOMP_RAD; i++) {
            elstrio.writeInt(nombro_da_rikordoj[i]);
         }
         for (int i = 0; i < MAKS_LONG_KOMP_RAD; i++) {
            System.out.println("Tabelo : " + i);
            elskribu_tabelon(la_tabeloj[i]);
         }
         elstrio.close();
      }
      catch ( IOException e) {
         System.out.println("Eraro dum elskribo de vortaro. " + e.getMessage());
      }

      System.out.println("\n >>>>>>>> finita <<<<<<<<<\n");

   }  // fino de "main"


   ////////////////////////////////////////////////////////////////
   // Kreu ordigitan vortaron. - Kreas la dosieron "vortaro.dat".
   static boolean kreu_ord() {

      try {
         File ordigita_vortaro = new File("vortaro.dat");
         if (ordigita_vortaro.exists()) {
            ordigita_vortaro.delete();
         }
         elstrio = new RandomAccessFile(ordigita_vortaro, "rw");
         elstrio.seek((long)0);
      }
      catch (Exception e) {
         System.out.println("Ne povis krei ordigitan vortaron \"vortaro.dat\" . ");
         return false;
      }

      return true;

   }  // fino de "kreu_ord"

   /////////////////////////////////////////////////////////////////
   // Plenigu tabelon. - Seræu la dosieron por informoj de la øusta
   // longeco. Enmetu la informon en tabelon.
   static void plenigu_tabelon(Bufro tabelo, int radiklongeco) {

      int indekso = 0;
      int nombro = 0;

      if (!malfermu_kom()) {
         System.out.println("Ne povis malfermi \"vortaro.kom\".");
         return;  // Malfermu kompaktan vortaron.
      }

      Reader r = new BufferedReader(new InputStreamReader(enstrio));
      simbolstrio = new StreamTokenizer(r);      
      try { 
         while (true) {
            tabelo.kresku(indekso + 20);
            if (simbolstrio.nextToken() == simbolstrio.TT_EOF) break;
            int nval = (int)simbolstrio.nval;
            if (nval == radiklongeco) {
               // Enmetu la informojn.
               for (int i = 0; i < radiklongeco + 2; i++, indekso++) {
                  simbolstrio.nextToken();
                  tabelo.bufro[indekso] = (char)simbolstrio.nval;
               }
               nombro++;
            }
            else {
              if (nval > MAKS_LONG_KOMP_RAD) 
                 System.out.println("Serioza problemo. " + nval);
               // Preterpasu la informojn.
               for (int i = 0; i < nval + 2; i++) simbolstrio.nextToken();
            }
         }
         enstrio.close();
      }
      catch (IOException e) {
         System.out.println(" Serioza problemo dum legado de \"vortaro.kom\".");
      }
      tabelo.nombro = indekso;
      nombro_da_rikordoj[radiklongeco-1] = nombro;
      System.out.println("Grandeco de tabelo: " + indekso + 
                         "  Nombro da radikoj: " + nombro);

   }  // fino de "plenigu_tabelon"


   /////////////////////////////////////////////////////////////////
   // Metodo por ordigi tabelon.
   static char[] portempa = new char[100];
   static void ordigu_tabelon(Bufro tabelo, int nombro_en_tabelo, int longeco_de_radiko) {

      int komparrezulto;
      int indekso1, indekso2;
      int i,j,k;
      int longeco_de_rikordo = longeco_de_radiko + 2;  // + 2 por du bajtoj da informo

      System.out.println("Nun ordigas. Bonvolu atendi.");
      // Ordigu la tabelon.
      for (i = 0; i < nombro_en_tabelo; i++) {

         for (j = 0; j < (nombro_en_tabelo - i - 1); j++) {

            indekso1 = j * longeco_de_rikordo;  
            indekso2 = indekso1 + longeco_de_rikordo;

            // komparu
            for (k = 0, komparrezulto = 0;
                (k < longeco_de_radiko) && (komparrezulto == 0); k++) {
                 komparrezulto = (tabelo.bufro[indekso1 + k] - tabelo.bufro[indekso2 + k]);
            }
         
            if (komparrezulto > 0) {
               // Interþanøu rikordojn.
               for (k = 0; k < longeco_de_rikordo; k++) 
                   portempa[k] = tabelo.bufro[indekso1 + k];
               for (k = 0; k < longeco_de_rikordo; k++) 
                   tabelo.bufro[indekso1 + k] = tabelo.bufro[indekso2 + k];
               for (k = 0; k < longeco_de_rikordo; k++) 
                   tabelo.bufro[indekso2 + k] = portempa[k];
            }
         }
      }  // fino de "for (i = 0"
   }  // fino de "ordigu_tabelon"
 

   ////////////////////////////////////////////////////////////////
   // Malfermu kompaktan vortaron. - Malfermas kompaktan vortaran 
   // dosieron kaj kreas simbolstrion. Redonas "true" se sukcesa.

   static boolean malfermu_kom() {

      File  komp_vortaro;

      try {
         komp_vortaro = new File("vortaro.kom");
      }
      catch (NullPointerException e) {
         return false;
      }

      if (!komp_vortaro.exists()) {
         System.out.println("\"Vortaro.kom\" estas netrovebla.");
         return false;
      }
 
      try {
         enstrio = new FileInputStream(komp_vortaro);
      }
      catch ( IOException e ) {
         System.out.println("Problemo okazis dum malfermo de vortaro.kom . " + e.getMessage());
         return false;
      }

      return true;

   }  // fino de "malfermu_kom"


   ///////////////////////////////////////////////
   // Elskribu tabelon.
   static void elskribu_tabelon(Bufro tabelo) throws IOException {

      int nombro = tabelo.nombro;

      for (int i = 0; i < nombro;  i++) {
         elstrio.write((int)tabelo.bufro[i]);         
      }
   }  // fino de "elskribu_tabelon"


}  // fino de "Ordigu"

