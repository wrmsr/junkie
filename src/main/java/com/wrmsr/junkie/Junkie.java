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
import com.wrmsr.junkie.konst.IntKonst;
import com.wrmsr.junkie.konst.Konst;
import com.wrmsr.junkie.konst.KonstVisitor;
import com.wrmsr.junkie.konst.RealKonst;

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
                        return c2.accept(new KonstVisitor<Void, Konst>() {
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
                        return c2.accept(new KonstVisitor<Void, Konst>() {
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
                return super.visitTimes(binOp, context);
            }

            @Override
            public Konst visitMinus(MinusBinOp binOp, Void context)
            {
                return super.visitMinus(binOp, context);
            }

            @Override
            public Konst visitConcat(ConcatBinOp binOp, Void context)
            {
                return super.visitConcat(binOp, context);
            }

            @Override
            public Konst visitLte(LteBinOp binOp, Void context)
            {
                return super.visitLte(binOp, context);
            }
        }, null);
    }

    public static void main(String[] args)
    {
    }
}
