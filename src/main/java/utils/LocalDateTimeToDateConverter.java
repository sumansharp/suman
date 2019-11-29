package utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;



 
 

/**
 * A converter that converts from {@link LocalDateTime} to {@link Date}
 * 
 * @author efficenz
 *
 */
public class LocalDateTimeToDateConverter implements Converter<Date, LocalDateTime> {

    
    @Override
    public Date convert(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    
}