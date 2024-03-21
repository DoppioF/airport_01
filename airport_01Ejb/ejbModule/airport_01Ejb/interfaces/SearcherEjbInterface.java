package airport_01Ejb.interfaces;

import java.util.List;

import javax.ejb.Local;

import customUtils.exceptions.SearcherException;
import customUtils.exceptions.UnforeseenException;
import customUtils.exceptions.ValidatorException;
import searcher.dto.QueryResultDto;

@Local
public interface SearcherEjbInterface {

	List<QueryResultDto> invokeSearcher(String query) throws ValidatorException, ValidatorException, SearcherException, UnforeseenException;
}
