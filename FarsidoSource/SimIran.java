/*
 * SimIran - Support for Persian language.
 * - Contains routines to convert between Unicode and Iran System.
 * SimIran - Subtenas Persan lingvon.
 * - Enhavas rutinojn por konverti inter Unikodo kaj Iran-Sistemo.
 *
 * Cleve Lendon (Klivo)
 * indriko@yahoo.com
 * http://purl.oclc.org/net/klivo/
 * 2000/12/01
 */

import java.awt.*;
import java.util.*;

public class SimIran {


   /*
    * Convert Unicode code to Iran System
    * Konvertu de Unikodo al IranSistemo
    */
   public final static int convertUniIran(char before, char uni, char after) {

      boolean isolated = false;      /* izolita litero */
      boolean finall = false;        /* litero æe fino de vorto */
      boolean medial = false;        /* litero en mezo de vorto */
      boolean initial = false;       /* litero æe komenco de vorto */
      boolean before_is_letter = false;  /* antaýa signo estas (persa) litero */
      boolean after_is_letter = false;    /* posta signo estas (persa) litero */

      if (uni < 0x0600) return uni;
      /* Arab numbers / Arabaj ciferoj */
      if (uni >= 0x6F0 && uni <= 0x6F9) return (uni - 0x6f0 + 0x80);

      /* Assume that a Persian letter is between the codes 0x0620 and 0x06D0 */
      /* Supozu ke Persa litero havas kodon inter 0x0620 kaj 0x06D0. */
      if (before >= 0x0620 && before <= 0x06D0) before_is_letter = true;
      if (after >= 0x0620 && after <= 0x06D0) after_is_letter = true;

      if (before_is_letter && after_is_letter) medial = true;
      else if (before_is_letter && !after_is_letter) finall = true;
      else if (!before_is_letter && after_is_letter) initial = true;
      else isolated = true;

      /* I can't see any systematic relationship between Unicode and 
         Iran System, so I'll just test the letters individually. */
      /* Mi ne rimarkas sisteman rilaton inter Unikodo kaj Iransistemo,
         tial mi simple testos literojn individue. */

      /* arabic comma */
      if (uni == 0x060C) return 0x8A;
      /* tatweel */
      if (uni == 0x0640) return 0x8B;
      /* arabic question mark / demando signo */
      if (uni == 0x061F) return 0x8C;

      /* alef with madda */
      if (uni == 0x0622) return 0x8D;
      /* yeh with hamza */
      if (uni == 0x0626) return 0x8E;  /* ??? */
      /* arabic letter with hamza */
      if (uni == 0x0621) return 0x8f;

      /* alef */
      if (uni == 0x0627 && (isolated || initial)) return 0x90;
      if (uni == 0x0627 && (medial || finall)) return 0x91;

      /* alef with hamza above */
      if (uni == 0x0623 && (isolated || initial)) return 0x90;
      if (uni == 0x0623 && (medial || finall)) return 0x91;

      /* alef with hamza below */
      if (uni == 0x0625 && (isolated || initial)) return 0x90;
      if (uni == 0x0625 && (medial || finall)) return 0x91;

      /* beh */
      if (uni == 0x0628 && (finall || isolated)) return 0x92;
      if (uni == 0x0628 && (initial || medial)) return 0x93;
      /* peh */
      if (uni == 0x067E && (finall || isolated)) return 0x94;
      if (uni == 0x067E && (initial || medial)) return 0x95;
      /* teh */
      if (uni == 0x062A && (finall || isolated)) return 0x5C;
      if (uni == 0x062A && (initial || medial)) return 0x9F;
      /* theh */
      if (uni == 0x062B && (finall || isolated)) return 0x98;
      if (uni == 0x062B && (initial || medial)) return 0x99;
      /* jeem */
      if (uni == 0x062C && (finall || isolated)) return 0x9A;
      if (uni == 0x062C && (initial || medial)) return 0x9B;
      /* cheh */
      if (uni == 0x0686 && (finall || isolated)) return 0x9C;
      if (uni == 0x0686 && (initial || medial)) return 0x9D;
      /* hah */
      if (uni == 0x062D && (finall || isolated)) return 0x9E;
      if (uni == 0x062D && (initial || medial)) return 0x9F;
      /* khah */
      if (uni == 0x062E && (finall || isolated)) return 0xA0;
      if (uni == 0x062E && (initial || medial)) return 0xA1;

      /* dal */
      if (uni == 0x062F) return 0xA2;
      /* thal */
      if (uni == 0x0630) return 0xA3;
      /* reh */
      if (uni == 0x0631) return 0xA4;
      /* zain */
      if (uni == 0x0632) return 0xA5;
      /* jeh */
      if (uni == 0x0698) return 0xA6;

      /* seen */
      if (uni == 0x0633 && (finall || isolated)) return 0xA7;
      if (uni == 0x0633 && (initial || medial)) return 0xA8;
      /* sheen */
      if (uni == 0x0634 && (finall || isolated)) return 0xA9;
      if (uni == 0x0634 && (initial || medial)) return 0xAA;
      /* sad */
      if (uni == 0x0635 && (finall || isolated)) return 0xAB;
      if (uni == 0x0635 && (initial || medial)) return 0xAC;
      /* dad */
      if (uni == 0x0636 && (finall || isolated)) return 0xAD;
      if (uni == 0x0636 && (initial || medial)) return 0xAE;

      /* arabic letter tah */
      if (uni == 0x0637) return 0xAF;

      /* arabic letter zah */
      if (uni == 0x0638) return 0xE0;

      /* ain */ 
      if (uni == 0x0639 && isolated) return 0xE1;
      if (uni == 0x0639 && finall) return 0xE2;
      if (uni == 0x0639 && medial) return 0xE3;
      if (uni == 0x0639 && initial) return 0xE4;
      /* ghain */ 
      if (uni == 0x063A && isolated) return 0xE5;
      if (uni == 0x063A && finall) return 0xE6;
      if (uni == 0x063A && medial) return 0xE7;
      if (uni == 0x063A && initial) return 0xE8;

      /* feh */
      if (uni == 0x0641 && (finall || isolated)) return 0xE9;
      if (uni == 0x0641 && (initial || medial)) return 0xEA;
      /* qah */
      if (uni == 0x0642 && (finall || isolated)) return 0xEB;
      if (uni == 0x0642 && (initial || medial)) return 0xEC;

      /* keheh = kaf  6A9 is probably wrong / 6A9 probable estas maløusta */
      if (uni == 0x06A9 && (finall || isolated)) return 0xED;
      if (uni == 0x06A9 && (initial || medial)) return 0xEE;
      /* keheh = kaf */
      if (uni == 0x0643 && (finall || isolated)) return 0xED;
      if (uni == 0x0643 && (initial || medial)) return 0xEE;

      /* gaf */
      if (uni == 0x06AF && (finall || isolated)) return 0xEF;
      if (uni == 0x06AF && (initial || medial)) return 0xF0;
      /* lam */
      if (uni == 0x0644 && (finall || isolated)) return 0xF1;
      if (uni == 0x0644 && after == 0x0627) return 0xF2; /* ligature with alef */
      if (uni == 0x0644 && (initial || medial)) return 0xF3;

      /* meem */
      if (uni == 0x0645 && (finall || isolated)) return 0xF4;
      if (uni == 0x0645 && (initial || medial)) return 0xF5;
      /* noon */
      if (uni == 0x0646 && (finall || isolated)) return 0xF6;
      if (uni == 0x0646 && (initial || medial)) return 0xF7;

      /* waw */
      if (uni == 0x0648) return 0xF8;

      /* heh */
      if (uni == 0x0647 && (finall || isolated)) return 0xF9;
      if (uni == 0x0647 && medial) return 0xFA;
      if (uni == 0x0647 && initial) return 0xFB;

      /* farsi yeh */
      /*  Kial estas du kodoj por sama litero ??? */
      /*  Why are there two codes for the same leter? */
      if (uni == 0x06CC && finall) return 0xFC;
      if (uni == 0x06CC && isolated) return 0xFD;
      if (uni == 0x06CC && (initial || medial)) return 0xFE;

      /* farsi yeh */
      if (uni == 0x0649 && finall) return 0xFC;
      if (uni == 0x0649 && isolated) return 0xFD;
      if (uni == 0x0649 && (initial || medial)) return 0xFE;


      /* farsi yeh - two dots underneath */
      if (uni == 0x064A && finall) return 0xFC;
      if (uni == 0x064A && isolated) return 0xFD;
      if (uni == 0x064A && (initial || medial)) return 0xFE;


      /* no-break space */
      if (uni == 0xA0) return 0xFF;

      System.out.println("Ne povis konverti: >  " + uni + " " + Integer.toHexString((int)uni));

      return uni;

   }  // convertUniIran




