package dev.tarvos.xyz.blockdata;

import lombok.Getter;

/**
 * Created by Nyvil, 05/03/2022 at 17:18
 *
 * @author Nyvil
 * @copyright Nyvil 2022 under Apache License 2.0, unless stated otherwise
 */

@Getter
public enum TreeCommandData {

    SMALL("small"), LARGE("large");

    private final String identifier; // for identifier purposes

    TreeCommandData(String name) {
        this.identifier = name;
    }

    public static TreeCommandData forName(String name) {
        for(TreeCommandData treeType : values()) {
            if(treeType.getIdentifier().equalsIgnoreCase(name)) {
                return treeType;
            }
        }
        return null;
    }
}
