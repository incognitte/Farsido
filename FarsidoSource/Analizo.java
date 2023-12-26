// Analizo - Æi tiu klaso kontrolas la literumadon de vortoj.
// Øi baziøas sur antaýa kontrolilo KL3 por DOS.
// Oni rajtas uzi kaj þanøi la kodon de æi tiu klaso laýplaæe.
// Klivo
// julio 1998
// februaro 1999 - Kreis "normigu_vorton" por Simredo 3.
//               - Forigis Latinon 3 de variablaj nomoj.

import java.net.*;

class Analizo extends Vkonstantoj {

   Vortaro  la_vortaro;
   public boolean  vortaro_shargita;

   char[]   linio;              // kontrolota linio
   int      longeco_de_linio;   // Æi tiu variablo eble estas pli longa ol unu "linio"
   int      indekso;   // indekso de analizata vorto en linio
   public   int      komenco;   // indekso al komenco de vorto
   public   int      longeco;   // longeco de analizata vorto


   char[]   normigita_vorto  = new char[200];
   int      nv_indekso;   // indekso al normigita_vorto

   Radiko[]   radikoj = new Radiko[MAKS_RAD_EN_KM_VORTO];
   int  lasta_radiko;         // numero de la lasta valida radiko en 'radikoj[]'
   int  konservita_finajxo;   // Indikas, ke estas gramatika finajxo konservita
                              // inter radikoj. Ekz: dorm-O-cxambro.
   int  finajxokategorio;     // En "dancis", la kategorio de "is" estas VERBO.
   int  senfinajxa_longeco;   // Ekzemple:  vidis - senfinajxa longeco estas 3

   // Konstruilo por ordinara aplikprogramo.
   public Analizo (String vortaronomo) {
      la_vortaro = new Vortaro(vortaronomo);
      vortaro_shargita = la_vortaro.vortaro_shargita;
      for (int i = 0; i < MAKS_RAD_EN_KM_VORTO; i++) 
           radikoj[i] = new Radiko(normigita_vorto);
   }

   // Konstruilo por retprogramo. 
   public Analizo (URL urlo, String vortaronomo) {
      la_vortaro = new Vortaro(urlo, vortaronomo);
      vortaro_shargita = la_vortaro.vortaro_shargita;
      for (int i = 0; i < MAKS_RAD_EN_KM_VORTO; i++) 
           radikoj[i] = new Radiko(normigita_vorto);
   }

   ///////////////////////////////////////////////////////////////
   // Preparu linion.
   public final void preparu_linion(char[] linio, int longeco_de_linio) {

      this.linio = linio;
      this.longeco_de_linio = longeco_de_linio;
      indekso = 0;

   }  // fino de "preparu_linion"


   ///////////////////////////////////////////////////////////////
   // Akiru vorton - kolektas vorton el kontrolobufro por analizo
   // per la kontrolilo de literumado. Redonas "vera" se øi akiris
   // vorton.
   public boolean akiru_vorton() {

      nv_indekso = 0;  // Indekso por normigita vorto.
      longeco = 0;

      // Preteriru neliterojn.
      while (!estas_litero(linio[indekso]) && indekso < longeco_de_linio)
             indekso++;

      komenco = indekso;  // Marku komenco de vorto.

      // Kolektu literojn de vorto.
      char k = linio[indekso];
      while (estas_litero(k) && indekso < longeco_de_linio) {
         konvertu_literon(k);
         longeco++;
         indekso++;
         k = linio[indekso];
      }

      if (longeco > 0) return true;   // Akiris vorton.
      return false;  // Ne akiris vorton..

   }  // fino de "akiru_vorton"


   ///////////////////////////////////////////////////////////////
   // Normigu vorton - Konvertu la analizotan vorton al norma formato
   // (a = 1, b = 2 ktp.) kaj metu en la bufron "normigita_vorto".
   // Æi tiu programo estas por Simredo 3. 
   // februaro 1999
   public void normigu_vorton(char[] vorto, int komenca_indekso, int fina_indekso) {

      char k;
      nv_indekso = 0;  // Indekso por normigita vorto.

      indekso = komenca_indekso;

      // Kolektu literojn de vorto.
      for (int i = komenca_indekso; i < fina_indekso; i++) {
         k = vorto[i];
         konvertu_literon(k);
      }

   }  // fino de "normigu_vorton"


   ////////////////////////////////////////////////////////////////////////
   // Estas_litero - Indikas, ke la litero estas inter A kaj Z, a kaj z, aý
   // super 127.
   final boolean estas_litero(char litero) {
      if (litero >= 'a' && litero <= 'z') return true;
      if (litero >= 'A' && litero <= 'Z') return true;
      if (litero > 127) return true;
      return false;
   }

   //////////////////////////////////////////////////////////////////////
   // La metodo "konvertu_literon" normigas literon (a = 1, b = 2, ktp)
   // kaj metas øin en la bufron "normigita_vorto". Se la litero estas super
   // 127, sed ne estas Esperanta supersigno, la metodo metas 0 (nulon) en la
   // unua pozicio de la normigita vorto, por indiki ke ne necesas kontroli la
   // vorton: øi estas malbona.
   // La algoritmo traktas w, x kaj y kiel neesperantajn literojn. Mi lasos
   // la literon q trapasi, æar la seræ-algoritmo povas indiki ke vorto kun
   // q estas neesperanta. La programo estos pli rapide, se mi minimumigas la
   // komparojn æi tie. Mi volas certigi, ke la litero x ne trapasu al la
   // seræ-algoritmo, æar la vortaro uzas ikson por indiki supersignon, kaj mi
   // ne volas, ke vortoj kiel "cxirkaux" estu akceptitaj kiel øustaj. 

