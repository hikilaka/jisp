package org.sysdevs.jisp.env;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.sysdevs.jisp.expr.BindingExpression;
import org.sysdevs.jisp.expr.Expression;
import org.sysdevs.jisp.expr.JavaFunction;
import org.sysdevs.jisp.expr.Lambda;
import org.sysdevs.jisp.expr.SExpression;
import org.sysdevs.jisp.parser.ConstantLiteral;
import org.sysdevs.jisp.parser.Symbol;
import org.sysdevs.lisp.test.TestCase;

public final class StandardEnvironment extends Environment {
	public static final Symbol NIL = new Symbol("nil");
	public static final Symbol TRUE = new Symbol("true");
	public static final Symbol FALSE = new Symbol("false");

	private final List<TestCase> testCases = new LinkedList<>();
	
	public StandardEnvironment() {
		// TODO: implemenet these as constants
		defineStandardVars();
		defineLanguageFunctions();
		defineStandardFunctions();
		defineTestFunctions();
	}

	private void defineStandardVars() {
		define("nil", NIL);
		define("true", TRUE);
		define("false", FALSE);
	}

	private void defineStandardFunctions() {
		definePrintFunctions();
		defineInputFunctions();
		defineLogicalFunctions();
		defineOperatorFunctions();
		defineRandomFunctions();
	}

