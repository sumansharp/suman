package utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateToLocalDateTimeConverter implements Converter<LocalDateTime, Date> {
	 @Override
	    public LocalDateTime convert(Date date) {
	        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
	    }
}
