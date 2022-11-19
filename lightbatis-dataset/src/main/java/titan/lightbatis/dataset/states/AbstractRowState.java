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

import org.springframework.jdbc.core.JdbcTemplate;
import titan.lightbatis.dataset.DataRow;
import titan.lightbatis.dataset.RowState;
import titan.lightbatis.dataset.WriteException;
import titan.lightbatis.dataset.exception.SQLRuntimeException;
import titan.lightbatis.dataset.jdbc.UpdateHandler;
import titan.lightbatis.dataset.jdbc.BasicUpdateHandler;


public abstract class AbstractRowState implements RowState {

    AbstractRowState() {
    }

    public void update(JdbcTemplate jdbcTemplate, DataRow row) throws WriteException {
        SqlContext ctx = getSqlContext(row);
        System.out.println(ctx.getSql());
        UpdateHandler handler = new BasicUpdateHandler(jdbcTemplate, ctx.getSql());
        execute(handler, ctx.getArgs(), ctx.getArgTypes());
    }


    protected abstract SqlContext getSqlContext(DataRow row);


    protected void execute(UpdateHandler handler, Object[] args,
                           int[] argTypes) throws WriteException{
        try {
            handler.execute(args, argTypes);
        }catch (SQLRuntimeException ex) {
            throw new WriteException(ex);
        }
    }
}