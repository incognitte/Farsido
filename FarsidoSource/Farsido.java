/*
 * @(#) Farsido (based on Simredo 3.31)
 * @author  Hooman Baradaran (Original Simredo by Klivo)
 * @version 3.33  - Beta 1.1  01/09/2002
 *
 * Farsido is an optimised version of Simredo3 for the Persian language.
 * It mainly supports VazheNegar conversion.
 *
 * Hooman Baradaran <www.hoomanb.com>
 *
 * Simredo3 is a simple open-source swing unicode editor.
 * The original Simredo3 is designed by
 * Cleve Lendon (Klivo)
 * indriko@yahoo.com
 * http://purl.oclc.org/net/klivo/
 *
 */

/*
 * Changed in Farsido
 * VazheNegar Conversion to and from Unicode
 * Optimised interface for the Persian language.
 *


 */

import java.awt.*;
import java.io.*;
import java.util.*;
import java.awt.event.*;
import java.awt.print.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.text.html.*;

class Farsido extends JPanel implements ActionListener, DocumentListener {

   private static ResourceBundle menu_resources;           // Rimedoj por internaciigo.
   private static ResourceBundle encoding_resources;       // Listo de kodaroj.
   private static ResourceBundle esp_encoding_resources;   // Listo de kodaroj, en Esperanto

   static {
      try {
         menu_resources = ResourceBundle.getBundle("Farsido",Locale.getDefault());
      } catch (MissingResourceException mre) {
         System.err.println("Can't find Farsido.properties");
         System.err.println("Ne povas trovi Farsido.properties");
         System.exit(1);
      }
      try {
         encoding_resources = ResourceBundle.getBundle("Encodings",Locale.getDefault());
      } catch (MissingResourceException mre) {
         System.err.println("Can't find Encodings.properties");
         System.err.println("Ne povas trovi Encodings.properties");
         System.exit(1);
      }
      try {
         esp_encoding_resources = ResourceBundle.getBundle("Kodaroj",Locale.getDefault());
      } catch (MissingResourceException mre) {
         System.err.println("Can't find Kodaroj.properties");
         System.err.println("Ne povas trovi Kodaroj.properties");
         System.exit(1);
      }
   }


   static JFrame   frame;    // kadro por la programo
   static Farsido Farsido;
   static String   current_language = "esperanto";   // aktiva lingvo
   static String   class_path = ".";      // Dosierujo de la Øavaj klasoj.


   public static void main (String[] args) {
	  System.out.println("Farsido 3.33 starts up slowly. Please wait.");
      frame = new JFrame();      // kreu kadron
      frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

      // Get the class path. We need this to find simlist.txt and simfont.txt .
      // Trovu la klasdosierujon. Ni bezonas øin por trovi simlist.txt kaj simfont.txt
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
      System.err.println("classpath   " + class_path);
      // Get language parameter. Akiru lingvan parametron.
      if (args.length > 0) {
         current_language = SimCon.toLower(args[0]);
      }
      else {
         // If the Esperanto dictionary is in the same directory,
         // open make this an Esperanto editor.
         // Se la Esperanta vortaro estas en la sama dosierujo,
         // malfermu kiel Esperanta redaktilo.
         File vortarodosiero = new File(class_path, "vortaro.dat");
         if (vortarodosiero.exists()) {
            current_language = "esperanto";
         }
         else {
            current_language = "english";
         }
      }
      Farsido = new Farsido();
      frame.setTitle("Farsido");
      frame.setBackground(simpurple);  
      frame.getContentPane().add(Farsido);
      frame.addWindowListener(Farsido.simcloser);
      frame.pack();
      frame.setBounds(40,40,640,480);   // pozicio kaj grandeco de la kadro
      frame.show();             // montru la kadron
      //System.out.println("java.home " + System.getProperty("java.home"));
      //System.out.println("java.class.path " + System.getProperty("java.class.path"));
      //System.out.println("user.name " + System.getProperty("user.name"));
      //System.out.println("user.home " + System.getProperty("user.home"));
      //System.out.println("user.dir " + System.getProperty("user.dir"));
   } // main 


   //////////////////////////////////////////
   // Program status.

   private static Stack   file_list = new Stack();   // list of previously edited files
                                                  // listo de antaýe redaktitaj dosieroj

   private Action[]          action_list;        // listo de agoklasoj
   private Hashtable         action_hash;        // asocia tabelo por agoklasoj - nomo -> klaso

   private JFileChooser        file_chooser;         // dosiero-dialogo
   private String              current_file_name   = null;    // nomo de la nuna dokumento
   private String              file_converted_from = null;    // originala formato de la dokumento

   private char[]              encryption_key = null;         // kriptiga þlosilo

   private SearchDialog        search_dialog;        // dialogo por seræi tekston
   private ChangeDialog        change_dialog;        // dialogo por seræi kaj þanøi tekston
   private EspConvertDialog    esp_convert_dialog;   // dialogo por konverti supersignojn
   private OpenConvertDialog   open_convert_dialog;  // demandas æu la uzanto volas konverti dum malfermo
   private SaveConvertDialog   save_convert_dialog;  // demandas kiel la uzanto volas konverti dum 
                                                     // konservo al disko
   private FileChangedDialog   file_changed_dialog;  // demandas æu la uzanto volas konservi þanøojn
   private SaveKeyDialog       save_key_dialog;      // dialogo por peti þlosilon dum konservado de dosiero
                                                     // this dialog asks for an encryption key for saving 
                                                     // a file
   private OpenKeyDialog       open_key_dialog;      // dialogo por peti þlosilon dum malfermo de dosiero
                                                     // this dialog asks for an encryption key for opening 
                                                     // a file
   private ErrorDialog         error_dialog;         // montras erarmesaøon

   private ShowCharsDialog     show_chars_dialog;

   private ExistsDialog        exists_dialog;      // File exists. Overwrite? / Surskribu?
   private SavedMessage        saved_message;      // File saved. / Dosiero konservita.


   public SimTextPane       text_pane;          // tekstujo
   private JScrollPane       scroll_pane;        // ujo kun glisbutonoj
   private JViewport         view_port; 

   /////////////////////////////////////
   // Fonts. Tiparoj.
   private static       GraphicsEnvironment  local_graphics;
   private Font         current_font;           // aktiva tiparo
   private String[]     font_families;          // nomoj de tiparaj familioj
   private JComboBox    font_list = new JComboBox();     // listo de tiparoj
   private JComboBox    font_sizes = new JComboBox();    // grandecoj de tiparoj
   private JPanel       font_panel = new JPanel();


   /////////////////////////////////////
   // User Keyboard Maps. Klavaraj Mapoj de Uzantoj

   private JComboBox    keymap_list   = new JComboBox();   // listo de klavaraj mapoj
   private JLabel       keymap_label  = new JLabel("");    // grandecoj de tiparoj
   private JPanel       keymap_panel  = new JPanel();

   private String current_keymap = "";

   /////////////////////////////////////
   // Strings for creating menus and dialogs.
   // Æenoj por krei menuojn kaj dialogojn.
   private String[]   menu_and_dialog_strings;           // tekstoj por menuoj kaj dialogoj
   private String[]   menus;         // menuoj
   private String[]   menu_labels;   // menu-etikedoj
   private String[]   menu_items;    // menuelektoj
   private String[]   commands;      // ordonoj

   // For the file chooser. Por la dosiero-selektilo
   private String     open_text    = "";
   private String     save_text    = "";
   private String     approve_text = "";

   // Miscellaneous  / Diversaj
   private String     new_document_text = "";
   private String     unicode_text = "";
   private String     keymap_text = "";
   private String     nokeymap_text = "";

   // Tool Panel - for menubar and toobar.
   // Panelo por menutrabo kaj ikontrabo.
   //private JPanel  tool_panel = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
   private JPanel  tool_panel = new JPanel(new BorderLayout());

   ////////////////////////////////////////
   // tool bar and icons / ikon-trabo kaj ikonoj

   private  JToolBar  toolbar  =  new JToolBar();
   private ImageIcon newicon = new ImageIcon(SimIcons.nova);
   private ImageIcon openicon = new ImageIcon(SimIcons.dosierujo);
   private ImageIcon saveicon = new ImageIcon(SimIcons.disketo);
   //private ImageIcon printicon = new ImageIcon();
   private ImageIcon searchicon = new ImageIcon(SimIcons.lupeo);
   private ImageIcon upicon = new ImageIcon(SimIcons.supren);
   private ImageIcon downicon = new ImageIcon(SimIcons.malsupren);
   private ImageIcon cuticon = new ImageIcon(SimIcons.tondilo); 
   private ImageIcon copyicon = new ImageIcon(SimIcons.kopio);
   private ImageIcon pasteicon = new ImageIcon(SimIcons.gluo);

   ////////////////////////////////////////
   // Strings for encodings. Used for conversions.
   // Æenoj por kodaroj. Uzitaj por konvertado.
   private String[]  encoding_names;          // nomoj de kodaroj
   private String[]  encoding_descriptions;   // priskriboj de kodaroj
  

   DefaultStyledDocument     doc;    // defaýlta kunstila dokumento
   //HTMLDocument     doc;    // HTML-dokumento

   boolean      document_has_changed;   // dokumento estas þanøita

   StyleContext              sc;
   Style                     sim_style;

   StyledEditorKit        the_editor_kit = new StyledEditorKit();
   //HTMLEditorKit          the_editor_kit = new HTMLEditorKit();

