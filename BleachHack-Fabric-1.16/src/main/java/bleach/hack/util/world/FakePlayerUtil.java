///TODO FakePlayer
//package bleach.hack.util.world;
//
//import com.mojang.authlib.GameProfile;
//import net.minecraft.client.MinecraftClient;
//import net.minecraft.client.network.OtherClientPlayerEntity;

//public class FakePlayerUtil extends OtherClientPlayerEntity
//    {
//    public FakePlayerUtil()
//        {
//            this(MinecraftClient.getInstance().player.getGameProfile());
//        }

//    public FakePlayerUtil(GameProfile profile)
//        {
//            this(profile, MinecraftClient.getInstance().player.getX(), MinecraftClient.getInstance().player.getY(), MinecraftClient.getInstance().player.getZ());
//        }
//
//    public FakePlayerUtil(GameProfile profile, double x, double y, double z)
//        {
//            super(MinecraftClient.getInstance().world, profile);
//            setPos(x, y, z);
//        }

//        public void spawn ()
//        {
//            MinecraftClient.getInstance().world.addEntity(this.getEntityId(), this);
//        }

//        public void despawn ()
//        {
//            MinecraftClient.getInstance().world.removeEntity(this.getEntityId());
//        }
//    }
