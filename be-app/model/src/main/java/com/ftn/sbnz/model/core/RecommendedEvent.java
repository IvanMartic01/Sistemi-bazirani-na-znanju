package com.ftn.sbnz.model.core;

import com.ftn.sbnz.model.event.EventEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendedEvent {
    private EventEntity event;
    private boolean recommended;
}
