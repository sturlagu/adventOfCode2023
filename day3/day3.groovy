String input = new File('./input.txt').getText('UTF-8')
String[] inputArray = input.split('\r\n')

List<String[]> engineSchematic = inputArray.collect { String line -> line.split("") }
int sumOfPartNumbers = 0
int sumOfGearRatios = 0
engineSchematic.eachWithIndex { row, int rowIndex ->
    // Part 1
    String collectedDigits = ""
    boolean hasSymbol = false
    row.eachWithIndex { String partNumber, int colIndex ->
        if (partNumber ==~ /\d/) {
            collectedDigits += partNumber
            if (!hasSymbol) {
                Map<String, String> adjacentValues = getAdjacentValues(engineSchematic, row, rowIndex, colIndex)
                hasSymbol = adjacentValues.any { it ->
                    return it.value && it.value != "." && !(it.value ==~ /\d/)
                }
            }
        } else {
            if (hasSymbol) {
                sumOfPartNumbers += Integer.parseInt(collectedDigits)
                hasSymbol = false
            }
            collectedDigits = ""
        }

        // Part 2
        if (partNumber == "*") {
            Map<String, String> adjacentValues = getAdjacentValues(engineSchematic, row, rowIndex, colIndex)
            Map<String, String> adjacentNumbers = adjacentValues.findAll { it ->
                return it.value && it.value ==~ /\d/
            }

            Map<String, String> upperValues = adjacentValues.findAll {
                return it.key.contains("upper") && it.value && it.value ==~ /\d/
            }
            Map<String, String> lowerValues = adjacentValues.findAll {
                return it.key.contains("lower") && it.value && it.value ==~ /\d/
            }

            // Remove values that are part of the same number.
            // This solves the following edge cases:
            /*
            *   444     4.4     444
            *   .*.     .*.     .*.
            *   ...     ...     333
             */
            if(adjacentValues.get("upperValue") ==~ /\d/) {
                if(upperValues.size() == 2) {
                    adjacentNumbers.remove("upperValue")
                }
                if(upperValues.size() == 3) {
                    adjacentNumbers.remove("upperValue")
                    adjacentNumbers.remove("upperRightValue")
                }
            }
            if(adjacentValues.get("lowerValue") ==~ /\d/) {
                if(lowerValues.size() == 2) {
                    adjacentNumbers.remove("lowerValue")
                }
                if(lowerValues.size() == 3) {
                    adjacentNumbers.remove("lowerValue")
                    adjacentNumbers.remove("lowerRightValue")
                }
            }

            int gearRatio = 1
            if (adjacentNumbers.size() == 2) {
                adjacentNumbers.each { adjacentValue ->
                    gearRatio *= getAdjacentPartNumber(engineSchematic, adjacentValue, rowIndex, colIndex, row.length)
                }
                sumOfGearRatios += gearRatio
            }
        }
    }
    // Part 1
    if (hasSymbol) {
        sumOfPartNumbers += Integer.parseInt(collectedDigits)
    }
}

println("Result for part 1: ${sumOfPartNumbers}")
println("Result for part 2: ${sumOfGearRatios}")

int getAdjacentPartNumber(List<String[]> engineSchematic, Map.Entry<String, String> adjacentValue, int rowPointer, int colPointer, int rowLength) {
    String number = ""
    switch (adjacentValue.key.toLowerCase()) {
        case { it.contains("upper") }:
            rowPointer--
            break
        case { it.contains("lower") }:
            rowPointer++
            break
    }
    switch (adjacentValue.key.toLowerCase()) {
        case { it.contains("right") }:
            colPointer++
            break
        case { it.contains("left") }:
            colPointer--
            break
    }

    if(adjacentValue.key != "rightValue") {
        while (engineSchematic[rowPointer][colPointer - 1] ==~ /\d/){
            colPointer--
        }
    }

    while (colPointer < rowLength && engineSchematic[rowPointer][colPointer] ==~ /\d/) {
        number += engineSchematic[rowPointer][colPointer]
        colPointer++
    }

    return number.toInteger()
}

Map<String, String> getAdjacentValues(List<String[]> engineSchematic, String[] row, int rowIndex, int colIndex) {
    boolean isFirstRow = rowIndex == 0
    boolean isLastRow = (rowIndex + 1) == engineSchematic.size()
    boolean isFirstColumn = colIndex == 0
    boolean isLastColumn = colIndex == (row.length - 1)

    String upperRightValue = (!isFirstRow && !isLastColumn) ? engineSchematic[rowIndex - 1][colIndex + 1] : null
    String rightValue = (!isLastColumn) ? engineSchematic[rowIndex][colIndex + 1] : null
    String lowerRightValue = (!isLastRow && !isLastColumn) ? engineSchematic[rowIndex + 1][colIndex + 1] : null
    String lowerValue = (!isLastRow) ? engineSchematic[rowIndex + 1][colIndex] : null
    String lowerLeftValue = (!isFirstColumn && !isLastRow) ? engineSchematic[rowIndex + 1][colIndex - 1] : null
    String leftValue = (!isFirstColumn) ? engineSchematic[rowIndex][colIndex - 1] : null
    String upperLeftValue = (!isFirstColumn && !isFirstRow) ? engineSchematic[rowIndex - 1][colIndex - 1] : null
    String upperValue = (!isFirstRow) ? engineSchematic[rowIndex - 1][colIndex] : null

    return [
            "upperRightValue": upperRightValue,
            "rightValue"     : rightValue,
            "lowerRightValue": lowerRightValue,
            "lowerValue"     : lowerValue,
            "lowerLeftValue" : lowerLeftValue,
            "leftValue"      : leftValue,
            "upperLeftValue" : upperLeftValue,
            "upperValue"     : upperValue
    ]
}