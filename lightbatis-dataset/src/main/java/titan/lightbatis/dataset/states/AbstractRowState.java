/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package titan.lightbatis.dataset.states;

import javax.sql.DataSource;

import titan.lightbatis.dataset.DataRow;
import titan.lightbatis.dataset.RowState;
import titan.lightbatis.dataset.handler.UpdateHandler;


public abstract class AbstractRowState implements RowState {

    AbstractRowState() {
    }

    public void update(DataSource dataSource, DataRow row) {
        SqlContext ctx = getSqlContext(row);
        //UpdateHandler handler = new BasicUpdateHandler(dataSource, ctx.getSql());
        //execute(handler, ctx.getArgs(), ctx.getArgTypes());
    }


    protected abstract SqlContext getSqlContext(DataRow row);


    protected void execute(UpdateHandler handler, Object[] args,
                           Class[] argTypes) {
        handler.execute(args, argTypes);
    }
}