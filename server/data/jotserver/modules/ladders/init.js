var ladders = new Array(1386, 3678, 5543);

function init(context) {
	var events = context.getEventEngine();
	var itemEvents = events.getItemEngine();
	itemEvents.registerItemUseHandler(climbLadder, ladders);
}

function climbLadder(event) {
	var ladder = event.getItem();
	var toTile = ladder.getTile();
	toTile = toTile.getTile(Direction.UP);
	toTile = toTile.getTile(Direction.SOUTH);
	var creature = event.getCreature();
	teleportCreature(creature, toTile, false);
}