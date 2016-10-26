package me.desht.modularrouters.logic.filter;

import com.google.common.collect.Lists;
import me.desht.modularrouters.item.module.ItemModule;
import me.desht.modularrouters.item.module.Module;
import me.desht.modularrouters.item.smartfilter.ItemSmartFilter;
import me.desht.modularrouters.item.smartfilter.SmartFilter;
import me.desht.modularrouters.logic.RouterTarget;
import me.desht.modularrouters.logic.filter.matchers.IItemMatcher;
import me.desht.modularrouters.logic.filter.matchers.SimpleItemMatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import org.apache.commons.lang3.Validate;

import java.util.List;

public class Filter {
    public static final int FILTER_SIZE = 9;

    public static final String NBT_FILTER = "ModuleFilter";

    private final Flags flags;
    private final List<IItemMatcher> matchers = Lists.newArrayList();

    public Filter() {
        flags = new Flags();
    }

    public Filter(RouterTarget target, ItemStack moduleStack) {
        if (moduleStack.getItem() instanceof ItemModule && moduleStack.hasTagCompound()) {
            flags = new Flags(moduleStack);
            NBTTagList tagList = moduleStack.getTagCompound().getTagList(NBT_FILTER, Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < tagList.tagCount(); ++i) {
                NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
                ItemStack filterStack = ItemStack.loadItemStackFromNBT(tagCompound);
                matchers.add(createMatcher(filterStack, moduleStack, target));
            }
        } else {
            flags = new Flags();
        }
    }

    private IItemMatcher createMatcher(ItemStack filterStack, ItemStack moduleStack, RouterTarget target) {
        if (filterStack.getItem() instanceof ItemSmartFilter) {
            SmartFilter f = ItemSmartFilter.getFilter(filterStack);
            return f.compile(filterStack, moduleStack, target);
        } else {
            return new SimpleItemMatcher(filterStack);
        }
    }

    public boolean pass(ItemStack stack) {
        for (IItemMatcher matcher : matchers) {
            if (matcher.matchItem(stack, flags)) {
                return !flags.isBlacklist();
            }
        }

        // no matches: pass if this is a blacklist, fail if a whitelist
        return flags.isBlacklist();
    }

    public class Flags {
        private final boolean blacklist;
        private final boolean ignoreMeta;
        private final boolean ignoreNBT;
        private final boolean ignoreOredict;

        public Flags(ItemStack moduleStack) {
            Validate.isTrue(moduleStack.getItem() instanceof ItemModule);

            Module module = ItemModule.getModule(moduleStack);
            blacklist = module.isBlacklist(moduleStack);
            ignoreMeta = module.ignoreMeta(moduleStack);
            ignoreNBT = module.ignoreNBT(moduleStack);
            ignoreOredict = module.ignoreOreDict(moduleStack);
        }

        public Flags() {
            blacklist = false;
            ignoreMeta = false;
            ignoreNBT = true;
            ignoreOredict = true;
        }

        public boolean isBlacklist() {
            return blacklist;
        }

        public boolean isIgnoreMeta() {
            return ignoreMeta;
        }

        public boolean isIgnoreNBT() {
            return ignoreNBT;
        }

        public boolean isIgnoreOredict() {
            return ignoreOredict;
        }

    }
}
