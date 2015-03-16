/* Copyright 2013-2015 www.snakerflow.com.
 *
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
package org.snaker.engine.entity.var;

import org.snaker.engine.core.ServiceContext;
import org.snaker.engine.helper.StringHelper;

import java.io.Serializable;
import java.util.*;

/**
 * 变量范围
 * @author yuqs
 * @since 3.0
 */
public abstract class VariableScope implements Serializable {
    private static final long serialVersionUID = 1L;
    protected Map<String, Variable> variables = null;

    protected abstract List<Variable> loadVariables();

    protected abstract void setVariableScope(Variable variable);

    protected void ensureVariablesInitialized() {
        if (variables == null) {
            variables = new HashMap<String, Variable>();
            List<Variable> variablesList = loadVariables();
            if (variablesList != null) {
                for (Variable variable : variablesList) {
                    variables.put(variable.getName(), variable);
                }
            }
        }
    }

    public Map<String, Variable> getVariables() {
        ensureVariablesInitialized();
        return variables;
    }

    public Object getVariableValue(String variableName) {
        ensureVariablesInitialized();
        Variable variable = variables.get(variableName);
        if (variable != null) {
            return variable.getValue();
        }
        return null;
    }

    public Variable getVariable(String variableName) {
        ensureVariablesInitialized();
        return variables.get(variableName);
    }

    public boolean hasVariables() {
        ensureVariablesInitialized();
        if (!variables.isEmpty()) {
            return true;
        }
        return false;
    }

    public boolean hasVariable(String variableName) {
        ensureVariablesInitialized();
        return variables.containsKey(variableName);
    }

    protected Set<String> collectVariableNames(Set<String> variableNames) {
        ensureVariablesInitialized();
        for (Variable variableInstance : variables.values()) {
            variableNames.add(variableInstance.getName());
        }
        return variableNames;
    }

    public Set<String> getVariableNames() {
        return collectVariableNames(new HashSet<String>());
    }

    public Map<String, Object> getVariableMap() {
        Map<String, Object> vars = new HashMap<String, Object>();
        ensureVariablesInitialized();
        for (Variable variable : variables.values()) {
            vars.put(variable.getName(), variable.getValue());
        }
        return vars;
    }

    public void setVariables(Map<String, Variable> variables) {
        if (variables != null) {
            for (Variable variable : variables.values()) {
                setVariableScope(variable);
                ServiceContext.getEngine().manager().createVariable(variable);
            }
            this.variables = variables;
        }
    }

    public void createVariables(Map<String, ? extends Object> variables) {
        if (variables != null) {
            for (String variableName : variables.keySet()) {
                createVariable(variableName, variables.get(variableName));
            }
        }
    }

    public void removeVariable(String variableName) {
        ensureVariablesInitialized();
        Variable variable = variables.get(variableName);
        ServiceContext.getEngine().manager().deleteVariable(variable);
        variables.remove(variableName);
    }

    public void createVariable(String variableName, Object value) {
        ensureVariablesInitialized();
        Variable variable = null;
        VariableType type = VariableHelper.findVariableType(value);
        if (type == null) return;
        if (hasVariable(variableName)) {
            variable = variables.get(variableName);
            variable.setVariableType(type);
            variable.setValue(value);
            ServiceContext.getEngine()
                    .manager().updateVariable(variable);
        } else {
            variable = new Variable();
            variable.setId(StringHelper.getPrimaryKey());
            setVariableScope(variable);
            variable.setName(variableName);
            variable.setVariableType(type);
            variable.setValue(value);
            ServiceContext.getEngine()
                    .manager().createVariable(variable);
        }
        variables.put(variableName, variable);
    }
}
