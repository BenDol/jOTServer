
var world = null;

function init(context) {
    world = context.getWorld();
    var chat = world.getChatManager();
    var channel = chat.getPublicChannel(0xA1);
    context.getEventEngine().getChatEngine().registerChannelSayHandler(channel, commandListener);
}

function commandListener(creature, type, message) {

    var scanner = new java.util.Scanner(message);
    if(scanner.hasNext()) {
        var command = scanner.next().toLowerCase();

        if(command.equals("teleport")) {
            teleportSelf(creature, scanner);
        } else if(command.equals("town")) {
            teleportTown(creature, scanner);
        } else if(command.equals("position")) {
            reportPosition(creature, scanner);
        } else {
            creature.getPrivateChannel().sendCancel(command + " is not a valid command.");
            return false;
        }
        return true;
    } else {
        return false;
    }
}

function teleportTown(creature, parameters) {
    var town = null;
    if(parameters.hasNext()) {
        var name = parameters.next();
        town = world.getMap().getTownAccessor().getTown(name);
    } else if(creature instanceof Player) {
        town = creature.getTown();
    }

    if(town != null) {
        var toTile = world.getMap().getTile(town.getPosition());
        teleportCreature(creature, toTile);
        creature.getPrivateChannel().sendCancel("You teleported to the town of " + town.getName() + ".");
    }

}

function teleportSelf(creature, parameters) {

    var dir = creature.getDirection();
    var distance = 1;

    while(parameters.hasNext()) {
        if(parameters.hasNextInt()) {
            distance = parameters.nextInt();
        } else {
            try {
                dir = Direction.valueOf(parameters.next().toUpperCase());
            } catch(e) {
                creature.getPrivateChannel().sendCancel("Please specify a valid direction.");
            }
        }
    }

    var delta = new Position(0, 0, 0).add(dir).scale(distance);
    var toPos = creature.getPosition().add(delta);
    var toTile = world.getMap().getTile(toPos);

    teleportCreature(creature, toTile, true);

    dir = dir.normalize();
    if(dir != Direction.NONE) {
        creature.turn(dir);
    }

}

function reportPosition(creature, parameters) {
    var pos = creature.getPosition();
    var target = creature;

    var name = "";
    while(parameters.hasNext()) {
        name = name + parameters.next();
        if(parameters.hasNext()) {
            name = name + " ";
        }
    }

    if(name != "") {
        var c = world.getPlayerByName(name);
        if(c != null) {
            pos = c.getPosition();
            target = c;
        }
    }

    creature.getPrivateChannel().sendDescription("The location of " + target.getName() + " is at " + pos.toString() + ".");
}