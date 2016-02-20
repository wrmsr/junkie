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

import com.wrmsr.junkie.expr.Expr;
import com.wrmsr.junkie.expr.ExprVisitor;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public final class IthExpr
        extends Expr
{
    private final int index;
    private final Expr expr;

    public IthExpr(int index, Expr expr)
    {
        this.index = index;
        this.expr = requireNonNull(expr);
    }

    public int getIndex()
    {
        return index;
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
        IthExpr ithExpr = (IthExpr) o;
        return index == ithExpr.index &&
                Objects.equals(expr, ithExpr.expr);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(index, expr);
    }

    @Override
    public <C, R> R accept(ExprVisitor<C, R> visitor, C context)
    {
        return visitor.visitIth(this, context);
    }
}
