String input = new File('./input.txt').getText('UTF-8')
String[] inputArray = input.split('\r\n')

int calibrationSumPart1 = 0
inputArray.each { String calibrationString ->
    List<String> calibrationDigits = calibrationString.findAll(/\d/)
    calibrationSumPart1 += getCalibrationValues(calibrationDigits)
}
println("Result for part 1: ${calibrationSumPart1}")

int calibrationSumPart2 = 0
inputArray.each { String calibrationString ->
    List<String> calibrationDigits = new ArrayList<>()
    String collectedCharacters = ""
    calibrationString.split("").each { String character ->
        if (character ==~ /\d/) {
            calibrationDigits.add(character)
            collectedCharacters = ""
        } else {
            collectedCharacters += character
            String stringValue = StringValue.find {
                collectedCharacters.contains(it.toString())
            }?.value

            if (stringValue) {
                calibrationDigits.add(stringValue)
                // Digits as text may overlap with one character. We keep the last character in case this occurs.
                // ex. twone should be parsed as "2" and "1", and eighthree as "8" and "3".
                collectedCharacters = collectedCharacters.substring(collectedCharacters.length() - 1)
            }
        }
    }
    calibrationSumPart2 += getCalibrationValues(calibrationDigits)
}
println("Result for part 2: ${calibrationSumPart2}")

int getCalibrationValues(List<String> calibrationDigits) {
    if (calibrationDigits.size() == 0) return 0
    if (calibrationDigits.size() >= 2) {
        return (calibrationDigits[0] + calibrationDigits[-1]).toInteger()
    } else {
        return (calibrationDigits[0] + calibrationDigits[0]).toInteger()
    }
}

enum StringValue {
    zero("0"),
    one("1"),
    two("2"),
    three("3"),
    four("4"),
    five("5"),
    six("6"),
    seven("7"),
    eight("8"),
    nine("9")

    final String value;

    StringValue(String value) {
        this.value = value
    }
}