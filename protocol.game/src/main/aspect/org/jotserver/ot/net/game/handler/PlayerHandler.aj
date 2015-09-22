package org.jotserver.ot.net.game.handler;

import org.jotserver.ot.model.TextMessageType;
import org.jotserver.ot.model.Thing;
import org.jotserver.ot.model.player.InternalPlayer;
import org.jotserver.ot.model.player.Player;
import org.jotserver.ot.model.world.LocalGameWorld;
import org.jotserver.ot.net.game.creature.PlayerAddVipWriter;
import org.jotserver.ot.net.game.out.PlayerCancelWalkWriter;
import org.jotserver.ot.net.game.out.TextMessageWriter;

public aspect PlayerHandler {

    private pointcut playerLookAt(Player player, Thing thing) :
        target(player) &&
        args(thing) &&
        execution(public boolean Player.lookAt(Thing));

    private pointcut playerCancelWalk(Player player) :
        target(player) &&
        execution(public boolean Creature.cancelWalk());

    private pointcut playerAddVip(InternalPlayer internalPlayer, Player vip) :
        target(internalPlayer) &&
        args(vip) &&
        execution(public void InternalPlayer.addVip(Player));

    private pointcut playerJoinWorld(LocalGameWorld world, Player player) :
        target(world) &&
        args(player) &&
        execution(public void LocalGameWorld.addPlayer(Player));

    private pointcut playerLeaveWorld(LocalGameWorld world, Player player) :
        target(world) &&
        args(player) &&
        execution(public void LocalGameWorld.removePlayer(Player));

    /*
     * Player.lookAt
     */
    after(Player player, Thing thing) returning(boolean ret) : playerLookAt(player, thing) {
        if(ret && thing.isPlaced() && player.isOnline()) {
            player.getGameProtocol().send(new TextMessageWriter(player, TextMessageType.DESCRIPTION, "You see " + thing.getDescription() + ". " + thing.getPosition() + "."));
        }
    }

    /*
     * Player.cancelWalk
     */
    after(Player player) returning(boolean ret) : playerCancelWalk(player) {
        if(ret && player.isOnline()) {
            player.getGameProtocol().send(new PlayerCancelWalkWriter(player));
        }
    }

    /*
     * InternalPlayer.addVip
     */
    after(InternalPlayer internalPlayer, Player vip) : playerAddVip(internalPlayer, vip) {
        Player player = internalPlayer.getPlayer();
        if(player.isOnline()) {
            LocalGameWorld world = player.getGameProtocol().getPlayerWorld(player);
            Player vipPlayer = world.getPlayerByGlobalId(vip.getGlobalId());
            player.getGameProtocol().send(new PlayerAddVipWriter(player, vip, vipPlayer != null));
        }
    }

    /*
     * LocalGameWorld.addPlayer
     */
    after(LocalGameWorld world, Player player) : playerJoinWorld(world, player) {
        for(Player p : world.getPlayers()) {
            if(p.isOnline() && p.isVip(player)) {
                p.getGameProtocol().send(new PlayerAddVipWriter(p, player, true));
            }
        }
    }

    /*
     * LocalGameWorld.removePlayer
     */
    after(LocalGameWorld world, Player player) : playerLeaveWorld(world, player) {
        for(Player p : world.getPlayers()) {
            if(p.isOnline() && p.isVip(player)) {
                p.getGameProtocol().send(new PlayerAddVipWriter(p, player, false));
            }
        }
    }
}
