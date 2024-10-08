package whirlfrenzy.itemdespawntimer.networking;


import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public abstract class ItemDataPacket {
    abstract boolean attemptSet();

    abstract ItemDataPacket createNextAttempt();

    abstract int getAttempts();

    abstract int getEntityId();

    public abstract Identifier getId();

    public abstract PacketByteBuf writeToBuffer(PacketByteBuf buffer);
}
