package whirlfrenzy.itemdespawntimer.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import whirlfrenzy.itemdespawntimer.ItemDespawnTimer;
import whirlfrenzy.itemdespawntimer.networking.ClientNetworking;
import whirlfrenzy.itemdespawntimer.networking.SetItemAgePacket;
import whirlfrenzy.itemdespawntimer.networking.SetItemLifespanPacket;

@Mod(ItemDespawnTimer.NEOFORGE_MOD_ID)
public final class ItemDespawnTimerNeoForge {
    public ItemDespawnTimerNeoForge(IEventBus eventBus) {
        eventBus.register(this);
    }

    @SubscribeEvent
    public void registerPayloadHandlers(final RegisterPayloadHandlersEvent event){
        final PayloadRegistrar registrar = event.registrar("1").optional();

        registrar.playToClient(SetItemAgePacket.PACKET_ID, SetItemAgePacket.PACKET_CODEC, (packet, context) -> {
            context.enqueueWork(() -> {
                ClientNetworking.addItemDataPacket(packet);
            });
        });

        registrar.playToClient(SetItemLifespanPacket.PACKET_ID, SetItemLifespanPacket.PACKET_CODEC, (packet, context) -> {
            context.enqueueWork(() -> {
                ClientNetworking.addItemDataPacket(packet);
            });
        });
    }
}
