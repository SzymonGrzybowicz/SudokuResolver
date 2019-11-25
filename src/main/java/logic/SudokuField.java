package logic;

import java.util.ArrayList;
import java.util.List;

public class SudokuField {
    private List<Integer> possibleValues = new ArrayList<>();
    private int value;
    private int column;
    private int row;

    public SudokuField(int value, int column, int row) {
        this.value = value;
        this.column = column;
        this.row = row;
        possibleValues.add(1);
        possibleValues.add(2);
        possibleValues.add(3);
        possibleValues.add(4);
        possibleValues.add(5);
        possibleValues.add(6);
        possibleValues.add(7);
        possibleValues.add(8);
        possibleValues.add(9);
    }

    public boolean removePossibleValue(Integer value) {
        if (possibleValues.contains(value)) {
            possibleValues.remove(value);
            return true;
        }
        return false;
    }

    public List<Integer> getPossibleValues() {
        return possibleValues;
    }

    public int getValue() {
        return value;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
