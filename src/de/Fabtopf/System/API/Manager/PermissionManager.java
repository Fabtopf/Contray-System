package de.Fabtopf.System.API.Manager;

import org.bukkit.entity.Player;

/**
 * Created by Fabi on 16.09.2017.
 */
public class PermissionManager {

    public static boolean check(Player p, String permission, boolean op) {

        if(p.hasPermission(permission)) return true;
        if(p.isOp()) return true;

        return false;

    }

}
