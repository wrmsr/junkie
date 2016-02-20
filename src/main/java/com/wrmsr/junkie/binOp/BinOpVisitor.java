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
package com.wrmsr.junkie.binOp;

public class BinOpVisitor<C, R>
{
    protected R visitBinOp(BinOp binOp, C context)
    {
        throw new IllegalStateException();
    }

    public R visitPlus(PlusBinOp binOp, C context)
    {
        return visitBinOp(binOp, context);
    }

    public R visitTimes(TimesBinOp binOp, C context)
    {
        return visitBinOp(binOp, context);
    }

    public R visitMinus(MinusBinOp binOp, C context)
    {
        return visitBinOp(binOp, context);
    }

    public R visitConcat(ConcatBinOp binOp, C context)
    {
        return visitBinOp(binOp, context);
    }

    public R visitLte(LteBinOp binOp, C context)
    {
        return visitBinOp(binOp, context);
    }
}