   private Toolkit     my_toolkit;
   private Properties  my_properties;

   static Closer simcloser;   // Closer listens for window events.
                              // "Closer" atentas fenestro-eventojn.


   static Color simpurple = new Color(0xddccff);   // Purpura koloro

   // Attributes, left and right alignment.
   // Atribuoj, maldekstra kaj dekstra flankoj 
   SimpleAttributeSet current_attributes, left_side, right_side;

   public Farsido () {

      super(true);
      setLayout(new BorderLayout());

      simcloser = new Closer();

      my_toolkit = Toolkit.getDefaultToolkit();
      my_properties = new Properties();

      sc = new StyleContext();
      sim_style = sc.addStyle("sim style",null);

      final int number_of_stops = 20;   // Nombro da taboj.
      TabStop[]  tabstops = new TabStop[number_of_stops];
      for (int i = 0; i < number_of_stops; i++) {
         //tabstops[i] = new TabStop(40.0f * i,TabStop.ALIGN_LEFT,TabStop.LEAD_NONE);
         tabstops[i] = new TabStop(40.0f * i);
      }
      TabSet tabset = new TabSet(tabstops);

      left_side = new SimpleAttributeSet();
      right_side = new SimpleAttributeSet();

      StyleConstants.setTabSet(left_side, tabset);
      StyleConstants.setLeftIndent(left_side, 10.0f);
      StyleConstants.setRightIndent(left_side, 10.0f);
      StyleConstants.setAlignment(left_side, StyleConstants.ALIGN_LEFT);

      StyleConstants.setTabSet(right_side, tabset);
      StyleConstants.setLeftIndent(right_side, 10.0f);
      StyleConstants.setRightIndent(right_side, 10.0f);
      StyleConstants.setAlignment(right_side, StyleConstants.ALIGN_RIGHT);

      current_attributes = left_side;
      sim_style.addAttributes(current_attributes);


      text_pane = new SimTextPane();
      text_pane.setLogicalStyle(sim_style);
      //the_editor_kit  = (HTMLEditorKit)text_pane.getEditorKit();
      text_pane.setEditorKit(the_editor_kit);


      //////////////////////////////////////////////////////////////
      // Get list of action classes and save them in a hash table.
      // Akiru liston de agoklasoj kaj konservu ilin en asocia tabelo.
      action_list = the_editor_kit.getActions();
      action_hash = new Hashtable();
      for (int i = 0; i < action_list.length; i++) {
         Action action = action_list[i];
         //System.out.print(" " + action.getValue(Action.NAME));
         action_hash.put(action.getValue(Action.NAME), action);
      }

      //////////////////////////////////////////////////////////////////////////
      // Put inner classes into the hash table.
      // In the code below, open-file is the name used in the hash table to 
      // refer to the OpenFile action class. The NAME in the class will probably
      // be changed, according to the language specified in the resource file, 
      // but the name in the hash table will remain the same.
      //////////////////////////////////////////////////////////////////////////
      // Metu internajn klasojn en la asocian tabelon.
      // En la malsupraj instrukcioj, open-file estas la nomo uzata en la asocia
      // tabelo (action_hash) por referenci la OpenFile-agklason. La nomo de la
      // klaso probable þanøiøos, laý la lingvo en la rimeda dosiero, sed la nomo 
      // en la asocia tabelo ne þanøiøos.
      action_hash.put("open-file", new OpenFile("open-file"));
      action_hash.put("save-file", new SaveFile("save-file"));
      action_hash.put("save-file-as", new SaveFileAs("save-file-as"));
      action_hash.put("new-file", new NewFile("new-file"));
      action_hash.put("exit", new Exit("exit"));
      action_hash.put("show-search", new ShowSearch("show-search"));
      action_hash.put("search-down", new SearchDownx("search-down"));
      action_hash.put("search-up", new SearchUpx("search-up"));
      action_hash.put("show-change", new ShowChange("show-change"));
      action_hash.put("show-esp-convert-dialog", new ShowEspConvertDialog("show-esp-convert-dialog"));
      action_hash.put("print-file", new PrintIt("print-file"));
      action_hash.put("esp-spell-check", new SpellCheck("esp-spell-check"));
      action_hash.put("right-left", new RightLeft("right-left"));
      action_hash.put("reverse-text", new ReverseText("reverse-text"));
      action_hash.put("show-chars", new ShowChars("show-chars"));

      // Check to make sure the language parameter is valid.
      // Kontrolu ke la lingva parametro estas valida.
      String languages = SimCon.toLower(menu_resources.getString("languages"));

      // Is the current language in the list? If not, set to Esperanto.
      // Æu la aktiva lingvo estas en la listo? Se ne, faru øin Esperanto.
      if (languages.indexOf(current_language) < 0) {
         current_language = "esperanto"; 
      }
      menu_and_dialog_strings = SimCon.tokenize(menu_resources.getString(current_language));


      getMiscellaneousTexts();

      initializeToolBar();

      initializeMenuBar(menu_and_dialog_strings[0]);


      tool_panel.add("North",menubar);
      tool_panel.add("South",toolbar);
      add("North",tool_panel);

      System.out.println("1");
      // Create dialogs. Kreu dialogajn fenestrojn.
      createFileChooser();        // Kreu dosierodialogon.
      System.out.println("2");
      createEspConvertDialog();   // Kreu konvertdialogon por Esperantaj supersignoj.
      createOpenConvertDialog();  // Kreu malfermokonvertodialogon.
      System.out.println("3");
      createSaveConvertDialog();  // Kreu konservokonvertodialogon.
      createFileChangedDialog();  // Kreu "dosiero-þanøita" -dialogon.
      createSearchAndChange();    // Kreu dialogojn por seræado kaj þanøado.
      createKeyDialogs();         // Kreu dialogojn por peti kriptoþlosilon.
      createErrorDialog();        // Kreu erarodialogon.
      createShowCharsDialog();
      createExistsDialog();
      createSavedMessage();

      System.out.println("4");
      initializeFontBoxes();      // The font names and sizes will be in the menubar,
                                  // instead of a separate dialogue.
                                  // La tiparaj nomoj kaj grandecoj estos en la menutrabo,
                                  // anstataý en aparta dialogfenestro. 


      keymap_label.setText(keymap_text);

      text_pane.setSelectionColor(simpurple);
      scroll_pane = new JScrollPane();
      view_port = scroll_pane.getViewport();
      view_port.add(text_pane);
      text_pane.setViewport(view_port);

      System.out.println("5");

      // If the current language is Esperanto, check spelling.
      // Se la aktiva lingvo estas Esperanto, kontrolu literumadon.
      if (current_language.equals("esperanto")) {
         text_pane.setSpellCheck(true);
      }

      add("Center",scroll_pane);
      get_new_doc();               // Akiru novan dokumentan objekton.
      getLastFont();    // Akiru la laste uzitan tiparon.
      font_list.setActionCommand("new font");
      font_sizes.setActionCommand("new font");
      document_has_changed = false;



      //////////////////////////////////////////////////
      // Set key bindings.  Pretigu klav-ligojn.
      // ctrl-f >> Show search dialogue.  Montru seræ-dialogon.

      //Action search_action = (Action)action_hash.get("show-search");
      Action search_dia    = new ShowSearch("show-search");
      Action search_down   = new SearchDownx("search-down");
      Action search_up     = new SearchUpx("search-up");

      JTextComponent.KeyBinding[] bindings = {
         new JTextComponent.KeyBinding(KeyStroke.getKeyStroke(
                        KeyEvent.VK_F, 
                        InputEvent.CTRL_MASK),
                        "show-search"),
         new JTextComponent.KeyBinding(KeyStroke.getKeyStroke(
                        KeyEvent.VK_N, 
                        InputEvent.CTRL_MASK),
                        "search-down"),
         new JTextComponent.KeyBinding(KeyStroke.getKeyStroke(
                        KeyEvent.VK_B, 
                        InputEvent.CTRL_MASK),
                        "search-up"),
      };
      Keymap parentmap = text_pane.getKeymap();
      Keymap simkeymap = JTextComponent.addKeymap("simkeymap", parentmap);
      Action[] short_action_list = { search_dia, search_down, search_up };
      JTextComponent.loadKeymap(simkeymap, bindings, short_action_list);
      text_pane.setKeymap(simkeymap);

   } // end of constructor




   // Create file chooser dialog. Kreu dosieroselektilon.
   private void createFileChooser () {
      String fc_resource = menu_and_dialog_strings[7];
      String[] fc_labels = SimCon.tokenize(menu_resources.getString(fc_resource));
      open_text    = fc_labels[0];
      save_text    = fc_labels[1];
      approve_text = fc_labels[2];
      file_chooser = new JFileChooser();  // Kreu dosierodialogon.
      // For convenience, set the current directory to the directory
      // of the last file read.
      // version 3.1 - Do not set current directory to the A drive. 
      // That seems to kill the program when there is no floppy disk.
      // Por oportuneco, þanøu la nunan dosierujon al la dosierujo
      // de la lasta þargita dosiero.
      // versio 3.1 - Ne þanøu la komencan dosierujon al la A-drajvo.
      // Tio mortigas la programon kiam ne estas fleksebla disko en 
      // la drajvo (pelilo?).
      try {
         String last_file = (String)file_list.peek();
         File last_directory = new File(last_file);
         last_file.toLowerCase();
         if (last_directory != null && !(last_file.startsWith("a:")))
                file_chooser.setCurrentDirectory(last_directory);
      } catch (EmptyStackException esx) {}  // Doesn't matter. Ne gravas.

   }  // createFileChooser



