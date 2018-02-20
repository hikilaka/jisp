package org.sysdevs.lisp.buffer;

import java.util.Optional;

/**
 * 
 * @author zack
 */
public interface InputBuffer {
	/**
	 * 
	 * @return
	 */
	default public Optional<Character> peekCharacter() {
		return peekCharacter(0);
	}

	/**
	 * 
	 * @param offset
	 * @return
	 */
	public Optional<Character> peekCharacter(int offset);

	/**
	 * 
	 * @param length
	 * @return
	 */
	default public Optional<String> peekString(int length) {
		return peekString(length, 0);
	}

	/**
	 * 
	 * @param length
	 * @param offset
	 * @return
	 */
	public Optional<String> peekString(int length, int offset);

	/**
	 * 
	 * @param length
	 * @return
	 */
	public Optional<String> take(int length);
	
	/**
	 * 
	 * @param length
	 * @return
	 */
	public Optional<String> take(int length, int offset);

	/**
	 * Advances this buffer's internal counter by <code>amount</code>
	 *
	 * @param amount
	 *            The amount the advance this buffer by, may be a positive or
	 *            negative value.
	 */
	public void advance(int amount);
}
