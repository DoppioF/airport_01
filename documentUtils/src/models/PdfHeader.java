package models;

public class PdfHeader {
	
	private String title;
	private Object titleFont;
	private float titleFontSize;
	private byte[] logo;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public byte[] getLogo() {
		return logo;
	}
	public void setLogo(byte[] logo) {
		this.logo = logo;
	}
	public Object getTitleFont() {
		return titleFont;
	}
	public void setTitleFont(Object titleFont) {
		this.titleFont = titleFont;
	}
	public float getTitleFontSize() {
		return titleFontSize;
	}
	public void setTitleFontSize(float titleFontSize) {
		this.titleFontSize = titleFontSize;
	}
	
	
}
