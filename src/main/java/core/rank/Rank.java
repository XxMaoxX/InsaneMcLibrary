package core.rank;

import java.util.LinkedList;

public class Rank {

    private String
            name,
            prefix,
            suffix;
    private boolean
            staff,
            donator;

    private LinkedList<String> permissions;

    public Rank() {}

    public Rank(String name, String prefix, String suffix, boolean staff, boolean donator, LinkedList<String> permissions) {
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
        this.staff = staff;
        this.donator = donator;
        this.permissions = permissions;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getPrefix() {
        return prefix;
    }


    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }


    public String getSuffix() {
        return suffix;
    }


    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }


    public boolean isStaff() {
        return staff;
    }


    public void setStaff(boolean staff) {
        this.staff = staff;
    }


    public boolean isDonator() {
        return donator;
    }


    public void setDonator(boolean donator) {
        this.donator = donator;
    }


    public LinkedList<String> getPermissions() {
        return permissions;
    }


    public void setPermissions(LinkedList<String> permissions) {
        this.permissions = permissions;
    }

    public boolean hasPermission(String permission) {
        return getPermissions().contains(permission);
    }

    public void addPermission(String permission) {
        if (hasPermission(permission) ? null : getPermissions().add(permission)) ;
    }


    public void removePermission(String permission) {
        if (!hasPermission(permission) ? null : getPermissions().remove(permission)) ;
    }

}
