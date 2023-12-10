String input = new File('./input.txt').getText('UTF-8')
String[] inputArray = input.split('\r\n')

Map<Integer, Integer> races = new HashMap<>()

String[] timeData = inputArray[0].split(" ").findAll { it ==~ /\d+/ }
String[] distanceData = inputArray[1].split(" ").findAll { it ==~ /\d+/ }
for (int i = 0; i < timeData.length; i++) {
    races.put(timeData[i].toInteger(), distanceData[i].toInteger())
}

int multipliedWaysToWin = 1
races.each { Map.Entry<Integer, Integer> race ->
    Integer time = race.key
    Integer distance = race.value

    int possibleWaysToWin = 0
    (0..time).each { Integer second ->
        int distanceTraveled = second * (time - second)
        if (distanceTraveled > distance) {
            possibleWaysToWin++
        }
    }
    multipliedWaysToWin *= possibleWaysToWin
}

println("Result for part 1: ${multipliedWaysToWin}")

Long time = timeData.collect().join().toLong()
Long distance = distanceData.collect().join().toLong()

int possibleWaysToWin2 = 0
(0..time).each { Long second ->
    Long distanceTraveled = second * (time - second)
    if (distanceTraveled > distance) {
        possibleWaysToWin2++
    }
}
println("Result for part 2: ${possibleWaysToWin2}")