   // Get texts. Akiru tekstojn.
   private void getMiscellaneousTexts () {
      String mt_resource = menu_and_dialog_strings[8];
      String[] mt_labels = SimCon.tokenize(menu_resources.getString(mt_resource));
      new_document_text = mt_labels[0];
      unicode_text  = mt_labels[1];
      keymap_text   = mt_labels[2];
      nokeymap_text = mt_labels[3];
   }  // getMiscellaneousText


   // Create the search and change dialogues. Kreu la seræodialogon kaj þanøodialogon.
   private void createSearchAndChange () {
      String resource = menu_and_dialog_strings[5];
      String[] labels = SimCon.tokenize(menu_resources.getString(resource));
      search_dialog = new SearchDialog(frame, this, labels);
      resource = menu_and_dialog_strings[6];
      labels = SimCon.tokenize(menu_resources.getString(resource));
      change_dialog = new ChangeDialog(frame, this, labels);
   }  // createSearchAndChange


   ///////////////////////////////////////////////////////////////
   // Create menus. Kreu menuojn.

   private String[]  menubar_info;
   private JMenuBar  menubar = new JMenuBar();
   private JMenu     file_menu = new JMenu();       
   private JMenu     edit_menu = new JMenu();
   private JMenu     other_menu = new JMenu();
   private JMenu     esperanto_menu = new JMenu();

   /**
    * Initialize a menu bar. 
    * Pretigu menutrabon.
    */
   protected void initializeMenuBar(String menubar_name) {
      font_list.setKeySelectionManager(new KeyManager());
      getFileList();  // Read list of previously edited files for file menu. 
                      // Legu liston de antaýe redaktitaj dosieroj por dokumentmenuo.
      menubar_info = SimCon.tokenize(menu_resources.getString(menubar_name));
      initializeFileMenu(file_menu);
      initializeMenu(edit_menu, menubar_info[2], menubar_info[3]);
      initializeMenu(other_menu, menubar_info[4], menubar_info[5]);
      menubar.add(file_menu);
      menubar.add(edit_menu);
      menubar.add(other_menu);
      if (menubar_info.length >= 8) {
         initializeMenu(esperanto_menu, menubar_info[6], menubar_info[7]);
         menubar.add(esperanto_menu);
      }
      font_panel.setLayout(new FlowLayout(FlowLayout.LEFT,5,0));
      font_panel.add(font_list);
      font_panel.add(font_sizes);
      menubar.add(font_panel);
      return;
   }  // initializeMenuBar

   /**
    * Initialize tool bar.
    * Pretigu ikon-trabon.
    */
   protected void initializeToolBar() {
      toolbar.setFloatable(false);
           // Why not float? I can't keep the toolbar above the main frame.
           // Kial ne flosi? Mi ne povas restigi la il-trabon super la æefan kadron.
      toolbar.add(new NewFile("",newicon));
      toolbar.add(new OpenFile("",openicon));
      toolbar.add(new SaveFile("",saveicon));
      toolbar.add(new ShowSearch("",searchicon));
      toolbar.add(new SearchUpx("",upicon));
      toolbar.add(new SearchDownx("",downicon));
      toolbar.add(new SimCut("",cuticon));
      toolbar.add(new SimCopy("",copyicon));
      toolbar.add(new SimPaste("",pasteicon));

      initializeKeyMapList();
      keymap_panel.setLayout(new FlowLayout(FlowLayout.LEFT,5,0));
      keymap_panel.add(keymap_label);
      keymap_panel.add(keymap_list);
      toolbar.add(keymap_panel);

   }


   /**
    * Initialize a menu.
    * Pretigu menuon.
    */
   protected  void  initializeMenu(JMenu menu, String resource_name, String menu_name) {
      menu.setText(menu_name);
      String[] menu_tokens = SimCon.tokenize(menu_resources.getString(resource_name));
      String   action_name;
      Action   the_action;
      int  len = menu_tokens.length;
      for (int i = 0; i < len; i=i+2) {
         action_name = menu_tokens[i];
         if (action_name.startsWith("separator")) {
            menu.addSeparator();
         }
         else {
            the_action = (Action)action_hash.get(action_name);
            if (the_action != null) {
               // Get a new label from the resource file and 
               // change the name of the action class.
               // Akiru novan etikedon de la rimeda dosiero kaj 
               // þanøu la nomon de la agklaso.
               the_action.putValue(Action.NAME, menu_tokens[i+1]);
               menu.add(the_action);
            }
            else menu.add(createMenuItem(menu_tokens[i+1],action_name));
         }
      }
      return;
   }  // initializeMenu


   /**
    * Initialize the file menu. This method adds a list of recently edited
    * files to the regular items.
    * Pretigu la dokument-menuon. Æi tiu metodo metas liston de laste redaktitaj
    * dokumentoj post la kutimaj menuelektoj.
    */
   protected  void  initializeFileMenu(JMenu menu) {
      // First, put in the regular menu items with initializeMenu.
      // Unue, enmetu la kutimajn menu-elektojn per initializeMenu.
      initializeMenu(menu, menubar_info[0], menubar_info[1]);
      // Next, put in the list of recently edited files.
      // Sekve, enmetu liston de laste redaktitaj dosieroj.
      if (file_list.size() > 0) {
         menu.addSeparator();
         String   the_file_name;
         for (int i = file_list.size() -1; i >= 0; i--) {
            try {
               the_file_name = (String)file_list.elementAt(i);
               menu.add(createMenuItem(the_file_name, "open " + the_file_name));
            } catch (ArrayIndexOutOfBoundsException e) {}
         }
      }
      return;
   }  // initializeFileMenu


   /**
    * Initialize keymap_list. (user keymaps)
    * Pretigu liston de klavmapoj.  (klavmapoj de uzantoj)
    */
   protected  void  initializeKeyMapList() {
      File keymap_files = new File(class_path);
      keymap_list.addItem(nokeymap_text);
      String[] list_of_maps = keymap_files.list(new KeyMapFilter());
      if (list_of_maps != null) {
         for (int i = 0; i < list_of_maps.length; i++) {
            keymap_list.addItem(list_of_maps[i]);
         }
      }
      keymap_list.setActionCommand("new keymap");
      keymap_list.addActionListener(this);
   }  // initializeKeyMapList



   /**
    * Create a menu item.
    * Kreu menu-elekton.
    */
   protected JMenuItem createMenuItem(String the_item_name, String the_action_name) {
      JMenuItem mi = new JMenuItem(the_item_name);
      mi.setActionCommand(the_action_name);
      mi.addActionListener(this);
      return mi;
   }  // createMenuItem


   /**
    * createEspConvertDialog - Creates dialogue to convert between accents 
    * conventions for Esperanto.
    * createEspConvertDialog - Kreas dialogon por konverti inter supersigno-
    * normoj por Esperanto.
    */
   protected void createEspConvertDialog() {
      String dialog_resource = menu_and_dialog_strings[1];
      String[] dialog_labels = SimCon.tokenize(menu_resources.getString(dialog_resource));
      esp_convert_dialog = new EspConvertDialog(frame, dialog_labels);
   }  // createEspConvertDialog

    /**
    * createOpenConvertDialog - Create a dialogue which asks the user whether he or 
    * she wants to convert the file to be opened to unicode.
    * createOpenconvertDialog - Kreu dialogon kiu demandas al la uzanto æu li aý þi 
    * volas konverti malfermotan dokumenton al unikodo.
    */

   protected void createOpenConvertDialog() {

      String   encodings;
      String   dialog_resource = menu_and_dialog_strings[2];
      String[] dialog_labels = SimCon.tokenize(menu_resources.getString(dialog_resource));
      if (current_language.equals("esperanto")) {
         encodings = esp_encoding_resources.getString("encodings");
      }
      else {
         encodings = encoding_resources.getString("encodings");
      }
      open_convert_dialog = new OpenConvertDialog(frame, dialog_labels, encodings);

   }  // createOpenConvertDialog


    /**
    * createSaveConvertDialog - Create a dialogue which asks the user how he or 
    * she wants to convert the file to be saved.
    * createSaveConvertDialog - Kreu dialogon kiu demandas al la uzanto kiel li aý þi 
    * volas konverti konservotan dokumenton.
    */

   protected void createSaveConvertDialog() {

      String   encodings;
      String   dialog_resource = menu_and_dialog_strings[3];
      String[] dialog_labels = SimCon.tokenize(menu_resources.getString(dialog_resource));
      if (current_language.equals("esperanto")) {
         encodings = esp_encoding_resources.getString("encodings");
      }
      else {
         encodings = encoding_resources.getString("encodings");
      }
      save_convert_dialog = new SaveConvertDialog(frame, dialog_labels, encodings);

   }  // createSaveConvertDialog




   /**
    * createFileChangedDialog - Create a dialog which ask the user whether he or she wants to
    * save changes to the current file.
    * createFileChangedDialog - Kreu dialogon kiu demandas al la uzanto æu li aý þi volas 
    * konservi þanøojn en la nuna dokumento.
    */
   protected void createFileChangedDialog() {
      String dialog_resource = menu_and_dialog_strings[4];
      String[] dialog_labels = SimCon.tokenize(menu_resources.getString(dialog_resource));
      file_changed_dialog = new FileChangedDialog(frame, dialog_labels);
   }  // createFileChangedDialog



