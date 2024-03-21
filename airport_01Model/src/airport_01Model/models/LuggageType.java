package airport_01Model.models;

public enum LuggageType {

	L ("56x45x25cm", 21.F, 30.F), 
	XL ("90x75x47cm", 32.F, 45.F);

	private final String maxDimension;
	private final Float maxWeight;
	private final Float price;
	
	LuggageType(String maxDimension, Float maxWeight, Float price) {
		this.maxDimension = maxDimension;
		this.maxWeight = maxWeight;
		this.price = price;
	}

	public String getMaxDimension() {
		return maxDimension;
	}

	public Float getMaxWeight() {
		return maxWeight;
	}

	public Float getPrice() {
		return price;
	}
	
}
