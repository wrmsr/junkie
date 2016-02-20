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
package com.wrmsr.junkie.konst;

import static java.util.Objects.requireNonNull;

public abstract class Konst<T>
{
    private final T value;

    public Konst(T value)
    {
        this.value = requireNonNull(value);
    }

    public T getValue()
    {
        return value;
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

        Konst<?> konst = (Konst<?>) o;

        return value != null ? value.equals(konst.value) : konst.value == null;
    }

    @Override
    public int hashCode()
    {
        return value != null ? value.hashCode() : 0;
    }

    public <C, R> R accept(KonstVisitor<C, R> visitor, C context)
    {
        return visitor.visitKonst(this, context);
    }
}