   final void konvertu_literon(char litero) {

      if (litero >= 'a' && litero <= 'v') {
         normigita_vorto[nv_indekso] = (char)(litero - 'a' + 1);
         nv_indekso++;
         return;
      } 
      else 
      if (litero >= 'A' && litero <= 'V') {
         normigita_vorto[nv_indekso] = (char)(litero - 'A' + 1);
         nv_indekso++;
         return;
      } else
      if (litero == 'z' || litero == 'Z') {
         normigita_vorto[nv_indekso] = (char)Z_;
         nv_indekso++;
         return;
      }
      else
      if (litero > 127) {
         if (litero == 0xe6 || litero == 0x109 ||
             litero == 0xc6 || litero == 0x108) {
             normigita_vorto[nv_indekso] = (char)C_;
             nv_indekso++;
             normigita_vorto[nv_indekso] = (char)X_;
             nv_indekso++;
             return;
         }
         else  
         if (litero == 0xf8 || litero == 0x11d ||
             litero == 0xd8 || litero == 0x11c) {
             normigita_vorto[nv_indekso] = (char)G_;
             nv_indekso++; 
             normigita_vorto[nv_indekso] = (char)X_;
             nv_indekso++;
             return;
         }
         else
         if (litero == 0xfe || litero == 0x15d ||
             litero == 0xde || litero == 0x15c) {
             normigita_vorto[nv_indekso] = (char)S_;
             nv_indekso++;
             normigita_vorto[nv_indekso] = (char)X_;
             nv_indekso++;
             return;
         }
         else
         if (litero == 0xfd || litero == 0x16d ||
             litero == 0xdd || litero == 0x16c) {
             normigita_vorto[nv_indekso] = (char)U_;
             nv_indekso++;
             normigita_vorto[nv_indekso] = (char)X_;
             nv_indekso++;
             return;
         }
         else
         if (litero == 0xbc || litero == 0x135 ||
             litero == 0xac || litero == 0x134) {
             normigita_vorto[nv_indekso] = (char)J_;
             nv_indekso++;
             normigita_vorto[nv_indekso] = (char)X_;
             nv_indekso++;
             return;
         }
         else
         if (litero == 0xb6 || litero == 0x125 ||
             litero == 0xa6 || litero == 0x124) {
             normigita_vorto[nv_indekso] = (char)H_;
             nv_indekso++;
             normigita_vorto[nv_indekso] = (char)X_;
             nv_indekso++;
             return;
         }
      }  // fino de "if (litero > 127)"
      
      normigita_vorto[0] = 0;  // Oni ne kontrolos æi tiun vorton.
      nv_indekso++;            // Necesas. Se ne, sekvonta litero superskribos.
      return;  

   }  // fino de "konvertu_literon"



   ////////////////////////////////////////////////////////////////////////
   // Fortranæu la fina¼on de la radiko.
   // Ekzemple: "vidis"  "katojn"  "danci"  fariøas  "vid" "kat" kaj "danc"
   final boolean fortranæu_finajxon(Radiko radiko) {
      int indekso = radiko.indekso;
      int longeco = radiko.longeco;
      if (longeco < 3) return false;
      int  lasta = radiko.literoj[indekso + longeco - 1];
      if (lasta == O_) {
         finajxokategorio = NOMO;
         radiko.longeco--;
         return true;
      }
      if (lasta == A_) {
         finajxokategorio = ADJEKTIVO;
         radiko.longeco--;
         return true;
      }
      if (lasta == I_ || lasta == U_) {
         finajxokategorio = VERBO;
         radiko.longeco--;
         return true;
      }
      if (lasta == E_) {
         finajxokategorio = ADVERBO;
         radiko.longeco--;
         return true;
      }
      if (longeco < 4) return false;
      int  antaulasta = radiko.literoj[indekso + longeco - 2];
      if (lasta == S_ &&
         (antaulasta == A_ || antaulasta == I_ || 
          antaulasta == O_ || antaulasta == U_)) {
         finajxokategorio = VERBO;
         radiko.longeco = longeco - 2;
         return true;
      }
      if (antaulasta == O_ && (lasta == N_ || lasta == J_)) {
         finajxokategorio = NOMO;
         radiko.longeco = longeco - 2;
         return true;
      }
      if (antaulasta == A_ && (lasta == N_ || lasta == J_)) {
         finajxokategorio = ADJEKTIVO;
         radiko.longeco = longeco - 2;
         return true;
      }
      if (lasta == N_ && antaulasta == E_) {
         finajxokategorio = ADVERBO;
         radiko.longeco = longeco - 2;
         return true;
      }
      if (longeco < 5) return false;
      int  antauantaulasta = radiko.literoj[indekso + longeco - 3];
      if (lasta == N_ && antaulasta == J_) {
         if (antauantaulasta == O_) {
            finajxokategorio = NOMO;
            radiko.longeco = longeco - 3;
            return true;
         }
         if (antauantaulasta == A_) {
            finajxokategorio = ADJEKTIVO;
            radiko.longeco = longeco - 3;
            return true;
         }
      }

      return false;  // Ne sukcesis fortranæi fina¼on.

   }  // fino de "fortranæu_fina¼on"


