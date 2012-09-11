package org.ced.domain.conference.model;

import java.util.Date;

import javax.validation.constraints.NotNull;

public class Duration {

    @NotNull
    private Date start;
    
    @NotNull
    private Date end;
    
    // hidden constructor for Persistence 
    Duration() {
    }
    
    public Duration(Date start, Date end) {
	if(start == null) {
	    throw new IllegalArgumentException("Start must be provided");
	}
	if(end == null) {
	    throw new IllegalArgumentException("End must be provided");
	}
	if(end.before(start)) {
	    throw new IllegalArgumentException("End can not be before Start");
	}
	this.start = start;
	this.end = end;
	
    }
    
    public Date getEnd() {
	return (Date)end.clone();
    }
    
    public Date getStart() {
	return (Date)start.clone();
    }
    
    public Integer getNumberOfDays() {
	return -1;
    }
    
    public Integer getNumberOfHours() {
	return -1;
    }
}
