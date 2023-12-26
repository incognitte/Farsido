// Vkonstantoj - Æi tiu klaso entenas konstantojn kiuj 
// estas komunaj al la klasoj kiuj rilatas al la vortaro kaj 
// kontrolilo de literumado por Simredo.
// Klivo   junio 1998


// La Øava tradukilo (javac) ne þatas la literojn ¬ kaj ¦. Pro
// tio mi devas uzi JX kaj HX.

abstract class Vkonstantoj {

   public final static int  NE  = 0;
   public final static int  JES = 1;

   public final static int  FALSA  = 0;
   public final static int  VERA   = 1;

   ///////////////////////////////////////////////////////////////
   // Maksimumoj
   public final static int  MAKS_VORTLONGECO  = 40;    // 40 literoj 
   public final static int  MAKS_RADIKLONGECO = 18;    // 18 literoj 

   // maksimuma longeco de kompakta radiko 
   public final static int  MAKS_LONG_KOMP_RAD = 9;
   
   // maksimuma nombro da radikoj en kunmetita vorto
   public final static int  MAKS_RAD_EN_KM_VORTO = 7;  

   /////////////////////////////////////////////////////////////
   // Sekvas valoroj uzataj por kontroli la sintezon de radikoj.

   public final static int  NELASTA = 0;    // nelasta radiko en kunmeta¼o
   public final static int  LASTA   = 1;    // lasta radiko en kunmeta¼o

   // kategorio de radiko, ordo tre gravas

   public final static int  NOMO        = 0;
   public final static int  NOMOVERBO   = 1;   // agas kiel nomo aý verbo 
   public final static int  VERBO       = 2;
   public final static int  ADJEKTIVO   = 3; 
   public final static int  NUMERO      = 4; 
   public final static int  ADVERBO     = 5;   // primitiva adverbo 
   public final static int  PRONOMO     = 6;
   public final static int  PRONOMADJ   = PRONOMO;  // pronom-adjektivo (tiu)
   public final static int  PREPOZICIO  = 7;
   public final static int  KONJUNKCIO  = 8;
   public final static int  SUBJUNKCIO  = 8;
   public final static int  INTERJEKCIO = 8;
   public final static int  PREFIKSO    = 9;
   public final static int  TEHXPREFIKSO = 10;       // te¶nika prefikso
   public final static int  SUFIKSO     = 11;
   public final static int  ARTIKOLO    = 12;
   public final static int  PARTICIPO   = 13;
   public final static int  MALLONGIGO  = 14;
   public final static int  LITERO      = 15;

   // signifo de radiko (nur tiuj signifoj uzataj por kontroli sintezon)

   public final static int  NEKONATA    =  0;
   public final static int  PARENCO     =  1;
   public final static int  ETNO        =  2;
   public final static int  PERSONO     =  3;
   public final static int  ANIMALO     =  4;
   public final static int  ILO         =  5;
   public final static int  LOKO        =  6;
   public final static int  LOKNOMO     =  7;
   public final static int  ARBO        =  8;

   // transitiveco de radiko 

   public final static int  NETRANSITIVA = 0;
   public final static int  NETRANS      = 0;
   public final static int  TRANSITIVA   = 1;
   public final static int  TRANS        = 1;

   ////////////////////////////////////////////////////////////////
   // kunmeta¼oj - Difinas, kiel la radikoj funkcias en kunmeta¼oj.

   public final static int  LIMIGITA        = 1;  // kunmetebleco limigita
   public final static int  NELIMIGITA      = 2;    
   public final static int  KIEL_PREFIKSO   = 3;  // radiko estas uzata kiel prefikso 
   public final static int  KIEL_SUFIKSO    = 4;  // radiko estas uzata kiel sufikso 
   public final static int  KIEL_PART       = 5;  // kiel participo
   public final static int  KIEL_PREP_PREF  = 6;  // kiel prepozicia prefiksoido
   public final static int  KIEL_ADV_PREF   = 7;  // kiel adverba prefiksoido

