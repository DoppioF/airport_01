package airport_01Model.models.utilityModels;

import java.io.Serializable;
import java.util.List;

import airport_01Model.dto.LuggageDto;

public class PassengerDto implements Serializable {

	private static final long serialVersionUID = 921225518473345393L;

	private String name;
	private String surname;
	private LuggageDto luggageToEmbark;
	
	
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public LuggageDto getLuggageToEmbark() {
		return luggageToEmbark;
	}
	public void setLuggageToEmbark(LuggageDto luggageToEmbark) {
		this.luggageToEmbark = luggageToEmbark;
	}
	@Override
	public String toString() {
		return "PassengerDto [name=" + name + ", surname=" + surname + ", luggageToEmbark=" + luggageToEmbark + "]";
	}

	
	
	
}
