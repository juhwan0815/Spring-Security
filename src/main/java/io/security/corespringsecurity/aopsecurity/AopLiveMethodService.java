package io.security.corespringsecurity.aopsecurity;

import org.springframework.stereotype.Service;

@Service
public class AopLiveMethodService {

    public void liveMethodService(){
        System.out.println("AopLiveMethodService.liveMethodService");

    }

}
