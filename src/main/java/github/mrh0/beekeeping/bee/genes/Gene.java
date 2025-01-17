package github.mrh0.beekeeping.bee.genes;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

import java.util.Random;

public interface Gene {
    Random random = new Random();

    static int get(ItemStack stack, String key) {
        if (stack.getTag() == null) {
            return 0;
        }

        return stack.getTag().getInt(key);
    }

    static void set(ItemStack stack, String key, int value) {
        if (stack.getTag() == null) {
            throw new IllegalArgumentException("[Beekeeping] Gene#set called on invalid stack");
        }

        stack.getTag().putInt(key, value);
    }

    String getName();

    int getIndex();

    interface RandomFunction {
        int rand(Random rand);
    }

    static int random5Wide(Random rand) {
        return rand.nextInt(5);
    }

    static int random5Narrow(Random rand) {
        return rand.nextInt(3) + 1;
    }

    static int random5Low(Random rand) {
        return rand.nextInt(3);
    }

    static int random5High(Random rand) {
        return rand.nextInt(3) + 2;
    }

    static int normal5(Random rand) {
        return 2;
    }

    static int strict(Random rand) {
        return 0;
    }
    static int picky(Random rand) {
        return 1;
    }
    static int any(Random rand) {
        return 2;
    }

    static int random3Low(Random rand) {
        return rand.nextInt(2);
    }

    static int random3High(Random rand) {
        return rand.nextInt(2) + 1;
    }

    static int eval(RandomFunction fn) {
        return fn.rand(Gene.random);
    }

    static int select(int a, int b) {
        return Gene.random.nextBoolean() ? a : b;
    }

    ChatFormatting[] formatting = {
            ChatFormatting.DARK_AQUA,
            ChatFormatting.AQUA,
            ChatFormatting.YELLOW,
            ChatFormatting.GOLD,
            ChatFormatting.RED
    };

    default MutableComponent getComponent() {
        return Component.translatable("text.beekeeping.gene.type." + getName()).withStyle(formatting[getIndex()]);
    }

    enum RandomFunctions {
        RANDOM_5_WIDE("random5wide", Gene::random5Wide),
        RANDOM_5_NARROW("random5narrow", Gene::random5Narrow),
        RANDOM_5_LOW("random5low", Gene::random5Low),
        RANDOM_5_HIGH("random5high", Gene::random5High),
        NORMAL_5("normal5", Gene::normal5),
        STRICT("strict", Gene::strict),
        PICKY("picky", Gene::picky),
        ANY("any", Gene::any),
        RANDOM_3_LOW("random3low", Gene::random3Low),
        RANDOM_3_HIGH("random3high", Gene::random3High);

        public final String name;
        public final RandomFunction func;

        RandomFunctions(String name, RandomFunction func) {
            this.name = name;
            this.func = func;
            RandomFunctionRegistry.register(this);
        }
    }
}
