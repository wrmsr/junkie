/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wrmsr.junkie.expr;

public abstract class ExprVisitor<C, R>
{
    protected R visitExpr(Expr expr, C context)
    {
        throw new IllegalStateException();
    }

    public R visitKonst(KonstExpr expr, C context)
    {
        return visitExpr(expr, context);
    }

    public R visitBinOp(BinOpExpr expr, C context)
    {
        return visitExpr(expr, context);
    }

    public R visitVar(VarExpr expr, C context)
    {
        return visitExpr(expr, context);
    }

    public R visitFn(FnExpr expr, C context)
    {
        return visitExpr(expr, context);
    }

    public R visitApp(AppExpr expr, C context)
    {
        return visitExpr(expr, context);
    }

    public R visitTuple(TupleExpr expr, C context)
    {
        return visitExpr(expr, context);
    }

    public R visitIth(IthExpr expr, C context)
    {
        return visitExpr(expr, context);
    }

    public R visitLet(LetExpr expr, C context)
    {
        return visitExpr(expr, context);
    }

    public R visitIf(IfExpr expr, C context)
    {
        return visitExpr(expr, context);
    }
}
