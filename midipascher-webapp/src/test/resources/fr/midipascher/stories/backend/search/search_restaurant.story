Search restaurant stories

Meta:
@refs 15
@progress wip

Narrative:
Given I provide a search restaurant request
When I send that request
It should either succeed or fail

Scenario: search restaurant by name should succeed
When I search for restaurants which "name" matches "awesome"
Then I should get the following restaurants:
| name                        | description                | mainOffer              | address.streetAddress | address.city | address.postalCode | address.countryCode | specialties | companyId   |
| awesome name 20             | fflflflflflflflflflflfl    | s5dfsd86gdfg786gd6gdg6 | 7 rue du Louvre       | paris        | 75001              | FR                  | SDW,SLD     | 552 100 520 |
| awesome name 21             | awesome description 21     | vgmfmdkksksks          | 12 rue de Clery       | paris        | 75002              | FR                  | BLG,SDW     | 552 100 521 |

Scenario: search restaurant by description should succeed
When I search for restaurants which "description" matches "awesome"
Then I should get the following restaurants:
| name                        | description                | mainOffer              | address.streetAddress | address.city | address.postalCode | address.countryCode | specialties | companyId   |
| awesome name 21             | awesome description 21     | vgmfmdkksksks          | 12 rue de Clery       | paris        | 75002              | FR                  | BLG,SDW     | 552 100 521 |
| dfglkdfjgdfgjdfiogjdiogjfio | awesome description 22     | awesome main offer 22  | 6 rue Dupetit-Thouars | paris        | 75003              | FR                  | BLG,CHN     | 552 100 522 |

Scenario: search restaurant by main offer should succeed
When I search for restaurants which "mainOffer" matches "awesome"
Then I should get the following restaurants:
| name                        | description                | mainOffer              | address.streetAddress | address.city | address.postalCode | address.countryCode | specialties | companyId   |
| dfglkdfjgdfgjdfiogjdiogjfio | awesome description 22     | awesome main offer 22  | 6 rue Dupetit-Thouars | paris        | 75003              | FR                  | BLG,CHN     | 552 100 522 |
| fflfkkgkgkhkhklhjl          | jkdfhkgdkfghdfghdfgudfuhgk | awesome main offer 23  | 29 rue Saint-Antoine  | paris        | 75004              | FR                  | CHN,JPN     | 552 100 523 |

Scenario: search restaurant by street address should succeed
When I search for restaurants which "address.streetAddress" matches "volta"
Then I should get the following restaurants:
| name                        | description                | mainOffer              | address.streetAddress | address.city | address.postalCode | address.countryCode | specialties | companyId   |
| dkgfhlfhi                   | dfkghdkfghdgkudfl          | sfdguhdfgudhudiguidfg  | 16 rue volta          | puteaux      | 92800              | FR                  | JPN,SLD     | 552 100 524 |
| mzaokszdfjkgjdkf            | wpdfjfgidgidfh             | sdfghighdfigudhughgkf  | 10 rue volta          | puteaux      | 92800              | FR                  | SDW,SLD     | 552 100 525 |

Scenario: search restaurant by city should succeed
When I search for restaurants which "address.city" matches "puteaux"
Then I should get the following restaurants:
| name                        | description                | mainOffer              | address.streetAddress | address.city | address.postalCode | address.countryCode | specialties | companyId   |
| dkgfhlfhi                   | dfkghdkfghdgkudfl          | sfdguhdfgudhudiguidfg  | 16 rue volta          | puteaux      | 92800              | FR                  | JPN,SLD     | 552 100 524 |
| mzaokszdfjkgjdkf            | wpdfjfgidgidfh             | sdfghighdfigudhughgkf  | 10 rue volta          | puteaux      | 92800              | FR                  | SDW,SLD     | 552 100 525 |

Scenario: search restaurant by postal code should succeed
When I search for restaurants which "address.postalCode" matches "92800"
Then I should get the following restaurants:
| name                        | description                | mainOffer              | address.streetAddress | address.city | address.postalCode | address.countryCode | specialties | companyId   |
| dkgfhlfhi                   | dfkghdkfghdgkudfl          | sfdguhdfgudhudiguidfg  | 16 rue volta          | puteaux      | 92800              | FR                  | JPN,SLD     | 552 100 524 |
| mzaokszdfjkgjdkf            | wpdfjfgidgidfh             | sdfghighdfigudhughgkf  | 10 rue volta          | puteaux      | 92800              | FR                  | SDW,SLD     | 552 100 525 |