   ///////////////////////////////////////////////////////////////////////
   // Difinoj de konstantoj por kompilado de la vortaro.

   public final static int  N     = NE;        // ne, nekonata, netransitiva

   public final static int  T     = TRANSITIVA;

   public final static int  SF    = VERA;     // sen fina¼o 
   public final static int  KF    = VERA;     // kun fina¼o 

   // kunmeta¼oj (mallongigoj) 
   public final static int  LM    = LIMIGITA;         // limigita 
   public final static int  NLM   = NELIMIGITA;       // nelimigita 
   public final static int  P     = KIEL_PREFIKSO;         // prefikso aý prefiksoido 
   public final static int  S     = KIEL_SUFIKSO;          // sufikso aý sufiksoido
   public final static int  KP    = KIEL_PART;        // kiel participo
   public final static int  KPP   = KIEL_PREP_PREF;   // kiel prepozicia prefiksoido
   public final static int  KAP   = KIEL_ADV_PREF;    // kiel adverba prefiksoido


   // signifoj 

   public final static int  MITPERSONO  = PERSONO;
   public final static int  POSTENO     = PERSONO;
   public final static int  PROFESIO    = PERSONO;
   public final static int  RANGO       = PERSONO;
   public final static int  REGANTO     = PERSONO;
   public final static int  RELPERSONO  = PERSONO;
   public final static int  RELPOSTENO  = PERSONO;
   public final static int  RELPROFESIO = PERSONO;
   public final static int  TITOLO      = PERSONO;

   public final static int  AMFIBIO     = ANIMALO;
   public final static int  ARAKNIDO    = ANIMALO;
   public final static int  BIRDO       = ANIMALO;
   public final static int  EHXINODERMO = ANIMALO;
   public final static int  FIÞO        = ANIMALO;
   public final static int  INSEKTO     = ANIMALO;
   public final static int  KOELENTERO  = ANIMALO;
   public final static int  KRUSTULO    = ANIMALO;
   public final static int  MAMULO      = ANIMALO;
   public final static int  MITBESTO    = ANIMALO;
   public final static int  MOLUSKO     = ANIMALO;
   public final static int  REPTILIO    = ANIMALO;
   public final static int  VERMO       = ANIMALO;

   public final static int  ARMILO      = ILO;
   public final static int  LUDILO      = ILO;
   public final static int  MAÞINO      = ILO;
   public final static int  MEZURILO    = ILO;
   public final static int  MUZIKILO    = ILO;
   public final static int  OPTIKO      = ILO;

   public final static int  ÆAMBRO      = LOKO;
   public final static int  GEOGRAFIO   = LOKO;
   public final static int  KONSTRUAJXO = LOKO;

   public final static int  ASTRO       = LOKNOMO;
   public final static int  DEZERTO     = LOKNOMO;
   public final static int  INSULO      = LOKNOMO;
   public final static int  INSULARO    = LOKNOMO;
   public final static int  KONTINENTO  = LOKNOMO;
   public final static int  LAGO        = LOKNOMO;
   public final static int  LANDO       = LOKNOMO;
   public final static int  MARO        = LOKNOMO;
   public final static int  MONTO       = LOKNOMO;
   public final static int  MONTARO     = LOKNOMO;
   public final static int  PROVINCO    = LOKNOMO;
   public final static int  REGIONO     = LOKNOMO;
   public final static int  RIVERO      = LOKNOMO;
   public final static int  ÞTATO       = LOKNOMO;
   public final static int  URBO        = LOKNOMO;

   public final static int  ARBUSTO     = ARBO;

   //////////////////////////////////////////////////////////////////
   // La sekvantaj signifoj ne estas uzataj por literumkontrolado.  

