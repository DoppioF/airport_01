package airport_01Rest.utils;

import airport_01Model.dto.ResponseDto;
import customUtils.constants.strings.GeneralConstants;

public class RestUtils {

	public ResponseDto generateOkDto(ResponseDto responseDto, Object object) {
		responseDto.setData(object);
		responseDto.setMessage(GeneralConstants.OK);
		responseDto.setState(true);
		return responseDto;
	}
}
