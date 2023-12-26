// Kompaktigu - Por kompaktigi vortaron. Estas du pa�oj por krei 
// uzeblan vortaron por la programo Simredo. Oni komencas per la teksta 
// dosiero vortaro.txt . �i enhavas informojn pri radikoj kaj vortoj 
// en alfabeta ordo. Ekzemple:
// melopsit NOMO BIRDO N N KF NLM 3 R
// (La informoj estas: radiko, kategorio, signifo, transitiveco, 
// sen_fina�o, kun_fina�o, kunmeta�o, rareco, flago.) La unua pa�o por 
// krei vortaron estas kompaktigi la informojn en vortaro.txt . 
// "Kompaktigu" kompaktigas la literojn de la radiko, kaj konvertas la 
// radik-informojn al du-bajta entjero. Poste �i skribas la rezulton al
// dosiero kiu nomi�as vortaro.kom . La dua pa�o en kreado de vortaro, 
// estas ordigi la informojn de vortaro.kom en tabelojn (la� longeco de 
// la radikoj). La programo "Ordigu" faras tion. "Ordigu" elskribas la
// tabelojn, kaj indeksojn al la tabeloj al la dosiero vortaro.dat, kiu
// estas uzata per Simredo. 
// �i tiu klaso bazi�as sur la kodo de mia anta�a kontrolilo de 
// literumado KL3 (1994).
// Klivo     junio 1998
// 1998/marto  - StreamTokenizer nun bezonas "Reader"


import java.io.*;

// Noto: �ar mi uzas duuman ser�algoritmon por ser�i "vortaro_tekstoj"-n,
// la vortoj en la teksta vortaro (vortaro.txt) devas uzi X-metodon.
 
class Kompaktigu extends Vkomuna {

   private static String[]   vortaro_tekstoj = {
      "ADJEKTIVO",
      "ADVERBO",
      "ANIMALO",
      "AMFIBIO",
      "ARAKNIDO",
      "ARBO",
      "ARBUSTO",
      "ARMILO",
      "ARTIKOLO",
      "ASTRO",
      "BIRDO",
      "CXAMBRO",
      "DEZERTO",
      "EHXINODERMO",
      "ETNO",
      "FISXO",
      "GEOGRAFIO",
      "ILO",
      "INSEKTO",
      "INSULO",
      "INSULARO",
      "INTERJEKCIO",
      "KAP",
      "KF",
      "KOELENTERO",
      "KONJUNKCIO",
      "KONSTRUAJXO",
      "KONTINENTO",
      "KP",
      "KPP",
      "KRUSTULO",
      "LAGO",
      "LANDO",
      "LITERO",
      "LM",
      "LOKNOMO",
      "LOKO",
      "LUDILO",
      "MALLONGIGO",
      "MAMULO",
      "MANGXAJXO",
      "MARO",
      "MASXINO",
      "MEZURILO",
      "MITBESTO",
      "MITPERSONO",
      "MOLUSKO",
      "MONTARO",
      "MONTO",
      "MUZIKILO",
      "N",
      "NEKONATA",
      "NLM",
      "NOMO",
      "NOMOVERBO",
      "NUMERO",
      "OPTIKO",
      "P",
      "PARENCO",
      "PARTICIPO",
      "PERSONO",
      "POSTENO",
      "PREFIKSO",
      "PREPOZICIO",
      "PROFESIO",
      "PRONOMO",
      "PRONOMADJ",
      "PROVINCO",
      "RANGO",
      "REGANTO",
      "REGIONO",
      "RELPERSONO",
      "RELPOSTENO",
      "RELPROFESIO",
      "REPTILIO",
      "RIVERO",
      "S",
      "SF",
      "SUBJUNKCIO",
      "SUFIKSO",
      "SXTATO",
      "T",
      "TEHXPREFIKSO",
      "TITOLO",
      "URBO",
      "VERBO",
      "VERMO",
   };