   //////////////////////////////////////////////////////////////////////
   // Kontrolu la literumadon de vorto.
   public boolean kontrolu_vorton() {

      // Se vorto enhavas neesperantan literon, reiru.
      if (normigita_vorto[0] == 0) return false; 

      // Eble la radiko povas estis senfina¼o, ekz. øis, antaý, mi.
      // Kontrolu tion unue.
      radikoj[0].indekso = 0;
      radikoj[0].longeco = nv_indekso;
      if (la_vortaro.seræu(radikoj[0]) && 
          (radikoj[0].sen_finajxo == VERA)) return true;

      // Fortranæu gramatikan fina¼on (se eblas).
      if (!fortranæu_finajxon(radikoj[0])) return false;
      senfinajxa_longeco = radikoj[0].longeco;

      // Provu trovi la radikon en la vortaro. 
      if (la_vortaro.seræu(radikoj[0]) && 
          (radikoj[0].kun_finajxo == VERA)) return true; 

      // Devas esti kunmeta¼o. Analizu.
      lasta_radiko = 0;
      konservita_finajxo = FALSA;
      if (trovu_radikon(0)) return true;

      return false;

   }  // fino de "kontrolu_vorton"


   ///************************************************************
   // La sekvanta rutino dividas kunmetitan vorton en radikojn. 

   boolean trovu_radikon(int n)    // n, numero de radiko
   {

      int indekso = radikoj[n].indekso;
      int longeco = radikoj[n].longeco;

      ////////////////////////////////////////////////
      // Iom da kodo por diagnozado.
      // System.out.println("n = " + n + " indekso = " + 
      // indekso + " longeco = " + longeco);
      // for (int i = 0; i < longeco; i++) 
      // System.out.print((char)(radikoj[n].literoj[indekso + i] + 'a' - 1));
      // System.out.println(" ");
      // System.out.println(" kunmetajxo " + radikoj[n].kunmetajxo);
      // if (longeco == 0) System.exit(0);

      if (n >= MAKS_RAD_EN_KM_VORTO) return false;

      // La resta¼o de la vorto povas esti valida radiko. Kontrolu tion unue. 
      // Se ne, la resta¼o povas esti kunmeta¼o de du aý pli radikoj. Provu 
      // trovi la unuan (antaýan) radikon en la resta¼o de la vorto. Radiko  
      // povas havi longecon de 2 aý pli. Tial, kontrolu radikolongecojn de 2 
      // øis resta¼olongeco - 2.

      if (n > 0) {
         // Eble la resta¼o de la vorto estas valida radiko. 
         // Kontrolu tion unue. 
         if (la_vortaro.seræu(radikoj[n]) && (radikoj[n].kunmetajxo != FALSA)) {
            lasta_radiko = n;
            if (sintezo_valida(n,LASTA)) return true;
         }
      }

      // Provu trovi validan radikon. 
      int longeco_minus_2 = longeco - 2;
      for (int rl = 2; rl <= longeco_minus_2; rl++) {
         radikoj[n].longeco = rl;
         if (la_vortaro.seræu(radikoj[n]) && (radikoj[n].kunmetajxo != FALSA))   {
            if (sintezo_valida(n,NELASTA)) return true;  
         }
      }

      // radikoj[n].longeco = longeco;  // Remetu antaýan valoron. Necesa?

      // Foje oni konservas la finajxon inter elementoj de kunmetajxo: (PIV 309) 
      // Ekz. dormOcxambro, fingrOmontri, gxustAtempe, minutE-bakita, unuAfoje...
      // Mi permesos "konservitan finajxon" nur unufoje en vorto. 
      // Longeco devas esti almenaux 3. Unu litero plus radiko de 2 literoj. 
      if (longeco >= 3 && konservita_finajxo == FALSA) {
         char litero = radikoj[n].literoj[indekso];
         radikoj[n].longeco        = 1;     // nur unu litero 
         radikoj[n].signifo        = NEKONATA;
         radikoj[n].transitiveco   = NETRANSITIVA;
         radikoj[n].kunmetajxo     = NELIMIGITA;
         if (litero == O_) {   // Estas normigita litero (-'a')
            radikoj[n].kategorio  = NOMO;
            konservita_finajxo = VERA;
            // Dividu la resta¼on de la vorto en radikojn.
            radikoj[n+1].indekso = indekso + 1;
            radikoj[n+1].longeco = longeco - 1;
            if (trovu_radikon(n+1)) return true;
            else {
               konservita_finajxo = FALSA;
               return false;
            }
         }
         else if (litero == A_ || litero == E_) {
            if (finajxokategorio == ADJEKTIVO || finajxokategorio == ADVERBO) {
               radikoj[n].kategorio  = ADJEKTIVO;
               konservita_finajxo = VERA;
               // Dividu la resta¼on de la vorto en radikojn. 
               radikoj[n+1].indekso = indekso + 1;
               radikoj[n+1].longeco = longeco - 1;
               if (trovu_radikon(n+1)) return true;
               else {
                  konservita_finajxo = FALSA;
                  return false;
               }
            }
         }
      }
      return false;  // ne sukcesis 
   }  // fino de "trovu_radikon"



