package util;

import java.util.HashMap;
import java.util.Map;

public class MapValueEnum {
	public enum WeekDay {
		MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
	}

	public enum Session {
		CA1, CA2, CA3, CA4, CA5
	};

	public static Map<Integer, WeekDay> generateMapWeekDay() {
		Map<Integer, WeekDay> result = new HashMap<Integer, WeekDay>();
		result.put(2, WeekDay.MONDAY);
		result.put(3, WeekDay.TUESDAY);
		result.put(4, WeekDay.WEDNESDAY);
		result.put(5, WeekDay.THURSDAY);
		result.put(6, WeekDay.FRIDAY);
		result.put(7, WeekDay.SATURDAY);
		result.put(7, WeekDay.SUNDAY);
		return result;
	}

	public static Map<Integer, Session> generateMaSession() {
		Map<Integer, Session> result = new HashMap<Integer, Session>();
		result.put(1, Session.CA1);
		result.put(2, Session.CA2);
		result.put(3, Session.CA3);
		result.put(4, Session.CA4);
		result.put(5, Session.CA5);
		return result;
	}
	
	

}
