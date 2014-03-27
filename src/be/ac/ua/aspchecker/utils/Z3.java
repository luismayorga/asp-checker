package be.ac.ua.aspchecker.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.FuncDecl;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;
import com.microsoft.z3.StringSymbol;
import com.microsoft.z3.Symbol;
import com.microsoft.z3.Z3Exception;

public class Z3 {

	public static boolean eval(String str, List<String[]> mdata){

		boolean res = false;
		try {
			//Init
			Context ctx = null;
			HashMap<String, String> cfg = new HashMap<String, String>();
			cfg.put("model", "true");
			ctx = new Context(cfg);

			List<Symbol> syms = new ArrayList<Symbol>();
			List<FuncDecl> funs = new ArrayList<FuncDecl>();

			//Type mapping
			for (String[] list : mdata) {
				StringSymbol sym = ctx.MkSymbol(list[0]);
				syms.add(sym);
				//TODO map types contained in list[1]
				funs.add(ctx.MkConstDecl(sym, ctx.MkIntSort()));
			}

			//Solving
			ctx.ParseSMTLIBString(str, null, null,
					(Symbol[]) syms.toArray(new Symbol[syms.size()]),
					(FuncDecl[])funs.toArray(new FuncDecl[funs.size()]));
			
			BoolExpr f = ctx.SMTLIBFormulas()[0];
			Solver s = ctx.MkSolver();
			s.Assert(f);
			res = s.Check().equals(Status.SATISFIABLE);
		} catch (Z3Exception e) {e.printStackTrace();}
		return res;
	}
}
