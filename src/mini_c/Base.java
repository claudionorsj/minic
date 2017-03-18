package mini_c;

import java.util.LinkedList;
import java.util.HashMap;
import java.util.HashSet;

abstract class Base{
  // list of maps from identifiers to types
  static LinkedList<HashMap<String,String>> list_context = new LinkedList<HashMap<String,String>>();

  // set of defined types
  static HashSet<String> set_types = new HashSet<String>();

  // map of defined structs to its map of fields to type
  static HashMap<String,HashMap<String,String>> map_structs = new HashMap<String,HashMap<String,String>>();

  // map of defined functions to its map of parameters to type
  static HashMap<String,LinkedList<Param>> map_fcts = new HashMap<String,LinkedList<Param>>();

  abstract void semantic_analysis(LinkedList<String> errors);
}
