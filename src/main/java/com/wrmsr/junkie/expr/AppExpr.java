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

public final class AppExpr
        extends Expr
{
    private final Expr outer;
    private final Expr inner;

    public AppExpr(Expr outer, Expr inner)
    {
        this.outer = requireNonNull(outer);
        this.inner = requireNonNull(inner);
    }

    public Expr getOuter()
    {
        return outer;
    }

    public Expr getInner()
    {
        return inner;
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
        AppExpr appExpr = (AppExpr) o;
        return Objects.equals(outer, appExpr.outer) &&
                Objects.equals(inner, appExpr.inner);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(outer, inner);
    }

    @Override
    public <C, R> R accept(ExprVisitor<C, R> visitor, C context)
    {
        return visitor.visitApp(this, context);
    }
}