   private static int[]   vortaro_kodoj   = {
      ADJEKTIVO,
      ADVERBO,
      ANIMALO,
      AMFIBIO,
      ARAKNIDO,
      ARBO,
      ARBUSTO,
      ARMILO,
      ARTIKOLO,
      ASTRO,
      BIRDO,
      �AMBRO,
      DEZERTO,
      EHXINODERMO,  // La �ava tradukilo ne �atas la literojn � kaj �.
      ETNO,
      FI�O,
      GEOGRAFIO,
      ILO,
      INSEKTO,
      INSULO,
      INSULARO,
      INTERJEKCIO,
      KAP,
      KF,
      KOELENTERO,
      KONJUNKCIO,
      KONSTRUAJXO,
      KONTINENTO,
      KP,
      KPP,
      KRUSTULO,
      LAGO,
      LANDO,
      LITERO,
      LM,
      LOKNOMO,
      LOKO,
      LUDILO,
      MALLONGIGO,
      MAMULO,
      MAN�AJXO,
      MARO,
      MA�INO,
      MEZURILO,
      MITBESTO,
      MITPERSONO,
      MOLUSKO,
      MONTARO,
      MONTO,
      MUZIKILO,
      N,
      NEKONATA,
      NLM,
      NOMO,
      NOMOVERBO,
      NUMERO,
      OPTIKO,
      P,
      PARENCO,
      PARTICIPO,
      PERSONO,
      POSTENO,
      PREFIKSO,
      PREPOZICIO,
      PROFESIO,
      PRONOMO,
      PRONOMADJ,
      PROVINCO,
      RANGO,
      REGANTO,
      REGIONO,
      RELPERSONO,
      RELPOSTENO,
      RELPROFESIO,
      REPTILIO,
      RIVERO,
      S,
      SF,
      SUBJUNKCIO,
      SUFIKSO,
      �TATO,
      T,
      TEHXPREFIKSO,
      TITOLO,
      URBO,
      VERBO,
      VERMO,
   };

   static int nombro_da_konstantoj;

   static File              vortarodosiero;
   static InputStream       enstrio;
   static StreamTokenizer   simbolstrio;
   static PrintStream       elstrio;


   static int bajto1, bajto2;  // Por teni kompaktigitan radikinformon.
   static Radiko radiko = new Radiko(new char[MAKS_RADIKLONGECO]);

   public Kompaktigu() {
   }

   public static void main (String args[]) {

      int kodo;
      int litero;
      int longeco, kr_longeco;

      if (args.length != 1) {
         System.out.println("Kompaktigu vortaron (por Simredo).");
         System.out.println("Uzo:\n  java Kompaktigu vortaro.txt");
         System.out.println("Chi tiu programo kreas dosieron \"vortaro.kom\".");
         System.out.println("Poste, uzu la programon \"Ordigu\" por krei \"vortaro.dat\" .");
         return;
      }

      nombro_da_konstantoj = vortaro_kodoj.length;

      if (!malfermu_vortaron(args[0])) return;
      if (!malfermu_kompaktan_vortaron()) return;

      try {

         int nombrilo = 0;

         while (true) {

            // Legu radikon a� vorton.
            if (simbolstrio.nextToken() == simbolstrio.TT_EOF) break;
            System.out.println(simbolstrio.sval);

            // Normigu la literojn kaj metu en "radiko"-n.
            longeco = simbolstrio.sval.length();
            for (int i = 0; i < longeco; i++) {
               litero = simbolstrio.sval.charAt(i);
               if (litero < 'a' || litero > 'z') {
                  System.out.println("Nevalida radiko.");
                  enstrio.close();
                  return;
               }
               radiko.literoj[i] = (char)(litero - 'a' + 1);  // Normigu.
            }
            radiko.longeco = longeco;
            kompaktigu(radiko);

            // Legu kategorion.
            if (simbolstrio.nextToken() == simbolstrio.TT_EOF) break;
            bajto1  = akiru_kodon(simbolstrio.sval);
            //kodo = akiru_kodon(simbolstrio.sval);
            //System.out.println(simbolstrio.sval + " = " + kodo);

            // Legu signifon.
            if (simbolstrio.nextToken() == simbolstrio.TT_EOF) break;
            bajto1  = (bajto1 << 4) | akiru_kodon(simbolstrio.sval);

            // Legu transitivecon.
            if (simbolstrio.nextToken() == simbolstrio.TT_EOF) break;
            bajto2  = akiru_kodon(simbolstrio.sval);

            // sen fina�o
            if (simbolstrio.nextToken() == simbolstrio.TT_EOF) break;
            bajto2  = (bajto2 << 1) | akiru_kodon(simbolstrio.sval);

            // kun fina�o
            if (simbolstrio.nextToken() == simbolstrio.TT_EOF) break;
            bajto2  = (bajto2 << 1) | akiru_kodon(simbolstrio.sval);

            // kunmeta�o
            if (simbolstrio.nextToken() == simbolstrio.TT_EOF) break;
            bajto2  = (bajto2 << 3) | akiru_kodon(simbolstrio.sval);

            // rareco - (0 - 4, Ne skribu al vortaro.kom)
            if (simbolstrio.nextToken() == simbolstrio.TT_EOF) break;

            bajto2  = (bajto2 << 2);

            // flago
            if (simbolstrio.nextToken() == simbolstrio.TT_EOF) break;

            kr_longeco = radiko.kr_longeco;

            // La flago X signifas, ke oni ne metu la informon en
            // la vortaron. La teksta vortaro registras radikojn 
            // kun samaj literoj kaj malsamaj vortinformoj, sed
            // la kontrolilo ne kapablas trakti tian situacion.
            // Ekzemple:
            // aktini NOMO ELEMENTO N N KF NLM 3 R
            // aktini NOMO KOELENTERO N N KF NLM 3 X
            // La unua radiko estas metata en la vortaron.
            if (simbolstrio.sval.charAt(0) != 'X' &&
                kr_longeco >= 1 && kr_longeco <= 9) {
               elstrio.print(kr_longeco);
               for (int i = 0; i < kr_longeco; i++) {
                  elstrio.print(" " + (int)radiko.kompakta_radiko[i] );
               }
               elstrio.println(" " + bajto1 + " " + bajto2);
               nombrilo++;
            }

         }  // fino de "while"

         System.out.println("Nombro da radikoj = " + nombrilo + ".");

         enstrio.close();
         elstrio.close();
      }
      catch ( IOException e )
      {
         System.out.println("Eraro dum legado de " + args[0] + 
                            " . " + e.getMessage());
      }
      System.out.println("\n >>>>>>>> finita <<<<<<<<<\n");
   }


