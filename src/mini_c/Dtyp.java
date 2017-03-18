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

      // stores number of errors before semantic analysis on the struct fields
      int number_of_errors_before = errors.size();

      // map of struct fields and their types to add to the types map
      HashMap<String,String> map_fields = new HashMap<String,String>();

      for(Dvar dvar : list_decl_var){
        // check for errors in each variable declaration
        dvar.semantic_analysis(errors);

        // adds field names and type to map if no error was found to this point
        if(errors.size() == number_of_errors_before)
          for(String ident : dvar.list_ident)
            map_fields.put(ident,dvar.type);
      }

      // remove context created for struct fields
      list_context.removeLast();

      // if no error found in the fields, add struct to the map of types
      if(errors.size() == number_of_errors_before){
        map_structs.put(ident,map_fields);
      }
    }
  }
}