   boolean sintezo_valida(int n, int æu_lasta)
      // n = numero de radiko 
      // æu_lasta = æu lasta radiko en vorto 
   {

      int indekso = radikoj[n].indekso;
      int longeco = radikoj[n].longeco;
      int kunmetajxo = radikoj[n].kunmetajxo;

      // Kontrolu sufiksojn tuj kiam ili estas trovataj.
      // La valideco de sufikso dependas de antaýaj radikoj. 
      if ((kunmetajxo == KIEL_PART) && !participoj(n)) return false;
      if ((kunmetajxo == KIEL_SUFIKSO) && !afikso_valida(n)) return false;

      if (æu_lasta == NELASTA) {

         // Dividu la resta¼on de la vorto en radikojn.
         int nova_indekso = indekso + longeco;
         radikoj[n+1].indekso = nova_indekso;
         radikoj[n+1].longeco = senfinajxa_longeco - nova_indekso;
         if (!trovu_radikon(n+1)) return false;

         // Kontrolu prefiksojn post la divido de la vorto, æar la 
         // valideco de prefikso dependas de la sekvantaj radikoj. 
         if ((kunmetajxo == KIEL_PREFIKSO) &&  !afikso_valida(n)) return false;
         if ((kunmetajxo == KIEL_PREP_PREF) && !prepoziciaj_prefiksoidoj(n)) return false;
         if ((kunmetajxo == KIEL_ADV_PREF) &&  !adverbaj_prefiksoidoj(n)) return false;

         // Kontrolu 'limigitajn' radikojn. 
         // Ilia valideco dependas de antaýaj kaj postaj radikoj.
         if ((kunmetajxo == LIMIGITA) && !limigita_radiko_okej(n)) return false;

         return true;
      }

      if (æu_lasta == LASTA) {
        if (kunmetajxo == KIEL_PREFIKSO || 
            kunmetajxo == KIEL_PREP_PREF ||
            kunmetajxo == KIEL_ADV_PREF) return false;
        return true;
      }

      return false;

   }  // fino de "sintezo_valida"


   //**************************************************************************
   // Limigitaj Radikoj                                                        
   // Iuj radikoj, æar ili estas mallongaj, povas kaýzi problemon por la  
   // kontrolilo. Por ekzemplo, se oni skribus 'ordano' anstataý 'ordono', la 
   // kontrolilo dividus la vorton æi tiel, or-dan-o, kaj indikus, ke la      
   // vorto estas øusta. Por solvi la problemon, la radiko 'dan' kaj multaj   
   // aliaj malgrandaj radikoj havas limigitan kombineblecon.                  
   // Limigita verb-radiko rajtas kombiniøi nur kun sufikso aux prefikso.     
   // Limigita animal-radiko rajtas kombiniøi kun vir-, -in, -id ktp.         
   // Limigita parencoradiko rajtas kombiniøi kun pra-, ge-, -in.             
   // Limigita etno-radiko rajtas kombiniøi kun -in, -land, ktp.              

   boolean limigita_radiko_okej(int n)  // n = radiko numero 
   {

      if (radikoj[n].kategorio == VERBO || radikoj[n].kategorio == NOMOVERBO) {
         if (n == 1 && radikoj[0].kunmetajxo == KIEL_PREFIKSO) return true;
         if ((lasta_radiko - n) == 1  // Se almenaý unu radiko sekvas...
            && radikoj[n+1].kunmetajxo == KIEL_SUFIKSO) return true;
      }
      else if (radikoj[n].signifo == ANIMALO) {
         if (n == 1 && estas_samaj(norm_vir,radikoj[0])) return true;
         if ((lasta_radiko - n) == 1
            && (estas_samaj(norm_in,radikoj[n+1]) ||
                estas_samaj(norm_id,radikoj[n+1]) ||
                estas_samaj(norm_ajx,radikoj[n+1]) ||
                estas_samaj(norm_ov,radikoj[n+1]))) return true;
      }
      else if (radikoj[n].signifo == PARENCO) {
         if (n == 1 && (estas_samaj(norm_pra,radikoj[0]) ||
            estas_samaj(norm_ge,radikoj[0]))) return true;
         if ((lasta_radiko - n) == 1
            && estas_samaj(norm_in,radikoj[n+1])) return true;
      }
      else if (radikoj[n].signifo == ETNO) {
         if (n == 1 && estas_samaj(norm_ge,radikoj[0])) return true;
         if ((lasta_radiko - n) == 1
            && (estas_samaj(norm_in,radikoj[n+1]) ||
                estas_samaj(norm_id,radikoj[n+1]) ||
                estas_samaj(norm_land,radikoj[n+1]) ||
                estas_samaj(norm_stil,radikoj[n+1]))) return true;
      }
      // Foje pronoma adjektivo estas uzata en kunmeta¼o, ekzemple: 
      // MIAopinie, SIAtempe, NIAlande...   Kontrolu æi tion.       
      else if (n == 0 && radikoj[0].kategorio == ADJEKTIVO
                && finajxokategorio == ADVERBO) return true;

      return false;

   }   // fino de "limigita_radiko_okej"


