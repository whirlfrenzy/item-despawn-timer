package whirlfrenzy.itemdespawntimer.networking;

import net.minecraft.network.packet.CustomPayload;

public interface ItemDataPacket extends CustomPayload {
    boolean attemptSet();

    ItemDataPacket createNextAttempt();

    int getAttempts();

    int getEntityId();
}
