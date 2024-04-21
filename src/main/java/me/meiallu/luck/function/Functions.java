package me.meiallu.luck.function;

public enum Functions {

    PRINTLINE("println", new PrintLine()),
    PRINT("print", new Print());

    Function function;
    String name;

    public static Functions get(String name) {
        for (Functions loopedFunction : Functions.values())
            if (loopedFunction.name.equals(name))
                return loopedFunction;

        return null;
    }

    public void call(Object... objects) {
        function.call(objects);
    }

    Functions(String name, Function function) {
        this.function = function;
        this.name = name;
    }
}
