String input = new File('./input.txt').getText('UTF-8')
String[] inputArray = input.split('\r\n')

String[] seeds
Map<Integer, String[]> seedToSoil = new HashMap<>()
Map<Integer, String[]> soilToFertilizer = new HashMap<>()
Map<Integer, String[]> fertilizerToWater = new HashMap<>()
Map<Integer, String[]> waterToLight = new HashMap<>()
Map<Integer, String[]> lightToTemperature = new HashMap<>()
Map<Integer, String[]> temperatureToHumidity = new HashMap<>()
Map<Integer, String[]> humidityToLocation = new HashMap<>()

String currentCategory
int categoryIndex = 0
inputArray.eachWithIndex { String line, int lineIndex ->
    if (lineIndex == 0) {
        seeds = line.split(": ")[1].split(" ")
    } else {
        if (line != "") {
            if (line.contains(":")) {
                currentCategory = line
                categoryIndex = 0
            } else {
                switch (currentCategory) {
                    case "seed-to-soil map:":
                        seedToSoil.put(categoryIndex, line.split(" "))
                        break
                    case "soil-to-fertilizer map:":
                        soilToFertilizer.put(categoryIndex, line.split(" "))
                        break
                    case "fertilizer-to-water map:":
                        fertilizerToWater.put(categoryIndex, line.split(" "))
                        break
                    case "water-to-light map:":
                        waterToLight.put(categoryIndex, line.split(" "))
                        break
                    case "light-to-temperature map:":
                        lightToTemperature.put(categoryIndex, line.split(" "))
                        break
                    case "temperature-to-humidity map:":
                        temperatureToHumidity.put(categoryIndex, line.split(" "))
                        break
                    case "humidity-to-location map:":
                        humidityToLocation.put(categoryIndex, line.split(" "))
                        break
                }
                categoryIndex++
            }
        }
    }
}

Map<Long, Long[]> mappedList = new HashMap<>()
seeds.each { String seed ->
    Long seedValue = seed.toLong()
    Long[] values = new Long[7]
    values[0] = getMappedValue(seedToSoil, seedValue)
    values[1] = getMappedValue(soilToFertilizer, values[0])
    values[2] = getMappedValue(fertilizerToWater, values[1])
    values[3] = getMappedValue(waterToLight, values[2])
    values[4] = getMappedValue(lightToTemperature, values[3])
    values[5] = getMappedValue(temperatureToHumidity, values[4])
    values[6] = getMappedValue(humidityToLocation, values[5])
    mappedList.put(seedValue, values)
}
println("Result for part 1: ${mappedList.min { it.value[6] }.value[6]}")

Map<Long, Long[]> mappedListPartTwo = new HashMap<>()
for (int i = 0; i < seeds.length; i++) {
    if (i != 0 && i % 2 != 0) {
        Long seedValue = seeds[i - 1].toLong()
        Long range = seeds[i].toLong()
        for (Long j = 0; j < range; j++) {
            Long[] values = new Long[7]
            values[0] = getMappedValue(seedToSoil, (seedValue + j))
            values[1] = getMappedValue(soilToFertilizer, values[0])
            values[2] = getMappedValue(fertilizerToWater, values[1])
            values[3] = getMappedValue(waterToLight, values[2])
            values[4] = getMappedValue(lightToTemperature, values[3])
            values[5] = getMappedValue(temperatureToHumidity, values[4])
            values[6] = getMappedValue(humidityToLocation, values[5])
            mappedListPartTwo.put((seedValue + j), values)
            println("j: ${j} of ${range}")
        }
    }
    println("Seed ${i} of ${seeds.length}")
}
println("Result for part 2: ${mappedListPartTwo.min { it.value[6] }.value[6]}")

Long getMappedValue(Map<Integer, String[]> entityMap, Long value) {
    for (entity in entityMap) {
        Long destinationValue = entity.value[0].toLong()
        Long sourceValue = entity.value[1].toLong()
        Long rangeValue = entity.value[2].toLong()
        if (value >= sourceValue && value <= (sourceValue + rangeValue)) {
            return destinationValue + (value - sourceValue)
        }
    }
    return value
}