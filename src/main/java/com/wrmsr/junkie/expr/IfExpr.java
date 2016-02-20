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

public final class IfExpr
        extends Expr
{
    private final Expr condition;
    private final Expr thenBody;
    private final Expr elseBody;

    public IfExpr(Expr condition, Expr thenBody, Expr elseBody)
    {
        this.condition = requireNonNull(condition);
        this.thenBody = requireNonNull(thenBody);
        this.elseBody = requireNonNull(elseBody);
    }

    public Expr getCondition()
    {
        return condition;
    }

    public Expr getThenBody()
    {
        return thenBody;
    }

    public Expr getElseBody()
    {
        return elseBody;
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
        IfExpr ifExpr = (IfExpr) o;
        return Objects.equals(condition, ifExpr.condition) &&
                Objects.equals(thenBody, ifExpr.thenBody) &&
                Objects.equals(elseBody, ifExpr.elseBody);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(condition, thenBody, elseBody);
    }

    @Override
    public <C, R> R accept(ExprVisitor<C, R> visitor, C context)
    {
        return visitor.visitIf(this, context);
    }
}
