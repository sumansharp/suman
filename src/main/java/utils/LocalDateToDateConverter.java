package utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

 

public class LocalDateToDateConverter implements Converter< Date,LocalDate> {

	@Override
	public Date convert(LocalDate u) {
		// TODO Auto-generated method stub
		ZoneId z = ZoneId.of( "Asia/Kolkata" );
		ZonedDateTime zdt = u.atStartOfDay( z );

	
		Instant instant = zdt.toInstant();
	return java.util.Date.from( instant ); 
	}

	 
}
