package mini_c;

import java.util.LinkedList;
import java.util.HashMap;
import java.util.HashSet;

// has all global fields
abstract class Base{
  // list of maps from identifiers to types
  static LinkedList<HashMap<String,String>> list_context = new LinkedList<HashMap<String,String>>();

  // set of defined types
  static HashSet<String> set_types = new HashSet<String>();

  // map of defined structs to its map of fields to type
  static HashMap<String,HashMap<String,String>> map_structs_maps_fields_types = new HashMap<String,HashMap<String,String>>();

  // map of defined structs to list of its fields
  static HashMap<String,LinkedList<String>> map_structs_lists_field = new HashMap<String,LinkedList<String>>();

  // map of defined functions to its map of parameters to type
  static HashMap<String,LinkedList<Param>> map_fcts_params = new HashMap<String,LinkedList<Param>>();

  static String current_fct = "";

  static HashMap<String,HashSet<String>> map_fcts_locals = new HashMap<String,HashSet<String>>();

  static RTLgraph current_rtlgraph;

  static RTLfile rtlfile = new RTLfile();

  // map from identifiers to registers
  static HashMap<String,Register> map_locals_registers = new HashMap<String,Register>();

  static ERTLfile ertlfile = new ERTLfile();

  static ERTLgraph current_ertlgraph;

  static Coloring current_color;

  static LTLfile ltlfile = new LTLfile();

  static LTLgraph current_ltlgraph;

  static int activation_table_size;

  abstract void semantic_analysis(LinkedList<String> errors);
}
