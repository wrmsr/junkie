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

public final class LetExpr
        extends Expr
{
    private final Var var;
    private final Expr expr;
    private final Expr body;

    public LetExpr(Var var, Expr expr, Expr body)
    {
        this.var = requireNonNull(var);
        this.expr = requireNonNull(expr);
        this.body = requireNonNull(body);
    }

    public Var getVar()
    {
        return var;
    }

    public Expr getExpr()
    {
        return expr;
    }

    public Expr getBody()
    {
        return body;
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
        LetExpr letExpr = (LetExpr) o;
        return Objects.equals(var, letExpr.var) &&
                Objects.equals(expr, letExpr.expr) &&
                Objects.equals(body, letExpr.body);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(var, expr, body);
    }

    @Override
    public <C, R> R accept(ExprVisitor<C, R> visitor, C context)
    {
        return visitor.visitLet(this, context);
    }
}
