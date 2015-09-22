package org.jotserver.ot.model.item;

import org.jotserver.ot.model.util.Direction;
import org.junit.Ignore;

@Ignore
public class TestItemProvider {

    public static Item createItem(ItemType type) {
        return new Item(type);
    }

    public static ItemType getGrassType() {
        ItemType ret = new ItemType() {{
            group = ItemType.Group.GROUND;
        }};
        return ret;
    }

    public static Item getGroundItem() {
        ItemType groundType = new ItemType();
        groundType.group = ItemType.Group.GROUND;
        return new Item(groundType);
    }

    public static Item getRegularItem() {
        return new Item(new ItemType());
    }

    public static Item getTopItem(int order) {
        ItemType type = new ItemType();
        type.addAttribute(ItemAttribute.ALWAYSONTOP);
        type.alwaysOnTopOrder = order;
        return new Item(type);
    }

    public static Item getFloorChangingItem(Direction dir) {
        ItemType type = new ItemType();
        type.setAttribute(ItemAttribute.FLOORCHANGENORTH, dir.contains(Direction.NORTH));
        type.setAttribute(ItemAttribute.FLOORCHANGESOUTH, dir.contains(Direction.SOUTH));
        type.setAttribute(ItemAttribute.FLOORCHANGEEAST, dir.contains(Direction.EAST));
        type.setAttribute(ItemAttribute.FLOORCHANGEWEST, dir.contains(Direction.WEST));
        return new Item(type);
    }

    public static Item getFloorChangeDownItem() {
        ItemType type = new ItemType();
        type.group = ItemType.Group.GROUND;
        type.addAttribute(ItemAttribute.FLOORCHANGEDOWN);
        return new Item(type);
    }

    public static Item getBlockSolidItem() {
        Item i = getRegularItem();
        i.getType().addAttribute(ItemAttribute.BLOCKSOLID);
        return i;
    }

    public static Item getSplashItem(FluidType fluid) {
        ItemType type = new ItemType();
        type.group = ItemType.Group.SPLASH;
        Item i = new Item(type);
        i.setFluidType(fluid);
        return i;
    }
}