   /*
    * Convert Iran System to Unicode 
    * Konvertu de Iransistemo al Unikodo
    */
   public final static int convertIranUni(byte vazhe) {

      int vn = vazhe & 0x00FF;
	  int iransys = 0;

      /* Arab numbers / Arabaj ciferoj */
      if (vn >= 0x30 && vn <= 0x39) return (vn - 0x30 + 0x660);

      /* arabic comma */
      if (iransys == 0x8a) return 0x060C;

      /* arabic tatweel */
      if (iransys == 0x8B) return 0x0640;

      /* arabic question mark / demandosigno */
      if (iransys == 0x8C) return 0x061F;

      /* alef with madda */
      if (iransys == 0x8D) return 0x0622;

      /* yeh with hamza */
      if (vn == 0x81 || vn == 0xE8) return 0x0626;

      /* hamza */
      if (iransys == 0x8F) return 0x0621;

      /* alef */ 
      if (iransys == 0x90 || iransys == 0x91) return 0x0627;

      /* beh */ 
      if (vn == 0x85 || vn == 0xA4 || vn == 0xCC || vn == 0x5B) return 0x0628;

      /* peh */ 
      if (iransys == 0x94 || iransys == 0x95DD) return 0x067E;

      /* teh */ 
      if (iransys == 0x86 || iransys == 0x9F || iransys == 0xCD || 
		  iransys == 0x5C || iransys == 0xC4 || iransys == 0xA5) return 0x062A;

      /* theh */ 
      if (iransys == 0x98 || iransys == 0x99) return 0x062B;

      /* jeem */ 
      if (iransys == 0x9A || iransys == 0x9B) return 0x062C;

      /* tcheh */ 
      if (iransys == 0x9C || iransys == 0x9D) return 0x0686;

      /* hah */ 
  //    if (iransys == 0x9E || iransys == 0x9F) return 0x062D;

      /* khah */ 
      if (iransys == 0xA0 || iransys == 0xA1) return 0x062E;

      /* dal */ 
      if (vn == 0xAC || vn == 0x64) return 0x062F;

      /* thal */ 
      if (vn == 0xAD || vn == 0x65) return 0x0630;

      /* re */ 
      if (vn == 0xAE || vn == 0x66) return 0x0631;

      /* zain */ 
      if (vn == 0xAF || vn == 0x67) return 0x0632;

      /* je */ 
      if (vn == 0xB0 || vn == 0x68) return 0x0698;

      /* seen */ 
      if (vn == 0x8D || vn == 0xD4 || vn == 0xB1 || vn == 0x69) return 0x0633;

      /* sheen */ 
      if (iransys == 0xA9 || iransys == 0xAA) return 0x0634;

      /* sad */ 
      if (iransys == 0xAB || iransys == 0xAC) return 0x0635;

      /* dad */ 
      if (iransys == 0xAD || iransys == 0xAEE) return 0x0636;

      /* ta */ 
      if (iransys == 0xAF) return 0x0637;

      /* za */ 
      if (iransys == 0xE0) return 0x0638;

      /* ain */ 
//      if (iransys >= 0xE1 && iransys <= 0xE4) return 0x0639;

      /* ghain */ 
//      if (iransys >= 0xE5 && iransys <= 0xE8) return 0x063A;

      /* feh */ 
      if (vn == 0x95 || vn == 0x71 || vn == 0xDC) return 0x0641;

      /* qaf */ 
      if (iransys == 0xEB || iransys == 0xEC) return 0x0642;

      /* keheh (kaf?) */ 
      if (iransys == 0xED || iransys == 0xEE) return 0x0643;   /* 6A9 is probably wrong, 6A9 probable maløustas */

      /* gaf */ 
      if (iransys == 0xEF || iransys == 0xF0) return 0x06AF;

      /* lam */ 
//      if (iransys >= 0xF1 && iransys <= 0xF3) return 0x0644;

      /* meem */ 
      if (iransys == 0xF4 || iransys == 0xF5) return 0x0645;

      /* noon */ 
      if (iransys == 0xF6 || iransys == 0xF7) return 0x0646;

      /* waw / vav */ 
      if (iransys == 0xF8) return 0x0648;

      /* heh */ 
 //     if (iransys >= 0xF9 && iransys <= 0xFB) return 0x0647;

      /* farsi yeh */ 
      if (vn == 0xA0 || vn == 0x9E || vn == 0xC5 || 
		  vn == 0xC3 || vn == 0x7B || iransys == 0x7D ||
		  vn == 0xE7 || vn == 0xE5) return 0x0649;

      /* no break space */ 
      if (iransys == 0xFF) return 0xA0;

      return iransys;

   }


