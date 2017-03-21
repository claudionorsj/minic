package mini_c;

import java.util.LinkedList;
import java.util.HashMap;
import java.util.HashSet;

// has all global fields for rtl generation
abstract class Base{
  // list of maps from identifiers to types
  static LinkedList<HashMap<String,String>> list_context;

  // set of defined types
  static HashSet<String> set_types;

  // map of defined structs to list of its fields
  static HashMap<String,LinkedList<String>> map_structs_lists_field;

  // map of defined structs to its map of fields to type
  static HashMap<String,HashMap<String,String>> map_structs_maps_fields_types;  

  // map of defined functions to its map of parameters to type
  static HashMap<String,LinkedList<Param>> map_fcts_params;

  // map functions identifiers to a set of its locals
  static HashMap<String,HashSet<String>> map_fcts_locals;

  // map from local identifiers to registers
  static HashMap<String,Register> map_locals_registers;

  // strores the name of current function being translated
  static String current_fct;  

  // stores the graph of the current function being translated
  static RTLgraph current_rtlgraph;

  // stores the rtlfile being generated
  static RTLfile rtlfile;

  // imposes all classes to have semantic analysis implemented that gets a list of errors to be thrown at the end
  abstract void semantic_analysis(LinkedList<String> errors);
}