   //////////////////////////////////////////////////////////////////////////////
   // La celo de "akiru_kodon()" estas konverti karaktran �enon al konstanto.
   // La metodo ser�as la tabelon "vortaro_tekstoj". Kiam �i trovas la �ustan tekston,
   // �i uzas la �ian indekson por akiri korespondan kodon el "vortaro_kodoj".
   // �i tiu metodo uzas duuman ser�algoritmon por trovi la tekston en "vortaro_teksto".

   static int akiru_kodon(String envorto) {

      int  malsupro,supro,mezo;
      int  komparrezulto;

      malsupro = 0;
      supro = nombro_da_konstantoj - 1;

      while (malsupro <= supro) {
         mezo = (supro + malsupro)/2;
         komparrezulto = envorto.compareTo(vortaro_tekstoj[mezo]);
         if (komparrezulto < 0)
            supro = mezo-1;
         else if (komparrezulto > 0)
            malsupro = mezo + 1;
         else {
            return (vortaro_kodoj[mezo]);  // Trovita
         }
      }
      return (0);  // Netrovita
   }  // fino de "akiru_kodon"


   ////////////////////////////////////////////////////////////////
   // Malfermu vortaron. - Malfermas tekstan vortaran dosieron kaj
   // kreas simbolstrion. Redonas "true" se sukcesa.

   static boolean malfermu_vortaron(String vortaronomo) {

      try {
         vortarodosiero = new File(vortaronomo);
      }
      catch (NullPointerException e) {
         System.out.println("Nomo de vortaro estas nevalida. ");
         return false;
      }

      if (vortarodosiero.exists()) {
      }
      else {
         System.out.println("Vortaro estas netrovebla.");
         return false;
      }
 
      try {
         enstrio = new FileInputStream(vortarodosiero);
         Reader r = new BufferedReader(new InputStreamReader(enstrio));
         simbolstrio = new StreamTokenizer(r);
      }
      catch ( IOException e )
      {
         System.out.println("Problemo okazis dum malfermo de vortaro. " + e.getMessage());
         return false;
      }

      return true;

   }  // fino de "malfermu_vortaron"


   ////////////////////////////////////////////////////////////////
   // Malfermu kompaktan vortaron. - Malfermas dosieron "vortaro.kom"
   // por eligi kompaktajn vortinformojn.
   // �i tiu metodo similas al la anta�a.

   static boolean malfermu_kompaktan_vortaron() {

      
      try {
         File kompakta_dosiero = new File("vortaro.kom");
         if (kompakta_dosiero.exists()) {
            kompakta_dosiero.delete();
         }
         elstrio = new PrintStream(new FileOutputStream(kompakta_dosiero));
      }
      catch (Exception e) {
         System.out.println("Ne povis krei kompaktan vortaron \"vortaro.kom\" . ");
         return false;
      }

      return true;

   }  // fino de "malfermu_kompaktan_vortaron"


}  // fino de "Kompaktigu"

