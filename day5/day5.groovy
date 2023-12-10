String input = new File('./input.txt').getText('UTF-8')
String[] inputArray = input.split('\r\n')

List<ScratchCard> scratchCards = new ArrayList<>()
inputArray.eachWithIndex { String game, int gameIndex ->
    String[] gameData = game.split(": ")

    List<Integer> gottenNumbers = gameData[1].split(" \\| ")[1].split(" ").collect {
        String stringValue = it.toString().trim()
        if(stringValue == "") return null
        return stringValue.toInteger()
    }.toList()
    gottenNumbers.removeAll { it == null }

    List<Integer> winningNumbers = gameData[1].split(" \\| ")[0].split(" ").collect {
        String stringValue = it.toString().trim()
        if(stringValue == "") return null
        return stringValue.toInteger()
    }.toList()
    winningNumbers.removeAll { it == null }


    scratchCards.add(new ScratchCard(gameIndex, gottenNumbers, winningNumbers))
}

int collectedPoints = 0
scratchCards.each { ScratchCard card ->
    collectedPoints += card.getPoints()
}

scratchCards.each { ScratchCard card ->
    int winsGotten = card.numberOfWinsGotten()
    int copiesOfCards = card.copiesOfCard
    if(winsGotten > 0) {
        for(int i= 1; i <= winsGotten; i++) {
            scratchCards.get(card.cardId + i).copiesOfCard++
            for(int j= 0; j < copiesOfCards; j++) {
                scratchCards.get(card.cardId + i).copiesOfCard++
            }
        }
    }
}

println("Result for part 1: ${collectedPoints}")
println("Result for part 2: ${scratchCards.size() + scratchCards.collect { it.copiesOfCard }.sum()}")
