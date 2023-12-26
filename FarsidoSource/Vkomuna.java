////////////////////////////////////////////////////////////////////////
// Vkomuna - Æi tiu klaso entenas metodojn kiuj estas komunaj al 
// la klasoj kiuj rilatas al la vortaro kaj kontrolilo de literumado 
// por Simredo.
// Klivo   junio 1998
// februaro 1999 - Forigu Latinon 3 pro Simredo 3.
//               - Forigu "normigu_vorton"

abstract class Vkomuna extends Vkonstantoj {

   public Vkomuna () {
   }


// La algoritmoj en æi tiu dosiero estis verkata originale por la programo
// KL (Kontrolu Literumadon 1993/94). Tiutempe, mi zorgis pri komputila
// memoro. Estis iom grava, ke la vortaro estu malpli granda ol 64 kilobajtoj. 
// (Oni kulpigu Intel-ar¶itekturon.) Por la programo Simredo, komputila memoro 
// ne estas konsiderinda. Sed mi kredas, ke estas pli rapide seræi kompaktigitan
// vortaron. Oni povas kompari du literon per unu instrukcio. En Øava programo,
// rapideco estas grava problemo. Pro tio, mi uzas kompaktigan algoritmon en
// Simredo.

// Estas 256 kodoj en bajto; nur 26 literoj en la alfabeto (angla alfabeto). 
// Por kompaktigi vorton, oni povas uzi æiujn kodojn. En esperanta vorto, 
// vokaloj estas tre oftaj. Oni povas þanøi parojn de konsonanto kaj vokalo 
// al unu kodo. Iuj paroj de konsonantoj estas tre oftaj, ekz. sk, sm, st ... 
// Oni þanøu ilin ankaý al unu kodo.

// Kompaktiga Algoritmo
// La programo normigas la literoj de la enigita vorto. 'a' øis 'z' iøas
// 1 øis 26. Poste, la programo uzas la du normigitajn literojn kiel
// indekson en la tabelon de kodoj. Se la kodo el la tabelo ne estas nulo,
// øi estas metata en la kompaktigitan vorton. Se la kodo estas nulo, la
// programo metas la normigitajn literojn en la kompaktigitan vorton.

// La formo de tabelo "kodoj" estas jene:
//   ------------------------------->
//   | __ _a _b _c _d _e _f _g _h ... _z
//   | a_ aa ab ac ad ae af ag ah ... az
//   | b_ ba bb bc bd be bf bg bh ... bz
//   | c_ ca cb cc cd ce cf cg ch ... cz
//   | d_ da db dc dd de df dg dh ... dz
//   | .  .  .  .  .  .  .  .  .  ... .
//   | z_ za zb zc zd ze zf zg zh ... zz
//

// Maloftaj paroj, ekz. aa, cf, hj, rr... havas la kodon 0.
// Oftaj paroj, ekz. ab, ac, ad, ba, ca, da, kl, kr... havas alian kodon.
// La kodoj devas esti super 26 kaj sub 256 (1 øis 26 estas por ne
// kodigitaj literoj). La ordo de la kodoj estas arbitra; la enkodigitaj 
// vortoj devas esti reordigitaj poste. La tabelo devas ne havi la saman 
// kodon dufoje (escepte de 0).

// Per æi tiu metodo, la kompaktigo estas sufiæe rapida kaj atingas
// preskaý duonon de la originala grandeco.

