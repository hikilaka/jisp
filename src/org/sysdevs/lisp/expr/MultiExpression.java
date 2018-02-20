
package org.sysdevs.lisp.expr;

import java.util.List;

public class MultiExpression extends Expression {
    public MultiExpression(List<Expression> expressions) {
        super(expressions);
    }
    
    @Override
    public String toString() {
        return "MultiExpression{...}";
    }
}