    /**
    * createKeyDialogs - Create dialogues which asks for an encryption key
    * before opening or saving a file.
    * createKeyDialogs - Kreu dialogojn kiu petas kriptoþlosilon antaý ol
    * malfermi aý konservi dokumenton.
    */

   protected void createKeyDialogs() {

      String   encodings;
      String   dialog_resource = menu_and_dialog_strings[9];
      String[] dialog_labels = SimCon.tokenize(menu_resources.getString(dialog_resource));
      save_key_dialog = new SaveKeyDialog(frame, dialog_labels);

      dialog_resource = menu_and_dialog_strings[10];
      dialog_labels = SimCon.tokenize(menu_resources.getString(dialog_resource));
      open_key_dialog = new OpenKeyDialog(frame, dialog_labels);

   }  // createKeyDialogs

   /**
    * createErrorDialog - Does just what the name says.
    * createErrorDialog - Kreas erardialogujon.
    */
   String  cant_save_message = "";
   protected void createErrorDialog() {
      String   dialog_resource = menu_and_dialog_strings[11];
      String[] dialog_labels = SimCon.tokenize(menu_resources.getString(dialog_resource));
      error_dialog = new ErrorDialog(frame, dialog_labels);
      cant_save_message = dialog_labels[1];
   }


   /**
    * createShowCharsDialog - Creates dialogue to show all characters.
    * createShowCharsDialog - Kreas dialogon por montri æiujn signojn.
    */
   protected void createShowCharsDialog() {
      String dialog_resource = menu_and_dialog_strings[12];
      String[] dialog_labels = SimCon.tokenize(menu_resources.getString(dialog_resource));
      show_chars_dialog = new ShowCharsDialog(frame, this, dialog_labels);
   }  // createShowCharsDialog


   /**
    * createExistsDialog - Create dialogue which asks 'File already exists. Overwrite?'
    * createExistsDialog - Kreas dialogon kiu demandas 'Dosiero jam ekzistas. Surskribu?'
    */
   protected void createExistsDialog() {
      String   dialog_resource = menu_and_dialog_strings[13];
      String[] dialog_labels = SimCon.tokenize(menu_resources.getString(dialog_resource));
      exists_dialog = new ExistsDialog(frame, dialog_labels);
   }


   /**
    * createSavedMessage - Create message which confirms that a file was saved.
    * createSavedMessage - Kreas mesaøon kiu konfirmas ke dosiero estis konservita
    */
   protected void createSavedMessage() {
      String   dialog_resource = menu_and_dialog_strings[14];
      String[] dialog_labels = SimCon.tokenize(menu_resources.getString(dialog_resource));
      saved_message = new SavedMessage(frame, dialog_labels);
   }



   /*
    * initializeFontBoxes - Initialize the comboboxes for the font name and size.
    * initializeFontBoxes - Pretigu la selektlistojn por tipara nomo kaj grandeco.
    */
   protected void initializeFontBoxes () {

      // Get available fonts. Akiru uzeblajn tiparojn.
      local_graphics  = GraphicsEnvironment.getLocalGraphicsEnvironment();

      // Note: I tried to get a list of all available fonts using the following command:
      // Font[] all_fonts       = local_graphics.getAllFonts();
      // It worked fine in Windows 98, but not in Windows 95 (Japanese version). It caused
      // Java to die silently. To avoid the problem, I'll send font family names to the 
      // font chooser.
      // Noto: Mi provis akiri liston de æiuj uzeblaj tiparoj uzante la sekvantan ordonon:
      // Font[] all_fonts       = local_graphics.getAllFonts();
      // Øi bone funkciis en Vindozo 98, sed ne en Vindozo 95 (Japana versio). Øi mortigis
      // Øavon silente. Por eviti la problemon, mi sendos liston de tiparaj familiaj nomo al 
      // la tipara dialogo.
      font_families   = local_graphics.getAvailableFontFamilyNames();

      // Initialize combobox for font names. Pretigu selektliston por tiparaj nomoj.
      for (int i = 0; i < font_families.length; i++) {
         //System.out.println(font_families[i]);
         font_list.addItem(font_families[i]);
      }

      // Initialize combobox for font sizes. Pretigu selektliston por tiparaj grandecoj.
      font_sizes.addItem("8"); font_sizes.addItem("9"); font_sizes.addItem("10"); 
      font_sizes.addItem("11"); font_sizes.addItem("12"); font_sizes.addItem("14");
      font_sizes.addItem("16"); font_sizes.addItem("18"); font_sizes.addItem("20"); 
      font_sizes.addItem("22"); font_sizes.addItem("24"); font_sizes.addItem("26");
      font_sizes.addItem("28"); font_sizes.addItem("36"); font_sizes.addItem("48"); 
      font_sizes.addItem("72"); font_sizes.addItem("144");

      font_list.addActionListener(this);
      font_list.addActionListener(search_dialog);
      font_list.addActionListener(change_dialog);
      font_list.addActionListener(show_chars_dialog);
      font_sizes.addActionListener(this);

   } // initializeFontBoxes



   /*
    *  Returns the selected font.
    *  Redonas la elektitan tiparon.
    */
   public Font getSelectedFont() {
      float point_size = Float.parseFloat((String)font_sizes.getSelectedItem());
      int   style = Font.PLAIN;
      Font my_new_font = Font.decode((String)font_list.getSelectedItem());
      Font very_new_font = my_new_font.deriveFont(style,point_size);
      //System.err.println("The new font is " + very_new_font.toString());
      text_pane.requestFocus();
      return very_new_font;
   }  // getSelectedFont

   /*
    *  Returns the selected font name.
    *  Redonas la nomon de la selectita tiparo.
    */
   public String getSelectedFontName() {
      String the_name;
      the_name = (String)font_list.getSelectedItem();
      text_pane.requestFocus();
      return the_name;
   }  // getSelectedFontName



   /*
    *  Returns a file object for the selected user keymap file.
    *  Redonas la dosieran objekton por la elektita klavara mapo de uzantoj.
    */
   public File getSelectedKeyMapFile() {
      // First item in keymap list is invalid (no keymap).
      // La unua elekta¼o en la klavmapo estas nevalida (Neaktiva klavmapo).
      if (keymap_list.getSelectedIndex() < 1) return null;
      return new File(class_path,(String)keymap_list.getSelectedItem());
   }  // getSelectedKeyMapFile


   public File getKeyMapFile(String keymap_file_name) {
      return new File(class_path,keymap_file_name);
   }  // getKeyMapFile




   /*
    * putIntoFileList - Save the file name in the list of previously edited files
    * putIntoFileList - Konservu la dosieronomon en la liston de antaýe redaktitaj dosieroj.
    */
   private static final int file_list_max_size = 8;
   private void putIntoFileList(String file_name) {
      int number_of_elements = file_list.size();
      // First, remove the file name, if it is already in the list.
      // Unue, forigu la dosieronomon, se øi jam estas en la listo.
      int index = file_list.search((String)file_name);
      if (index > 0) {
         file_list.removeElementAt(number_of_elements - index);
      }
      // Remove the last name, if the list is full.
      // Forigu la lastan nomon, se la listo estas plena.
      if (file_list.size() == file_list_max_size) {
         file_list.removeElementAt(0);
      }
      // Put in the new file name.
      // Enmetu la novan dosieronomon.
      file_list.push(file_name);

   }  // putIntoFileList


   /*
    * saveFileList - Writes out the list of recently edited files.
    * saveFileList - Elskribas la liston de laste redaktitaj dosieroj.
    */
   private void saveFileList () {
      File               path_and_name;   // pado kaj nomo
      RandomAccessFile   outfile;         // el-dosiero
      if (file_list != null && simlist != null) {
         try {
            if (simlist.exists()) simlist.delete();
            outfile = new RandomAccessFile(simlist,"rw");
            for (int i = 0; i < file_list.size(); i++) {
               outfile.writeBytes((String)file_list.elementAt(i) + "\n");
            }
            outfile.close();
         } catch (IOException e) {
            System.err.println("Error writing out simlist.txt. Eraro dum elskribado de simlist.txt");
         }
      }
   } // saveFileList



   /*
    * getFileList - Reads in the list of recently edited files.
    * getFileList - Legas la liston de laste redaktitaj dosieroj.
    */
   File simlist;
   private void getFileList () {
      RandomAccessFile   infile;          // en-dosiero
      String             line;

      if (file_list == null) file_list = new Stack();

      try {
         simlist = new File(class_path,"simlist.txt");
      }
      catch (NullPointerException e) {
         return;
      }

      try {
         infile = new RandomAccessFile(simlist,"rw");
         for (int i = 0; i < file_list_max_size; i++) {
            line = infile.readLine();
            if (line == null) break;
            file_list.push(line);
         }
         infile.close();
      }
      catch (IOException e) {
         System.err.println("Could not read simlist.txt. Ne povis legi simlist.txt.");
      }
      return;
   } // getFileList




