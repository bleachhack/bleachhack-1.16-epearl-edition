package bleach.hack.util.world;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;

import java.util.UUID;

public class FakePlayerUtils extends OtherClientPlayerEntity
{
    public FakePlayerUtils()
    {
        this(new GameProfile(UUID.fromString("0155842b-0328-4e6a-94ee-4b3b9cd85c01"), "NotVp"));
    }

    public FakePlayerUtils(GameProfile profile)
    {
        this(profile, MinecraftClient.getInstance().player.getX(), MinecraftClient.getInstance().player.getY(), MinecraftClient.getInstance().player.getZ());
    }

    public FakePlayerUtils(GameProfile profile, double x, double y, double z)
    {
        super(MinecraftClient.getInstance().world, profile);
        setPos(x, y, z);
    }

    public void spawn()
    {
        MinecraftClient.getInstance().world.addEntity(this.getId(), this);
    }

    public void despawn()
    {
        MinecraftClient.getInstance().world.removeEntity(this.getId(), Entity.RemovalReason.DISCARDED);
    }
}