package com.wrmsr.junkie;

import com.wrmsr.junkie.binOp.BinOp;
import com.wrmsr.junkie.binOp.BinOpVisitor;
import com.wrmsr.junkie.binOp.ConcatBinOp;
import com.wrmsr.junkie.binOp.LteBinOp;
import com.wrmsr.junkie.binOp.MinusBinOp;
import com.wrmsr.junkie.binOp.PlusBinOp;
import com.wrmsr.junkie.binOp.TimesBinOp;
import com.wrmsr.junkie.expr.AppExpr;
import com.wrmsr.junkie.expr.BinOpExpr;
import com.wrmsr.junkie.expr.Expr;
import com.wrmsr.junkie.expr.ExprVisitor;
import com.wrmsr.junkie.expr.FnExpr;
import com.wrmsr.junkie.expr.IfExpr;
import com.wrmsr.junkie.expr.IthExpr;
import com.wrmsr.junkie.expr.KonstExpr;
import com.wrmsr.junkie.expr.LetExpr;
import com.wrmsr.junkie.expr.TupleExpr;
import com.wrmsr.junkie.expr.VarExpr;
import com.wrmsr.junkie.konst.BoolKonst;
import com.wrmsr.junkie.konst.IntKonst;
import com.wrmsr.junkie.konst.Konst;
import com.wrmsr.junkie.konst.KonstVisitor;
import com.wrmsr.junkie.konst.RealKonst;
import com.wrmsr.junkie.konst.StringKonst;

import java.util.stream.Collectors;

public class Junkie
{
    public static boolean isValue(Expr expr)
    {
        return expr.accept(new ExprVisitor<Void, Boolean>()
        {
            @Override
            public Boolean visitKonst(KonstExpr expr, Void context)
            {
                return true;
            }

            @Override
            public Boolean visitBinOp(BinOpExpr expr, Void context)
            {
                return false;
            }

            @Override
            public Boolean visitVar(VarExpr expr, Void context)
            {
                return false;
            }

            @Override
            public Boolean visitFn(FnExpr expr, Void context)
            {
                return true;
            }

            @Override
            public Boolean visitApp(AppExpr expr, Void context)
            {
                return false;
            }

            @Override
            public Boolean visitTuple(TupleExpr expr, Void context)
            {
                return expr.getExprs().stream().allMatch(e -> isValue(e));
            }

            @Override
            public Boolean visitIth(IthExpr expr, Void context)
            {
                return false;
            }

            @Override
            public Boolean visitLet(LetExpr expr, Void context)
            {
                return false;
            }

            @Override
            public Boolean visitIf(IfExpr expr, Void context)
            {
                return false;
            }
        }, null);
    }

/*
(* substitute v for x within e *)
fun subst(v:exp, x:var, e:exp):exp =
  case e of
    Konst _ => e
  | Binop(e1,b,e2) => Binop(subst(v,x,e1), b, subst(v,x,e2))
  | Var y => if (x = y) then v else Var y
  | Fn(y,e1) => if (x = y) then Fn(y,e1) else Fn(y,subst(v,x,e1))
  | App(e1,e2) => App(subst(v,x,e1), subst(v,x,e2))
  | Tuple(es) => Tuple(map (fn e => subst(v,x,e)) es)
  | Ith(i,e1) => Ith(i,subst(v,x,e1))
  | Let(y,e1,e2) =>
      if (x = y) then Let(y,subst(v,x,e1),e2)
      else Let(y,subst(v,x,e1),subst(v,x,e2))
  | If(e1,e2,e3) =>
        If(subst(v,x,e1),subst(v,x,e2),subst(v,x,e3))
*/