Scenario: search restaurant by country code should succeed
When I search for restaurants which "address.countryCode" matches "FR"
Then I should get the following restaurants:
| name                        | description                | mainOffer              | address.streetAddress | address.city | address.postalCode | address.countryCode | specialties | companyId   |
| awesome name 20             | fflflflflflflflflflflfl    | s5dfsd86gdfg786gd6gdg6 | 7 rue du Louvre       | paris        | 75001              | FR                  | SDW,SLD     | 552 100 520 |
| awesome name 21             | awesome description 21     | vgmfmdkksksks          | 12 rue de Clery       | paris        | 75002              | FR                  | BLG,SDW     | 552 100 521 |
| dfglkdfjgdfgjdfiogjdiogjfio | awesome description 22     | awesome main offer 22  | 6 rue Dupetit-Thouars | paris        | 75003              | FR                  | BLG,CHN     | 552 100 522 |
| fflfkkgkgkhkhklhjl          | jkdfhkgdkfghdfghdfgudfuhgk | awesome main offer 23  | 29 rue Saint-Antoine  | paris        | 75004              | FR                  | CHN,JPN     | 552 100 523 |
| dkgfhlfhi                   | dfkghdkfghdgkudfl          | sfdguhdfgudhudiguidfg  | 16 rue volta          | puteaux      | 92800              | FR                  | JPN,SLD     | 552 100 524 |
| mzaokszdfjkgjdkf            | wpdfjfgidgidfh             | sdfghighdfigudhughgkf  | 10 rue volta          | puteaux      | 92800              | FR                  | SDW,SLD     | 552 100 525 |

Scenario: search restaurant by company id should succeed
When I search for restaurants which "companyId" matches "552 100 522"
Then I should get the following restaurants:
| name                        | description                | mainOffer              | address.streetAddress | address.city | address.postalCode | address.countryCode | specialties | companyId   |
| dfglkdfjgdfgjdfiogjdiogjfio | awesome description 22     | awesome main offer 22  | 6 rue Dupetit-Thouars | paris        | 75003              | FR                  | BLG,CHN     | 552 100 522 |

Scenario: search restaurant by geo distance should succeed
Given my current position is "19 rue ponthieu, 75008, paris"
When I search for restaurants around me
Then I should get the following restaurants:
| name                        | description                | mainOffer              | address.streetAddress | address.city | address.postalCode | address.countryCode | specialties | companyId   |
| fflfkkgkgkhkhklhjl          | jkdfhkgdkfghdfghdfgudfuhgk | awesome main offer 23  | 29 rue Saint-Antoine  | paris        | 75004              | FR                  | CHN,JPN     | 552 100 523 |
| dkgfhlfhi                   | dfkghdkfghdgkudfl          | sfdguhdfgudhudiguidfg  | 16 rue volta          | puteaux      | 92800              | FR                  | JPN,SLD     | 552 100 524 |

Scenario: search restaurant by geo distance should succeed
Given my current location is "19 rue ponthieu, 75008, paris"
When I search for restaurants near my location
Then I should get the following restaurants:
| name                    | description                        | mainOffer             | address.streetAddress | address.city  | address.postalCode | address.countryCode | specialties | companyId   |
| Monteko café            | restaurant cacher sandwiches snack | menu sandwich à 11,5€ | 45 Rue de Ponthieu    | paris         | 75008              | FR                  | SDW,SLD     | 552 100 600 |
| Class'croute            | sandwiches snack                   | menu sandwich à 9,5€  | 12 Rue du Colisée     | paris         | 75008              | FR                  | SDW,SLD     | 552 100 601 |
| Flam's                  | restaurant, tartes chaudes         | menu sandwich à 11,5€ | 45 Rue de Ponthieu    | paris         | 75008              | FR                  | SDW,SLD     | 552 100 602 |
| Nooi                    | sandwiches snack à emporter        | menu sandwich à 11,5€ | 16 rue du Colisée     | paris         | 75008              | FR                  | SDW,SLD     | 552 100 603 |
| Cappuccino              | sandwiches, paninis, snack         | menu sandwich à 11,5€ | 11 bis rue du colisée | paris         | 75008              | FR                  | SDW,SLD     | 552 100 604 |
| Traiteur elysée gourmet | restaurant asiatique               | menu sandwich à 11,5€ | 14 rue du colisée     | paris         | 75008              | FR                  | SDW,SLD     | 552 100 605 |
| PDG Rive Droite         | restaurant, sandwiches, salades    | menu sandwich à 11,5€ | 20 Rue de Ponthieu    | paris         | 75008              | FR                  | SDW,SLD     | 552 100 606 |
| Le pré'lud              | rotisserie, salades                | menu sandwich à 11,5€ | 14 Rue de Ponthieu    | paris         | 75008              | FR                  | SDW,SLD     | 552 100 607 |
| Jour                    | salades, sandwiches                | menu sandwich à 11,5€ | 44 Rue de Ponthieu    | paris         | 75008              | FR                  | SDW,SLD     | 552 100 608 |

Scenario: search restaurant by geo distance with additional criteria should succeed
Given my current location is "19 rue ponthieu, 75008, paris"
When I search for restaurants near my location which "<property>" matches "<value>"
Then I should get the following restaurants:
| name                    | description                        | mainOffer             | address.streetAddress | address.city  | address.postalCode | address.countryCode | specialties | companyId   |
| Monteko café            | restaurant cacher sandwiches snack | menu sandwich à 11,5€ | 45 Rue de Ponthieu    | paris         | 75008              | FR                  | SDW,SLD     | 552 100 600 |
Examples:
| property    | value |
| kosher      | true  |
| specialties | SDW   |
