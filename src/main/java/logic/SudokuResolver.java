package logic;

import exception.SudokuNotResolvedException;

public class SudokuResolver {

    public SudokuField[][] resolve(SudokuField[][] fields) throws SudokuNotResolvedException {
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                if (fields[row][column].getValue() == 0) {
                    for (Integer value : fields[row][column].getPossibleValues()) {
                        fields[row][column].setValue(value);
                        if (isValid(fields, fields[row][column])) {
                            try {
                                resolve(fields);
                                return fields;
                            } catch (SudokuNotResolvedException e) {
                                //do nothing
                            }
                        }
                        fields[row][column].setValue(0);
                    }
                    throw new SudokuNotResolvedException();
                } else {
                    if (!isValid(fields, fields[row][column])) {
                        throw new SudokuNotResolvedException();
                    }
                }
            }
        }
        return fields;
    }

    private boolean isValid(SudokuField[][] fields, SudokuField field) {
        return checkFieldsInTheSameColumn(field, fields) &&
                checkFieldsInTheSameRow(field, fields) &&
                checkFieldsInTheSameSection(field, fields);
    }

    private boolean checkFieldsInTheSameRow(SudokuField field, SudokuField[][] fields) {
        int row = field.getRow();
        for (int i = 0; i < fields.length; i++) {
            if (field.getColumn() != fields[row][i].getColumn()) {
                if (field.getValue() == fields[row][i].getValue()) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkFieldsInTheSameColumn(SudokuField field, SudokuField[][] fields) {
        int column = field.getColumn();
        for (int i = 0; i < fields[0].length; i++) {
            if (field.getRow() != fields[i][column].getRow()) {
                if (field.getValue() == fields[i][column].getValue()) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkFieldsInTheSameSection(SudokuField field, SudokuField[][] fields) {
        int section = calculateSection(field);
        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields[0].length; j++) {
                if (calculateSection(fields[i][j]) == section &&
                        (field.getRow() != fields[i][j].getRow() || field.getColumn() != fields[i][j].getColumn())) {
                    if (field.getValue() == fields[i][j].getValue()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    //(SectionRow*10) + (SectionColumn)
    private int calculateSection(SudokuField field) {
        int sectionRow = (field.getRow() / 3) + 1;
        int sectionColumn = (field.getColumn() / 3) + 1;
        return sectionRow * 10 + sectionColumn;
    }
}
