package org.sysdevs.jisp;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.sysdevs.jisp.buffer.FileBuffer;
import org.sysdevs.jisp.buffer.InputBuffer;
import org.sysdevs.jisp.env.Environment;
import org.sysdevs.jisp.env.StandardEnvironment;
import org.sysdevs.jisp.expr.Expression;
import org.sysdevs.jisp.parser.Parser;
import org.sysdevs.jisp.token.Tokenizer;

public final class Jisp {

	public static void main(String[] args) throws Exception {
		Path file = Paths.get("test.lisp");
		InputBuffer buffer = new FileBuffer(file);
		Tokenizer tokenizer = new Tokenizer(buffer);
		Parser parser = new Parser(tokenizer.stream());
		Environment env = new StandardEnvironment();

		// hacky, TODO: make this not so.. hacky
		parser.parse().forEach(expr -> {
			try {
				while (expr.getArguments().size() > 0) {
					expr.evaluate(env);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		Optional<Expression> main = env.get("main");

		if (!main.isPresent()) {
			System.out.println("Warning: no main function found");
		} else {
			main.get().evaluate(env);
		}
	}

}