   /*
    * getLastFont - Reads in the name and size of the last font 
    * used from the file simfont.txt .
    * For 3.3, read in last key map used.
    * getLastFont - Legas la nomon kaj grandecon de la laste uzita tiparo 
    * de la dosiero simfont.txt .
    * Por 3.3, legu nomon pri lasta klavmapo.
    */
   File simfont;
   private void getLastFont () {

      RandomAccessFile   infile;          // en-dosiero
      String             font_name = "";
      String             font_size = "";
      String             keymap_name = "";

      current_font = text_pane.getFont();

      try {
         simfont = new File(class_path,"simfont.txt");
      }
      catch (NullPointerException e) {  }

      if (simfont != null && simfont.exists()) {

         try {
            infile = new RandomAccessFile(simfont,"rw");
            font_name   = infile.readLine();
            font_size   = infile.readLine();
            keymap_name = infile.readLine();
            infile.close();
            if (font_name != null && font_size != null) {
               float point_size = Float.parseFloat(font_size);
               if (point_size > 144.0f) point_size = 144.0f;
               int   style = Font.PLAIN;
               Font my_new_font = Font.decode(font_name);
               current_font = my_new_font.deriveFont(style,point_size);
            }
            if (keymap_name != null) {
               current_keymap = keymap_name;
               File the_keymap_file = getKeyMapFile(keymap_name);
               if (the_keymap_file.exists()) {
                  UserKeyMap.loadUserKeyMap(getKeyMapFile(keymap_name));
                  keymap_list.setSelectedItem(current_keymap);
               }
            }

         }
         catch (IOException e) {    // The file simfont.txt does not exist. 
                                    // Simfont.txt ne ekzistas.
         }

      }   // if simfont exists

      if (current_font != null) {
         String font_family = current_font.getFamily();
         int    size   = current_font.getSize();
         font_list.setSelectedItem(font_family);
         font_sizes.setSelectedItem(Integer.toString(size));
         text_pane.setFont(current_font);
         text_pane.requestFocus();
         text_pane.validate();
      }

      if (search_dialog != null) search_dialog.new_font();
      if (change_dialog != null) change_dialog.new_font();

      return;

   } // getLastFont



   /*
    * saveLastFont - Writes out the name and size of the last font used to 
    * a file, simfont.txt . And save the current keymap.
    * saveLastFont - Elskribas la nomon kaj grandecon de la laste uzita tiparo 
    * al dosiero, simfont.txt . Kaj konservu la aktivan klavmapon.
    */
   private void saveLastFont () {
      RandomAccessFile   outfile;         // el-dosiero
      if (simfont != null) {
         try {
            String code_table;
            if (simfont.exists()) simfont.delete();
            outfile = new RandomAccessFile(simfont,"rw");
            outfile.writeBytes(current_font.getFamily() + "\n");
            outfile.writeBytes(current_font.getSize() + "\n");
            outfile.writeBytes(current_keymap);
            outfile.close();
         } catch (IOException e) {
            System.err.println("Error writing out simfont.txt. " +
                               "Eraro dum elskribado de simfont.txt");
         }
      }
   } // saveLastFont




   public void actionPerformed(ActionEvent e) {
      String  the_command = e.getActionCommand();


      if (the_command.equals("new font")) {
         current_font = getSelectedFont();
         System.out.println("New font. Nova tiparo.  " + current_font.getFontName());
         System.out.println(current_font.getSize() + "  " + current_font.getNumGlyphs());
         text_pane.setFont(current_font);
         text_pane.validate();
      }
      else
      if (the_command.equals("new keymap")) {
         current_keymap = (String)keymap_list.getSelectedItem();
         File keymap_file = getSelectedKeyMapFile();
         UserKeyMap.loadUserKeyMap(keymap_file);
      }
      else
      if (the_command.startsWith("open ")) {

         if (document_has_changed) {     // se dokumento estas þanøita
            file_changed_dialog.showIt();
            String decision = file_changed_dialog.getDecision();
            if (decision.equals("yes")) {
               save_to_disk(false);  // Konservu øin.
            }
            if (decision.equals("cancel")) {
               return;
            }
         }

         String file_name = the_command.substring(5);
         File file_to_get = new File(file_name);
         load_file_to_doc(file_to_get);
      }

   }  // actionPerformed




   ////////////////////////////////////////////////////////////////////////////
   // Methods related to document events. The goal is to detect when a document
   // changes, in order to decide if it needs to be saved.
   // Metodoj kiuj rilatas al dokument-eventoj. La celo estas determini kiam
   // dokumento þanøiøas, por decidi æu necesas konservi øin. 
   //



   /*
    * get_new_doc - Get a new document. 
    * get_new_doc - Akiru novan dokumenton. 
    */
   protected void get_new_doc () {
      doc = new DefaultStyledDocument(sc);
      //doc = new HTMLDocument();
      text_pane.setDocument((Document)doc);
      set_doc_listener();
      document_has_changed = false;
////????? experimental ////////////////////////////////
//Style s = doc.addStyle("Green",null);
//s.addAttribute(StyleConstants.Foreground, Color.green);
      doc.setParagraphAttributes(0,0,current_attributes,false);
   }  // get_new_doc


   /*
    * set_doc_listener - Set the document listener. 
    * set_doc_listener - Initu la dokument-aýskultanton. 
    */
   private void set_doc_listener() {
      doc.addDocumentListener(this);
   }  // set_doc_listener



   // The following 3 methods are part of the DocumentListener Interface.
   // La sekvantaj 3 metodoj estas plenumas la dokument-aýskultan interfacon.

   public void changedUpdate(DocumentEvent de) {
      // Plain text files don't need to have the document_has_changed flag 
      // set when the font changes. Plain text file don't save font information. 
      // Oni ne levu la dokumento_þanøita-flagon kiam oni þanøas tiparon
      // de ordinara teksta dokumento. Ordinaraj tekstaj dokumentoj ne konservas
      // tiparan informon.
      // document_has_changed = true;
      // System.err.println("this is change");
   }

   public void insertUpdate(DocumentEvent de) {
      // System.err.println("this is insert");
      document_has_changed = true;
   }

   public void removeUpdate(DocumentEvent de) {
      // System.err.println("this is remove");
      document_has_changed = true;
   }






////////////////////////////////////////////////////////////
// Action Classes   / Agklasoj


   /**
    * NewFile - Action class to create a new document.
    * NewFile - Agoklaso por krei novan dokumenton.
    */
   class NewFile extends AbstractAction {

      NewFile(String default_label) {
         super(default_label);
      }

      NewFile(String default_label, Icon the_icon) {
         super(default_label, the_icon);
      }

      public void actionPerformed(ActionEvent e) {

         if (document_has_changed) {     // se dokumento estas þanøita
            file_changed_dialog.showIt();
            String decision = file_changed_dialog.getDecision();
            if (decision.equals("yes")) {
               save_to_disk(false);  // Konservu øin.
            }
            if (decision.equals("cancel")) {
               return;
            }
         }
         get_new_doc();
         // Show the file name in the frame.
         // Montru nomon en supra trabo de kadro.
         set_title(new_document_text);          // New Document  / Nova Dokumento
         current_file_name = null;   // Oni ankoraý ne donis nomon.
         file_converted_from = null;
         validate();
         text_pane.requestFocus();

      }  // actionPerformed

   }  // class NewFile


   /**
    * OpenFile - Action class to open a file.
    * OpenFile - Agoklaso por malfermi dosieron.
    */
   class OpenFile extends AbstractAction {

      OpenFile(String default_label) {
         super(default_label);
      }

      OpenFile(String default_label, Icon the_icon) {
         super(default_label, the_icon);
      }

      public void actionPerformed(ActionEvent e) {

         File  file_to_open;   // malfermota dosiero

         if (document_has_changed) {     // se dokumento estas þanøita
            file_changed_dialog.showIt();
            String decision = file_changed_dialog.getDecision();
            if (decision.equals("yes")) {
               save_to_disk(false);  // Konservu øin.
            }
            if (decision.equals("cancel")) {
               text_pane.requestFocus();
               return;
            }
         }

         // Create a file dialog, if it doesn't already exist.
         // Kreu dosierodialogon, se øi ne jam ekzistas.
         if (file_chooser == null) {
            file_chooser = new JFileChooser();  
         } 
         file_chooser.rescanCurrentDirectory();

         // Choose the file. Elektu la dosieron.
         file_chooser.setDialogTitle(open_text);
         file_chooser.setApproveButtonText(approve_text);
         int returnVal = file_chooser.showOpenDialog(frame);
         if(returnVal == JFileChooser.APPROVE_OPTION) { 
            file_to_open = file_chooser.getSelectedFile();
         }
         else {
            text_pane.requestFocus();
            return;
         }
         load_file_to_doc(file_to_open);
         text_pane.requestFocus();

      }  // actionPerformed

   }  // class OpenFile



   /*
    * set_title - Write the file name and current mode on the frame bar.
    * set_title - Skribu la dosieronomon kaj nunan reøimon sur la kadrotrabo.
    */
   protected void set_title (String file_name) {
      String field1 = file_name;
      if (field1 == null) field1 = "";
      frame.setTitle("  " + field1);
   } // set_title



