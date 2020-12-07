```
                                    ___________________________
                                   |                           |
                                   |       ATMCalculator       |
                                   |  _______________________  |
 ____________                      | |                       | |
|            |                     | | AmountToMapCalculator | |
| ATMStorage |                     | |_______________________| |
|____________|                     |___________________________|
       |                                         |
       | getState()                              |
       | getBanknotes(Map<Banknote, Integer>)    | getBalance(Map<Banknote, Integer>
       | putBanknote(Banknote)                   | amountToBanknotes(int, Map<Banknote, Integer>)
       | putBanknotes(Map<Banknote, Integer>)    |
       |__________________     __________________|
                          |   |
                         _|___|_
                        |       |
                        |  ATM  |
                        |_______|
                            |
                            | getBalance()
                            | acceptBanknote(banknote)
                            | acceptBanknotes(banknotes)
                            | getMyMoney(int)
                            |
                            O
                           /U\
                           / \
                          Client
```