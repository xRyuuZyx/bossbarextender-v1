package id.roleplay.bossbarextender.mixin;

import id.roleplay.bossbarextender.BossBarExtenderConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.UUID;

/**
 * Keeps vanilla BossBar drawing methods/textures, but removes vanilla's early
 * one-third-screen cutoff. The user-configured limit and screen-height guard
 * prevent bars from being rendered indefinitely below the visible screen.
 */
@Mixin(BossBarHud.class)
public abstract class BossBarHudMixin {
    @Shadow @Final private MinecraftClient client;
    @Shadow @Final private Map<UUID, ?> bossBars;

    @Shadow
    private void renderBossBar(DrawContext context, int x, int y, BossBar bossBar) {
        throw new AssertionError("Shadow method was not transformed");
    }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void bossbarExtender$renderExpandedList(DrawContext context, CallbackInfo ci) {
        if (this.bossBars.isEmpty()) {
            return;
        }

        ci.cancel();

        BossBarExtenderConfig.Settings config = BossBarExtenderConfig.get();
        int screenWidth = context.getScaledWindowWidth();
        int screenHeight = context.getScaledWindowHeight();
        int top = config.topOffset();
        int spacing = config.barSpacing();
        int bottomLimit = Math.max(top + spacing,
                screenHeight * config.maxScreenUsagePercent() / 100);
        int heightBasedLimit = Math.max(1, (bottomLimit - top) / spacing + 1);
        int renderLimit = Math.min(config.maxBossBars(), heightBasedLimit);

        int y = top;
        int rendered = 0;
        for (Object value : this.bossBars.values()) {
            if (rendered >= renderLimit) {
                break;
            }

            BossBar bossBar = (BossBar) value;
            this.renderBossBar(context, screenWidth / 2 - 91, y, bossBar);

            Text name = bossBar.getName();
            int textWidth = this.client.textRenderer.getWidth(name);
            context.drawTextWithShadow(this.client.textRenderer, name,
                    screenWidth / 2 - textWidth / 2, y - 9, 0xFFFFFF);

            y += spacing;
            rendered++;
        }
    }
}
