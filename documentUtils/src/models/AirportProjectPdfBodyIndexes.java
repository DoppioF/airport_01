package models;

public enum AirportProjectPdfBodyIndexes {
	INDEX_RESERVATION_SUMMARY_TITLE 	(0)
	, INDEX_RESERVATION_SUMMARY_KEYS 	(1)
	, INDEX_RESERVATION_SUMMARY_VALUES 	(2)
	, INDEX_TICKET_LIST_TITLE 			(3)
	, INDEX_TICKET_LIST_TABLE_HEADERS 	(4)
	, INDEX_TICKET_LIST_TABLE_VALUES 	(5);
	
	int index;
	
	AirportProjectPdfBodyIndexes(int index) {
		this.index = index;
	}
	
	public int getValue() {
		return index;
	}
}
