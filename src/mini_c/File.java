package mini_c;

import java.util.LinkedList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

class File extends Base{
  final LinkedList<Decl> list_decl;

  File(LinkedList<Decl> list_decl) {
    super();
    this.list_decl = list_decl;
  }

  void semantic_analysis(LinkedList<String> errors){
    // init global variables
    list_context = new LinkedList<HashMap<String,String>>();
    set_types = new HashSet<String>();
    map_structs_maps_fields_types = new HashMap<String,HashMap<String,String>>();
    map_structs_lists_field = new HashMap<String,LinkedList<String>>();
    map_fcts_params = new HashMap<String,LinkedList<Param>>();
    map_fcts_locals = new HashMap<String,HashSet<String>>();
    map_locals_registers = new HashMap<String,Register>();

    // add global context (list_context(0))
    list_context.add(new HashMap<String,String>());

    // add int to the set of types
    set_types.add("int");

    // add putchar and sbrk to the map of functions and to the global context
    LinkedList<Param> list_param = new LinkedList<Param>();
    list_param.add(new Param("int","n"));
    map_fcts_params.put("putchar", list_param);
    map_fcts_params.put("sbrk", list_param);
    list_context.get(0).put("putchar","int");
    list_context.get(0).put("sbrk","void");

    // store global variables
    map_fcts_locals.put("",new HashSet<String>());

    // check for semantic errors in each declaration
    for(Decl decl : list_decl){      
      current_fct = "";
      decl.semantic_analysis(errors);
    }
  }

  RTLfile generate_rtl(){
    //creates a new RTL file
    rtlfile = new RTLfile();
    // translates each function
    for(Decl decl : list_decl){
      if(decl instanceof Dfct)
        ((Dfct)decl).generate_rtl();
      else if(decl instanceof Dvar)
        for(String ident : ((Dvar)decl).list_ident)
          rtlfile.gvars.add(ident);
    }

    return rtlfile;
  }  
}
