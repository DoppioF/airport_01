package validatorUtils;

import java.util.List;

import customUtils.exceptions.ValidatorException;

public class ErrorsLogManager {

	public void dtoValidationErrorMessagesHandler(final List<String> errors, final String className, final String methodName, final String errorType) throws ValidatorException {
		if (!errors.isEmpty()) {
			System.out.println("RETURN " + className + " " + methodName + " " + errorType);
			String errorString = String.join(" ### ", errors);
			System.out.println(errorString);
			throw new ValidatorException(errorString);
		}
	}
	
	public String[] errorMessageBreakUp(final String errorMessage) {
		String[] splittedErrorMessage = errorMessage.split(" -> | ### ");
		String[] errorDetails = new String[splittedErrorMessage.length / 3];
		for (int index = 0; index < errorDetails.length; index++) {
			errorDetails[index] = splittedErrorMessage[(index * 3) + 2];
		}
		return errorDetails;
	}
}
