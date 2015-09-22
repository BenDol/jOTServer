
function init(context) {
    context.getEventEngine().getChatEngine().registerDefaultHandler(filter);
}

function filter(creature, type, message) {
    var ret = message.toLowerCase().indexOf("fuck") == -1;
    if(!ret) {
        creature.getPrivateChannel().sendCancel("It's not nice to say that.");
    }
    return ret;
}