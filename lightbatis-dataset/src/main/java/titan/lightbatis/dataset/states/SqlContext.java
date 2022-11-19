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


import lombok.Data;

import java.util.List;

@Data
public class SqlContext {

    private String sql;

    private Object[] args;

//    private Class[] argTypes;

    private int[] argTypes;

    public SqlContext() {
    }


//    public SqlContext(String sql, Object[] args, Class[] argTypes) {
//        setSql(sql);
//        setArgs(args);
//        setArgTypes(argTypes);
//    }

    public SqlContext(String sql, Object[] args, List<Integer> argTypeList) {
        //int[] argTypes
        setSql(sql);
        setArgs(args);
        //setArgTypes(argTypes);
        System.out.println("================>>>>>>>>" + argTypeList);
        argTypes = new int[argTypeList.size()];
        for (int i=0;i< argTypeList.size();i++) {
            argTypes[i] = argTypeList.get(i);
        }
    }

}
