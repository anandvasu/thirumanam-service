package com.thirumanam.scheduler;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class ProfileStatusUpdateScheduler {
	
	//@Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
    }
}