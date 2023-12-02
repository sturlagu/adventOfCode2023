String input = new File('./input.txt').getText('UTF-8')
String[] inputArray = input.split('\r\n')

Map<String, Integer> gameRestrictions = [
        "red"  : 12,
        "green": 13,
        "blue" : 14
]

int sumOfGameIDs = 0
inputArray.eachWithIndex { String game, currentGameIndex ->
    List<String> gameResults = game.split(": ")[1]?.replaceAll(";", ",")?.split(", ")
    boolean isGameInvalid = false
    for (restriction in gameRestrictions) {
        isGameInvalid = gameResults.any {
            it.contains(restriction.key) && (it.split(" ")[0].toInteger() > restriction.value)
        }

        if (isGameInvalid) {
            break
        }
    }
    if (!isGameInvalid) {
        sumOfGameIDs += currentGameIndex + 1
    }
}
println("Result for part 1: ${sumOfGameIDs}")

int sumOfPower = 0
inputArray.each { String game ->
    List<String> gameResults = game.split(": ")[1]?.replaceAll(";", ",")?.split(", ")
    int power = 1
    for (restriction in gameRestrictions) {
        int highestValue = gameResults.findAll {
            it.contains(restriction.key)
        }.collect {
            it.split(" ")[0].toInteger()
        }.max()

        power *= highestValue
    }
    sumOfPower += power
}
println("Result for part 2: ${sumOfPower}")