   public final static int  ALGO        = 0;
   public final static int  ALOJO       = 0;
   public final static int  ANATOMIO    = 0;
   public final static int  ARKITEKTURO = 0;
   public final static int  ARTO        = 0;
   public final static int  ASTRONOMIO  = 0;
   public final static int  AVIADILO    = 0;
   public final static int  BIOLOGIO    = 0;
   public final static int  BOATO       = 0;
   public final static int  CEREALO     = 0;
   public final static int  DANCO       = 0;
   public final static int  DRAMO       = 0;
   public final static int  DROGO       = 0;
   public final static int  ELEMENTO    = 0;
   public final static int  EPOKO       = 0;
   public final static int  ERAO        = 0;
   public final static int  FESTO       = 0;
   public final static int  FILOZOFIO   = 0;
   public final static int  FONETIKO    = 0;
   public final static int  FRUKTO      = 0;
   public final static int  FUNGO       = 0;
   public final static int  GEOMETRIO   = 0;
   public final static int  GRAMATIKO   = 0;
   public final static int  HERBO       = 0;
   public final static int  KANTO       = 0;
   public final static int  KEMIAJXO    = 0;
   public final static int  KOLORO      = 0;
   public final static int  KREDO       = 0;
   public final static int  KURACARTO   = 0;
   public final static int  LEGOMO      = 0;
   public final static int  LIBRO       = 0;
   public final static int  LINGVO      = 0;
   public final static int  LUDO        = 0;
   public final static int  MALSANO     = 0;
   public final static int  MANØAJXO    = 0;
   public final static int  MATEMATIKO  = 0;
   public final static int  MEDIKAMENTO = 0;
   public final static int  METIO       = 0;
   public final static int  MEZURUNUO   = 0;
   public final static int  MIKROBO     = 0;
   public final static int  MINERALO    = 0;
   public final static int  MONATO      = 0;
   public final static int  MONERO      = 0;
   public final static int  MUZIKO      = 0;
   public final static int  NUKSO       = 0;
   public final static int  ORNAMAJXO   = 0;
   public final static int  PERIODO     = 0;
   public final static int  PLANTO      = 0;
   public final static int  POEMO       = 0;
   public final static int  POEZIO      = 0;
   public final static int  PSEÝDOSCI   = 0;
   public final static int  RELIGIO     = 0;
   public final static int  SCIENCO     = 0;
   public final static int  SPICO       = 0;
   public final static int  SPORTO      = 0;
   public final static int  STUDO       = 0;
   public final static int  ÞIPO        = 0;
   public final static int  ÞTOFO       = 0;
   public final static int  TAGO        = 0;
   public final static int  TEHXNOLOGIO = 0;
   public final static int  TRINKAJXO   = 0;
   public final static int  VESTAJXO    = 0;
   public final static int  VETERO      = 0;
   public final static int  VETURILO    = 0;

   // Normigita alfabeto
   public final static char  A_   =  (char)1;
   public final static char  B_   =  (char)2;
   public final static char  C_   =  (char)3;
   public final static char  D_   =  (char)4;
   public final static char  E_   =  (char)5;
   public final static char  F_   =  (char)6;
   public final static char  G_   =  (char)7;
   public final static char  H_   =  (char)8;
   public final static char  I_   =  (char)9;
   public final static char  J_   =  (char)10;
   public final static char  K_   =  (char)11;
   public final static char  L_   =  (char)12;
   public final static char  M_   =  (char)13;
   public final static char  N_   =  (char)14;
   public final static char  O_   =  (char)15;
   public final static char  P_   =  (char)16;
   public final static char  Q_   =  (char)17;
   public final static char  R_   =  (char)18;
   public final static char  S_   =  (char)19;
   public final static char  T_   =  (char)20;
   public final static char  U_   =  (char)21;
   public final static char  V_   =  (char)22;
   public final static char  W_   =  (char)23;
   public final static char  X_   =  (char)24;
   public final static char  Y_   =  (char)25;
   public final static char  Z_   =  (char)26;

   public Vkonstantoj () {
   }


}  // fino de "Vkonstantoj"




