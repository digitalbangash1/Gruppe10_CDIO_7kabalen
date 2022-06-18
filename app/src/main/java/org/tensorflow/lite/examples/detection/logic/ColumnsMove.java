package org.tensorflow.lite.examples.detection.logic;

public class ColumnsMove {

    private int columnOnTop;
    private int columnAtBottom;

    public ColumnsMove(int columnOnTop, int columnAtBottom){
        this.columnOnTop = columnOnTop;
        this.columnAtBottom = columnAtBottom;
    }

    public int getColumnOnTop() {
        return columnOnTop;
    }

    public int getColumnAtBottom() {
        return columnAtBottom;
    }
}
