package com.mitchtalmadge.uofu_cs_bot.event.listeners;

import com.mitchtalmadge.uofu_cs_bot.service.discord.channel.ChannelSynchronizationService;
import net.dv8tion.jda.core.events.channel.voice.GenericVoiceChannelEvent;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Receives all voice-channel-based events.
 */
public class VoiceChannelEventListener extends EventListenerAbstract<GenericVoiceChannelEvent> {

    private final ChannelSynchronizationService channelSynchronizationService;

    @Autowired
    public VoiceChannelEventListener(ChannelSynchronizationService channelSynchronizationService) {
        this.channelSynchronizationService = channelSynchronizationService;
    }

    @Override
    public void onEvent(GenericVoiceChannelEvent event) {
        channelSynchronizationService.requestSynchronization();
    }

}