   /**
    * load_file_to_doc - This method reads a file and puts it into a document
    * object. It sets the text_pane, writes to the list of previously edited files
    * (file_list), and recreates the file menu.
    *
    * load_file_to_doc - Æi tiu metodo legas dosieron kaj metas øin en dokument-
    * objekton. Øi plenigas la tekst-objekton (text_pane), skribas al la listo de
    * antaýe redaktitaj dosieroj (file_list), kaj rekreas la dokument-menuon. 
    */
   protected void load_file_to_doc (File file_to_open) {

      char[] char_buf;       // matrico de dubajtaj signoj
      char_buf = read_file_to_charbuf(file_to_open);
      if (char_buf == null) return;
      CharArrayReader  reader = new CharArrayReader(char_buf);
      doc = new DefaultStyledDocument(sc);
      doc.setParagraphAttributes(0,0,current_attributes,false);
      //doc = new HTMLDocument();
      try {
         the_editor_kit.read(reader,doc,0);
      } catch (IOException io) {
         System.err.println("Could not open file. Ne povis malfermi dosieron. -A- " 
                            + io.getMessage());
         return;
      } catch (BadLocationException bad) {
         System.err.println("Could not open file. Ne povis malfermi dosieron. -B- " 
                            + bad.getMessage());
         return;
      }
      text_pane.setDocument(doc);
      set_doc_listener();
      document_has_changed = false;
      putIntoFileList(file_to_open.getPath()); 
      // Recreate the file menu. Rekreu la dokument-menuon.
      file_menu.removeAll();
      initializeFileMenu(file_menu);
      validate();
   }  // load_file_to_doc



   /**
    * read_file_to_charbuf - Opens a file and reads it into a character buffer.
    * This method properly loads single byte files and double byte unicode 
    * (big endian and little endian). 
    *
    * read_file_to_charbuf - Malfermas dosieron kaj legas øin en karaktran bufron.
    * Æi tiu metodo øuste þargas unubitokajn dosierojn kaj unikodajn dosierojn 
    * (pezkomenca aý pezfina). 
    */
   private char[] read_file_to_charbuf (File file_to_open) {

      byte[] byte_buffer;
      char[] char_buffer;
      int    big, little;
      int    file_length;
      int    file_format;

      if (!file_to_open.exists()) {       // Se la dosiero ne ekzistas...
          System.err.println("Ooops, file not found. Ho ve, dosiero netrovita. " 
                              + file_to_open.getPath());
          current_file_name = null;
          file_converted_from = null;
          return null;
      }

      current_file_name = file_to_open.getPath();
      set_title(current_file_name);
      System.out.println("Open file. Malfermu dosieron. " + current_file_name); 

      try {

         file_length = (int)file_to_open.length();
         byte_buffer = new byte[file_length];
         FileInputStream fin = new FileInputStream(file_to_open);
         fin.read(byte_buffer);
         fin.close();

      } catch (IOException io) {
         System.err.println("I/O error. Eraro dum legado. " + io.getMessage());
         current_file_name = null;
         return null;
      } 


      file_format = Detect.whatIsIt(byte_buffer);

      if (file_format == Detect.UNI_LITTLE_ENDIAN) {
         file_converted_from = "UTF16 little";     // Pezfina unikodo.
         int number_of_characters = (file_length - 2)/2;
         char_buffer = new char[number_of_characters];
         int i,j;
         for (i = 2, j = 0; i < file_length; i = i + 2, j++) {
            little = (int)(byte_buffer[i] & 0x00FF);
            big    = (int)(byte_buffer[i+1] & 0x00FF);
            char_buffer[j] = (char)((big * 0x100) + little);
         }
         encryption_key = null;  // Not an encrypted file. Ne estas kripta dosiero.
         return char_buffer;
      }
      else 
      if (file_format == Detect.UNI_BIG_ENDIAN) {
         file_converted_from = "UTF16 big";      // Pezkomenca unikodo.
         int number_of_characters = (file_length - 2)/2;
         char_buffer = new char[number_of_characters];
         int i,j;
         for (i = 2, j = 0; i < file_length; i = i + 2, j++) {
            little = (int)(byte_buffer[i+1] & 0x00FF);
            big    = (int)(byte_buffer[i] & 0x00ff);
            char_buffer[j] = (char)((big * 0x100) + little);
         }
         encryption_key = null;  // Not an encrypted file. Ne estas kripta dosiero.
         return char_buffer;
      }
      else 
      if (file_format == Detect.ENCRYPTED) {
         // Get encryption key. Akiru kriptigan þlosilon.
         open_key_dialog.showIt();
         encryption_key = open_key_dialog.getKey();
         char_buffer = Encrypt.decrypt2(byte_buffer, encryption_key);
         if (char_buffer != null) {
            file_converted_from = "Encrypted"; 
            return char_buffer;
         }
         // If encryption key was bad.    Se þlosilo estas maløusta.
         get_new_doc();
         set_title(new_document_text);       // New Document  / Nova Dokumento
         current_file_name = null;    // Oni ankoraý ne donis nomon.
         file_converted_from = null;
         encryption_key = null;
         validate();
         text_pane.requestFocus();
         return null;
      }
      else {
         /* Can't automatically detect format. Check for UTF8 and ask. */
         /* Ne povas aýtomate determini formaton. Kontrolu UTF8 kaj demandu. */
         if (file_format == Detect.UTF8) {
            open_convert_dialog.showIt("UTF8");
         }
         else {
            open_convert_dialog.showIt("ISO8859_1");
         }
         String decision = open_convert_dialog.getDecision();
         file_converted_from = decision;    // Remember the original format. 
                                            // Memoru la originalan formaton.
         String unicode_string = SimCon.convertToUnicode(byte_buffer, decision);
         encryption_key = null;  // Not an encrypted file. Ne estas kripta dosiero.
         return unicode_string.toCharArray();
      }

   }  // read_file_to_charbuf


   /**
    * toggleRightLeft - If the current attributes are right aligned, change
    * them to left aligned, and vice versa. For Persian.
    * - Se la nuna atribuo estas maldekstraflanka teksto, þanøu la dekstraflankak,
    * kaj inverse.
    */
   public void toggleRightLeft() {
      if (current_attributes == left_side) {
         current_attributes = right_side;
         doc.setParagraphAttributes(0,doc.getLength(),current_attributes,false);
      }
      else {
         current_attributes = left_side;
         doc.setParagraphAttributes(0,doc.getLength(),current_attributes,false);
      }
   }


/////////////////////////////////////////////////////////////////////////////////////


   /**
    * SaveFile - Action class to save a file.
    * SaveFile - Agoklaso por konservi dosieron.
    */
   class SaveFile extends AbstractAction {

      SaveFile(String default_label) {
         super(default_label);
      }

      SaveFile(String default_label, Icon the_icon) {
         super(default_label, the_icon);
      }

      public void actionPerformed(ActionEvent e) {
         save_to_disk(false);
         text_pane.requestFocus();
      }  // actionPerformed

   }  // SaveFile


   /**
    * SaveFileAs - Action class to save a file. This Action shows the file dialogue.
    * SaveFileAs - Agoklaso por konservi dosieron. Æi tiu Agklaso montras la dosiero-dialogon.
    */
   class SaveFileAs extends AbstractAction {

      SaveFileAs(String default_label) {
         super(default_label);
      }

      public void actionPerformed(ActionEvent e) {
         save_to_disk(true);  // Show the file dialogue. Montru la dosiero-dialogon.
      }  // actionPerformed

   }  // SaveFileAs



