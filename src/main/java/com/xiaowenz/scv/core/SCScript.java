package com.xiaowenz.scv.core;


import java.util.Arrays;
import java.util.List;

public class SCScript implements Comparable<SCScript> {

    private String fileName;
    private String location;
    private SCAction scAction;
    private SCType scType;
    private SCVersion scv;

    /**
     * Expect filename to be '[X][Version]_[TYPE]_[Comment].sql'
     *
     * @param fileName
     * @return
     * @throws InvalidVersionException
     */
    public static SCScript create(String fileName, String location) throws InvalidVersionException {
        SCScript sScript = new SCScript();
        sScript.fileName = fileName;
        sScript.setLocation(location);
        String errorMsg = "Invalid File Name: " + fileName;
        try {
            List<String> tags = Arrays.asList(fileName.split("_"));
            if (tags.size() <= 2) {
                throw new InvalidVersionException(errorMsg);
            }

            SCAction scAction = SCAction.valueOf(String.valueOf(tags.get(0).charAt(0)));
            sScript.setScAction(scAction);
            SCType scType = SCType.valueOf(tags.get(1));
            sScript.setScType(scType);
            sScript.setScv(SCVersion.create(tags.get(0).substring(1)));

        } catch (InvalidVersionException ie) {
            throw ie;
        } catch (Exception e) {
            //e.printStackTrace();
            throw new InvalidVersionException(errorMsg, e);
        }

        return sScript;
    }

    private SCScript() {

    }

    public static void main(String[] args) throws InvalidVersionException {
        String name = "R1.0.0_DDL_xxxxxx.sql";
        SCAction.valueOf("R");
        System.out.println(SCScript.create(name, ""));
        List<String> tags = Arrays.asList(name.split("_"));
        System.out.println(name.substring(1));
        System.out.println(tags);

        System.out.println(SCType.valueOf("PATCH").compareTo(SCType.valueOf("DDL")));
    }

    /**
     * different version: compare version
     * same version: action R < U
     * same action: DDL < DML < PATCH(rely on enum SCType order)
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(SCScript o) {
        int versionCompare = this.scv.compareTo(o.getScv());
        if(versionCompare == 0){
            int actionCompare = this.getScAction().compareTo(o.getScAction());
            if(actionCompare == 0) {
                return this.getScType().compareTo(o.getScType());
            } else {
                return actionCompare;
            }
        } else {
            return versionCompare;
        }
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setScv(SCVersion scv) {
        this.scv = scv;
    }

    public void setScAction(SCAction scAction) {
        this.scAction = scAction;
    }

    public void setScType(SCType scType) {
        this.scType = scType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFileName() {
        return fileName;
    }

    public SCAction getScAction() {
        return scAction;
    }

    public SCType getScType() {
        return scType;
    }

    public SCVersion getScv() {
        return scv;
    }

    @Override
    public String toString() {
        return "SchemaScript{" +
                "fileName='" + fileName + '\'' +
                ", scAction=" + scAction +
                ", scType=" + scType +
                ", scv=" + scv +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof SCScript) {
            SCScript ss = (SCScript)obj;
            return ss.getFileName().equals(this.getFileName()) && ss.getLocation().equals(this.getLocation());
        }
        return false;
    }
}