   /*
    * Convert Unicode code to VazheNegar
    * Designed by Hooman Baradaran - Last Update: 17/07/2002
    */
   public final static int convertUniVazhe(char before, char uni, char after) {

      boolean isolated = false;      /* izolita litero */
      boolean finall = false;        /* litero æe fino de vorto */
      boolean medial = false;        /* litero en mezo de vorto */
      boolean initial = false;       /* litero æe komenco de vorto */
      boolean before_is_letter = false;  /* antaýa signo estas (persa) litero */
      boolean after_is_letter = false;    /* posta signo estas (persa) litero */

	// Numbers
	  if (uni >= 0x22 && uni <= 0x3F) return uni;
	  /* Arab numbers / Arabaj ciferoj */
      if (uni >= 0x6F0 && uni <= 0x6F9) return (uni - 0x6C0);

      /* Assume that a Persian letter is between the codes 0x0620 and 0x06D0 */
      if (before >= 0x0620 && before <= 0x06D0) before_is_letter = true;
      if (after >= 0x0620 && after <= 0x06D0) after_is_letter = true;

      if (before_is_letter && after_is_letter) medial = true;
      else if (before_is_letter && !after_is_letter) finall = true;
      else if (!before_is_letter && after_is_letter) initial = true;
      else isolated = true;

      /* arabic comma */
      if (uni == 0x060C) return 0x2C;
      /* tatweel */
      if (uni == 0x0640) return 0x83;
      /* arabic question mark / demando signo */
      if (uni == 0x061F) return 0x3F;

	  /* Paranthesis */
      if (uni == 0x0028) return 0x28;
      if (uni == 0x0029) return 0x29;
      /* Double Paranteses */
      if (uni == 0x00AB) return 0x41;
      if (uni == 0x00BB) return 0x40;

      /* arabic letter with hamza */
      if (uni == 0x0621) return 0x59;

      /* alef */
      if (uni == 0x0627 && (isolated || initial)) return 0x5A;
      if (uni == 0x0627 && (finall || medial)) return 0xA3;

      /* alef with madda */
      if (uni == 0x0622 && (isolated || initial)) return 0xF2;
      if (uni == 0x0622 && (finall || medial)) return 0xF3;

      /* alef with hamza above */
      if (uni == 0x0623 && (isolated || initial)) return 0x7E;
      if (uni == 0x0623 && (finall || medial)) return 0xC6;

      /* alef with hamza below */
      if (uni == 0x0625 && (isolated || initial)) return 0x5A;
      if (uni == 0x0625 && (finall || medial)) return 0xA3;

      /* beh */
      if (uni == 0x0628 && (finall)) return 0xCC;
      if (uni == 0x0628 && (initial)) return 0x85;
      if (uni == 0x0628 && (isolated)) return 0x5B;
      if (uni == 0x0628 && (medial)) return 0xA4;

      /* peh */
      if (uni == 0x067E && (finall)) return 0xCF;
      if (uni == 0x067E && (initial)) return 0x88;
      if (uni == 0x067E && (isolated)) return 0x5E;
      if (uni == 0x067E && (medial)) return 0xA7;

      /* teh */
      if (uni == 0x062A && (finall)) return 0xCD;
      if (uni == 0x062A && (initial)) return 0x86;
      if (uni == 0x062A && (isolated)) return 0x5C;
      if (uni == 0x062A && (medial)) return 0xC4;

      /* theh */
      if (uni == 0x062B && (finall)) return 0xCE;
      if (uni == 0x062B && (initial)) return 0x87;
      if (uni == 0x062B && (isolated)) return 0x5D;
      if (uni == 0x062B && (medial)) return 0xA6;

      /* jeem */
      if (uni == 0x062C && (finall)) return 0xD0;
      if (uni == 0x062C && (initial)) return 0x89;
      if (uni == 0x062C && (isolated)) return 0x5F;
      if (uni == 0x062C && (medial)) return 0xA8;

      /* cheh */
      if (uni == 0x0686 && (finall)) return 0xD3;
      if (uni == 0x0686 && (initial)) return 0x8C;
      if (uni == 0x0686 && (isolated)) return 0x63;
      if (uni == 0x0686 && (medial)) return 0xAB;

      /* hah */
      if (uni == 0x062D && (finall)) return 0xD1;
      if (uni == 0x062D && (initial)) return 0x8A;
      if (uni == 0x062D && (isolated)) return 0x61;
      if (uni == 0x062D && (medial)) return 0xA9;

      /* khah */
      if (uni == 0x062E && (finall)) return 0xD2;
      if (uni == 0x062E && (initial)) return 0x8B;
      if (uni == 0x062E && (isolated)) return 0x62;
      if (uni == 0x062E && (medial)) return 0xAA;

      /* dal */
      if (uni == 0x062F && (finall || medial)) return 0xAC;
      if (uni == 0x062F && (isolated || initial)) return 0x64;

      /* thal */
      if (uni == 0x0630 && (finall || medial)) return 0xAD;
      if (uni == 0x0630 && (isolated || initial)) return 0x65;

      /* reh */
      if (uni == 0x0631 && (finall || medial)) return 0xAE;
      if (uni == 0x0631 && (isolated || initial)) return 0x66;

      /* zain */
      if (uni == 0x0632 && (finall || medial)) return 0xAF;
      if (uni == 0x0632 && (isolated || initial)) return 0x67;

      /* jeh */
      if (uni == 0x0698 && (finall || medial)) return 0xB0;
      if (uni == 0x0698 && (isolated || initial)) return 0x68;

      /* seen */
      if (uni == 0x0633 && (finall)) return 0xD4;
      if (uni == 0x0633 && (initial)) return 0x8D;
      if (uni == 0x0633 && (isolated)) return 0x69;
      if (uni == 0x0633 && (medial)) return 0xB1;

      /* sheen */
      if (uni == 0x0634 && (finall)) return 0xD5;
      if (uni == 0x0634 && (initial)) return 0x8E;
      if (uni == 0x0634 && (isolated)) return 0x6A;
      if (uni == 0x0634 && (medial)) return 0xB2;

      /* sad */
      if (uni == 0x0635 && (finall)) return 0xD6;
      if (uni == 0x0635 && (initial)) return 0x8F;
      if (uni == 0x0635 && (isolated)) return 0x6B;
      if (uni == 0x0635 && (medial)) return 0xB3;

      /* dad */
      if (uni == 0x0636 && (finall)) return 0xD7;
      if (uni == 0x0636 && (initial)) return 0x90;
      if (uni == 0x0636 && (isolated)) return 0x6C;
      if (uni == 0x0636 && (medial)) return 0xB4;

      /* arabic letter ta */
      if (uni == 0x0637 && (finall)) return 0xD8;
      if (uni == 0x0637 && (initial)) return 0x91;
      if (uni == 0x0637 && (isolated)) return 0x6D;
      if (uni == 0x0637 && (medial)) return 0xB5;

      /* arabic letter zah */
      if (uni == 0x0638 && (finall)) return 0xD9;
      if (uni == 0x0638 && (initial)) return 0x92;
      if (uni == 0x0638 && (isolated)) return 0x6E;
      if (uni == 0x0638 && (medial)) return 0xB6;

      /* ain */ 
      if (uni == 0x0639 && isolated) return 0x6F;
      if (uni == 0x0639 && finall) return 0xDA;
      if (uni == 0x0639 && medial) return 0xB7;
      if (uni == 0x0639 && initial) return 0x93;

      /* ghain */ 
      if (uni == 0x063A && isolated) return 0x70;
      if (uni == 0x063A && finall) return 0xDB;
      if (uni == 0x063A && medial) return 0xB8;
      if (uni == 0x063A && initial) return 0x94;

      /* feh */
      if (uni == 0x0641 && (finall)) return 0xDC;
      if (uni == 0x0641 && (initial)) return 0x95;
      if (uni == 0x0641 && (isolated)) return 0x71;
      if (uni == 0x0641 && (medial)) return 0xB9;

	  /* qah */
      if (uni == 0x0642 && (finall)) return 0xDD;
      if (uni == 0x0642 && (initial)) return 0x96;
      if (uni == 0x0642 && (isolated)) return 0x72;
      if (uni == 0x0642 && (medial)) return 0xBA;

      /* kaf */
      if (uni == 0x0643 && (finall)) return 0xDF;
      if (uni == 0x0643 && (initial)) return 0x98;
      if (uni == 0x0643 && (isolated)) return 0x74;
      if (uni == 0x0643 && (medial)) return 0xBC;
      if (uni == 0x06A9 && (finall)) return 0xDF;
      if (uni == 0x06A9 && (initial)) return 0x98;
      if (uni == 0x06A9 && (isolated)) return 0x74;
      if (uni == 0x06A9 && (medial)) return 0xBC;

      /* gaf */
      if (uni == 0x06AF && (finall)) return 0xE0;
      if (uni == 0x06AF && (initial)) return 0x99;
      if (uni == 0x06AF && (isolated)) return 0x75;
      if (uni == 0x06AF && (medial)) return 0xBD;

	  /* lam */
      if (uni == 0x0644 && (finall)) return 0xE1;
      if (uni == 0x0644 && (initial)) return 0x9A;
      if (uni == 0x0644 && (isolated)) return 0x76;
      if (uni == 0x0644 && (medial)) return 0xBE;

      /* meem */
      if (uni == 0x0645 && (finall)) return 0xE2;
      if (uni == 0x0645 && (initial)) return 0x9B;
      if (uni == 0x0645 && (isolated)) return 0x77;
      if (uni == 0x0645 && (medial)) return 0xBF;

	  /* noon */
      if (uni == 0x0646 && (finall)) return 0xE3;
      if (uni == 0x0646 && (initial)) return 0x9C;
      if (uni == 0x0646 && (isolated)) return 0x78;
      if (uni == 0x0646 && (medial)) return 0xC0;

      /* waw */
      if (uni == 0x0648 && (finall || medial)) return 0xC2;
      if (uni == 0x0648 && (isolated || initial)) return 0x7A;
      if (uni == 0x0676) return 0xC8;

      /* waw with Hamzeh */
      if (uni == 0x0624 && (finall || medial)) return 0xC8;
      if (uni == 0x0624 && (isolated || initial)) return 0x80;

      /* heh */
      if (uni == 0x0647 && (finall)) return 0xE4;
      if (uni == 0x0647 && (isolated)) return 0x79;
      if (uni == 0x0647 && medial) return 0xC1;
      if (uni == 0x0647 && initial) return 0x9D;

      /* farsi yeh */
      /*  Why are there some many codes for the same leter? */
      if (uni == 0x0649 && finall) return 0xE7;
      if (uni == 0x0649 && isolated) return 0x7D;
      if (uni == 0x0649 && initial) return 0xA0;
      if (uni == 0x0649 && medial) return 0xC3;

      if (uni == 0x06CC && finall) return 0xE7;
      if (uni == 0x06CC && isolated) return 0x7D;
      if (uni == 0x06CC && initial) return 0xA0;
      if (uni == 0x06CC && medial) return 0xC3;

/*
	  if (uni == 0xFBFC && finall) return 0xE5;
      if (uni == 0xFBFC && isolated) return 0x7B;
      if (uni == 0xFBFC && (initial)) return 0xA0;
      if (uni == 0xFBFC && (medial)) return 0xC3;
      if (uni == 0xFBFD && finall) return 0xE5;
      if (uni == 0xFBFD && isolated) return 0x7B;
      if (uni == 0xFBFD && (initial)) return 0xA0;
      if (uni == 0xFBFD && (medial)) return 0xC3;
      if (uni == 0xFEEF && finall) return 0xE5;
      if (uni == 0xFEEF && isolated) return 0x7B;
      if (uni == 0xFEEF && (initial)) return 0xA0;
      if (uni == 0xFEEF && (medial)) return 0xC3;
      if (uni == 0xFEF0 && finall) return 0xE5;
      if (uni == 0xFEF0 && isolated) return 0x7B;
      if (uni == 0xFEF0 && (initial)) return 0xA0;
      if (uni == 0xFEF0 && (medial)) return 0xC3;
*/


      /* yeh - two dots underneath */
      if (uni == 0x064A && finall) return 0xE5;
      if (uni == 0x064A && isolated) return 0x7B;
      if (uni == 0x064A && (initial)) return 0xA0;
      if (uni == 0x064A && (medial)) return 0xC3;

      /* yeh - hamzeh above */
      if (uni == 0x0626 && finall) return 0xE8;
      if (uni == 0x0626 && isolated) return 0x7D;
      if (uni == 0x0626 && (initial)) return 0xA1;
      if (uni == 0x0626 && (medial)) return 0xC9;

      /* no-break space */
      if (uni == 0xA0) return 0xFF;

      System.out.println("Ne povis konverti: >  " + uni + " " + Integer.toHexString((int)uni));

      return uni;

   }  // convertUniVazhe

