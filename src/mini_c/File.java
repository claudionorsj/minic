package mini_c;

import java.util.LinkedList;
import java.util.HashMap;

class File extends Base{
  final LinkedList<Decl> list_decl;

  File(LinkedList<Decl> list_decl) {
    super();
    this.list_decl = list_decl;
  }

  void semantic_analysis(LinkedList<String> errors){
    // add global context
    list_context.add(new HashMap<String,String>());

    // add int to the set of types
    set_types.add("int");

    // add putchar and sbrk to the map of functions and to the global context
    LinkedList<Param> list_param = new LinkedList<Param>();
    list_param.add(new Param("int","n"));
    map_fcts.put("putchar", list_param);
    map_fcts.put("sbrk", list_param);
    list_context.get(0).put("putchar","int");
    list_context.get(0).put("sbrk","void");

    // check for semantic errors in each declaration
    for(Decl decl : list_decl)
      decl.semantic_analysis(errors);
  }
}
