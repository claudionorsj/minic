package mini_c;

import java.util.LinkedList;
import java.util.HashMap;

class Dtyp extends Decl {
  final String ident;
  final LinkedList<Dvar> list_decl_var;

  Dtyp(String ident, LinkedList<Dvar> list_decl_var){
    super();
    this.ident = ident;
    this.list_decl_var = list_decl_var;
  }

  void semantic_analysis(LinkedList<String> errors){
    // gets current context for type check
    HashMap<String,String> current_context = list_context.getLast();
    // checks for a possible double declaration of the identifier
    if(set_types.contains(ident))
      errors.add("Multiple declarations of " + ident);
    // starts the semantic check for the struct fields and possible add the struct as a new type
    else{
      set_types.add(ident);
      // create context to the struct fields
      list_context.add(new HashMap<String,String>());

      // map of struct fields and their types to add to the types map
      HashMap<String,String> map_fields_types = new HashMap<String,String>();
      LinkedList<String> list_field = new LinkedList<String>();

      for(Dvar dvar : list_decl_var){
        // check for errors in each variable declaration
        dvar.semantic_analysis(errors);

        // adds field names and type to map
        for(String ident : dvar.list_ident){
          map_fields_types.put(ident,dvar.type);
          list_field.add(ident);
        }
      }

      // remove context created for struct fields
      list_context.removeLast();

      // add struct to the map of types
      map_structs_maps_fields_types.put(ident,map_fields_types);
      map_structs_lists_field.put(ident,list_field);
    }
  }

  void generate_rtl(){}
}