    public static Expr subst(Expr v, Var x, Expr e)
    {
        return e.accept(new ExprVisitor<Void, Expr>()
        {
            @Override
            public Expr visitKonst(KonstExpr expr, Void context)
            {
                return expr;
            }

            @Override
            public Expr visitBinOp(BinOpExpr expr, Void context)
            {
                return new BinOpExpr(subst(v, x, expr.getLeft()), expr.getBinOp(), subst(v, x, expr.getRight()));
            }

            @Override
            public Expr visitVar(VarExpr expr, Void context)
            {
                if (x.equals(expr.getVar())) {
                    return v;
                }
                else {
                    return new VarExpr(expr.getVar());
                }
            }

            @Override
            public Expr visitFn(FnExpr expr, Void context)
            {
                if (x.equals(expr.getVar())) {
                    return new FnExpr(expr.getVar(), expr.getExpr());
                }
                else {
                    return new FnExpr(expr.getVar(), subst(v, x, expr.getExpr()));
                }
            }

            @Override
            public Expr visitApp(AppExpr expr, Void context)
            {
                return new AppExpr(subst(v, x, expr.getOuter()), subst(v, x, expr.getInner()));
            }

            @Override
            public Expr visitTuple(TupleExpr expr, Void context)
            {
                return new TupleExpr(expr.getExprs().stream().map(e -> subst(v, x, e)).collect(Collectors.toList()));
            }

            @Override
            public Expr visitIth(IthExpr expr, Void context)
            {
                return new IthExpr(expr.getIndex(), subst(v, x, expr.getExpr()));
            }

            @Override
            public Expr visitLet(LetExpr expr, Void context)
            {
                if (x.equals(expr.getVar())) {
                    return new LetExpr(expr.getVar(), subst(v, x, expr.getExpr()), expr.getBody());
                }
                else {
                    return new LetExpr(expr.getVar(), subst(v, x, expr.getExpr()), subst(v, x, expr.getBody()));
                }
            }

            @Override
            public Expr visitIf(IfExpr expr, Void context)
            {
                return new IfExpr(subst(v, x, expr.getCondition()), subst(v, x, expr.getThenBody()), subst(v, x, expr.getElseBody()));
            }
        }, null);
    }

/*
fun substitute(v:exp, x:var, e:exp):exp =
  (print "Substituting "; print_exp v; print " for "; print x; print " in ";
   print_exp e; print "\n"; pause(); subst(v,x,e))
*/

    public static Expr substitute(Expr v, Var x, Expr e)
    {
        return subst(v, x, e);
    }

/*
fun eval_binop(b:binop, c1:const, c2:const):const =
  case (b, c1, c2) of
    (Plus, Int i, Int j) => Int(i+j)
  | (Plus, Real r, Real s) => Real(r+s)
  | (Times, Int i, Int j) => Int(i*j)
  | (Times, Real r, Real s) => Real(r*s)
  | (Minus, Int i, Int j) => Int(i-j)
  | (Minus, Real r, Real s) => Real(r-s)
  | (Lte, Int i, Int j) => Bool(i <= j)
  | (Lte, Real r, Real s) => Bool(r <= s)
  | (Concat, String s, String t) => String(s^t)
  | (_,_,_) => raise Eval_Error("type mismatch for binop")
*/

