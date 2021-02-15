package com.lolpvp.perkbooks;



public class PerkBook
{
    private final String perk;
    private String group;
    private String permission;
    private String[] permissions;
    private final PerkBookType type;

    public PerkBook(String perkName, PerkBookType type, String... args) {
        this.perk = perkName;
        this.type = type;
        switch (type) {
            case PERMISSION:
                this.permission = args[0];
                break;
            case GROUP:
                this.group = args[0];
                break;
            case MULTIPLE_PERMISSIONS:
                this.permissions = args;
                break;
        }
    }


    public String getPerk() {
        return this.perk;
    }


    public String getAuthor() {
        return "ohvals";
    }


    public String getPermission() {
        return this.permission;
    }


    public String[] getPermissions() {
        return this.permissions;
    }


    public String getGroup() {
        return this.group;
    }


    public PerkBookType getType() {
        return this.type;
    }
}
