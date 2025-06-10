package org.tourlink.attractionservice.service.event;

import org.tourlink.common.dto.dataPlatformDTO.UserBehaviorMessage;

public interface BehaviorEventSender {

    void send(UserBehaviorMessage message);
}
