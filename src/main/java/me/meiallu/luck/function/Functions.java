package me.meiallu.luck.function;

public enum Functions {

    PRINTLINE("println", new PrintLine()),
    PRINT("print", new Print()),
    FORMAT("format", new Format());

    private final Function function;
    private final String name;

    public static Functions get(String name) {
        for (Functions loopedFunction : Functions.values())
            if (loopedFunction.name.equals(name))
                return loopedFunction;

        return null;
    }

    public Object call(Object... objects) {
        return function.call(objects);
    }

    Functions(String name, Function function) {
        this.function = function;
        this.name = name;
    }
}