   // Normigitaj radikoj.
   char[] norm_acx       = {A_,C_,X_};
   char[] norm_ad        = {A_,D_};
   char[] norm_ajx       = {A_, J_, X_};
   char[] norm_an        = {A_,N_};
   char[] norm_anstataux = {A_,N_,S_,T_,A_,T_,A_,U_,X_};
   char[] norm_antaux    = {A_,N_,T_,A_,U_,X_};
   char[] norm_ar        = {A_,R_};
   char[] norm_ov        = {O_, V_};
   char[] norm_land      = {L_, A_, N_, D_};
   char[] norm_stil      = {S_, T_, I_, L_};
   char[] norm_vir       = {V_, I_, R_};
   char[] norm_cis       = {C_,I_,S_};
   char[] norm_cxi       = {C_,X_,I_};
   char[] norm_cxirkaux  = {C_,X_,I_,R_,K_,A_,U_,X_};
   char[] norm_ebl       = {E_,B_,L_};
   char[] norm_ec        = {E_,C_};
   char[] norm_eg        = {E_,G_};
   char[] norm_ej        = {E_,J_};
   char[] norm_eks       = {E_,K_,S_};
   char[] norm_ekster    = {E_,K_,S_,T_,E_,R_};
   char[] norm_em        = {E_,M_};
   char[] norm_end       = {E_,N_,D_};
   char[] norm_er        = {E_,R_};
   char[] norm_estr      = {E_,S_,T_,R_};
   char[] norm_et        = {E_,T_};
   char[] norm_id        = {I_,D_};
   char[] norm_ig        = {I_,G_};
   char[] norm_igx       = {I_,G_,X_};
   char[] norm_ik        = {I_,K_};
   char[] norm_il        = {I_,L_};
   char[] norm_in        = {I_,N_};
   char[] norm_ind       = {I_,N_,D_};
   char[] norm_ing       = {I_,N_,G_};
   char[] norm_inter     = {I_,N_,T_,E_,R_};
   char[] norm_ism       = {I_,S_,M_};
   char[] norm_ist       = {I_,S_,T_};
   char[] norm_kontraux  = {K_,O_,N_,T_,R_,A_,U_,X_};
   char[] norm_krom      = {K_,R_,O_,M_};
   char[] norm_kun       = {K_,U_,N_};
   char[] norm_obl       = {O_,B_,L_};
   char[] norm_op        = {O_,P_};
   char[] norm_on        = {O_,N_};
   char[] norm_po        = {P_,O_};
   char[] norm_pra       = {P_,R_,A_};
   char[] norm_pseuxdo   = {P_,S_,E_,U_,X_,D_,O_};
   char[] norm_sen       = {S_,E_,N_};
   char[] norm_sin       = {S_,I_,N_};
   char[] norm_sub       = {S_,U_,B_};
   char[] norm_sur       = {S_,U_,R_};
   char[] norm_uj        = {U_,J_};
   char[] norm_ul        = {U_,L_};
   char[] norm_bo        = {B_,O_};
   char[] norm_ge        = {G_,E_};
   char[] norm_mal       = {M_,A_,L_};
   char[] norm_ne        = {N_,E_};
   char[] norm_retro     = {R_,E_,T_,R_,O_};


   // "estas_samaj" Komparas du normigitajn vortojn. 
   // Redonas "vera" se ili estas samaj.
   final boolean estas_samaj(char[] s, Radiko radiko) {
      int indekso = radiko.indekso;
      int nombro  = s.length;
      if (radiko.longeco != nombro) return false;
      for (int i = 0; i < nombro; i++) {
          if ((radiko.literoj[indekso + i] - s[i]) != 0) return false;
      }
      return true;
   }  // fino de "estas_samaj"

   // estas_samaj2 - Sama al la antaýa metodo, sed øi ne komparas la unuan literon.
   // Mi kreis æi tion pro rapideco. (Uzata en afikso_valida.)
   final boolean estas_samaj2(char[] s, Radiko radiko) {
      int indekso = radiko.indekso;
      int nombro  = s.length;
      if (radiko.longeco != nombro) return false;
      for (int i = 1; i < nombro; i++) {
          if ((radiko.literoj[indekso + i] - s[i]) != 0) return false;
      }
      return true;
   }  // fino de "estas_samaj2"

 
   final boolean adverbaj_prefiksoidoj(int n)  // n = radiko numero 
   {
      int   kat;
      // Adverbo kvalifikas verbon. Provu trovi verban radikon. 
      if ( (lasta_radiko > 0) && finajxokategorio == VERBO) return true;
      n++;
      while (n <= lasta_radiko) {
         kat = radikoj[n].kategorio;
         if (kat == VERBO || kat == NOMOVERBO) return true;
         n++;
      }
      return false;
   } // fino de "adverbaj_prefiksoidoj"


   // Multaj prepozicioj, kiam uzataj kiel prefiksoj, funkcias æi tiel:    
   // - Ili ligiøas al verbo, kiel adverba adjekto aý predikata adjektivo.
   // - Ili funkcias prepozicie, kiel maldekstra flankelemento. (PIV 373).  
   // La sekvanta regulo kontrolas æi tiujn kondiæojn.                    

