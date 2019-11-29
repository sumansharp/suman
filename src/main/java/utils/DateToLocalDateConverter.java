package utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;


public class DateToLocalDateConverter implements Converter<LocalDate,Date> {

	@Override
	public LocalDate convert(Date u) {
		// TODO Auto-generated method stub
		Instant instant = u.toInstant();
		ZoneId z = ZoneId.of( "Asia/Kolkata" );
		ZonedDateTime zdt = instant.atZone( z );
		LocalDate ld = zdt.toLocalDate();
		return ld;
	}

}
