package customUtils.classes;

import java.util.List;
import java.util.stream.Stream;

public class CustomStreamUtils {

	public static <T> Stream<T> nullpointerSafeStream(List<T> list) {
		return (null == list ? Stream.empty() : list.stream());
	}
}