   final static int[] kodoj = {
/*    _ a b c d e f g h i j k l m n o p q r s t u v w x y z */
/*_*/ 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
/*a*/ 0,0,27,28,29,0,30,31,32,0,33,34,35,36,37,0,38,0,39,40,41,42,43,0,0,0,44,
/*b*/ 0,45,0,0,0,46,0,0,0,47,0,0,205,0,0,48,0,0,206,0,0,49,0,0,0,0,0,
/*c*/ 0,50,0,0,0,51,0,0,0,52,0,0,0,0,0,53,0,0,0,0,0,54,0,0,207,0,0,
/*d*/ 0,55,0,0,0,56,0,0,0,57,0,0,0,0,0,58,0,0,208,0,0,59,0,0,0,0,0,
/*e*/ 0,0,60,61,62,0,63,64,65,0,66,67,68,69,70,0,71,0,72,73,74,75,76,0,0,0,77,
/*f*/ 0,78,0,0,0,79,0,0,0,80,0,0,209,0,0,81,0,0,210,0,0,82,0,0,0,0,0,
/*g*/ 0,83,0,0,0,84,0,0,0,85,0,0,211,0,0,86,0,0,212,0,0,87,213,0,177,0,0,
/*h*/ 0,88,0,0,0,89,0,0,0,90,0,0,0,0,0,91,0,0,0,0,0,92,0,0,214,0,0,
/*i*/ 0,0,93,94,95,0,96,97,98,0,99,100,101,102,103,0,104,0,105,106,107,0,108,0,0,0,109,
/*j*/ 0,110,0,0,0,111,0,0,0,112,0,0,0,0,0,113,0,0,0,0,0,114,0,0,215,0,0,
/*k*/ 0,115,0,0,0,116,0,0,0,117,0,0,216,0,217,118,0,0,218,219,0,119,220,0,0,0,0,
/*l*/ 0,120,0,0,236,121,237,238,0,122,0,239,0,240,241,123,242,0,0,243,244,124,0,0,0,0,0,
/*m*/ 0,125,0,0,0,126,0,0,0,127,0,0,0,0,0,128,251,0,0,0,0,129,0,0,0,0,0,
/*n*/ 0,130,0,0,221,131,0,222,0,132,0,223,0,0,0,133,0,0,0,252,224,134,0,0,0,0,0,
/*o*/ 0,0,135,136,137,0,138,139,140,0,141,142,143,144,145,0,146,0,147,148,149,0,150,0,0,0,151,
/*p*/ 0,152,0,0,0,153,0,0,0,154,0,0,225,0,0,155,0,0,226,0,0,156,0,0,0,0,0,
/*q*/ 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
/*r*/ 0,157,253,0,227,158,254,0,0,159,0,245,0,246,247,160,248,0,0,249,228,161,0,0,0,0,0,
/*s*/ 0,162,0,0,0,163,0,0,0,164,0,229,230,231,232,165,233,0,0,0,234,166,255,0,250,0,0,
/*t*/ 0,167,0,0,0,168,0,0,0,169,0,0,0,0,0,170,0,0,235,0,0,171,0,0,0,0,0,
/*u*/ 0,0,172,173,174,0,175,176,0,0,178,179,180,181,182,0,183,0,184,185,186,0,187,0,188,0,189,
/*v*/ 0,190,0,0,0,191,0,0,0,192,0,0,0,0,0,193,0,0,0,0,0,194,0,0,0,0,0,
/*w*/ 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
/*x*/ 0,195,0,0,0,196,0,0,0,197,0,0,0,0,0,198,0,0,0,0,0,199,0,0,0,0,0,
/*y*/ 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
/*z*/ 0,200,0,0,0,201,0,0,0,202,0,0,0,0,0,203,0,0,0,0,0,204,0,0,0,0,0,
   };

   public static void kompaktigu(Radiko radiko) {
      // "radiko.literoj" enhavas normigitan vorton.

      int   n1,n2;   // øeneralaj nombriloj
      int   longeco2 = radiko.longeco - 1;
      char  kodo = 0;

      n1 = radiko.indekso;
      n2 = 0;

      int i;
      for (i = 0; i < longeco2; i = i + 2) {
         // Akiru kodo el la tabelo de kodoj.
         kodo = (char)kodoj[((radiko.literoj[n1]*27) + radiko.literoj[n1+1])];
         if (kodo != 0) {
            radiko.kompakta_radiko[n2] = kodo;
            n1 = n1 + 2;
            n2++;
         }
         else {   // Se kodo ne estis trovata... 
            radiko.kompakta_radiko[n2] = radiko.literoj[n1];
            n1++; n2++;
            radiko.kompakta_radiko[n2] = radiko.literoj[n1];
            n1++; n2++;
         }
      }  // fino de "for"

      if (i == longeco2) {
         radiko.kompakta_radiko[n2] = radiko.literoj[n1]; // Enmetu la lastan literon. 
         n2++;
      }

      radiko.kr_longeco = n2;

   }  // fino de "kompatkigu"



}  // fino de "Vkomuna"




