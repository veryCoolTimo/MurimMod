package com.timo.murimmod.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.timo.murimmod.MurimMod;
import com.timo.murimmod.cultivation.PlayerCultivation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CultivationScreen extends Screen {
    private static final ResourceLocation TEXTURE = new ResourceLocation(MurimMod.MOD_ID, "textures/gui/cultivation_screen.png");
    private final PlayerCultivation playerCultivation;
    private int tickCount = 0;
    private static final int CULTIVATION_DURATION = 200; // 10 seconds at 20 ticks per second
    private List<Integer> keySequence;
    private int currentKeyIndex = 0;
    private boolean cultivationSuccess = false;
    private float energyFlow = 0f;

    public CultivationScreen(PlayerCultivation playerCultivation) {
        super(new StringTextComponent("Cultivation"));
        this.playerCultivation = playerCultivation;
        this.keySequence = generateKeySequence();
    }

    private List<Integer> generateKeySequence() {
        List<Integer> sequence = new ArrayList<>();
        Random random = new Random();
        int sequenceLength = 5 + playerCultivation.getLevel().getLevel() * 2; // Увеличиваем сложность с уровнем
        int[] possibleKeys = {GLFW.GLFW_KEY_W, GLFW.GLFW_KEY_A, GLFW.GLFW_KEY_S, GLFW.GLFW_KEY_D};
        for (int i = 0; i < sequenceLength; i++) {
            sequence.add(possibleKeys[random.nextInt(possibleKeys.length)]);
        }
        return sequence;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getInstance().getTextureManager().bind(TEXTURE);
        int width = this.width;
        int height = this.height;
        this.blit(matrixStack, width / 2 - 100, height / 2 - 100, 0, 0, 200, 200);

        // Render player silhouette
        this.blit(matrixStack, width / 2 - 32, height / 2 - 64, 200, 0, 64, 128);

        // Render key sequence
        for (int i = 0; i < keySequence.size(); i++) {
            int key = keySequence.get(i);
            String keyChar = String.valueOf((char) key);
            int color = i < currentKeyIndex ? 0x00FF00 : 0xFFFFFF;
            drawCenteredString(matrixStack, this.font, keyChar, width / 2 - 50 + i * 20, height / 2 + 70, color);
        }

        // Render energy flow
        renderEnergyFlow(matrixStack, width / 2, height / 2);

        // Render cultivation progress
        int progress = (int) (tickCount * 200.0f / CULTIVATION_DURATION);
        this.blit(matrixStack, width / 2 - 100, height - 30, 0, 200, progress, 10);

        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    private void renderEnergyFlow(MatrixStack matrixStack, int centerX, int centerY) {
        int particleCount = (int) (energyFlow * 100);
        Random random = new Random();
        for (int i = 0; i < particleCount; i++) {
            float angle = random.nextFloat() * 360;
            float distance = random.nextFloat() * 100;
            int x = (int) (centerX + Math.cos(Math.toRadians(angle)) * distance);
            int y = (int) (centerY + Math.sin(Math.toRadians(angle)) * distance);
            fill(matrixStack, x, y, x + 2, y + 2, 0x8000FFFF);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (tickCount < CULTIVATION_DURATION) {
            tickCount++;
            energyFlow = (float) tickCount / CULTIVATION_DURATION;
        } else {
            completeCultivation();
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (currentKeyIndex < keySequence.size() && keyCode == keySequence.get(currentKeyIndex)) {
            currentKeyIndex++;
            if (currentKeyIndex == keySequence.size()) {
                cultivationSuccess = true;
                completeCultivation();
            }
        } else {
            cultivationSuccess = false;
            completeCultivation();
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void completeCultivation() {
        if (cultivationSuccess) {
            boolean levelUp = playerCultivation.cultivate();
            if (levelUp) {
                Minecraft.getInstance().player.sendMessage(new StringTextComponent("Cultivation successful! You've reached a new level!"), Minecraft.getInstance().player.getUUID());
            } else {
                Minecraft.getInstance().player.sendMessage(new StringTextComponent("Cultivation successful! Your qi has increased."), Minecraft.getInstance().player.getUUID());
            }
        } else {
            Minecraft.getInstance().player.sendMessage(new StringTextComponent("Cultivation failed. Try again."), Minecraft.getInstance().player.getUUID());
            playerCultivation.failCultivation();
        }
        this.onClose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
