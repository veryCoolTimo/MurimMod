package com.timo.murimmod.cultivation;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;

public class PlayerCultivation {
    private CultivationLevel level;
    private int qi;
    private boolean hasDantian;

    public PlayerCultivation() {
        this.level = CultivationLevel.MORTAL;
        this.qi = 0;
        this.hasDantian = false;
    }

    public void createDantian() {
        if (!hasDantian) {
            hasDantian = true;
            level = CultivationLevel.QI_CONDENSATION;
        }
    }

    public boolean cultivate() {
        if (!hasDantian) {
            createDantian();
            return true;
        }
        
        qi += 10 + level.getLevel() * 5; // Увеличиваем количество получаемого ци с уровнем
        if (qi >= getQiRequiredForNextLevel()) {
            levelUp();
            return true;
        }
        return false;
    }

    public void failCultivation() {
        qi -= 5; // Уменьшаем ци при неудачной попытке
        if (qi < 0) qi = 0;
    }

    private void levelUp() {
        if (level.ordinal() < CultivationLevel.values().length - 1) {
            level = CultivationLevel.values()[level.ordinal() + 1];
            qi = 0;
        }
    }

    private int getQiRequiredForNextLevel() {
        return (level.getLevel() + 1) * 100;
    }

    public CultivationLevel getLevel() {
        return level;
    }

    public int getQi() {
        return qi;
    }

    public boolean hasDantian() {
        return hasDantian;
    }

    public void applyDamageFromFailedCultivation(PlayerEntity player) {
        player.hurt(DamageSource.MAGIC, 2.0f); // Наносим небольшой урон при неудачной культивации
    }

    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("level", level.getLevel());
        nbt.putInt("qi", qi);
        nbt.putBoolean("hasDantian", hasDantian);
        return nbt;
    }

    public void deserializeNBT(CompoundNBT nbt) {
        level = CultivationLevel.fromLevel(nbt.getInt("level"));
        qi = nbt.getInt("qi");
        hasDantian = nbt.getBoolean("hasDantian");
    }
}