   final boolean prepoziciaj_prefiksoidoj(int n)  // n = radiko numero 
   {
      int   kat;
      if ( (lasta_radiko > 0) && 
            (finajxokategorio == ADJEKTIVO || finajxokategorio == ADVERBO))
            return true;
      n++;
      while (n <= lasta_radiko) {
         kat = radikoj[n].kategorio;
         if (kat == VERBO || kat == NOMOVERBO) return true;
         n++;
      }
      return false;
   }  // fino de "prepoziciaj_prefiksoidoj"

   // Kontrolu sintezon de participoj.
   final boolean participoj(int n)  // n = radiko numero (unua radiko estas 0) 
   {
      if (n == 0) return false;   // 'ant' ne povas esti la unua radiko 

      Radiko radiko = radikoj[n-1];
      int  l  = radiko.longeco;

      if (radiko.kategorio == VERBO || radiko.kategorio == NOMOVERBO) return true;
      if (estas_samaj(norm_anstataux,radiko))  return true;
      if (estas_samaj(norm_cxirkaux,radiko))  return true;
      if (estas_samaj(norm_kontraux,radiko))  return true;
      return false;

   }  // fino de "participoj"



   //**********************************************************************
   // La rutino 'afikso_valida' vokas metodoj por kontroli la øustecon de 
   // prefiksoj kaj sufiksoj, kiuj estas individue traktataj.

   final boolean afikso_valida(int n) {

      Radiko radiko = radikoj[n];
      char unua_litero = radiko.literoj[radiko.indekso];

      if (unua_litero == A_) {   // Devas kompari normigitajn literojn.
         if (estas_samaj2(norm_acx,radiko))        return acx(n);
         if (estas_samaj2(norm_ad,radiko))         return ad(n);
         if (estas_samaj2(norm_ajx,radiko))        return ajx(n);
         if (estas_samaj2(norm_an,radiko))         return an(n);
         if (estas_samaj2(norm_ar,radiko))         return ar(n);
         if (estas_samaj2(norm_anstataux,radiko))  return true;
         if (estas_samaj2(norm_antaux,radiko))     return true;
      }
      else
      if (unua_litero == C_) { 
         if (estas_samaj2(norm_cxi,radiko))  return cxi(n);
         if (estas_samaj2(norm_cis,radiko))  return cis(n);
         if (estas_samaj2(norm_cxirkaux,radiko))  return true;
      }
      else
      if (unua_litero == E_) { 
         if (estas_samaj2(norm_ebl,radiko))    return ebl(n);
         if (estas_samaj2(norm_ec,radiko))     return ec(n);
         if (estas_samaj2(norm_eg,radiko))     return eg_et(n);
         if (estas_samaj2(norm_ej,radiko))     return ej(n);
         if (estas_samaj2(norm_eks,radiko))    return eks(n);
         if (estas_samaj2(norm_ekster,radiko)) return true;
         if (estas_samaj2(norm_em,radiko))      return em(n);
         if (estas_samaj2(norm_end,radiko))    return end_ind(n);
         if (estas_samaj2(norm_er,radiko))     return er_ik_ing_ism(n);
         if (estas_samaj2(norm_estr,radiko))   return estr(n);
         if (estas_samaj2(norm_et,radiko))     return eg_et(n);
      }
      else
      if (unua_litero == I_) { 
         if (estas_samaj2(norm_id,radiko))     return id(n);
         if (estas_samaj2(norm_ig,radiko))     return ig_igx(n);
         if (estas_samaj2(norm_igx,radiko))    return ig_igx(n);
         if (estas_samaj2(norm_ik,radiko))     return er_ik_ing_ism(n);
         if (estas_samaj2(norm_il,radiko))     return il(n);
         if (estas_samaj2(norm_in,radiko))     return in(n);
         if (estas_samaj2(norm_ind,radiko))    return end_ind(n);
         if (estas_samaj2(norm_ing,radiko))    return er_ik_ing_ism(n);
         if (estas_samaj2(norm_inter,radiko))  return true;
         if (estas_samaj2(norm_ism,radiko))    return er_ik_ing_ism(n);
         if (estas_samaj2(norm_ist,radiko))    return ist(n);
      }
      else
      if (unua_litero == K_) { 
         if (estas_samaj2(norm_kontraux,radiko))  return true;
         if (estas_samaj2(norm_krom,radiko))  return true;
         if (estas_samaj2(norm_kun,radiko))  return kun(n);
      }
      else
      if (unua_litero == O_) { 
         if (estas_samaj2(norm_obl,radiko))    return obl_op_on(n);
         if (estas_samaj2(norm_op,radiko))     return obl_op_on(n);
         if (estas_samaj2(norm_on,radiko))     return obl_op_on(n);
      }
      else
      if (unua_litero == P_) { 
         if (estas_samaj2(norm_po,radiko))       return po(n);
         if (estas_samaj2(norm_pra,radiko))      return pra(n);
         if (estas_samaj2(norm_pseuxdo,radiko))  return pseuxdo(n);
      }
      else
      if (unua_litero == S_) { 
         if (estas_samaj2(norm_sen,radiko))     return sen(n);
         if (estas_samaj2(norm_sin,radiko))     return sin(n);
         if (estas_samaj2(norm_sub,radiko))     return subsur(n);
         if (estas_samaj2(norm_sur,radiko))     return subsur(n);
      }
      else
      if (unua_litero == U_) { 
         if (estas_samaj2(norm_uj,radiko))     return uj(n);
         if (estas_samaj2(norm_ul,radiko))     return ul(n);
      }
      else {
         if (estas_samaj(norm_bo,radiko))       return bo(n);
         if (estas_samaj(norm_ge,radiko))       return ge(n);
         if (estas_samaj(norm_mal,radiko))      return mal(n);
         if (estas_samaj(norm_ne,radiko))       return ne(n);
         if (estas_samaj(norm_retro,radiko))    return true;

         // Tehxnikaj prefiksoj (anti- ,mono- ktp) ne estas en la tabelo de  
         // afiksoj. La sola limigo por ili estas, ke ili devas esti la unua 
         // radiko en la vorto. 
         if (radikoj[n].kategorio == TEHXPREFIKSO) {
            if (n == 0) return true;
            return false;
         }

         // System.out.print("Nekonata afikso. ");
         // char asdf;
         // for (int i = 0; i < radiko.longeco; i++) {
         //    asdf = (char)(radiko.literoj[i] + 'a' - 1);
         //    System.out.print(asdf);
         // }

         return false;
      }
      return false;
   }  // fino de "afikso_valida"


