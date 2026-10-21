package io.github.pryshio.ae2channels.part;

/**
 * Часть-кабель с увеличенным лимитом каналов.
 */
public interface ChannelCapacity {

    /** Максимум каналов, которые поддерживает этот кабель. */
    int getAmplifiedMaxChannels();

    /** Тир кабеля для рендера: 1 = 64, 2 = 128, 3 = 256 каналов. */
    int getAmplifiedTier();
}