    public static Konst evalBinOp(BinOp b, Konst c1, Konst c2)
    {
        return b.accept(new BinOpVisitor<Void, Konst>()
        {
            @Override
            public Konst visitPlus(PlusBinOp binOp, Void context)
            {
                return c1.accept(new KonstVisitor<Void, Konst>()
                {
                    @Override
                    public Konst visitInt(IntKonst k1, Void context)
                    {
                        return c2.accept(new KonstVisitor<Void, Konst>()
                        {
                            @Override
                            public Konst visitInt(IntKonst k2, Void context)
                            {
                                return new IntKonst(k1.getValue() + k2.getValue());
                            }
                        }, null);
                    }

                    @Override
                    public Konst visitReal(RealKonst k1, Void context)
                    {
                        return c2.accept(new KonstVisitor<Void, Konst>()
                        {
                            @Override
                            public Konst visitReal(RealKonst k2, Void context)
                            {
                                return new RealKonst(k1.getValue() + k2.getValue());
                            }
                        }, null);
                    }
                }, null);
            }

            @Override
            public Konst visitTimes(TimesBinOp binOp, Void context)
            {
                return c1.accept(new KonstVisitor<Void, Konst>()
                {
                    @Override
                    public Konst visitInt(IntKonst k1, Void context)
                    {
                        return c2.accept(new KonstVisitor<Void, Konst>()
                        {
                            @Override
                            public Konst visitInt(IntKonst k2, Void context)
                            {
                                return new IntKonst(k1.getValue() * k2.getValue());
                            }
                        }, null);
                    }

                    @Override
                    public Konst visitReal(RealKonst k1, Void context)
                    {
                        return c2.accept(new KonstVisitor<Void, Konst>()
                        {
                            @Override
                            public Konst visitReal(RealKonst k2, Void context)
                            {
                                return new RealKonst(k1.getValue() * k2.getValue());
                            }
                        }, null);
                    }
                }, null);
            }

            @Override
            public Konst visitMinus(MinusBinOp binOp, Void context)
            {
                return c1.accept(new KonstVisitor<Void, Konst>()
                {
                    @Override
                    public Konst visitInt(IntKonst k1, Void context)
                    {
                        return c2.accept(new KonstVisitor<Void, Konst>()
                        {
                            @Override
                            public Konst visitInt(IntKonst k2, Void context)
                            {
                                return new IntKonst(k1.getValue() - k2.getValue());
                            }
                        }, null);
                    }

                    @Override
                    public Konst visitReal(RealKonst k1, Void context)
                    {
                        return c2.accept(new KonstVisitor<Void, Konst>()
                        {
                            @Override
                            public Konst visitReal(RealKonst k2, Void context)
                            {
                                return new RealKonst(k1.getValue() - k2.getValue());
                            }
                        }, null);
                    }
                }, null);
            }

            @Override
            public Konst visitConcat(ConcatBinOp binOp, Void context)
            {
                return c1.accept(new KonstVisitor<Void, Konst>()
                {
                    @Override
                    public Konst visitString(StringKonst k1, Void context)
                    {
                        return c2.accept(new KonstVisitor<Void, Konst>()
                        {
                            @Override
                            public Konst visitString(StringKonst k2, Void context)
                            {
                                return new StringKonst(k1.getValue() + k2.getValue());
                            }
                        }, null);
                    }
                }, null);
            }

            @Override
            public Konst visitLte(LteBinOp binOp, Void context)
            {
                return c1.accept(new KonstVisitor<Void, Konst>()
                {
                    @Override
                    public Konst visitInt(IntKonst k1, Void context)
                    {
                        return c2.accept(new KonstVisitor<Void, Konst>()
                        {
                            @Override
                            public Konst visitInt(IntKonst k2, Void context)
                            {
                                return new BoolKonst(k1.getValue() <= k2.getValue());
                            }
                        }, null);
                    }

                    @Override
                    public Konst visitReal(RealKonst k1, Void context)
                    {
                        return c2.accept(new KonstVisitor<Void, Konst>()
                        {
                            @Override
                            public Konst visitReal(RealKonst k2, Void context)
                            {
                                return new BoolKonst(k1.getValue() <= k2.getValue());
                            }
                        }, null);
                    }
                }, null);
            }
        }, null);
    }

/*
fun eval'(e:exp):exp =
  case e of
    Const c => Const c
  | Binop (e1, b, e2) =>
      let val v1 = eval e1
          val v2 = eval e2
      in
        case (v1, v2) of
          (Const c1, Const c2) => Const(eval_binop(b, c1, c2))
        | _ => raise Eval_Error("bad binop expression")
      end
  | Var x => raise Eval_Error("unbound variable "^x)
  | Fn(x,e1) => Fn(x,e1)
  | App(e1,e2) =>
      let val v1 = eval e1
          val v2 = eval e2
      in
        case v1 of
          Fn(x,e) => eval(substitute(v2, x, e))
        | _ => raise Eval_Error("attempt to apply a non-function")
      end
  | Tuple(es) => Tuple(map eval es)
  | Ith(i,e) =>
      let val v = eval e
      in
        case v of
          Tuple(vs) =>
            if i < 1 orelse i > (length vs) then
              raise Eval_Error("tuple index out of bounds")
            else List.nth(vs,i-1)
        | _ => raise Eval_Error("attempt to project from non-tuple")
      end
  | Let(x,e1,e2) =>
      let val v1 = eval e1
      in
        eval(substitute(v1, x, e2))
      end
  | If(e1,e2,e3) =>
      let val v1 = eval e1
      in
        case v1 of
          Const(Bool true) => eval e2
        | Const(Bool false) => eval e3
        | _ => raise Eval_Error("attempt to do if on non-bool")
      end
*/

