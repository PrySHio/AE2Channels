package io.github.pryshio.ae2channels.compat.jade;

import io.github.pryshio.ae2channels.block.WirelessTransmitterBlock;
import io.github.pryshio.ae2channels.block.WirelessTransmitterBlockEntity;

import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class AE2ChannelsJadePlugin implements IWailaPlugin {

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(
                WirelessTransmitterProvider.INSTANCE,
                WirelessTransmitterBlockEntity.class
        );
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(
                WirelessTransmitterProvider.INSTANCE,
                WirelessTransmitterBlock.class
        );
    }
}