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

import com.wrmsr.junkie.Var;
import com.wrmsr.junkie.expr.Expr;
import com.wrmsr.junkie.expr.ExprVisitor;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public final class FnExpr
        extends Expr
{
    private final Var var;
    private final Expr expr;

    public FnExpr(Var var, Expr expr)
    {
        this.var = requireNonNull(var);
        this.expr = requireNonNull(expr);
    }

    public Var getVar()
    {
        return var;
    }

    public Expr getExpr()
    {
        return expr;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FnExpr fnExpr = (FnExpr) o;
        return Objects.equals(var, fnExpr.var) &&
                Objects.equals(expr, fnExpr.expr);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(var, expr);
    }

    @Override
    public <C, R> R accept(ExprVisitor<C, R> visitor, C context)
    {
        return visitor.visitFn(this, context);
    }
}