	private void defineTestFunctions() {
		define("test-case", new JavaFunction((env, args) -> {
			if (args.size() != 3) {
				System.err.println("error: test-case expects exactly 3 arguments");
				System.err.println("\tusage: test-case <description:string> <expected_value:any> <test_case:function>");
				return NIL;
			}
			
			try {
				Expression descExp = args.get(0).evaluate(env);
				Expression expectedValue = args.get(1).evaluate(env);
				Expression funcExpr = args.get(2).evaluate(env);
				
				String description = ConstantLiteral.class.cast(descExp).asString();
				Lambda function = Lambda.class.cast(funcExpr);
				
				testCases.add(new TestCase(description, expectedValue, function));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return NIL;
		}));
		define("run-tests", new JavaFunction((env, args) -> {
			List<TestCase> failed = new LinkedList<>();
			
			testCases.forEach(testCase -> {
				try {
					System.out.printf("Running test: \"%s\"... ", testCase.description());
					Expression result = testCase.function().evaluate(env);
					
					if (result.equals(testCase.expectedValue())) {
						System.out.println("PASSED");
					} else {
						System.out.println("FAILED");
						System.out.printf("\tExpected %s but got %s%n", testCase.expectedValue(), result);
						failed.add(testCase);
					}
				} catch (Exception e) {
					e.printStackTrace();
					failed.add(testCase);
				}
			});
			
			System.out.println();
			if (!failed.isEmpty()) {
				System.out.printf("%d tests failed. ", failed.size());
			}
			System.out.printf("%d tests passed.%n", testCases.size() - failed.size());
			
			return failed.size() == 0 ? TRUE : FALSE;
		}));
	}

	private void defineLanguageFunctions() {
		define("define", new JavaFunction((env, args) -> {
			try {
				Expression identifier = args.get(0).evaluate(env);

				if (!(identifier instanceof Symbol)) {
					return NIL;
				}

				Symbol sym = Symbol.class.cast(identifier);
				Expression value = args.get(1).evaluate(env);

				env.define(sym, value);
				return sym;
			} catch (Exception e) {
				return NIL;
			}
		}));
		define("lambda", new JavaFunction((env, args) -> {
			Expression parameters = args.remove(0); // don't evaluate just yet
			SExpression body = new SExpression(args);

			return new Lambda(parameters, body);
		}));
		define("let", new JavaFunction((env, args) -> {
			Expression bindings = args.remove(0); // don't evaluate now
			Expression body = new SExpression(args);

			return new BindingExpression(bindings, body);
		}));
		define("nil?", new JavaFunction((env, args) -> {
			boolean containsNil = args.stream()
				.map(e -> {
					try {
						return e.evaluate(env);
					} catch (Exception ex) {
						ex.printStackTrace();
						return NIL;
					}
				})
				.anyMatch(e -> e.equals(NIL));
			
			return containsNil ? TRUE : FALSE;
		}));
		define("loop-to", new JavaFunction((env, args) -> {
			try {
				Expression nExp = args.get(0).evaluate(env);
				Expression funcExpr = args.get(1).evaluate(env);
				
				Integer n = ConstantLiteral.class.cast(nExp).asNumber();
				Lambda function = Lambda.class.cast(funcExpr);
				Expression result = NIL;
				
				for (int i = 0; i < n; i++) {
					result = function.evaluate(env, Arrays.asList(new ConstantLiteral(i)));
				}

				return result;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return NIL;
		}));
	}

	private void definePrintFunctions() {
		define("print-line", new JavaFunction((env, args) -> {
			System.out.println(args.stream().map(arg -> {
				try {
					return arg.evaluate(env);
				} catch (Exception e) {
					e.printStackTrace();
					return NIL;
				}
			}).map(Object::toString).collect(Collectors.joining()));
			return NIL;
		}));
		define("print", new JavaFunction((env, args) -> {
			System.out.print(args.stream().map(arg -> {
				try {
					return arg.evaluate(env);
				} catch (Exception e) {
					e.printStackTrace();
					return NIL;
				}
			}).map(Object::toString).collect(Collectors.joining()));
			return NIL;
		}));
		define("print-env", new JavaFunction((env, args) -> {
			System.out.println(env);
			return NIL;
		}));
	}

	private void defineInputFunctions() {
		// TODO: optional input stream parameter? should default to stdin
		define("read-string", new JavaFunction((env, args) -> {
			@SuppressWarnings("resource")
			Scanner scanner = new Scanner(System.in);
			return new ConstantLiteral(scanner.nextLine());
		}));
		define("read-integer", new JavaFunction((env, args) -> {
			@SuppressWarnings("resource")
			Scanner scanner = new Scanner(System.in);
			return new ConstantLiteral(scanner.nextInt());
		}));
		define("parse-integer", new JavaFunction((env, args) -> {
			try {
				Expression exp = args.get(0).evaluate(env);
				ConstantLiteral literal = ConstantLiteral.class.cast(exp);
				
				try {
					return new ConstantLiteral(Integer.parseInt(literal.asString()));
				} catch (NumberFormatException nfe) {
					return NIL;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return NIL;
		}));
	}

	private void defineLogicalFunctions() {
		define("if", new JavaFunction((env, args) -> {
			try {
				Expression predicate = args.get(0).evaluate(env);

				if (predicate.equals(TRUE)) {
					return args.get(1).evaluate(env);
				} else if (args.size() > 1) {
					return args.get(2).evaluate(env);
				} else {
					return NIL;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return NIL;
			}
		}));
		define("=", new JavaFunction((env, args) -> {
			try {
				// TODO: implement this better
				for (int i = 1; i < args.size(); i++) {
					Expression prev = args.get(i - 1).evaluate(env);
					Expression curr = args.get(i).evaluate(env);

					if (!curr.equals(prev)) {
						return FALSE;
					}
				}
				return TRUE;
			} catch (Exception e) {
				e.printStackTrace();
				return NIL;
			}
		}));
		define("and", new JavaFunction((env, args) -> {
			boolean noneFalse = args.stream()
					.map(e -> {
						try {
							return e.evaluate(env);
						} catch (Exception e1) {
							e1.printStackTrace();
							return NIL;
						}
					})
					.noneMatch(e -> e.equals(FALSE) || e.equals(NIL));

			return noneFalse ? TRUE : FALSE;
		}));
		define(">", new JavaFunction((env, args) -> {
			// only works on integral values
			try {
				Expression firstExp = args.get(0).evaluate(env);
				Expression secondExp = args.get(1).evaluate(env);

				ConstantLiteral first = ConstantLiteral.class.cast(firstExp);
				ConstantLiteral second = ConstantLiteral.class.cast(secondExp);
				
				return (first.asNumber() > second.asNumber()) ? TRUE : FALSE;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return FALSE;
		}));
		define("<", new JavaFunction((env, args) -> {
			// only works on integral values
			try {
				Expression firstExp = args.get(0).evaluate(env);
				Expression secondExp = args.get(1).evaluate(env);

				ConstantLiteral first = ConstantLiteral.class.cast(firstExp);
				ConstantLiteral second = ConstantLiteral.class.cast(secondExp);
				
				return (first.asNumber() < second.asNumber()) ? TRUE : FALSE;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return FALSE;
		}));
		define(">=", new JavaFunction((env, args) -> {
			// only works on integral values
			try {
				Expression firstExp = args.get(0).evaluate(env);
				Expression secondExp = args.get(1).evaluate(env);

				ConstantLiteral first = ConstantLiteral.class.cast(firstExp);
				ConstantLiteral second = ConstantLiteral.class.cast(secondExp);
				
				return (first.asNumber() >= second.asNumber()) ? TRUE : FALSE;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return FALSE;
		}));
		define("<=", new JavaFunction((env, args) -> {
			// only works on integral values
			try {
				Expression firstExp = args.get(0).evaluate(env);
				Expression secondExp = args.get(1).evaluate(env);

				ConstantLiteral first = ConstantLiteral.class.cast(firstExp);
				ConstantLiteral second = ConstantLiteral.class.cast(secondExp);
				
				return (first.asNumber() <= second.asNumber()) ? TRUE : FALSE;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return FALSE;
		}));
	}

	private void defineOperatorFunctions() {
		define("+", new JavaFunction((env, args) -> {
			if (args.isEmpty()) {
				return NIL;
			}

			try {
				Expression head = args.get(0).evaluate(env);

				if (head instanceof Symbol) {
					// + for symbols
					Symbol sym = Symbol.class.cast(head);
					
					for (int i = 1; i < args.size(); i++) {
						Expression e = args.get(i).evaluate(env);
						sym = new Symbol(sym.toString() + e.toString());
					}
					
					return sym;
				} else if (head instanceof ConstantLiteral) {
					// + for string & integers
					ConstantLiteral c = ConstantLiteral.class.cast(head);

					if (c.isNumber()) {
						Integer result = c.asNumber();

						for (int i = 1; i < args.size(); i++) {
							Expression e = args.get(i).evaluate(env);
							
							if (e instanceof ConstantLiteral) {
								ConstantLiteral literal = ConstantLiteral.class.cast(e);
								result += literal.asNumber();
							}
						}
						return new ConstantLiteral(result);

					} else if (c.isString()) {
						String result = c.asString();
						
						for (int i = 1; i < args.size(); i++) {
							Expression e = args.get(i).evaluate(env);
							result += e.toString();
						}
						
						return new ConstantLiteral(result);
					}
				} else {
					// + for everything else (nothing)
					return NIL;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return NIL;
		}));
		define("-", new JavaFunction((env, args) -> {
			if (args.isEmpty()) {
				return NIL;
			}

			try {
				Expression head = args.get(0).evaluate(env);

				if (head instanceof Symbol) {
					return NIL;
				} else if (head instanceof ConstantLiteral) {
					// + for string & integers
					ConstantLiteral c = ConstantLiteral.class.cast(head);

					if (c.isNumber()) {
						Integer result = c.asNumber();

						for (int i = 1; i < args.size(); i++) {
							Expression e = args.get(i).evaluate(env);
							
							if (e instanceof ConstantLiteral) {
								ConstantLiteral literal = ConstantLiteral.class.cast(e);
								result += literal.asNumber();
							}
						}
						return new ConstantLiteral(result);

					} else if (c.isString()) {
						return NIL;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return NIL;
		}));
		define("*", new JavaFunction((env, args) -> {
			if (args.isEmpty()) {
				return NIL;
			}

			try {
				Expression exp = args.get(0).evaluate(env);

				if (exp instanceof ConstantLiteral) {
					ConstantLiteral c = ConstantLiteral.class.cast(exp);
					
					if (c.isNumber()) {
						Integer result = c.asNumber();
						
						for (int i = 1; i < args.size(); i++) {
							Expression e = args.get(i).evaluate(env);
							
							if (e instanceof ConstantLiteral) {
								ConstantLiteral literal = ConstantLiteral.class.cast(e);
								result *= literal.asNumber();
							}
						}

						return new ConstantLiteral(result);
					} else if (c.isString()) {
						String result = c.asString();
						StringBuilder sb = new StringBuilder();
						
						for (int i = 1; i < args.size(); i++) {
							Expression e = args.get(i).evaluate(env);
							
							if (e instanceof ConstantLiteral) {
								ConstantLiteral literal = ConstantLiteral.class.cast(e);
								for (int j = 0; j < literal.asNumber(); j++) {
									sb.append(result);
								}
							}
						}
						
						return new ConstantLiteral(sb.toString());
					} else {
						// ?? shouldn't get here
						return NIL;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return NIL;
		}));
		define("/", new JavaFunction((env, args) -> {
			System.out.println("TODO: add / function :|");
			return NIL;
		}));
	}
	
	private void defineRandomFunctions() {
		define("random-integer", new JavaFunction((env, args) -> {
			ThreadLocalRandom random = ThreadLocalRandom.current();
			
			try {
				switch (args.size()) {
				case 0: // random value from -2^-31 to 2^31-1
					return new ConstantLiteral(random.nextInt());
				case 1: // random value from 0 to A
					Expression exp = args.get(0).evaluate(env);
					ConstantLiteral literal = ConstantLiteral.class.cast(exp);
					
					return new ConstantLiteral(random.nextInt(literal.asNumber()));
				default: // 2 or more args, random value from A to B
					Expression lowerExp = args.get(0).evaluate(env);
					Expression upperExp = args.get(1).evaluate(env);

					ConstantLiteral lower = ConstantLiteral.class.cast(lowerExp);
					ConstantLiteral upper = ConstantLiteral.class.cast(upperExp);
					
					return new ConstantLiteral(random.nextInt(lower.asNumber(), upper.asNumber()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return NIL;
		}));
	}
}
