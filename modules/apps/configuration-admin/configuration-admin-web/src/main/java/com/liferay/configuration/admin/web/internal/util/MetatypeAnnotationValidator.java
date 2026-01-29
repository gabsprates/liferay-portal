package com.liferay.configuration.admin.web.internal.util;

import aQute.bnd.annotation.metatype.Meta;

import jakarta.validation.ValidationException;

import java.lang.reflect.Method;

import java.util.Dictionary;

/**
 * @author Gabriel Prates
 */
public class MetatypeAnnotationValidator {
	public static void validateValuesWithMetatypeAnnotation(Method[] methods, Dictionary<String, Object> properties) {
		for (Method method : methods) {
			Meta.AD ad = method.getAnnotation(Meta.AD.class);

			if (ad == null) {
				continue;
			}

			String methodName = method.getName();
			Object value = properties.get(methodName);

			_validateRequiredValue(ad.required(), value, methodName);
			_validateMinValue(ad.min(), value, methodName);
			_validateMaxValue(ad.max(), value, methodName);
		}
	}

	private static boolean _isReferenceNull(String referenceValue) {
		return referenceValue == null || Meta.NULL.equals(referenceValue);
	}

	private static void _validateMaxValue(String maxReference, Object maxIncomingValue, String methodName) {
		if (_isReferenceNull(maxReference)) {
			return;
		}

		long maxRefereceValue = Long.parseLong(maxReference);
		long incomingValue = ((Number) maxIncomingValue).longValue();

		if (maxRefereceValue < incomingValue) {
			throw new ValidationException(
				"The maximum possible value for \"" + methodName + "\" is " + maxReference + ".");
		}
	}

	private static void _validateMinValue(String minReference, Object minIncomingValue, String methodName) {
		if (_isReferenceNull(minReference)) {
			return;
		}

		long minRefereceValue = Long.parseLong(minReference);
		long incomingValue = ((Number) minIncomingValue).longValue();

		if (minRefereceValue > incomingValue) {
			throw new ValidationException(
				"The minimum possible value for \"" + methodName + "\" is " + minReference + ".");
		}
	}

	private static void _validateRequiredValue(Boolean isRequired, Object value, String methodName) {
		if (isRequired && (value == null)) {
			throw new ValidationException(
				"The property \"" + methodName + "\" is required.");
		}
	}
}