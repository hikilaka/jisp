package org.sysdevs.jisp.expr;

import java.util.List;
import java.util.function.BiFunction;
import org.sysdevs.jisp.env.Environment;

public interface JavaFunctionBody extends BiFunction<Environment, List<Expression>, Expression> {

}
