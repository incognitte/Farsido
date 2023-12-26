/*
 * Bufro - This class is a buffer for text. It can grow, when necessary.
 * Bufro - Bufro por teksto. La bufro kapablas kreski, kiam necese.
 * versio 1.0, majo 1998
 *
 * Cleve Lendon (Klivo)
 * klivo@infotrans.or.jp
 * http://www.infotrans.or.jp/~klivo/
 */

// Klivo

class Bufro {

   private int kapacito;       // kapacito de la bufro
   private int kvantumo;       // La kvanto, kiu aldoniøas, kiam la bufro kreskas.

   char[]  bufro;         // Entenas la karaktrojn.
   int     nombro = 0;    // nombro da karaktroj en bufro

   //++++++++++++++++++++++++++++++++++++++++++++
   //  konstruilo de la klaso
   public Bufro (char[] bufro) {
      this.bufro    = bufro;
      kapacito  = bufro.length;
      nombro    = kapacito;
      kvantumo  = 1000;
      if (kapacito < 2000) kresku(2000);  // Certigu minimuman kapaciton.
   }

   public Bufro () {
      bufro    = new char[4000];
      kapacito  = bufro.length;
      kvantumo  = 2000;
      nombro    = 0;
   }

   // Kresku - certigas, ke la bufro estas sufiæe granda, 
   // por la teksto kiu enmetiøos.
   public void kresku(int nova_kapacito) {
      if (nova_kapacito > kapacito) {
         int  malnova_kapacito = kapacito;
         char[] malnova_bufro  = bufro;
         kapacito = nova_kapacito + kvantumo;
         kvantumo = kapacito / 2;
         bufro = new char[kapacito];
         System.arraycopy(malnova_bufro,0,bufro,0,malnova_kapacito);
      }
   }     // fino de "kresku"


   // Æi tiu metodo liberigas neuzatan memoron. Øi estas utila, kiam
   // la kapacito estas multe pli granda ol la nombro da karaktroj.
   private void malkresku() {
      char[] malnova_bufro = bufro;
      kapacito = nombro;
      bufro = new char[kapacito];
      kvantumo = kapacito / 2;
      if (kvantumo < 1000) kvantumo = 1000;
      System.arraycopy(malnova_bufro,0,bufro,0,nombro);
      System.gc();  // For¼etu ruba¼on.
   }

   // Malfermu spacon en la bufro. (Oni plenigu la spacon per aliaj metodoj.)
   private void malfermu_spacon (int indekso, int n) {
      kresku(nombro+n);
      if (indekso < 0 || indekso > nombro) return;
      int fonto   = nombro - 1;    // nombro estas nombro da karaktroj en bufro
      int celo    = nombro - 1 + n;
      for (; fonto >= indekso; fonto--) {
         bufro[celo] = bufro[fonto];
         celo--;
      }
      nombro = nombro + n;
   }  // fino de "malfermu_spacon"

   // Forigu spacon de la bufro - eltondas karaktrojn, (Ne konservas ilin).
   public void forigu_spacon (int indekso, int n) {
      if ((indekso + n) > nombro) {
         n = nombro - indekso;
      }
      if (indekso < 0 || indekso > nombro) return;
      System.arraycopy(bufro,indekso+n,bufro,indekso,nombro-indekso-n);
      nombro = nombro - n;
   }  // fino de "forigu_spacon"


   // superskribu - Skribas unu karaktron en la bufron. 
   // Antaýa karaktro estas perdita.
   public void superskribu(char kar, int indekso) {
      if (indekso > nombro) return;
      if (indekso == nombro) kresku(nombro+1);  // Se estas æe la fino de la bufro
      bufro[indekso] = kar;
      if (indekso == nombro) nombro++;
   }  // fino de "superskribu"

   // superskribu - Skribas karaktrojn en la bufron.
   // Antaýaj karaktroj estas perditaj.
   // Notu, ke la bufro eble devos longiøi.
   public void superskribu(char[] fonto, int indekso) {
      if (indekso > nombro) return;
      int n = fonto.length;
      int nova_nombro = indekso + n;
      if (nova_nombro > nombro) {
         nombro = nova_nombro;
         kresku(nombro);
      }
      System.arraycopy(fonto,0,bufro,indekso,n);
   }  // fino de "superskribu"

   // interskribu - Skribas unu karaktron en la bufron. 
   // Unue, øi faras spacon.
   public void interskribu(char kar, int indekso) {
      malfermu_spacon(indekso,1);  // "malfermu_spacon" adicias 1 al nombro (da karaktroj)
      bufro[indekso] = kar;
   }  // fino de "interskribu"

   // interskribu - Skribas karaktrojn en la bufron. 
   // Unue, øi faras spacon.
   public void interskribu(char[] fonto, int indekso) {
      int n = fonto.length;
      malfermu_spacon(indekso, n);   // "malferm_spacon" adicicas n al nombro
      System.arraycopy(fonto,0,bufro,indekso,n);
      //for (int i = 0; i < n; i++, indekso++) {
      //   bufro[indekso] = fonto[i];
      //}
   }  // fino de "interskribu"

   // interskribu - Skribas karaktrojn en la bufron. 
   // Æi tiu versio skribas n karaktrojn.
   public void interskribu(char[] fonto, int indekso, int n) {
      malfermu_spacon(indekso, n);           // "malfermu_spacon" adicias n al nombro
      System.arraycopy(fonto,0,bufro,indekso,n);
   }  // fino de "interskribu"

   // Akiru tekston de la bufro. Ne eltondas la tekston.
   public String akiru_tekston(int indekso, int nombro) {
      return (new String(bufro,indekso,nombro));
   }  // fino de "akiru_tekston"
 
   // Akiru karaktron de la bufro.
   public char akiru_karaktron(int indekso) {
      return bufro[indekso];
   }  // fino de "akiru_karaktron"
 
   // Akiru karaktrojn de la bufro.
   public char[] akiru_karaktrojn(int indekso, int n) {
      char[]  karaktroj = new char[n];
      System.arraycopy(bufro,indekso,karaktroj,0,n);
      return karaktroj;
   }  // fino de "akiru_karaktrojn"
 

}  // fino de Bufro




