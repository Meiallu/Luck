package me.meiallu.luck;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Node {

    TokenType tokenType;
    Object object;
    List<Node> childNodes;

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        print(buffer, "", "");
        return buffer.toString();
    }

    private void print(StringBuilder buffer, String prefix, String childrenPrefix) {
        buffer.append(prefix);
        buffer.append(tokenType.name() + (object == null ? "" : "(" + object + ")"));
        buffer.append('\n');

        for (Iterator<Node> it = childNodes.iterator(); it.hasNext(); ) {
            Node next = it.next();

            if (it.hasNext())
                next.print(buffer, childrenPrefix + "├── ", childrenPrefix + "│   ");
            else
                next.print(buffer, childrenPrefix + "└── ", childrenPrefix + "    ");
        }
    }

    public Node(TokenType tokenType, Object object) {
        this.tokenType = tokenType;
        this.object = object;
        this.childNodes = new ArrayList<>();
    }
}
