package utils;

public interface Converter <T, U> {

	/**
	 * Convert type U to type T
	 * 
	 * @param u type U to convert from
	 * @return converted type T
	 */
	T convert(U u);
	
}