   /**
    * save_to_disk - This method write the current document out to the disk.
    * The parameter must_ask_for_filename forces the method to show the file 
    * chooser dialog.
    *
    * save_to_disk - Æi tiu metodo skribas la nunan dokumenton al la disko. 
    * La parametro must_ask_for_filename (devas demandi dosieronomon) devigas la
    * metodon montri la dosiero-dialogon.
    */
   protected void save_to_disk (boolean must_ask_for_filename) {

      int  hi, lo;
      String  decision = "";  // Which format to use. Kiun formaton por uzi.

      File  file_to_save;   // konservota dosiero

      // Create a file dialog, if it doesn't already exist.
      // Kreu dosierodialogon, se øi ne jam ekzistas.
      if (file_chooser == null) {
         file_chooser = new JFileChooser();  
      } 


      // If the current document doesn't have a name, or the must_ask_for_filename
      // flag is set, show the file chooser to select a name (and location).
      // Otherwise, just use the current_file_name.
      // Se la nuna dokumento ne havas nomon, aý la "devas-demandi-dosieron"-flagon
      // estas aktiva, montru la dosiero-dialogon por selekti nomon (kaj dosierujon).
      // Alie, uzu la nomon en "current_file_name".
      if (current_file_name == null || must_ask_for_filename || file_converted_from == null) {
         // First, ask for the conversion format for the output file.
         // Unue, demandu la konvert-formaton por la konservota dokumento.
         save_convert_dialog.showIt(file_converted_from);
         file_converted_from = save_convert_dialog.getDecision();

         // If user chooses "encrypted" display a dialogue to ask for the encryption key.
         // Se uzanto elektas "kripta" montru dialogon por peti la þlosilon.
         if (file_converted_from.startsWith("Encrypted")) {
               save_key_dialog.showIt(encryption_key);
               encryption_key = save_key_dialog.getKey();
            if (encryption_key == null) {
               return;
            }
         }
         // Choose a file name. Elektu dosieronomon.
         file_chooser.setDialogTitle(save_text);
         file_chooser.setApproveButtonText(approve_text);
         int returnVal = file_chooser.showSaveDialog(frame);
         if(returnVal == JFileChooser.APPROVE_OPTION) { 
            file_to_save = file_chooser.getSelectedFile();
            // If the file already exists, ask the user if he/she wants to overwrite.
            // Se la dosiero jam ekzistas, demandu al la uzanto æu li/þi volas surskribi.
            if (file_to_save.exists()) {
               exists_dialog.showIt();
               if (exists_dialog.getDecision().equals("no")) return;
            }
         }
         else {
            return;
         }
      }
      else {
         file_to_save = new File(current_file_name);
      } 

      System.out.println("Save file. Konservu dosieron. " + file_to_save.getPath()); 

      try {
         FileOutputStream fout = new FileOutputStream(file_to_save);
         OutputStreamWriter w;
         Document doc = text_pane.getDocument();
         if (file_converted_from.startsWith("UTF16")) {
            // Save as 16 bit Unicode. Konservu kiel 16-bita Unikodo.
            // Get the text into a segment. Metu la tekston en "segment"
            Segment the_segment = new Segment();
            int length = doc.getLength();
            doc.getText(0,length,the_segment);
            // Write codes to start unicode document, little endian.
            // Elskribu komencajn kodojn por unikoda dokumento, pezfine.
            try {
               if (file_converted_from.startsWith("UTF16 little")) {
                  // Unicode little endian. Pezfina Unikodo.
                  fout.write(255); fout.write(254);
                  for (int i = 0; i < length; i++) {
                     lo = (int)the_segment.array[i] & 0xFF;         
                     hi =  (int)the_segment.array[i] / 0x100;         
                     fout.write(lo);         
                     fout.write(hi);
                  }  // for
               }
               else {
                  // Must be Unicode big endian. Devas esti pezkomenca Unikodo.
                  fout.write(254); fout.write(255);
                  for (int i = 0; i < length; i++) {
                     lo = (int)the_segment.array[i] & 0xFF;         
                     hi =  (int)the_segment.array[i] / 0x100;         
                     fout.write(hi);         
                     fout.write(lo);
                  }  // for
               }
               saved_message.showIt();
            } catch ( IOException m ) {
               error_dialog.showIt(cant_save_message + " -C-");
               System.err.println("Could not save file. Ne povis konservi dosieron. -C- " + 
                           m.getMessage());
            }  // try/catch
         }
         else
         if (file_converted_from.startsWith("Encrypted")) {
            // At this point there should be an encryption key.
            // Æi tie devas ekzisti kriptoþlosilo.
            if (encryption_key != null) {
               // Get the text into a segment. Metu la tekston en "segment"
               try {
                  Segment the_segment = new Segment();
                  int length = doc.getLength();
                  doc.getText(0,length,the_segment);
                  Encrypt.writeEncrypted(fout, the_segment, length, encryption_key);
                  saved_message.showIt();
               } catch ( IOException s ) {
                  error_dialog.showIt(cant_save_message + " -G-");
                  System.err.println("Could not save file. Ne povis konservi dosieron. -G- " + 
                             s.getMessage());
               }  // try/catch
            }
            else {
               error_dialog.showIt(cant_save_message + " -K-");
               System.err.println
                 ("Did not save encrypted file. Ne konservis kriptan dosieron. -K- ");
            }
         }
         else  // convert to Iran System
         if (file_converted_from.startsWith("Iran")) { 
               // Get the text into a segment. Metu la tekston en "segment"
               Segment the_segment = new Segment();
               int length = doc.getLength();
               doc.getText(0,length,the_segment);
               try {
                  int i;
                  // Convert first letter. Konvertu unuan literon.
                  fout.write( 
                     SimIran.convertUniIran('a',the_segment.array[0],the_segment.array[1])
                  );

                  for (i = 1; i < length-1; i++) {
                     fout.write( 
                        SimIran.convertUniIran(
                           the_segment.array[i-1],the_segment.array[i],the_segment.array[i+1]
                        )
                     );
                  }
                  // Convert last letter.
                  fout.write( 
                     SimIran.convertUniIran(the_segment.array[i-1],the_segment.array[i],'a')
                  );
                  saved_message.showIt();
               } catch ( IOException r ) {
                  error_dialog.showIt(cant_save_message + " -C-");
                  System.err.println("Could not save file. Ne povis konservi dosieron. -C- " + 
                             r.getMessage());
               }  // try/catch
         }
		 // Convert Unicode to VazheNegar
		 if (file_converted_from.startsWith("Vazhe")) { 
               // Get the text into a segment.
               Segment the_segment = new Segment();
               int length = doc.getLength();
               doc.getText(0,length,the_segment);
               try {
                  int i;
                  // Convert first letter. Konvertu unuan literon.
                  fout.write( 
                     SimIran.convertUniVazhe('a',the_segment.array[0],the_segment.array[1])
                  );

                  for (i = 1; i < length-1; i++) {
                     fout.write( 
                        SimIran.convertUniVazhe(
                           the_segment.array[i-1],the_segment.array[i],the_segment.array[i+1]
                        )
                     );
                  }
                  // Convert last letter. Konvertu lastan literon.
                  fout.write( 
                     SimIran.convertUniIran(the_segment.array[i-1],the_segment.array[i],'a')
                  );
                  saved_message.showIt();
               } catch ( IOException r ) {
                  error_dialog.showIt(cant_save_message + " -C-");
                  System.err.println("Could not save file. Ne povis konservi dosieron. -C- " + 
                             r.getMessage());
               }  // try/catch
         }
         else {
            try {
               w = new OutputStreamWriter(fout,file_converted_from);
               the_editor_kit.write(w,doc,0,doc.getLength());
               saved_message.showIt();
            } catch ( IOException e ) {
               error_dialog.showIt(cant_save_message + " -F-");
               System.err.println("Could not save file. Ne povis konservi dosieron. -F- " + 
                          e.getMessage());
            }  // try/catch
         }
         
         fout.close();
         // Show the file name in the frame.
         // Montru nomon en supra trabo de kadro.
         current_file_name = file_to_save.getPath();   
         // Don't use getName. It doesn't return the full path.
         // Ne uzu getName. Øi ne redonas la kompletan nomon.
         set_title(current_file_name); 
         putIntoFileList(current_file_name); 
         // Recreate the file menu.     Rekreu la dokument-menuon.
         file_menu.removeAll();
         initializeFileMenu(file_menu);
         System.out.println("Number of bytes/ Nombro da bitokoj: " + 
                             (int)(file_to_save.length()));
      } catch (IOException io) {
         error_dialog.showIt(cant_save_message + " -D-");
         System.err.println
           ("Could not save file. Ne povis konservi dosieron. -D- " + io.getMessage());
         return;
      } catch (BadLocationException bad) {
         error_dialog.showIt(cant_save_message + " -E-");
         System.err.println("Could not save file. Ne povis konservi dosieron. -E- " 
                             + bad.getMessage());
         return;
      }
      file_chooser.rescanCurrentDirectory();
      document_has_changed = false;

   }  // save_to_disk


   /**
    * Exit - Action class to exit the program.
    * Exit - Agklaso por fermi la programon.
    */
   class Exit extends AbstractAction {

      Exit(String default_label) {
         super(default_label);
      }

      public void actionPerformed(ActionEvent e) {

         if (document_has_changed) {     // se dokumento estas þanøita
            file_changed_dialog.showIt();
            String decision = file_changed_dialog.getDecision();
            if (decision.equals("yes")) {
               save_to_disk(false);  // Konservu øin.
            }
            if (decision.equals("cancel")) {
               return;
            }
         }

         saveFileList();
         saveLastFont();
         System.exit(0);
      }  // actionPerformed

   }  // Exit



   /**
    * ShowSearch - Action class show the search dialogue.
    * ShowSearch - Agklaso por montri seræodialogon.
    */
   class ShowSearch extends AbstractAction {

      ShowSearch(String default_label) {
         super(default_label);
      }

      ShowSearch(String default_label, Icon the_icon) {
         super(default_label, the_icon);
      }

      public void actionPerformed(ActionEvent e) {
         search_dialog.showIt(text_pane);
      }  // actionPerformed

   }  // ShowSearch





   /**
    * SearchUpx - Action class to search upward.
    * SearchUpx - Agoklaso por seræi supren.
    */
   public class SearchUpx extends AbstractAction {

      SearchUpx(String default_label) {
         super(default_label);
      }

      SearchUpx(String default_label, Icon the_icon) {
         super(default_label, the_icon);
      }

      public void actionPerformed(ActionEvent e) {
         search_dialog.search_up();
         text_pane.requestFocus();
      }  // actionPerformed

   }  // SearchUpx



   /**
    * SearchDownx - Action class to search downward.
    * SearchDownx - Agoklaso por seræi malsupren.
    */
   public class SearchDownx extends AbstractAction {

      SearchDownx(String default_label) {
         super(default_label);
      }


      SearchDownx(String default_label, Icon the_icon) {
         super(default_label, the_icon);
      }

      public void actionPerformed(ActionEvent e) {
         search_dialog.search_down();
         text_pane.requestFocus();
      }  // actionPerformed

   }  // SearchDownx



   /**
    * ShowChange - Action class show the change dialogue.
    * ShowChange - Agklaso por montri þanøodialogon.
    */
   class ShowChange extends AbstractAction {

      ShowChange(String default_label) {
         super(default_label);
      }
      public void actionPerformed(ActionEvent e) {
         change_dialog.showIt(text_pane);
      }  // actionPerformed

   }  // ShowChange




   /**
    * ShowEspConvertDialog - Action class show the Esperanto convert dialog.
    * ShowEspConvertDialog - Agklaso por montri konvert-dialogon por Esperantaj supersignoj.
    */
   class ShowEspConvertDialog extends AbstractAction {