   //**************************************************************************
   // La sekvantaj rutinoj estas uzataj por kontroli la sintezon de prefiksoj, 
   // sufiksoj, prefiksoidoj kaj sufiksoidoj.                                  
   // Sufiks(oid)oj estas kontrolataj tuj kiam ili estas trovataj de            
   // 'trovu_radikon'. Prefiks(oid)oj estas kontrolataj post la trovo de æiuj 
   // radikoj, kiam 'lasta_radiko' estas valida.                               

   // Kontrolu æu la radiko signifas homon. 
   final boolean estas_homo(int signifo) {
      if (signifo == PERSONO || signifo == ETNO || signifo == PARENCO)
         return true;
      return false;
   }

   final boolean acx(int n) {
      int   kat;

      if (n == 0) {
         radikoj[n].kategorio = ADJEKTIVO;   // iom arbitra decido 
         return true;
      }
      kat = radikoj[n-1].kategorio;
      if (kat <= ADJEKTIVO || kat == PARTICIPO) {
         // 'Acx' ne þanøas la karakteron de la antaýa radiko. 
         radikoj[n].kategorio = kat;
         radikoj[n].signifo = radikoj[n-1].signifo;
         radikoj[n].transitiveco = radikoj[n-1].transitiveco;
         return true;
      }
      return false;
   }  // fino de "acx"

   final boolean  ad(int n) {
      int   kat;

      if (n == 0) return false;
      kat = radikoj[n-1].kategorio;
      if (kat <= VERBO) {
         radikoj[n].kategorio = VERBO;
         radikoj[n].transitiveco = radikoj[n-1].transitiveco;
         return true;
      }
      return false;
   }

   final boolean ajx(int n) {
      int   kat;

      if (n == 0) return true;
      kat = radikoj[n-1].kategorio;
      if (kat <= ADJEKTIVO || kat == PREPOZICIO || kat == PARTICIPO) return true;
      return false;
   }

   final boolean an(int n) {
      if (n == 0) return true;
      if (radikoj[n-1].kategorio <= NOMOVERBO && 
         !estas_homo(radikoj[n-1].signifo)) return true;
      return false;
   }

   final boolean ar(int n) {
      int   kat;

      if (n == 0) return true;
      kat = radikoj[n-1].kategorio;
      if (kat <= NOMOVERBO || kat == PARTICIPO) return true;
      return false;
   }

   final boolean bo(int n) {
      if ((lasta_radiko - n) > 0 && radikoj[n+1].signifo == PARENCO) return true;
      return false;
   }

   final boolean cis(int n) {
      if ((lasta_radiko - n) > 0 && 
         (radikoj[n+1].signifo == LOKNOMO || radikoj[n+1].signifo == LOKO)) 
         return true;
      return false;
   }

   final boolean cxi(int n) {
      if (n == 0 &&
         (finajxokategorio == ADJEKTIVO || finajxokategorio == ADVERBO))
         return true;
      return false;
   }

   final boolean ebl(int n) {
      int   kat;

      if (n == 0) return true;
      kat = radikoj[n-1].kategorio;
      if (kat == VERBO || kat == NOMOVERBO) return true;
      return false;
   }

   final boolean ec(int n) {
      int   kat;

      if (n == 0) return false;
      radikoj[n].kategorio = VERBO;
      kat = radikoj[n-1].kategorio;
      if (kat <= NUMERO || kat == PARTICIPO) return true;
      return false;
   }

   final boolean eg_et(int n) {
      if (n == 0) return false;   // ne povas esti la unua radiko 
      if (radikoj[n-1].kategorio <= ADJEKTIVO) {
         radikoj[n].kategorio = radikoj[n-1].kategorio;
         radikoj[n].signifo = radikoj[n-1].signifo;
         radikoj[n].transitiveco = radikoj[n-1].transitiveco;
         return true;
      }
      return false;
   }

   final boolean ej(int n) {
      int   sig;

      if (n == 0) return false;
      sig = radikoj[n-1].signifo;
      if (radikoj[n-1].kategorio <= ADJEKTIVO &&
         sig != LOKO && sig != LOKNOMO) return true;
      return false;
   }

