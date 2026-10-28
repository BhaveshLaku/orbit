package com.bhavesh.orbit.connectionService.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConnectionRequestedEvent {

    private Long senderId;
    private Long receiverId;
}