   /*
    * Convert Vazhe Negar to Unicode 
    * Last Update: 17/07/2002 by Hooman Baradaran
    */
   public final static int convertVazheUni(byte vazhe) {

      int vazhenegar = vazhe & 0x00FF;

     // if (vazhenegar < 0x7F) return vazhenegar;
      /* Arab numbers / Arabaj ciferoj */
      if (vazhenegar >= 0x30 && vazhenegar <= 0x39) return (vazhenegar + 0x6C0);
	  
	  /* open and close paranthesis */
	  
	  if (vazhenegar == 0x28) return 0x0029;
	  if (vazhenegar == 0x29) return 0x0028;

       /* Double Paranteses */
	  if (vazhenegar == 0x40) return 0x00AB;
	  if (vazhenegar == 0x41) return 0x00BB;

      /* arabic comma */
      if (vazhenegar == 0x2C) return 0x060C;

      /* arabic tatweel */
      if (vazhenegar == 0x83) return 0x0640;

      /* arabic question mark / demandosigno */
      if (vazhenegar == 0x3F) return 0x061F;

      /* alef with madda */
      if (vazhenegar == 0xF2 || vazhenegar == 0xF3 || vazhenegar == 0xF4) return  0x0622;
	  
	  /*alef with hamza above*/
	  if (vazhenegar == 0xC6 || vazhenegar == 0x7E) return 0x0627;
	  
	  /*
	  
	  

      /* yeh with hamza above */
      //if (vazhenegar == 0x8E) return 0x0626;
	  
	  /* initial */
	  if (vazhenegar == 0xA1 || vazhenegar == 0xA2) return 0x0626;
	  /* medial */
	  if (vazhenegar == 0xC9 || vazhenegar == 0xCB) return 0x0626;
	  /* final */
	  if (vazhenegar == 0x81) return 0x0626;
	  
	  /* isolated */
	  if (vazhenegar == 0xE8) return 0x0626;
	  

      /* hamza */
      if (vazhenegar == 0x59) return 0x0621;
	  
	  /*FARSI Do ZEER / ARABIC KASRATAN*/
	  if (vazhenegar == 0x4C) return 0x064D;
	  
	  /*FARSI DO ZEBAR / ARABIC FATHATAN*/
	  if (vazhenegar == 0x4A) return 0x064B;
	  
	  /*FARSI DO PEESH / ARABIC DAMMATAN*/
	  if (vazhenegar == 0x4B) return 0x064C;
	  
	  /* FATHA*/
	  if (vazhenegar == 0x47) return 0x064E;
	  
	  /* DAMMA*/
	  if (vazhenegar == 0x48) return 0x064F;
	  
	  /* KASRA*/
	  if (vazhenegar == 0x49) return 0x0650;
	  
	  /* SHADDA*/
	  if (vazhenegar == 0x4E) return 0x0651;
	  
	  /* SOKUN*/
	  if (vazhenegar == 0x4D) return 0x0652;
	  
	  
	  
	  
      //if (vazhenegar == 0x4A) return 0x627/0x0644;
	  
      /* alef */ 
	  
	  /*alef final*/
	  if (vazhenegar ==0xA3 || vazhenegar == 0xF5) return 0x0627 ;
	  
	  /*alef isolated*/
      if (vazhenegar ==0x5A || vazhenegar == 0xF4) return  0x0627 ;
	  
	  
      /* beh initial */
	  if (vazhenegar == 0x85 ) return 0x0628;
	  
	  /* beh medial */
	  if (vazhenegar == 0xA4 ) return 0x0628;
	  
	  /* beh final */
	  if (vazhenegar == 0xCC) return 0x0628;
	  /* beh isolated */
	  
	  if (vazhenegar == 0x5B) return 0x0628;
	  

	  /* peh initial */
	  if (vazhenegar == 0x88) return 0x067E;
	  
	  /* peh medial */
	  if (vazhenegar == 0xA7) return 0x067E;
	  
	  /* peh final) */
	  if (vazhenegar == 0xCF) return 0x067E;
	  
	  /* peh isolated */
	  if (vazhenegar == 0x5E) return 0x067E;
	  

      /* teh initial */
      if (vazhenegar == 0x86 || vazhenegar == 0x9F ) return 0x062A;
      
      /* teh medial */
      if (vazhenegar == 0xC4 || vazhenegar == 0xA5 ) return 0x062A;
      
      /* teh final) */
      if (vazhenegar == 0xCD) return 0x062A;
      
      /* teh isolated */
      if (vazhenegar == 0x5C) return 0x062A;


      /* theh initial */
      if (vazhenegar == 0x87) return 0x062B;
      
      /* theh medial */
      if (vazhenegar == 0xA6 ) return 0x062B;
      
      /* theh finall) */
      if (vazhenegar == 0xCE) return 0x062B;
      
      /* theh isolated */
      if (vazhenegar == 0x5D) return 0x062B;
	  

      /* jeem initial */
      if (vazhenegar == 0x89 ) return 0x062C;
      
      /* jeem medial */
      if (vazhenegar == 0xA8 ) return 0x062C;
      
      /* jeem final) */
      if (vazhenegar == 0xD0) return 0x062C;
      
      /* jeem isolated */
      if (vazhenegar == 0x5F) return 0x062C;
	  

      /* tcheh initial */
      if (vazhenegar == 0x8C ) return 0x0686;
      
      /* tcheh medial */
      if (vazhenegar == 0xAB ) return 0x0686;
      
      /* tcheh final) */
      if (vazhenegar == 0xD3) return 0x0686;
      
      /* tcheh isolated */
      if (vazhenegar == 0x63) return 0x0686;


      /* hah initial */
      if (vazhenegar == 0x8A ) return 0x062D;

      
      /* hah medial */
      if (vazhenegar == 0xA9 ) return 0x062D;
      
      /* hah fianl) */
      if (vazhenegar == 0xD1) return 0x062D;
      
      /* hah isolated */
      if (vazhenegar == 0x61) return 0x062D;


      /* khah initial */
      if (vazhenegar == 0x8B ) return 0x062E;
      
      /* khah medial */
      if (vazhenegar == 0xAA ) return 0x062E;
      
      /* khah fianl) */
      if (vazhenegar == 0xD2) return 0x062E;
      
      /* khah isolated */
      if (vazhenegar == 0x62) return 0x062E;
	  

      /* dal isolated*/ 
      if (vazhenegar == 0x64) return 0x062F;
	  
	  /* dal final */
	  if (vazhenegar == 0xAC) return 0x062F;
	  
	  
      /* thal isolated */ 
      if (vazhenegar == 0x65) return 0x0630;
	  
	  /* thal final */
	  if (vazhenegar == 0xAD) return 0x0630;
	  
	  
      /* re isolated*/ 
      if (vazhenegar == 0x66) return 0x0631;
	  
      /* re final*/ 
      if (vazhenegar == 0xAE) return 0x0631;
	  
      
      /* zain isolated*/ 
      if (vazhenegar == 0x67) return 0x0632;
      
      /* zain final*/ 
      if (vazhenegar == 0xAF) return 0x0632;
	  

      /* je isolated*/ 
      if (vazhenegar == 0x68) return 0x0698;
      
      /* je final*/ 
      if (vazhenegar == 0xB0) return 0x0698;
      

      /* seen */ 
	  
	  /* seen initial */
	  if (vazhenegar == 0x8D ) return 0x0633;
	  
	  /* seen medial */
	  if (vazhenegar == 0xB1 ) return 0x0633;
	  
	  /* seen final */
	  if (vazhenegar == 0xD4 ) return 0x0633;
	  
	  /* seen isolated */
	  if (vazhenegar == 0x69 ) return 0x0633;
	  

      /* sheen */ 
	  
      /* sheen initial */
      if (vazhenegar == 0x8E ) return 0x0634;
      
      /* sheen medial */
      if (vazhenegar == 0xB2 ) return 0x0634;
      
      /* sheen final */
      if (vazhenegar == 0xD5 ) return 0x0634;
      
      /* sheen isolated */
      if (vazhenegar == 0x6A ) return 0x0634;
	  

      /* sad */ 
      /* sad initial */
      if (vazhenegar == 0x8F ) return 0x0635;
      
      /* sad medial */
      if (vazhenegar == 0xB3 ) return 0x0635;
      
      /* sad final */
      if (vazhenegar == 0xD6 ) return 0x0635;
      
      /* sad isolated */
      if (vazhenegar == 0x6B ) return 0x0635;


      /* dad */ 
      /* dad initial */
      if (vazhenegar == 0x90 ) return 0x0636;
      
      /* dad medial */
      if (vazhenegar == 0xB4 ) return 0x0636;
      
      /* dad final */
      if (vazhenegar == 0xD7 ) return 0x0636;
      
      /* dad isolated */
      if (vazhenegar == 0x6C ) return 0x0636;
	  

      /* ta initial*/ 
      if (vazhenegar == 0x91) return 0x0637;
	  
	  /* ta medial */
	  if (vazhenegar == 0xB5) return 0x0637;
	  
	  /* ta final*/ 
	  if (vazhenegar == 0xD8) return 0x0637;
	  
	  /* ta isolated*/ 
	  if (vazhenegar == 0x6D) return 0x0637;

	  
      /* za initial*/ 
      if (vazhenegar == 0x92) return 0x0638;
	  
	  /* za medial */
	  if (vazhenegar == 0xB6) return 0x0638;
      
      /* za final*/ 
      if (vazhenegar == 0xD9) return 0x0638;
      
      /* za isolated*/ 
      if (vazhenegar == 0x6E) return 0x0638;
	  

      /* ain initial*/ 
	  if (vazhenegar == 0x93) return 0x0639;
	  
	  /* ain medial */
	  if (vazhenegar == 0xB7) return 0x0639;
	  
	  /* ain final */
	  if (vazhenegar == 0xDA) return 0x0639;
	  
	  /* ain isolated */
	  if (vazhenegar == 0x6F) return 0x0639;
	  
	  
      /* ghain initial*/ 
      if (vazhenegar == 0x94) return 0x063A;
      
      /* ghain medial */
      if (vazhenegar == 0xB8) return 0x063A;
      
      /* ghain final */
      if (vazhenegar == 0xDB) return 0x063A;
      
      /* ghain isolated */
      if (vazhenegar == 0x70) return 0x063A;
	  

	  /* feh initial */
	  if (vazhenegar == 0x95) return 0x0641;
	  
	  /* feh medial */
	  if (vazhenegar == 0xB9 ) return 0x0641;
	  
	  /* feh final */
	  if (vazhenegar == 0xDC) return 0x0641;
	  
	  /* feh isolated */
	  if (vazhenegar == 0x71) return 0x0641;

	  
      /* qaf initial */
      if (vazhenegar == 0x96) return 0x0642;
      
      /* qaf medial */
      if (vazhenegar == 0xBA ) return 0x0642;
      
      /* qaf final */
      if (vazhenegar == 0xDD) return 0x0642;
      
      /* qaf isolated */
      if (vazhenegar == 0x72) return 0x0642;
	  

      /* kaf initial */
      if (vazhenegar == 0x98 ) return 0x0643;
      
      /* kaf medial */
      if (vazhenegar == 0xBC ) return 0x0643;
      
      /* kaf final */
      if (vazhenegar == 0xDF ) return 0x0643;
      
      /* kaf isolated */
      if (vazhenegar == 0x74 ) return 0x0643;

      /* gaf */ 
      /* gaf initial */
      if (vazhenegar == 0x99 ) return 0x06AF;
      
      /* gaf medial */
      if (vazhenegar == 0xBD ) return 0x06AF;
      
      /* gaf final */
      if (vazhenegar == 0xE0 ) return 0x06AF;
      
      /* gaf isolated */
      if (vazhenegar == 0x75 ) return 0x06AF;

      /* lam */ 
      /* lam initial */
      if (vazhenegar == 0x9A ) return 0x644;
      
      /* lam medial */
      if (vazhenegar == 0xBE ) return 0x644;
      
      /* lam final */
      if (vazhenegar == 0xE1 ) return 0x644;
      
      /* lam isolated */
      if (vazhenegar == 0x76 ) return 0x644;
	  
	  
	  /* la final*/
	  if (vazhenegar == 0xEA || vazhenegar == 0xEC || vazhenegar == 0xEE || vazhenegar == 0xF0) return 0xFEFB;
	  
	  /* la isolated */	  
	  if (vazhenegar == 0xEB  || vazhenegar == 0xED || vazhenegar == 0xEF || vazhenegar == 0xF1) return 0xFEFC;
	  

      /* meem */ 
      /* meem initial */
      if (vazhenegar == 0x9B ) return 0x0645;
      
      /* meem medial */
      if (vazhenegar == 0xBF ) return 0x0645;
      
      /* meem final */
      if (vazhenegar == 0xE2 ) return 0x0645;
      
      /* meem isolated */
      if (vazhenegar == 0x77 ) return 0x0645;
	  
	  
      /* noon */ 
      /* noon initial */
      if (vazhenegar == 0x9C ) return 0x0646;
      
      /* noon medial */
      if (vazhenegar == 0xC0 ) return 0x0646;
      
      /* noon final */
      if (vazhenegar == 0xE3 ) return 0x0646;
      
      /* noon isolated */
      if (vazhenegar == 0x78 ) return 0x0646;
	  
	  
      /* waw / vav */ 
      /* waw final */
      if (vazhenegar == 0xC2) return 0x0648;
	  
	  /* Arabic HIGH HAMZA WAV */
	  if (vazhenegar == 0xC8) return 0x0676;
      
      /* waw isolated */
      if (vazhenegar == 0x7A || vazhenegar == 0x080) return 0x0648;

      /* heh */ 
 
      /* heh initial */
      if (vazhenegar == 0x9D ) return 0x0647;
      
      /* heh medial */
      if (vazhenegar == 0xC1 ) return 0x0647;
      
      /* heh final */
      if (vazhenegar == 0xE4 ) return 0x0647;
      
      /* heh isolated */
      if (vazhenegar == 0x79 ) return 0x0647;
	  
	  /* teh marbutah final */
	  
	  if (vazhenegar == 0xE6) return 0x06C3;
	  
	  /* teh marbutah isolated */
	  if (vazhenegar == 0x7C) return 0x0629;

      /* farsi yeh */ 
  
	  /* yeh initial */
	  if (vazhenegar == 0xA0 || vazhenegar == 0x9E ) return 0x064A ;
	  
	  /* yeh medial */
	  if (vazhenegar == 0xC3 || vazhenegar == 0xC5 ) return 0x064A ;
	  
	  /* yeh final */
	  if (vazhenegar == 0xE5 || vazhenegar == 0xE7 ) return 0x0649 ;
	  
	  /* yeh isolated */
	  if (vazhenegar ==0x7B || vazhenegar == 0x7D ) return 0x0649;
	  
	  	  
      /* no break space */ 
      if (vazhenegar == 0xFF) return 0xA0;

      return vazhenegar;
	  

   }
   // END VAZHE NEGAR TO UNICODE




}  // SimIran



