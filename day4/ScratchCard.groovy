class ScratchCard {
    int cardId
    List<Integer> gottenNumbers
    List<Integer> winningNumbers
    int copiesOfCard

    ScratchCard(int cardId, List<Integer> gottenNumbers, List<Integer> winningNumbers) {
        this.cardId = cardId
        this.gottenNumbers = gottenNumbers
        this.winningNumbers = winningNumbers
        this.copiesOfCard = 0
    }

    int getPoints() {
        List<Integer> gottenWinningNumbers = this.gottenNumbers.intersect(this.winningNumbers)
        int points = 0
        gottenWinningNumbers.eachWithIndex { int number, int index ->
            if (index == 0) {
                points = 1
            } else {
                points *= 2
            }
        }
        return points
    }

    int numberOfWinsGotten() {
        return this.gottenNumbers.intersect(this.winningNumbers).size()
    }
}