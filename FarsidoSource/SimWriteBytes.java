// This program writes out the bytes of a file as comma separated decimal numbers.
// Æi tiu programo elskribas la bitokojn de dosiero kiel komo-apartigitan dekumajn numberojn.
// Klivo  aýgusto 1999

import java.io.*;

class SimWriteBytes  {

   static FileInputStream  instream;

   static RandomAccessFile  outstream;

   static String  infilename, outfilename;

   static File   infile, outfile;

   public SimWriteBytes() {
   }

   public static void main (String args[]) {

      int count = 0;

      if (args.length > 1) {
         infilename = args[0];
         outfilename = args[1];
      }
      else {
         System.out.println("Write out bytes as comma separated decimal numbers.");
         System.out.println("Usage:     java SimWriteBytes   infilename  outfilename");
         System.out.println("Elskribu bitokojn kiel komo-apartigitajn dekumojn numerojn.");
         System.out.println("Uzo:       java SimWriteBytes   endosiero  eldosiero");
         System.out.println("Klivo 1999");
         System.out.println("");
         System.out.println("");
         return ;
    }


    // Open input file.  Malfermu "en"-dosieron.

      try {
         infile = new File(infilename);
      }
      catch (NullPointerException e) {
         System.out.println("Oops, couldn't open that file.");
         System.out.println("Ho ve, ne povis malfermi tiun dosieron.");
         return;
      }

      if (!infile.exists()) {
         System.out.println(infilename + " can't be found.");
         System.out.println(infilename + " estas netrovebla.");
         return;
      }
 
      try {
         instream = new FileInputStream(infile);
      }
      catch ( IOException e ) {
         System.out.println("A problem occurred while opening  " + infilename + e.getMessage());
         System.out.println("Problemo okazis dum malfermo de  " + infilename + e.getMessage());
         return;
      }


   // Open output file.  Malfermu "el"-dosieron.

      try {
         File outfile = new File(outfilename);
         outstream = new RandomAccessFile(outfile, "rw");
         outstream.seek((long)0);
      }
      catch (Exception e) {
         System.out.println("Could not open output file. ");
         System.out.println("Ne povis malfermi el-dosieron. ");
         return;
      }

      try {

         int thebyte;
         
         while ((thebyte = instream.read()) != -1) {
            outstream.writeBytes(Byte.toString((byte)(thebyte & 0xFF)) + ",");
            count++;
            if (count%20 == 0) outstream.writeBytes("\n");
         }

         outstream.writeBytes("\n");
         instream.close();
         outstream.close();
      }
      catch ( IOException e) {
         System.out.println("Error while writing. " + e.getMessage());
         System.out.println("Eraro du elskribado. " + e.getMessage());
      }

      System.out.println("\n >>>>>>>> finita <<<<<<<<<  " + count);

   }  // fino de "main"


}  // fino de "SimWriteBytes"

