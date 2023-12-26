/*
 *
 */

import java.awt.*;
import java.util.*;
import java.awt.print.*;
import javax.swing.*;
import javax.swing.text.*;


public class PageDivider implements Pageable {

   int          number_of_pages;
   SimTextPane  text_pane;
   PageFormat   pf;      // Formato de Paøo

   public PageDivider (SimTextPane text_pane, PageFormat pf) {

      this.text_pane = text_pane;
      this.pf = pf;
   }

   public int getNumberOfPages() {
      return text_pane.numberOfPages(pf);
   }  // getNumberOfPages

   public PageFormat getPageFormat (int pageindex) {
      return pf;
   }

   public Printable getPrintable (int pageindex) {
      return (Printable)text_pane; 
   }

}  // PageDivider



