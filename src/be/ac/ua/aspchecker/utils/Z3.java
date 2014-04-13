package be.ac.ua.aspchecker.utils;

import java.util.HashMap;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;
import com.microsoft.z3.Z3Exception;

public class Z3 {

	public static Context init() throws Z3Exception{
		Context ctx = null;
		HashMap<String, String> cfg = new HashMap<String, String>();
		cfg.put("model", "true");
		ctx = new Context(cfg);
		return ctx;
	}

	public static boolean eval(Context ctx) throws Z3Exception{
		boolean res = false;
		BoolExpr f = ctx.SMTLIBFormulas()[0];
		Solver s = ctx.MkSolver();
		s.Assert(f);
		res = s.Check().equals(Status.SATISFIABLE);
		ctx.Dispose();
		return res;
	}
}
