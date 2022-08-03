package com.xiaowenz.scv.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SCVersion implements Comparable<SCVersion>{

    private String version;
    private List<Integer> versionList = new ArrayList<Integer>();

    /**
     * Standard format of version should be int with delimiter .
     *
     * e.g 2.3.4.100.0.1
     *
     * Failed to parse version will throw InvalidVersionException
     *
     * @param version
     * @return
     * @throws InvalidVersionException
     */
    public static SCVersion create(String version) throws InvalidVersionException {
        if(SCUtil.isBlank(version)){
            throw new InvalidVersionException("No valid version: " + version);
        }

        SCVersion scv = new SCVersion();
        scv.setVersion(version);
        try {
            List<String> versions = Arrays.asList(version.split("\\."));

            for (String str: versions) {
                scv.getVersionList().add(Integer.parseInt(str));
            }
        } catch (Exception e) {
            throw new InvalidVersionException("No valid version: " + version);
        }

        return scv;
    }

    /**
     * new Version object
     * 
     * @param v
     * @return
     * @throws InvalidVersionException
     */
    public static SCVersion version(String v) throws InvalidVersionException {
        return create(v);
    }

    public List<Integer> getVersionList() {
        return versionList;
    }

    /**
     * get version int by digit. Beyong max digit will always return 0 with no exception
     *
     * 1.0.4 - get(2) = 4 | get(1) = 0
     *
     * @param i
     * @return
     */
    public int getVersion(int i){
        try{
            return this.versionList.get(i);
        }catch(Exception e){
            return 0;
        }
    }

    /**
     * get number of digits of the version
     *
     * 1.0.0 - 3
     * 1.2 - 2
     *
     * @return
     */
    public int getDigits(){
        return this.versionList.size();
    }

    // private construction
    private SCVersion() {

    }

    @Override
    public String toString() {
        final String s = "SCVersion{" +
                "version='" + version + '\'' +
                ", versionList=" + versionList +
                '}';
        return s;
    }

    @Override
    public int compareTo(SCVersion scv) {

        int larger = scv.getDigits() >= this.getDigits()? scv.getDigits(): this.getDigits();

        for (int i = 0; i < larger; i++) {
            if(this.getVersion(i) > scv.getVersion(i)){
                return 1;
            } else if(this.getVersion(i) < scv.getVersion(i)) {
                return -1;
            }
            // equal case goes here
        }
        // every digits are equal goes here
        return 0;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }
}