    public static Expr eval1(Expr e)
    {
        e.accept(new ExprVisitor<Void, Expr>()
        {
            @Override
            public Expr visitKonst(KonstExpr expr, Void context)
            {
                return expr;
            }

            @Override
            public Expr visitBinOp(BinOpExpr expr, Void context)
            {
                Expr v1 = eval(expr.getLeft());
                Expr v2 = eval(expr.getRight());
                return v1.accept(new ExprVisitor<Void, Expr>()
                {
                    @Override
                    public Expr visitKonst(KonstExpr k1, Void context)
                    {
                        return v2.accept(new ExprVisitor<Void, Expr>()
                        {
                            @Override
                            public Expr visitKonst(KonstExpr k2, Void context)
                            {
                                return new KonstExpr(evalBinOp(expr, k1.getKonst(), k2.getKonst()));
                            }
                        }, null);
                    }
                }, null);
            }

            @Override
            public Expr visitVar(VarExpr expr, Void context)
            {
                throw new IllegalStateException("Unbound variable: " + expr.getVar().getValue());
            }

            @Override
            public Expr visitFn(FnExpr expr, Void context)
            {
                return new FnExpr(expr.getVar(), expr.getExpr());
            }

            @Override
            public Expr visitApp(AppExpr expr, Void context)
            {
                Expr v1 = eval(expr.getOuter());
                Expr v2 = eval(expr.getInner());
                return v1.accept(new ExprVisitor<Void, Expr>()
                {
                    @Override
                    public Expr visitFn(FnExpr expr, Void context)
                    {
                        return eval(substitute(v2, expr.getVar(), expr.getExpr()));
                    }
                }, null);
            }

            @Override
            public Expr visitTuple(TupleExpr expr, Void context)
            {
                return new TupleExpr(expr.getExprs().stream().map(e -> eval(e)).collect(Collectors.toList()));
            }

            @Override
            public Expr visitIth(IthExpr ith, Void context)
            {
                Expr v = eval(ith.getExpr());
                return v.accept(new ExprVisitor<Void, Expr>() {
                    @Override
                    public Expr visitTuple(TupleExpr t, Void context)
                    {
                        if (ith.getIndex() < 1 || ith.getIndex() > t.getExprs().size()) {
                            throw new IllegalStateException("Tuple index out of bounds");
                        }
                        else {
                            return t.getExprs().get(ith.getIndex() - 1);
                        }
                    }
                }, null);
            }

            @Override
            public Expr visitLet(LetExpr expr, Void context)
            {
                return super.visitLet(expr, context);
            }

            @Override
            public Expr visitIf(IfExpr expr, Void context)
            {
                return super.visitIf(expr, context);
            }
        }, null);
    }

/*
and eval(e:exp):exp =
  if is_value e then e else
    (print "Evaluating: "; print_exp e; print "\n"; pause();
     let val v = eval'(e)
     in
       print "Result of "; print_exp e; print "\n is ";
       print_exp v; print "\n\n"; v
     end)
*/

    public static Expr eval(Expr e)
    {
        if (isValue(e)) {
            return e;
        }
        else {
            Expr v = eval1(e);
            return v;
        }
    }

    public static void main(String[] args)
    {
    }
}
