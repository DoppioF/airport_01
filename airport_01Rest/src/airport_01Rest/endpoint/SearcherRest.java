package airport_01Rest.endpoint;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import airport_01Ejb.interfaces.AirplaneEjbInterface;
import airport_01Ejb.interfaces.CommonControllerEjbInterface;
import airport_01Ejb.interfaces.SearcherEjbInterface;
import airport_01Model.dto.AirplaneDto;
import airport_01Model.dto.ResponseDto;
import airport_01Rest.config.EJBFactory;
import airport_01Rest.interfaces.IAirplaneRest;
import airport_01Rest.interfaces.ISearcherRest;
import airport_01Rest.utils.RestUtils;
import customUtils.constants.strings.GeneralConstants;
import customUtils.constants.strings.RestConstants;
import customUtils.exceptions.DBQueryException;
import customUtils.exceptions.UnforeseenException;
import customUtils.exceptions.ValidatorException;

@SuppressWarnings("finally")
@Path(RestConstants.Airport_01Path.SEARCH)
public class SearcherRest implements ISearcherRest {

	@Override
	public Response search(String input) {
		System.out.println("SearcherRest search, input:" + input);

		ResponseDto responseDto = new ResponseDto();
		Status responseStatus = Status.INTERNAL_SERVER_ERROR;
			try {
				new RestUtils().generateOkDto(responseDto, new EJBFactory<SearcherEjbInterface>(SearcherEjbInterface.class).getEJB().invokeSearcher(input));
				responseStatus = Status.OK;
				System.out.println("RETURN SearcherRest search, DTO:" + responseDto);
			} catch (ValidatorException e) {
				responseDto.setMessage(e.getMessage());
			} catch (DBQueryException e) {
				responseDto.setMessage(e.getMessage());
			} catch (UnforeseenException e) {
				responseDto.setMessage(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				responseDto.setMessage(GeneralConstants.GENERIC_ERROR);
			} finally {
				System.out.println("RETURN SearcherRest search, DTO:" + responseDto);
				return Response.status(responseStatus).entity(responseDto).build();
			}
	}
}
