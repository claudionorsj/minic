package mini_c;

import java.util.LinkedList;
import java.util.HashMap;
import java.util.HashSet;

class Dfct extends Decl {
  final String type;
  final String ident;
  final LinkedList<Param> list_param;
  final Iblock block;

  Dfct(String type, String ident, LinkedList<Param> list_param, Iblock block){
    super();
    this.type = type;
    this.ident = ident;
    this.list_param = list_param;
    this.block = block;
  }

  void semantic_analysis(LinkedList<String> errors){
    HashMap<String,String> current_context = list_context.getLast();

    if(current_context.containsKey(ident))
      errors.add("Multiple declarations of" + ident);
    else if(!set_types.contains(type))
      errors.add("No type " + type);
    else{
      map_fcts_params.put(ident,list_param);
      current_context.put(ident,type);
      current_fct = ident;
      map_fcts_locals.put(ident, new HashSet<String>());
      block.semantic_analysis(errors);
    }
  }

  void generate_rtl(){
    LinkedList<Register> list_param_register = new LinkedList<Register>();
    HashSet<Register> set_locals_registers = new HashSet<Register>();
    Register aux_r;
    map_locals_registers = new HashMap<String,Register>();
    for(Param param : list_param){
      aux_r = new Register();
      list_param_register.add(aux_r);
      map_locals_registers.put(param.ident,aux_r);
    }
    for(String local : map_fcts_locals.get(ident)){
      aux_r = new Register();
      set_locals_registers.add(aux_r);
      map_locals_registers.put(local,aux_r);
    }
    Register return_r = new Register();
    Label return_l = new Label();
    current_rtlgraph = new RTLgraph();
    Label entry_l = block.generate_rtl(return_l,return_r,return_l);
    RTLfun rtlfun = new RTLfun(ident);
    rtlfun.formals = list_param_register;
    rtlfun.locals = set_locals_registers;
    rtlfun.entry = entry_l;
    rtlfun.result = return_r;
    rtlfun.exit = return_l;
    rtlfun.body = current_rtlgraph;
    rtlfile.funs.add(rtlfun);
  }
}