      ShowEspConvertDialog(String default_label) {
         super(default_label);
      }
      public void actionPerformed(ActionEvent e) {
         esp_convert_dialog.showIt(text_pane);
      }  // actionPerformed

   }  // ShowEspConvertDialog



   /**
    * PrintIt - Action class to print the document.
    * PrintIt - Agklaso por presi la dokumenton.
    */
   class PrintIt extends AbstractAction {

      PrintIt(String default_label) {
         super(default_label);
      }

      public void actionPerformed(ActionEvent e) {

/*
         I couldn't get the following code to work. (Japanese Windows 95, Java 1.2)
         Mi ne sukcesis presi per la sekvantaj instrukcioj. (Japana Vindozo 95, Øavo 1.2)
         PrintJob print_job = my_toolkit.getPrintJob(frame,"Simredo Print Job",my_properties);
         if (print_job != null) {
            Graphics g = print_job.getGraphics();
            if (g != null) {
               g.translate(0,0);
               text_pane.printAll(g);
               g.dispose();
               print_job.end();
            }
         }
*/
         // This works. Æi tio funkcias.
         PrinterJob  job = PrinterJob.getPrinterJob(); 
         Paper       paper = new Paper();
         // The imageable height must be a multiple of the font size, less than 720.
         // La desegnebla alteco devas esti oblo de la tipara grandeco, malpli ol 720.
         double font_height = text_pane.heightOfFont();
         int lines_per_page = (int)(720.0 / font_height);
         paper.setImageableArea(36.0,36.0,540.0,(double)lines_per_page * font_height);
         double paperh = paper.getHeight(); 
         double paperw = paper.getWidth(); 
         double imagex = paper.getImageableX(); 
         double imagey = paper.getImageableY(); 
         double imagewidth = paper.getImageableWidth(); 
         double imageheight = paper.getImageableHeight(); 
         System.out.println("Font height. Tipara alteco.  " + font_height);
         System.out.println("Page  w " + paperw + "  h " + paperh);
         System.out.println("Image   x " + imagex + "  y " + imagey + 
                            "  w " + imagewidth + "  h " + imageheight);
         PageFormat  pf = new PageFormat();
         pf.setPaper(paper);
         job.setPrintable(text_pane,pf);
         PageDivider pd = new PageDivider(text_pane, pf);
         job.setPageable(pd);
         text_pane.repaint();
         if (job.printDialog()) {
            try {
               job.print();
            } catch (Exception ex) { 
               System.err.println("Print Error. Presada Eraro. " + ex.toString()); 
            }
         } // if

      }  // actionPerformed

   }  // PrintIt


   /**
    * SpellCheck - Action class to activate Esperanto Spellcheck.
    * Agklaso por montri aktivigi kontrolon de literumado.
    */
   class SpellCheck extends AbstractAction {

      SpellCheck(String default_label) {
         super(default_label);
      }
      public void actionPerformed(ActionEvent e) {
         text_pane.toggleSpellCheck();
      }  // actionPerformed

   }  // SpellCheck



   /**
    * RightLeft - Changes the  alignment. (For rtl languages.)
    * Por þanøi la vicflankon (?) de teksto. (Por lingvoj kiuj fluas de la dekstro.)
    */
   class RightLeft extends AbstractAction {

      RightLeft(String default_label) {
         super(default_label);
      }

      public void actionPerformed(ActionEvent e) {
         toggleRightLeft();
      }  // actionPerformed

   }  // RightLeft



   /**
    * ReverseText - Reverses the order of letters on each line.
    * This is useful for making Persian homepages for browsers which
    * cannot display right to left text.
    * ReverseText - Inversigas la ordon de literoj en æiu linio.
    * Æi tio utilas por krei html-paøojn por foliumiloj kiuj ne kapablas
    * montri tekston kiu fluas de la dekstro.
    */
   class ReverseText extends AbstractAction {

      ReverseText(String default_label) {
         super(default_label);
      }

      public void actionPerformed(ActionEvent e) {
         text_pane.reverseText();
      }  // actionPerformed

   }  // ReverseText







   /**
    * ShowChars - Show the complete Unicode character set for the current font.
    * ShowChars - Montru la tutan Unikodan kodaron por la aktiva tiparo.
    */
   class ShowChars extends AbstractAction {

      ShowChars(String default_label) {
         super(default_label);
      }

      public void actionPerformed(ActionEvent e) {
         show_chars_dialog.showIt();
      }  // actionPerformed

   }  // ShowChars




   /*
    * KeyManager
    * This class is used to disable the key manager for the font list.
    * Why, you may ask? On startup, the font list was always getting 
    * focus. The first letter I typed was not going into the text pane,
    * but was changing the font. For example, if the startup font was
    * Garamond, and I typed an "a", the font would change to Arial.
    * I tried putting "text_pane.requestFocus()" all over. Nothing worked,
    * except this.
    *
    * March 16, 1999 Don't pass focus to the text pane. Force the user 
    * to click on the text pane before starting. If not, problems occurs
    * because the BasicTextUI gets confused and starts throughing out 
    * BadLocationExceptions. (Type a few words, and then backspace, to 
    * cause the problem.)
    * 
    * KlavRegilo
    * Æi tiu klaso nuligas la klavregilon por la tipara listo (font_list).
    * 'Kial?' vi eble demandas. Kiam Simredo ekaktiviøis, la tipara listo
    * æiam akiris fokuson. La unua litero kiun mi tajpis ne eniris la 
    * tekstujon (text_pane), sed þanøis la tiparon. Ekzemple, se la 
    * komenca tiparo estis Garamond, kaj mi tajpis "a", la tiparo þanøiøis
    * al Arial. Mi provis meti "text_pane.requestFocus()" en diversaj lokoj.
    * Nenio sukcesis, escepte de æi tio. 
    *
    * marto 16, 1999 Ne transdonu fokuson al la tekstujo. Devigu la uzanton
    * premi sur la tekstujon antaý ol komenci. Se ne, problemoj okazas
    * æar BasicTextUI konfuziøas kaj komencas el¼eti BadLocationException.
    * (Por kaýzi la problemon, tajpu kelkajn vortojn, kaj tiam retropaþon (BS).
    *
    */
   class KeyManager implements JComboBox.KeySelectionManager {
      public int selectionForKey(char achar, ComboBoxModel amodel) {
         //text_pane.requestFocus();
         return -1;  // Whatever. Io ajn.
      }  
   }  // KeyManager



   /**
    * Closer - This class closes the program.
    * Closer - Æi tiu klaso fermas la programon.
    */
   class Closer extends WindowAdapter {

      public Closer() {
         super();
      }

      public void windowClosing(WindowEvent e) {
         if (document_has_changed) {     // se dokumento estas þanøita
            file_changed_dialog.showIt();
            String decision = file_changed_dialog.getDecision();
            if (decision.equals("yes")) {
               save_to_disk(false);  // Konservu øin.
            }
            if (decision.equals("cancel")) {  
               return;   // Ne fermu la programon.
            }
         }
         saveFileList();
         saveLastFont();
         System.exit(0);
      }
   }  // Closer


   /**
    * SimCut - Action class to cut text.
    * Why not just use the Action class in DefaultEditorKit?
    * Because I need an icon.
    * SimCut - Agklaso por eltondi tekston.
    * Kial mi ne uzas la agklason en DefaultEditorKit?
    * Æar mi bezonas ikonon.
    */
   class SimCut extends AbstractAction {

      SimCut(String default_label) {
         super(default_label);
      }

      SimCut(String default_label, Icon the_icon) {
         super(default_label, the_icon);
      }

      public void actionPerformed(ActionEvent e) {
         text_pane.cut();
         text_pane.requestFocus();
      }  // actionPerformed

   }  // SimCut



   /**
    * SimCopy - Action class to copy text.
    * Why not just use the Action class in DefaultEditorKit?
    * Because I need an icon.
    * SimCopy - Agklaso por kopii tekston.
    * Kial mi ne uzas la agklason en DefaultEditorKit?
    * Æar mi bezonas ikonon.
    */
   class SimCopy extends AbstractAction {

      SimCopy(String default_label) {
         super(default_label);
      }

      SimCopy(String default_label, Icon the_icon) {
         super(default_label, the_icon);
      }

      public void actionPerformed(ActionEvent e) {
         text_pane.copy();
         text_pane.requestFocus();
      }  // actionPerformed

   }  // SimCopy


   /**
    * SimPaste - Action class to paste text.
    * Why not just use the Action class in DefaultEditorKit?
    * Because I need an icon.
    * SimCopy - Agklaso por interglui tekston.
    * Kial mi ne uzas la agklason en DefaultEditorKit?
    * Æar mi bezonas ikonon.
    */
   class SimPaste extends AbstractAction {

      SimPaste(String default_label) {
         super(default_label);
      }

      SimPaste(String default_label, Icon the_icon) {
         super(default_label, the_icon);
      }

      public void actionPerformed(ActionEvent e) {
         text_pane.paste();
         text_pane.requestFocus();
      }  // actionPerformed

   }  // SimPaste


   // This little class filters file names that end in kmp (keymap)
   // Æi tiu eta klaso filtras dosieronomojn kiuj finiøas per kmp (klavmapo)
   class KeyMapFilter implements FilenameFilter {

      KeyMapFilter() {
      }

      public boolean accept(File dir, String name) {
         if (name.toLowerCase().endsWith("kmp")) return true;
         return false;
      }
   }


} // Farsido






