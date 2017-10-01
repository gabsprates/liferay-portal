/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.vulcan.function;

import java.util.Objects;
import java.util.function.Function;

/**
 * Function {@link java.util.function.Function} with eleven input parameters.
 * As all the function interfaces, it receives several arguments and returns one value
 * (of type R)
 *
 * Being a functional interface, it can be implemented with a lambda function
 *
 * @author Alejandro Hernández
 * @see    java.util.function.Function
 * @review
 */
@FunctionalInterface
public interface UndecaFunction<A, B, C, D, E, F, G, H, I, J, K, R> {

	/**
	 * Method that creates a lambda function (also a {@code UndecaFunction}) that
	 * executes the {@code apply} method of this instance and uses the result as the input for the {@code apply} method of the {@code afterFunction} input parameter when invoked.
	 *
	 * @param  afterFunction the {@code UndecaFunction} to execute after this instance
	 * @return another {@code UndecaFunction} that executes both inputs
	 * (this own instance plus the input parameter) in order using the return value of the first one as the input for the second
	 * @review
	 */
	public default <V> UndecaFunction<A, B, C, D, E, F, G, H, I, J, K, V>
		andThen(Function<? super R, ? extends V> afterFunction) {

		Objects.requireNonNull(afterFunction);

		return (A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k) ->
			afterFunction.apply(apply(a, b, c, d, e, f, g, h, i, j, k));
	}

	/**
	 * The function to implement (explicitly or with a lambda), that operates
	 * with eleven parameters and returns void
	 *
	 * @param  a the first function argument
	 * @param  b the second function argument
	 * @param  c the third function argument
	 * @param  d the fourth function argument
	 * @param  e the fifth function argument
	 * @param  f the sixth function argument
	 * @param  g the seventh function argument
	 * @param  h the eighth function argument
	 * @param  i the ninth function argument
	 * @param  j the tenth function argument
	 * @param  k the eleventh function argument
	 * @return the function result
	 * @review
	 */
	public R apply(A a, B b, C c, D d, E e, F f, G g, H h, I i, J j, K k);

}