package org.snaker.engine.entity.var;

import org.snaker.engine.SnakerException;
import org.snaker.engine.helper.StringHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 变量类型处理类
 *
 * @author yuqs
 * @since 3.0
 */
public class VariableHelper {
    private static final List<VariableType> typesList = new ArrayList<VariableType>();
    private static final Map<String, VariableType> typesMap = new HashMap<String, VariableType>();

    static {
        addType(new BooleanType());
        addType(new DateType());
        addType(new DoubleType());
        addType(new IntegerType());
        addType(new LongType());
        addType(new NullType());
        addType(new ShortType());
        addType(new StringType());
    }

    public static void addType(VariableType type) {
        typesList.add(type);
        typesMap.put(type.getTypeName(), type);
    }

    public static VariableType getVariableType(String typeName) {
        return typesMap.get(typeName);
    }

    public static VariableType getVariableType(int typeIndex) {
        return typesList.get(typeIndex);
    }

    public static VariableType findVariableType(Object value) {
        for (VariableType type : typesList) {
            if (type.isAbleToStore(value)) {
                return type;
            }
        }
        throw new SnakerException("couldn't find a variable type that is able to serialize " + value);
    }

    public static int getTypeIndex(VariableType type) {
        return typesList.indexOf(type);
    }

    public static Map<String, Object> convertVariablesToMap(List<Variable> variables) {
        Map<String, Object> variableMap = new HashMap<String, Object>();
        for (Variable variable : variables) {
            variableMap.put(variable.getName(), variable.getValue());
        }
        return variableMap;
    }

    public static Map<String, Object> convertHistoryVariablesToMap(List<HistoryVariable> historyVariables) {
        Map<String, Object> variableMap = new HashMap<String, Object>();
        for (HistoryVariable historyVariable : historyVariables) {
            if(variableMap.containsKey(historyVariable.getName())
                    && StringHelper.isEmpty(historyVariable.getTaskId())) {
                    continue;
            }
            variableMap.put(historyVariable.getName(), historyVariable.getValue());
        }
        return variableMap;
    }

    public static List<HistoryVariable> convertToHistoryVariables(Map<String, Variable> variableMap) {
        List<HistoryVariable> historyVariables = new ArrayList<HistoryVariable>();
        if (variableMap != null) {
            for (Variable variable : variableMap.values()) {
                historyVariables.add(new HistoryVariable((variable)));
            }
        }
        return historyVariables;
    }

    public static Map<String, Variable> convertToVariableMap(List<HistoryVariable> historyVariables) {
        Map<String, Variable> variables = new HashMap<String, Variable>();
        if (historyVariables != null) {
            for (HistoryVariable historyVariable : historyVariables) {
                variables.put(historyVariable.getName(), new Variable((historyVariable)));
            }
        }
        return variables;
    }

    public static List<HistoryVariable> createHistoryVariables(Map<String, Object> args, String orderId, String taskId) {
        List<HistoryVariable> historyVariables = new ArrayList<HistoryVariable>();
        if (args != null) {
            for (Map.Entry<String, Object> entry : args.entrySet()) {
                VariableType type = VariableHelper.findVariableType(entry.getValue());
                if(type == null) continue;
                HistoryVariable variable = new HistoryVariable();
                variable.setId(StringHelper.getPrimaryKey());
                variable.setOrderId(orderId);
                variable.setTaskId(taskId);
                variable.setName(entry.getKey());
                variable.setVariableType(type);
                variable.setValue(entry.getValue());
                historyVariables.add(variable);
            }
        }
        return historyVariables;
    }
}
