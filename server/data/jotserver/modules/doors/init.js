var lockedDoors = new Array(5098, 5107, 1209, 1212, 5137, 5140, 1231, 1234, 1249, 1252, 
		5116, 5125, 5134, 5143, 3535, 3544, 5278, 5281, 6891, 6900, 7033, 7042, 6192, 
		6195, 6249, 6252);
var closedDoors = new Array(5099, 5101, 5103, 5105, 5108, 5110, 5112, 5114, 1210, 1213, 
		1219, 1221, 1223, 1225, 1227, 1229, 5138, 5141, 1232, 1235, 1237, 1239, 1241, 
		1243, 1245, 1247, 1250, 1253, 1255, 1257, 1259, 1261, 5515, 5517, 5117, 5119, 
		5121, 5123, 5126, 5128, 5130, 5132, 5135, 5144, 3536, 3538, 3540, 3542, 3545, 
		3547, 3549, 3551, 5279, 5282, 5284, 5286, 5288, 5290, 5292, 5294, 6892, 6894, 
		6896, 6898, 6901, 6903, 6905, 6907, 7034, 7036, 7038, 7040, 7043, 7045, 7047, 
		7049, 7054, 7056, 6193, 6196, 6198, 6200, 6202, 6204, 6206, 6208, 3250, 6253, 
		6255, 6257, 6259, 6261, 6263, 6265, 6795, 6797, 6799, 6801);
var openedVerticalDoors = new Array(5109, 5111, 5113, 5115, 1211, 1220, 1224, 1228, 5142, 
		1233, 1238, 1242, 1246, 1251, 1256, 1260, 5516, 5127, 5129, 5131, 5133, 5145, 
		3546, 3548, 3550, 3552, 5283, 5285, 5289, 5293, 6902, 6904, 6906, 6908, 7044, 
		7046, 7048, 7050, 7055, 6194, 6199, 6203, 6207, 6251, 6256, 6260, 6264, 6798, 
		6802);
var openedHorizontalDoors = new Array(5100, 5102, 5104, 5106, 1214, 1222, 1226, 1230, 5139, 
		1236, 1240, 1244, 1248, 1254, 1258, 1262, 5518, 5118, 5120, 5122, 5124, 5136, 
		3537, 3539, 3541, 3543, 5280, 5287, 5291, 5295, 6893, 6895, 6897, 6899, 7035, 
		7037, 7039, 7041, 7057, 6197, 6201, 6205, 6209, 6254, 6258, 6262, 6266, 6796, 
		6800);

function init(context) {
	var events = context.getEventEngine();
	var itemEvents = events.getItemEngine();
	itemEvents.registerItemUseHandler(openLockedDoor, lockedDoors);
	itemEvents.registerItemUseHandler(openDoor, closedDoors);
	itemEvents.registerItemUseHandler(closeVerticalDoor, openedVerticalDoors);
	itemEvents.registerItemUseHandler(closeHorizontalDoor, openedHorizontalDoors);
}

function openLockedDoor(event) {
	var world = event.getWorld();
	var item = event.getItem();
	var items = world.getItemTypes();
	transform(items, item, item.getId()+2);
}

function openDoor(event) {
	var world = event.getWorld();
	var item = event.getItem();
	var items = world.getItemTypes();
	transform(items, item, item.getId()+1);
}

function closeVerticalDoor(event) {
	var world = event.getWorld();
	var item = event.getItem();
	var items = world.getItemTypes();
	
	var fromTile = item.getTile();
	var toTile = fromTile.getTile(org.jotserver.ot.model.util.Direction.EAST);
	if(toTile != null) {
		relocate(fromTile, toTile);
	}
	transform(items, item, item.getId()-1);
}

function closeHorizontalDoor(event) {
	var world = event.getWorld();
	var item = event.getItem();
	var items = world.getItemTypes();
	
	var fromTile = item.getTile();
	var toTile = fromTile.getTile(org.jotserver.ot.model.util.Direction.SOUTH);
	if(toTile != null) {
		relocate(fromTile, toTile);
	}
	transform(items, item, item.getId()-1);
}

function transform(items, item, newId) {
	var newItem = items.createItem(newId);
	var tile = item.getTile();
	tile.executeRemoveItem(item);
	tile.executeAddItem(newItem);
}

function relocate(fromTile, toTile) {
	var it = fromTile.getItems().iterator();
	while(it.hasNext()) {
		var item = it.next();
		if(item.getType().hasAttribute(ItemAttribute.MOVEABLE) && fromTile.queryRemoveItem(item)) {
			fromTile.executeRemoveItem(item);
			toTile.executeAddItem(item);
		}
	}
	it = fromTile.getCreatures().iterator();
	while(it.hasNext()) {
		var creature = it.next();
		fromTile.executeMoveCreature(creature, toTile);
	}
}