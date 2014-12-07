/*
 * Define classes.
 */
Direction = org.jotserver.ot.model.util.Direction;
Position = org.jotserver.ot.model.util.Position;
Direction = org.jotserver.ot.model.util.Direction;
Cylinder = org.jotserver.ot.model.Cylinder;
Effect = org.jotserver.ot.model.Effect;
Light = org.jotserver.ot.model.Light;
Outfit = org.jotserver.ot.model.Outfit;
Thing = org.jotserver.ot.model.Thing;
TextMessageType = org.jotserver.ot.model.TextMessageType;
Account = org.jotserver.ot.model.account.Account;

ConsoleChannel = org.jotserver.ot.model.chat.ConsoleChannel;
DefaultChatChannel = org.jotserver.ot.model.chat.DefaultChatChannel;
PrivateChatChannel = org.jotserver.ot.model.chat.PrivateChatChannel;
PrivateMessageChannel = org.jotserver.ot.model.chat.PrivateMessageChannel;
SpeakType = org.jotserver.ot.model.chat.SpeakType;

Creature = org.jotserver.ot.model.creature.Creature;
Path = org.jotserver.ot.model.creature.Path;
DirectionPath = org.jotserver.ot.model.creature.DirectionPath;

Container = org.jotserver.ot.model.item.Container;
FluidType = org.jotserver.ot.model.item.FluidType;
Item = org.jotserver.ot.model.item.Item;
ItemType = org.jotserver.ot.model.item.ItemType;
ItemAttribute = org.jotserver.ot.model.item.ItemAttribute;
Stackable = org.jotserver.ot.model.item.Stackable;

Map = org.jotserver.ot.model.map.Map;
Tile = org.jotserver.ot.model.map.Tile;
Town = org.jotserver.ot.model.map.Town;

Camera = org.jotserver.ot.model.player.Camera;
Inventory = org.jotserver.ot.model.player.Inventory;
InventorySlot = org.jotserver.ot.model.player.InventorySlot;
Player = org.jotserver.ot.model.player.Player;
Premium = org.jotserver.ot.model.player.Premium;
Skills = org.jotserver.ot.model.player.Skills;
Stance = org.jotserver.ot.model.player.Stance;

LocalGameWorld = org.jotserver.ot.model.world.LocalGameWorld;
WorldState = org.jotserver.ot.model.world.WorldState;


/*
 * Functions
 */

function teleportCreature(creature, toTile, effect) {
	var fromTile = creature.getTile();
	if(toTile != null && toTile != fromTile) {
		fromTile.executeRemoveCreature(creature);
		toTile.executeAddCreature(creature);
		if(effect) {
			fromTile.executeAddEffect(Effect.TELEPORT);
			toTile.executeAddEffect(Effect.TELEPORT);
		}
	} else {
		creature.getPrivateChannel().sendCancel("You cannot teleport to that location.");
	}
}