package org.sysdevs.lisp;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.sysdevs.lisp.buffer.FileBuffer;
import org.sysdevs.lisp.buffer.InputBuffer;
import org.sysdevs.lisp.env.Environment;
import org.sysdevs.lisp.env.GlobalEnvironment;
import org.sysdevs.lisp.expr.Expression;
import org.sysdevs.lisp.expr.ExpressionParseException;
import org.sysdevs.lisp.token.TokenParser;
import org.sysdevs.lisp.token.Tokenizer;

public final class Lisp {
	
	public static void main(String[] args) throws Exception {
		Path file = Paths.get("test.lisp");
		InputBuffer buffer = new FileBuffer(file);
		Tokenizer tokenizer = new Tokenizer(buffer);
		TokenParser parser = new TokenParser(tokenizer);
		
		try {
            List<Expression> expressions = parser.parse();
            Environment env = GlobalEnvironment.build();
            
            expressions.stream()
                .map(expr -> expr.evaluate(env))
                .count();
		} catch (ExpressionParseException e) {
			System.err.println("Error parsing source: " + e.getMessage());
			System.err.println("\ton row " + e.expression().token().row()
					+ ", column " + e.expression().token().column());
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
		}
	}
}
