package com.xiaowenz.scv.core;


import java.util.ArrayList;

public class SCIndex {
    private ArrayList<SCScript> scripts = new ArrayList<SCScript>();

    public static SCIndex create(){
        return new SCIndex();
    }

    private SCIndex(){

    }

    /**
     * Load scripts by version scope.
     *
     * Return a same index object with same order
     *
     *
     * @param action
     * @param start
     * @param end
     * @return
     */
    public SCIndex loadScripts(SCAction action, SCVersion start, SCVersion end){
        SCIndex newIndex = SCIndex.create();
        if(start.compareTo(end) > 0) {
            return newIndex;
        }
        for (SCScript script: this.scripts) {
            if(start.compareTo(script.getScv()) <= 0 && end.compareTo(script.getScv()) >= 0) {
                if(action == script.getScAction()) {
                    newIndex.add(script);
                }
            }
        }
        return newIndex;
    }

    public ArrayList<SCScript> listScripts(){
        return this.scripts;
    }
    /**
     * Add each script object into list
     *
     * @param ss
     */
    public void add(SCScript ss) {
        sortedAdd(scripts, ss);
    }

    /**
     * put script object in sorted order of each list.
     *
     * @param list
     * @param o
     */
    private void sortedAdd(ArrayList<SCScript> list, SCScript o) {
        int i = 0;
        for (Comparable item: list) {
            if(item.compareTo(o) <= 0) {
                i++;
                continue;
            } else {
                list.add(i, o);
                return;
            }
        }
        list.add(i, o);
    }

    @Override
    public String toString() {
        return "SCIndex{" +
                "scripts=" + scripts +
                '}';
    }

    public String scriptsToString(){
        StringBuilder sb = new StringBuilder();
        sb.append("<Script List>\n");
        for (SCScript ss: this.scripts) {
            sb.append(ss.getFileName() + "\n");
        }
        sb.append("\n");

        return sb.toString();
    }


    public static void main(String[] args) throws InvalidVersionException {
        SCIndex index = SCIndex.create();
        index.add(SCScript.create("R2.0.0_DDL_abc.sql","dummy"));
        index.add(SCScript.create("R1.0.0_DDL_abc.sql","dummy"));
        index.add(SCScript.create("R1.0.1_DDL_abc.sql","dummy"));

        index.add(SCScript.create("R1.0.0_DML_abc.sql","dummy"));
        index.add(SCScript.create("R2.0.1_PATCH_abc.sql","dummy"));
        index.add(SCScript.create("R3.0.1_DDL_abc.sql","dummy"));
        index.add(SCScript.create("R2.0.1_DDL_abc.sql","dummy"));
        index.add(SCScript.create("R1.0.1_DML_abc.sql","dummy"));

        index.add(SCScript.create("U1.0.1_DDL_abc.sql","dummy"));
        index.add(SCScript.create("U2.0.1_DDL_abc.sql","dummy"));
        index.add(SCScript.create("U2.0.1_DML_abc.sql","dummy"));

        System.out.println(index.scriptsToString());
        System.out.println("------ load -----");
        System.out.println(index.loadScripts(SCAction.R, SCVersion.create("1.0.0"),  SCVersion.create("2.0.0")).scriptsToString());
    }
}

