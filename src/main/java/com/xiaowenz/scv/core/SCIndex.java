package com.xiaowenz.scv.core;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SCIndex {
    private ArrayList<SCScript> scripts = new ArrayList<SCScript>();

    public static SCIndex create(){
        return new SCIndex();
    }

    private SCIndex(){

    }

    /**
     * List R scripts in forward order, excluding PATCH
     * 
     * @return
     */
    public List<SCScript> release(SCVersion start, SCVersion end){
        SCIndex newIndex = this.loadScripts(SCAction.R, start, end);
        List<SCScript> list = newIndex.listScripts().stream().filter(script -> !SCType.PATCH.equals(script.getScType())).toList();

        return list;
    }

    /**
     * List U scripts in backward order, excluding PATCH
     * 
     * @return
     */
    public List<SCScript> undo(SCVersion start, SCVersion end){
        SCIndex newIndex = this.loadScripts(SCAction.U, start, end);
        List<SCScript> list = newIndex.listScripts().stream().filter(script -> !SCType.PATCH.equals(script.getScType())).toList();
        ArrayList<SCScript> array = new ArrayList<SCScript>(list);
        Collections.reverse(array); 

        return array;
    }

    /**
     * List Patch scripts in forward order
     * 
     * @return
     */
    public List<SCScript> patch(SCVersion start, SCVersion end){
        SCIndex newIndex = this.loadScripts(SCAction.R, start, end);
        List<SCScript> list = newIndex.listScripts().stream().filter(script -> SCType.PATCH.equals(script.getScType())).toList();

        return list;
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
    protected SCIndex loadScripts(SCAction action, SCVersion start, SCVersion end){
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
        for (final Comparable<SCScript> item: list) {
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

}

