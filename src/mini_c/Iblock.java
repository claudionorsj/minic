package mini_c;

import java.util.LinkedList;
import java.util.HashMap;

class Iblock extends Inst {
  final LinkedList<Dvar> list_decl_var;
  final LinkedList<Inst> list_inst;
  final LinkedList<Param> list_param;

  Iblock(LinkedList<Dvar> list_decl_var, LinkedList<Inst> list_inst){
    super();
    this.list_decl_var = list_decl_var;
    this.list_inst = list_inst;
    this.list_param = new LinkedList<Param>();
  }

  Iblock(Iblock block, LinkedList<Param> list_param){
    super();
    this.list_decl_var = block.list_decl_var;
    this.list_inst = block.list_inst;
    this.list_param = list_param;
  }

  void semantic_analysis(LinkedList<String> errors){
    list_context.add(new HashMap<String,String>());

    for(Param param : list_param)
      param.semantic_analysis(errors);

    for(Dvar dvar : list_decl_var)
      dvar.semantic_analysis(errors);

    for(Inst inst : list_inst)
      inst.semantic_analysis(errors);

    list_context.removeLast();
  }
}