   final boolean eks(int n) {
      int   sig;
      n++;
      while (n <= lasta_radiko) {
         sig = radikoj[n].signifo;
         if (sig == PERSONO || sig == LOKNOMO) return true;
         n++;
      }
      return false;
   }

   final boolean em(int n) {
      if (n == 0) return false;
      if (radikoj[n-1].kategorio <= ADJEKTIVO) return true;
      return false;
   }

   final boolean end_ind(int n) {
      if (n == 0) return false;
      if (radikoj[n-1].transitiveco == TRANSITIVA) return true;
      return false;
   }

   final boolean er_ik_ing_ism(int n) {
      if (n == 0) return false;
      if (radikoj[n-1].kategorio <= NOMOVERBO) return true;
      return false;
   }

   final boolean estr(int n) {
      int   kat;
      radikoj[n].kategorio = NOMO;
      if (n == 0) return true;
      if (radikoj[n-1].kategorio <= NOMOVERBO) return true;
      return false;
   }

   final boolean ge(int n) {
      int   sig;

      n++;
      while (n <= lasta_radiko) {
         sig = radikoj[n].signifo;
         if (estas_homo(sig) || sig == ANIMALO) return true;
         n++;
      }
      return false;
   }

   final boolean id(int n) {
      int  sig;  // signifo 

      if (n == 0) return true;
      sig = radikoj[n-1].signifo;
      if (sig == ETNO || sig == ANIMALO) return true;
      return false;
   }

   final boolean ig_igx(int n) {
      int   kat;
      if (n == 0) return false;
      kat = radikoj[n-1].kategorio;
      if (kat <= NUMERO || kat == PREPOZICIO || 
          kat == ADVERBO || kat == PREFIKSO) return true;
      return false;
   }

   final boolean il(int n) {
      int   kat;
      if (n == 0) return true;
      kat = radikoj[n-1].kategorio;
      if ((kat == VERBO || kat == NOMOVERBO) && radikoj[n-1].signifo != ILO)
         return true;
      return false;
   }

   final boolean in(int n) {
      int  sig;  // signifo 

      if (n == 0) return true;
      sig = radikoj[n-1].signifo;
      if (estas_homo(sig) || sig == ANIMALO) return true;
      return false;
   }

   final boolean ist(int n) {
      if (n == 0) return false;
      if (radikoj[n-1].kategorio <= VERBO && 
         !estas_homo(radikoj[n-1].signifo)) return true;
      return false;
   }

   final boolean kun(int n) {
      if (prepoziciaj_prefiksoidoj(n)) return true;
      n++;
      while (n <= lasta_radiko) {
         if (radikoj[n].kategorio == NOMO) return true;
         n++;
      }
      return false;
   }

   final boolean mal(int n) {
      int   kat;

      if ((lasta_radiko > 0) && (finajxokategorio == VERBO 
         || finajxokategorio == ADJEKTIVO 
         || finajxokategorio == ADVERBO)) return true;
      n++;
      while (n <= lasta_radiko) {
         kat = radikoj[n].kategorio;
         if (kat == VERBO || kat == NOMOVERBO || kat == ADJEKTIVO) return true;
         n++;
      }
      return false;
   }

   final boolean ne(int n) {
      if (n == 0) return true;
      return false;
   }

   final boolean obl_op_on(int n) {
      if (n > 0 && radikoj[n-1].kategorio == NUMERO) return true;
      return false;
   }

   final boolean po(int n) {
      if (finajxokategorio == ADVERBO) return true;
      return false;
   }

   final boolean pra(int n) {
      if ((lasta_radiko - n) > 0 && radikoj[n+1].kategorio == NOMO) return true;
      return false;
   }

   final boolean pseuxdo(int n) {
      if ((lasta_radiko - n) > 0 && (radikoj[n+1].kategorio == NOMO ||
         radikoj[n+1].kategorio == ADJEKTIVO)) return true;
      return false;
   }

   final boolean sen(int n) {
      Radiko rad;
      if (prepoziciaj_prefiksoidoj(n)) return true;
      rad = radikoj[lasta_radiko];
      if (estas_samaj(norm_ul,rad) ||
          estas_samaj(norm_ajx,rad) ||
          estas_samaj(norm_ej,rad)) return true;
      return false;
   }

   final boolean sin(int n) {
      if (n == 0 && (lasta_radiko > 0) && radikoj[n+1].transitiveco == TRANSITIVA) 
         return true;
      return false;
   }

   final boolean subsur(int n) {
      if (prepoziciaj_prefiksoidoj(n)) return true;
      if (lasta_radiko > 0 && finajxokategorio == NOMO) return true;
      return false;
   }

   final boolean uj(int n) {
      if (n > 0 && radikoj[n-1].kategorio <= NOMOVERBO &&
         radikoj[n-1].signifo != ARBO) return true;
      return false;
   }

   final boolean ul(int n) {
      if (n == 0) return true;
      if (radikoj[n-1].kategorio <= ADJEKTIVO &&
         !estas_homo(radikoj[n-1].signifo)) return true;
      if (radikoj[n-1].kategorio == PREPOZICIO) return true;
      return false;
   }




}  // fino de Analizo

